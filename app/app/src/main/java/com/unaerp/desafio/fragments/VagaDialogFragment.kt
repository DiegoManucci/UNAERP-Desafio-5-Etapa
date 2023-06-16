package com.unaerp.desafio.fragments

import com.unaerp.desafio.model.Vaga
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.unaerp.desafio.R
import com.unaerp.desafio.responses.AnnouncementResponse
import com.unaerp.desafio.services.AnnouncementService
import com.unaerp.desafio.services.config.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class VagaDialogFragment(private val vaga: Vaga) : DialogFragment() {

    private var deleteButtonVisible: Boolean = false

    fun setDeleteButtonVisible(visible: Boolean) {
        deleteButtonVisible = visible
    }

    override fun onStart() {
        super.onStart()

        val phoneImageView = dialog?.window?.decorView?.findViewById<ImageView>(R.id.phoneImageView)
        phoneImageView?.setOnClickListener {

            val phoneTextView = dialog?.window?.decorView?.findViewById<TextView>(R.id.contactPhoneValueTextView)
            val phone = phoneTextView?.text.toString().replace("[^\\d]".toRegex(), "")

            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone))
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    1
                )
            } else {
                startActivity(intent)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)

            val view = LayoutInflater.from(context).inflate(R.layout.fragment_vaga_dialog, null)

            builder.setView(view)

            val areaValueTextView = view.findViewById<TextView>(R.id.areaValueTextView)
            val descriptionValueTextView = view.findViewById<TextView>(R.id.descriptionValueTextView)
            val remunerationValueTextView = view.findViewById<TextView>(R.id.remunerationValueTextView)
            val cityValueTextView = view.findViewById<TextView>(R.id.cityValueTextView)
            val contactEmailValueTextView = view.findViewById<TextView>(R.id.contactEmailValueTextView)
            val contactPhoneValueTextView = view.findViewById<TextView>(R.id.contactPhoneValueTextView)
            val advertiserValueTextView = view.findViewById<TextView>(R.id.advertiserValueTextView)
            val startDateValueTextView = view.findViewById<TextView>(R.id.startDateValueTextView)
            val endDateValueTextView = view.findViewById<TextView>(R.id.endDateValueTextView)

            areaValueTextView.text = vaga.area
            descriptionValueTextView.text = vaga.description
            remunerationValueTextView.text = vaga.remuneration.toString()
            cityValueTextView.text = vaga.city
            contactEmailValueTextView.text = vaga.contactEmail
            contactPhoneValueTextView.text = vaga.contactPhone
            advertiserValueTextView.text = vaga.advertiser
            startDateValueTextView.text = vaga.startDate
            endDateValueTextView.text = vaga.endDate

            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onStart()
            }
        }
    }
}