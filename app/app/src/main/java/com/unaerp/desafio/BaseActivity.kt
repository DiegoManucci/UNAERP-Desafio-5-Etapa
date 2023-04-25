package com.unaerp.desafio

import com.unaerp.desafio.R
import com.unaerp.desafio.ListaVagasFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        // Pega o FragmentManager
        val fragmentManager: FragmentManager = supportFragmentManager

        // Abre uma transação e adiciona
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_content, ListaVagasFragment())
        fragmentTransaction.commit()

        // Substitui um Fragment
//        val ft: FragmentTransaction = fragmentManager.beginTransaction()
//        ft.replace(R.id.fragment_content, MainFragment())
//        ft.commit()

        // Remove um Fragment
//        val fragment: Fragment? = fragmentManager.findFragmentById(R.id.fragment_content)
//        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//        if (fragment != null) {
//            ft.remove(fragment)
//        }
//        ft.commit()

    }
}