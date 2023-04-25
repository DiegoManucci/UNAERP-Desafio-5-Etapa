package com.unaerp.desafio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var loginButtom = findViewById<TextView>(R.id.loginButton)

        loginButtom.setOnClickListener {
            val intent = Intent(this, BaseActivity::class.java)
            startActivity(intent)
        }
        
        var cadastroMessage = findViewById<TextView>(R.id.cadastro_message)

        cadastroMessage.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }
    }
}