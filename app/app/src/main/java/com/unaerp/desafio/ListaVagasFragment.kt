package com.unaerp.desafio

import com.unaerp.desafio.R
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import java.text.SimpleDateFormat
import java.util.*


class ListaVagasFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var vagaListAdapter: VagaAdapter

    val formatter = SimpleDateFormat("dd/MM/yyyy")
    private var dummyList = listOf(
        Vaga(
            "Design",
            "UX Designer",
            5000.00,
            "São Paulo",
            "contato@empresa.com",
            "(11) 1111-1111",
            "Empresa A",
            formatter.parse("01/05/2023"),
            formatter.parse("31/05/2023")
        ),
        Vaga(
            "Software Development",
            "Android Developer",
            8000.00,
            "Rio de Janeiro",
            "contato@empresa.com",
            "(21) 2222-2222",
            null,
            formatter.parse("10/05/2023"),
            formatter.parse("30/06/2023")
        ),
        Vaga(
            "Marketing",
            "Social Media Manager",
            4000.00,
            "Belo Horizonte",
            "contato@empresa.com",
            "(31) 3333-3333",
            "Empresa B",
            formatter.parse("15/05/2023"),
            formatter.parse("30/05/2023")
        ),
        Vaga(
            "Data Science",
            "Data Analyst",
            6000.00,
            "Porto Alegre",
            "contato@empresa.com",
            "(51) 4444-4444",
            null,
            formatter.parse("01/06/2023"),
            formatter.parse("30/06/2023")
        ),
        Vaga(
            "Web Development",
            "Full-Stack Developer",
            10000.00,
            "Curitiba",
            "contato@empresa.com",
            "(41) 5555-5555",
            "Empresa C",
            formatter.parse("01/07/2023"),
            formatter.parse("31/07/2023")
        )
    )
    private var sortDateState = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_lista_vagas, container, false)
        recyclerView = rootView.findViewById(R.id.recycler_view_job_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        vagaListAdapter = VagaAdapter(dummyList, object : VagaAdapter.JobItemClickListener {
            override fun onJobItemClick(vaga: Vaga) {
                TODO("Not yet implemented")
            }
        })
        recyclerView.adapter = vagaListAdapter
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.menu)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_sort -> {
                    vagaListAdapter.sortByStartDate(sortDateState)
                    sortDateState = !sortDateState
                    true
                }
                else -> false
            }
        }
    }
}