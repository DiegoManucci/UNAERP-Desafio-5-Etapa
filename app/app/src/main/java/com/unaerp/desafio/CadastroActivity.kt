package com.unaerp.desafio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro)

        // Initialize views
        var nameEditText = findViewById<EditText>(R.id.name_input)
        var mailEditText = findViewById<EditText>(R.id.mail_input)
        var passwordEditText = findViewById<EditText>(R.id.password_input)

        val cadastroButton = findViewById<MaterialButton>(R.id.cadastro_button)
        cadastroButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = mailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val toggleButton = findViewById<MaterialButtonToggleGroup>(R.id.toggleButton)
            val selectedButton = toggleButton.checkedButtonId
            val userType = when (selectedButton) {
                R.id.interessado_button -> "Interessado"
                R.id.anunciante_button -> "Anunciante"
                else -> ""
            }

            nameEditText.text?.clear()
            mailEditText.text?.clear()
            passwordEditText.text?.clear()
        }

        var loginMessage = findViewById<TextView>(R.id.login_account_message)

        loginMessage.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}