package com.unaerp.desafio.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.unaerp.desafio.R
import com.unaerp.desafio.responses.LoginResponse
import com.unaerp.desafio.responses.RecuperarSenhaResponse
import com.unaerp.desafio.services.UserService
import com.unaerp.desafio.services.config.ServiceCreator
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var loginButton = findViewById<Button>(R.id.loginButton)
        var cadastroButton = findViewById<Button>(R.id.cadastroButton)
        val emailEditText = findViewById<EditText>(R.id.inputEmail)
        val passwordEditText = findViewById<EditText>(R.id.inputPassword)
        val forgotPasswordButton = findViewById<Button>(R.id.forgotPasswordButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Insira Email e Senha!", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

        cadastroButton.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
            finish()
        }

        forgotPasswordButton.setOnClickListener {
            showEsqueciASenhaDialog()
        }
    }

    private fun loginUser(email: String, password: String) {
        val service = ServiceCreator.createService<UserService>()
        service.login(email, password).enqueue(LoginCallBack())
    }

    private fun recuperarSenha(email: String) {
        val service = ServiceCreator.createService<UserService>()
        service.recuperarSenha(email).enqueue(RecuperarSenhaCallback())
    }

    inner class LoginCallBack : Callback<LoginResponse> {
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            runOnUiThread {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()

                        val token = response.body()?.token
                        val role = response.body()?.role

                        editor.putString("ROLE", role)
                        editor.putString("TOKEN", token)
                        editor.apply()

                        val intent = Intent(this@LoginActivity, BaseActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else if (response.code() == 400) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    Toast.makeText(
                        applicationContext,
                        jObjError.getString("error"),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            Toast.makeText(this@LoginActivity, "Erro ao efetuar login", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showEsqueciASenhaDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_esqueci_a_senha, null)

        AlertDialog.Builder(this)
            .setTitle("Recuperação de senha")
            .setView(dialogView)
            .setPositiveButton("Enviar") { dialog, which ->
                val emailEditText = dialogView.findViewById<EditText>(R.id.emailEditText)
                val email = emailEditText.text.toString().trim()

                recuperarSenha(email)
            }
            .setNegativeButton("Voltar", null)
            .show()
    }

    inner class RecuperarSenhaCallback : Callback<RecuperarSenhaResponse> {
        override fun onResponse(call: Call<RecuperarSenhaResponse>, response: Response<RecuperarSenhaResponse>) {
            runOnUiThread {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@LoginActivity,
                        response.body()?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (response.code() == 400) {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    Toast.makeText(
                        applicationContext,
                        jObjError.getString("error"),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        override fun onFailure(call: Call<RecuperarSenhaResponse>, t: Throwable) {
            Toast.makeText(this@LoginActivity, "Erro ao efetuar login", Toast.LENGTH_SHORT).show()
        }
    }
}


