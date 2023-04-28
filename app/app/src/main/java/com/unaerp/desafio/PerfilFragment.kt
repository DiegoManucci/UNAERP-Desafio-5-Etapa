package com.unaerp.desafio

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.unaerp.desafio.model.Usuario

class PerfilFragment : Fragment() {

    private lateinit var inputName: TextInputEditText
    private lateinit var inputEmail: TextInputEditText
    private lateinit var inputPassword: TextInputEditText
    private lateinit var usuario: Usuario

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_perfil, container, false)

        usuario = Usuario("Diego Brino", "diegobrino@unaerp.br", "123deOlivera4")

        inputName = rootView.findViewById(R.id.inputName)
        inputEmail = rootView.findViewById(R.id.inputEmail)
        inputPassword = rootView.findViewById(R.id.inputPassword)

        inputName.setText(usuario.email)
        inputEmail.setText(usuario.nome)
        inputPassword.setText(usuario.senha)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val salvarButton = view.findViewById<Button>(R.id.salvarButton)
        val cancelarButton = view.findViewById<Button>(R.id.cancelarButton)

        salvarButton.setOnClickListener {
            if (inputName.text.toString().trim().isEmpty() ||
                    inputEmail.text.toString().trim().isEmpty() ||
                    inputPassword.text.toString().trim().isEmpty()) {
                Toast.makeText(
                    context,
                    "Você precisa obrigatoriamente preencher todos os campos!",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener;
            }

            usuario.nome = inputName.text.toString()
            usuario.email = inputEmail.text.toString()
            usuario.senha = inputPassword.text.toString()

            Toast.makeText(
                context,
                "Perfil editado com sucesso!",
                Toast.LENGTH_SHORT
            ).show()
        }

        cancelarButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Confirmação")
            builder.setMessage("Tem certeza que deseja cancelar a edição?")

            builder.setPositiveButton("Sim") { dialog, which ->
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_content, ListaVagasFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            }

            builder.setNegativeButton("Não") { dialog, which -> }

            builder.show()
        }
    }
}