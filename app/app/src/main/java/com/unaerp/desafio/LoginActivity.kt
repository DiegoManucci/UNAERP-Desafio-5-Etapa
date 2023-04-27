package com.unaerp.desafio

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var loginButton = findViewById<TextView>(R.id.loginButton)
        val emailEditText = findViewById<EditText>(R.id.inputEmail)
        val passwordEditText = findViewById<EditText>(R.id.inputPassword)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Insira Email e Senha!", Toast.LENGTH_SHORT).show()
            } else {
                if(email == "teste@teste.com" && password == "teste"){
                    val sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("ROLE", "anunciante")
                    editor.apply()
                    val intent = Intent(this, BaseActivity::class.java)
                    startActivity(intent)
                } else if(email == "teste2@teste2.com" && password == "teste2"){
                    val sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("ROLE", "interessado")
                    editor.apply()
                    val intent = Intent(this, BaseActivity::class.java)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this, "Email ou Senha incorretos!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        var cadastroMessage = findViewById<TextView>(R.id.cadastro_message)

        cadastroMessage.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }
    }
}