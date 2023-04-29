package com.unaerp.desafio

import Vaga
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class VagaAdapter(private var jobs: List<Vaga>, private val listener: JobItemClickListener) :
    RecyclerView.Adapter<VagaAdapter.VagaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VagaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_vaga, parent, false)
        return VagaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VagaViewHolder, position: Int) {
        holder.bind(jobs[position], listener)
    }

    override fun getItemCount(): Int {
        return jobs.size
    }

    class VagaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val areaTextView: TextView = itemView.findViewById(R.id.textArea)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textDescription)
        private val locationTextView: TextView = itemView.findViewById(R.id.textLocation)
        private val startDateTextView: TextView = itemView.findViewById(R.id.textStartDate)
        private val endDateTextView: TextView = itemView.findViewById(R.id.textEndDate)

        fun bind(vaga: Vaga, listener: JobItemClickListener) {
            val inputDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
            val formatter = SimpleDateFormat("dd/MM/yyyy")

            areaTextView.text = vaga.area
            descriptionTextView.text = vaga.description
            locationTextView.text = vaga.city
            startDateTextView.text = "Início: " + formatter.format(inputDateFormat.parse(vaga.startDate.toString()))
            endDateTextView.text = "Fim: " + formatter.format(inputDateFormat.parse(vaga.endDate.toString()))

            itemView.setOnClickListener {
                listener.onJobItemClick(vaga)
            }
        }
    }

    interface JobItemClickListener {
        fun onJobItemClick(vaga: Vaga)
    }

    fun sortByStartDate(desc: Boolean) {
        if (desc){
            jobs = jobs.sortedByDescending { it.startDate }
        } else {
            jobs = jobs.sortedBy { it.startDate }
        }
        notifyDataSetChanged()
    }

    fun setFilteredList(filteredList: MutableList<Vaga>) {
        jobs = filteredList
        notifyDataSetChanged()
    }
}