package com.unaerp.desafio.activities

import com.unaerp.desafio.R
import com.unaerp.desafio.fragments.ListaVagasFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        val bundle = Bundle()
        bundle.putString("anuncios", "gerais")

        val fragment = ListaVagasFragment()
        fragment.arguments = bundle

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_content, fragment)
        fragmentTransaction.commit()
    }
}