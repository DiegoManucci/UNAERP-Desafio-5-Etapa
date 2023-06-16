package com.unaerp.desafio.fragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import com.unaerp.desafio.R
import com.unaerp.desafio.model.Vaga
import com.unaerp.desafio.responses.AnnouncementResponse
import com.unaerp.desafio.services.AnnouncementService
import com.unaerp.desafio.services.config.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


class CriarVagaFragment : Fragment() {

    val formatter = SimpleDateFormat("dd/MM/yyyy")

    lateinit var areaInput : TextInputEditText
    lateinit var descricaoInput : TextInputEditText
    lateinit var remuneracaoInput : TextInputEditText
    lateinit var cidadeInput : TextInputEditText
    lateinit var emailInput : TextInputEditText
    lateinit var telefoneInput : TextInputEditText
    lateinit var dtInicioInput : TextInputEditText
    lateinit var dtFimInput : TextInputEditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_criar_vaga, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)

        val sortItem = toolbar.menu.findItem(R.id.action_sort)
        sortItem.isVisible = false

        val filterItem = toolbar.menu.findItem(R.id.action_search)
        filterItem.isVisible = false

        val criarItem = toolbar.menu.findItem(R.id.action_create)
        criarItem.isVisible = false

        toolbar.title = "Criar Anúncio"

        toolbar.setOnMenuItemClickListener { menuItem ->
            Log.d("VALOR", menuItem.itemId.toString())
            when (menuItem.itemId) {
                R.id.action_profile -> {
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_content, PerfilFragment())
                    transaction.addToBackStack(null)
                    transaction.commit()
                    true
                }
                R.id.action_anuncios -> {
                    val bundle = Bundle()
                    bundle.putString("anuncios", "meus")

                    val fragment = ListaVagasFragment()
                    fragment.arguments = bundle

                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_content, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                    true
                }
                R.id.action_anuncios_gerais -> {
                    val bundle = Bundle()
                    bundle.putString("anuncios", "gerais")

                    val fragment = ListaVagasFragment()
                    fragment.arguments = bundle

                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_content, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                    true
                }
                else -> false
            }
        }

        val sharedPreferences =  requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val role = sharedPreferences.getString("ROLE", "nenhum")

        if(role != "anunciante"){
            val createItem = toolbar.menu.findItem(R.id.action_create)
            createItem.isVisible = false

            val meusAnunciosItem = toolbar.menu.findItem(R.id.action_anuncios)
            meusAnunciosItem.isVisible = false
        }

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_profile -> {
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_content, PerfilFragment())
                    transaction.addToBackStack(null)
                    transaction.commit()
                    true
                }
                R.id.action_anuncios -> {
                    val bundle = Bundle()
                    bundle.putString("anuncios", "meus")

                    val fragment = ListaVagasFragment()
                    fragment.arguments = bundle

                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_content, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                    true
                }
                R.id.action_anuncios_gerais -> {
                    val bundle = Bundle()
                    bundle.putString("anuncios", "gerais")

                    val fragment = ListaVagasFragment()
                    fragment.arguments = bundle

                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_content, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                    true
                }
                else -> false
            }
        }

        val toggleGroup = view.findViewById<MaterialButtonToggleGroup>(R.id.toggleButton)
        var mostrarAnunciante = -1

        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked && checkedId != -1) {
                mostrarAnunciante = checkedId
            } else {
                mostrarAnunciante = -1
            }
        }

        val criarButton = view.findViewById<Button>(R.id.criarButton)
        criarButton.setOnClickListener {
            val areaInput = view.findViewById<TextInputEditText>(R.id.area_input)
            val descricaoInput = view.findViewById<TextInputEditText>(R.id.descricao_input)
            val remuneracaoInput = view.findViewById<TextInputEditText>(R.id.remuneracao_input)
            val cidadeInput = view.findViewById<TextInputEditText>(R.id.cidade_input)
            val emailInput = view.findViewById<TextInputEditText>(R.id.email_input)
            val telefoneInput = view.findViewById<TextInputEditText>(R.id.telefone_input)
            val dtInicioInput = view.findViewById<TextInputEditText>(R.id.dtinicio_input)
            val dtFimInput = view.findViewById<TextInputEditText>(R.id.dtfim_input)

            val areaMask = Regex("[a-zA-Z\\s]+")
            val descricaoMask = Regex("[a-zA-Z0-9\\s]+")
            val remuneracaoMask = Regex("\\d+(\\.\\d{1,2})?")
            val cidadeMask = Regex("[a-zA-Z\\s]+")
            val emailMask = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
            val telefoneMask = Regex("\\d{10,11}")

            val areaError = "Área inválida!"
            val descricaoError = "Descrição inválida!"
            val remuneracaoError = "Salário inválido!"
            val cidadeError = "Cidade inválida!"
            val emailError = "E-mail inválido!"
            val telefoneError = "Telefone inválido!"

            val area = areaInput.text.toString()
            val descricao = descricaoInput.text.toString()
            val remuneracao = remuneracaoInput.text.toString()
            val cidade = cidadeInput.text.toString()
            val email = emailInput.text.toString()
            val telefone = telefoneInput.text.toString()
            val dtInicio: String = dtInicioInput.text.toString()
            val dtFim: String = dtFimInput.text.toString()

            if (!areaMask.matches(areaInput.text.toString())) {
                areaInput.error = areaError
                return@setOnClickListener
            }

            if (!descricaoMask.matches(descricaoInput.text.toString())) {
                descricaoInput.error = descricaoError
                return@setOnClickListener
            }

            if (!remuneracaoMask.matches(remuneracaoInput.text.toString())) {
                remuneracaoInput.error = remuneracaoError
                return@setOnClickListener
            }

            if (!cidadeMask.matches(cidadeInput.text.toString())) {
                cidadeInput.error = cidadeError
                return@setOnClickListener
            }

            if (!emailMask.matches(emailInput.text.toString())) {
                emailInput.error = emailError
                return@setOnClickListener
            }

            if (!telefoneMask.matches(telefoneInput.text.toString())) {
                telefoneInput.error = telefoneError
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(area)) {
                areaInput.error = "Área de Conhecimento é obrigatória";
                areaInput.requestFocus();
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(descricao)) {
                descricaoInput.error = "Descrição é obrigatória";
                descricaoInput.requestFocus();
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(remuneracao)) {
                remuneracaoInput.error = "Remuneração é obrigatória";
                remuneracaoInput.requestFocus();
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(cidade)) {
                cidadeInput.error = "Cidade é obrigatória";
                cidadeInput.requestFocus();
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(email)) {
                emailInput.error = "E-mail é obrigatório";
                emailInput.requestFocus();
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = "E-mail inválido";
                emailInput.requestFocus();
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(telefone)) {
                telefoneInput.error = "Telefone é obrigatório";
                telefoneInput.requestFocus();
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(dtInicio)) {
                dtInicioInput.error = "Data de Início é obrigatória";
                dtInicioInput.requestFocus();
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(dtFim)) {
                dtFimInput.error = "Data de Fim é obrigatória";
                dtFimInput.requestFocus();
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(dtFim)) {
                dtFimInput.error = "Data de Fim é obrigatória";
                dtFimInput.requestFocus();
                return@setOnClickListener
            }

            if (mostrarAnunciante == -1) {
                Toast.makeText(requireContext(), "É necessário selecionar uma opção para o anunciante!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPreferences = context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val token = sharedPreferences?.getString("TOKEN", null)

            var valor = ""

            if(mostrarAnunciante != 1){
                valor = "S"
            } else {
                valor = "N"
            }

            if (token != null) {
                criar(area, descricao, remuneracao, cidade, email, telefone, token.toInt(), valor, dtInicio, dtFim)
            }

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_content, ListaVagasFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun criar(area: String, descricao: String, remuneracao: String, cidade: String, email: String, telefone: String, token: Int, mostrarAnunciante: String, dtFim: String, dtInicio: String) {
        val service = ServiceCreator.createService<AnnouncementService>()

        if (token != null) {
            service.criar(area, remuneracao, email, telefone, descricao, cidade, token.toInt(), mostrarAnunciante, dtInicio, dtFim).enqueue(criarCallback())
        }
    }

    inner class criarCallback : Callback<AnnouncementResponse> {
        override fun onResponse(call: Call<AnnouncementResponse>, response: Response<AnnouncementResponse>) {
            if (response.isSuccessful) {
                Toast.makeText(context, "Vaga criada com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Erro ao criar vaga!", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<AnnouncementResponse>, t: Throwable) {
            Toast.makeText(context, "Erro ao criar vaga!", Toast.LENGTH_SHORT).show()
        }
    }
}