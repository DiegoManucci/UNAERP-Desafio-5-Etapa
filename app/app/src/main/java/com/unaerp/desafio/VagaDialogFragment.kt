package com.unaerp.desafio

import Vaga
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*


class VagaDialogFragment(private val vaga: Vaga) : DialogFragment() {

    override fun onStart() {
        super.onStart()

        val phoneImageView = dialog?.window?.decorView?.findViewById<ImageView>(R.id.phoneImageView)
        phoneImageView?.setOnClickListener {

            val phoneTextView = dialog?.window?.decorView?.findViewById<TextView>(R.id.contactPhoneValueTextView)
            val phone = phoneTextView?.text.toString().replace("[^\\d]".toRegex(), "").toInt()

            //Make a phone call
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

            val inputDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
            val formatter = SimpleDateFormat("dd/MM/yyyy")

            areaValueTextView.text = vaga.area
            descriptionValueTextView.text = vaga.description
            remunerationValueTextView.text = vaga.remuneration.toString()
            cityValueTextView.text = vaga.city
            contactEmailValueTextView.text = vaga.contactEmail
            contactPhoneValueTextView.text = vaga.contactPhone
            advertiserValueTextView.text = vaga.advertiser
            startDateValueTextView.text = formatter.format(inputDateFormat.parse(vaga.startDate.toString()))
            endDateValueTextView.text = formatter.format(inputDateFormat.parse(vaga.endDate.toString()))

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