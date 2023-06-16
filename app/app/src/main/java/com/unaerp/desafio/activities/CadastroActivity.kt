package com.unaerp.desafio.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.button.MaterialButtonToggleGroup
import com.unaerp.desafio.R
import com.unaerp.desafio.responses.LoginResponse
import com.unaerp.desafio.services.UserService
import com.unaerp.desafio.services.config.ServiceCreator
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        var nameEditText = findViewById<EditText>(R.id.name_input)
        var mailEditText = findViewById<EditText>(R.id.mail_input)
        var passwordEditText = findViewById<EditText>(R.id.password_input)

        val cadastroButton = findViewById<Button>(R.id.cadastro_button)
        cadastroButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = mailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val toggleButton = findViewById<MaterialButtonToggleGroup>(R.id.toggleButton)
            val selectedButton = toggleButton.checkedButtonId
            val userType = when (selectedButton) {
                R.id.interessado_button -> "interessado"
                R.id.anunciante_button -> "anunciante"
                else -> ""
            }

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || userType.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else {
                cadastrarUser(name, email, password, userType)
            }
        }

        var loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun cadastrarUser(nome: String, email: String, password: String, tipo: String) {
        val service = ServiceCreator.createService<UserService>()
        service.cadastrar(nome, email, password, tipo).enqueue(CadastrarCallBack())
    }

    inner class CadastrarCallBack : retrofit2.Callback<LoginResponse> {
        override fun onResponse(call: retrofit2.Call<LoginResponse>, response: retrofit2.Response<LoginResponse>) {
            runOnUiThread {
                if (response.isSuccessful) {
                    Log.d("CadastroActivity", response.body().toString())
                    if(response.body() != null) {
                        val sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()

                        val token = response.body()?.token
                        val role = response.body()?.role

                        editor.putString("ROLE", role)
                        editor.putString("TOKEN", token)
                        editor.apply()

                        val intent = Intent(this@CadastroActivity, BaseActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else if(response.code() == 400) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    Toast.makeText(applicationContext, jObjError.getString("error"), Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
            Toast.makeText(this@CadastroActivity, "Erro ao efetuar login", Toast.LENGTH_SHORT).show()
        }
    }
}