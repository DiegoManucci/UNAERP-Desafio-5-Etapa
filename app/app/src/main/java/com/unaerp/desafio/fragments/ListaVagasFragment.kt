package com.unaerp.desafio.fragments

import com.unaerp.desafio.model.Vaga
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.unaerp.desafio.R
import com.unaerp.desafio.activities.BaseActivity
import com.unaerp.desafio.adapters.VagaAdapter
import com.unaerp.desafio.responses.AnnouncementResponse
import com.unaerp.desafio.responses.LoginResponse
import com.unaerp.desafio.services.AnnouncementService
import com.unaerp.desafio.services.UserService
import com.unaerp.desafio.services.config.ServiceCreator
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


class ListaVagasFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var vagaListAdapter: VagaAdapter

    private var anuncios: List<Vaga> = listOf()
    private var sortDateState = false

    val formatter = SimpleDateFormat("dd/MM/yyyy")

    override fun onResume() {
        super.onResume()

        val tipoAnuncios = arguments?.getString("anuncios") ?: return

        if (tipoAnuncios == "gerais") {
            getAllAnnouncements()
        } else {
            getMyAnnouncements()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_lista_vagas, container, false)

        val tipoAnuncios = arguments?.getString("anuncios")

        if(tipoAnuncios == null){
            return@onCreateView rootView
        }

        if (tipoAnuncios == "gerais") {
            getAllAnnouncements()
        } else {
            getMyAnnouncements()
        }

        recyclerView = rootView.findViewById(R.id.recycler_view_job_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        vagaListAdapter = VagaAdapter(anuncios, object : VagaAdapter.JobItemClickListener {
            override fun onJobItemClick(vaga: Vaga) {
                val myDialog = VagaDialogFragment(vaga)

                myDialog.show(requireActivity().supportFragmentManager, "vagaDialog")
            }
        }, ::delete, tipoAnuncios)
        recyclerView.adapter = vagaListAdapter

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)

        val tipoAnuncios = arguments?.getString("anuncios")

        if (tipoAnuncios == "gerais") {
            toolbar.title = "Anúncios"
            val anunciosItem = toolbar.menu.findItem(R.id.action_anuncios_gerais)
            anunciosItem.isVisible = false

        } else {
            toolbar.title = "Meus Anúncios"
            val meusAnunciosItem = toolbar.menu.findItem(R.id.action_anuncios)
            meusAnunciosItem.isVisible = false
        }

        val sharedPreferences =
            requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val role = sharedPreferences.getString("ROLE", "nenhum")

        if (role != "anunciante") {
            val createItem = toolbar.menu.findItem(R.id.action_create)
            createItem.isVisible = false

            val meusAnunciosItem = toolbar.menu.findItem(R.id.action_anuncios)
            meusAnunciosItem.isVisible = false
        }

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_sort -> {
                    vagaListAdapter.sortByStartDate(sortDateState)
                    sortDateState = !sortDateState
                    true
                }
                R.id.action_profile -> {
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_content, PerfilFragment())
                    transaction.addToBackStack(null)
                    transaction.commit()
                    true
                }
                R.id.action_create -> {
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_content, CriarVagaFragment())
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

        val searchItem = toolbar.menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var filteredList: MutableList<Vaga> = ArrayList()
                anuncios.forEach { vaga ->
                    if (newText != null && vaga.area.toLowerCase()
                            .contains(newText.toLowerCase())
                    ) {
                        filteredList.add(vaga)
                    }
                }
                vagaListAdapter.setFilteredList(filteredList)
                return true
            }
        })
    }

    private fun getAllAnnouncements() {
        val service = ServiceCreator.createService<AnnouncementService>()
        service.getAll().enqueue(GetAllAnnouncementsCallback())
    }

    private fun getMyAnnouncements() {
        val service = ServiceCreator.createService<AnnouncementService>()

        val sharedPreferences = context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences?.getString("TOKEN", null)

        if (token != null) {
            service.getMy(token.toInt()).enqueue(GetMyAnnouncementsCallback())
        }
    }

    inner class GetAllAnnouncementsCallback : Callback<List<AnnouncementResponse>> {
        override fun onResponse(call: Call<List<AnnouncementResponse>>, response: Response<List<AnnouncementResponse>>) {
            anuncios = response.body()?.map { announcement ->
                Vaga(
                    announcement.idannouncement,
                    announcement.area,
                    announcement.description,
                    announcement.remuneration,
                    announcement.city,
                    announcement.email,
                    announcement.phone,
                    announcement.advertiser,
                    announcement.startDate,
                    announcement.endDate
                )
            } ?: listOf()

            vagaListAdapter.setFilteredList(anuncios as MutableList<Vaga>)
        }

        override fun onFailure(call: Call<List<AnnouncementResponse>>, t: Throwable) {
            Toast.makeText(context, "Erro ao buscar vagas!", Toast.LENGTH_SHORT).show()
        }
    }

    inner class GetMyAnnouncementsCallback : Callback<List<AnnouncementResponse>> {
        override fun onResponse(call: Call<List<AnnouncementResponse>>, response: Response<List<AnnouncementResponse>>) {

            Log.d("teste", response.body().toString())

            anuncios = response.body()?.map { announcement ->
                Vaga(
                    announcement.idannouncement,
                    announcement.area,
                    announcement.description,
                    announcement.remuneration,
                    announcement.city,
                    announcement.email,
                    announcement.phone,
                    announcement.advertiser,
                    announcement.startDate,
                    announcement.endDate
                )
            } ?: listOf()

            vagaListAdapter.setFilteredList(anuncios as MutableList<Vaga>)
        }

        override fun onFailure(call: Call<List<AnnouncementResponse>>, t: Throwable) {
            Toast.makeText(context, "Erro ao buscar vagas!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun delete(id: Int) {
        val service = ServiceCreator.createService<AnnouncementService>()
        service.deletar(id).enqueue(deleteCallback())
    }

    inner class deleteCallback : Callback<Unit> {
        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
            Toast.makeText(context, "Vaga deletada com sucesso!", Toast.LENGTH_SHORT).show()
            getMyAnnouncements()
        }

        override fun onFailure(call: Call<Unit>, t: Throwable) {
            Toast.makeText(context, "Erro ao buscar vagas!", Toast.LENGTH_SHORT).show()
        }
    }
}