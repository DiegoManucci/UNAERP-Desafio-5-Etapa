package com.unaerp.desafio.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Vaga(
    val id: Int,
    val area: String,
    val description: String,
    val remuneration: Double,
    val city: String,
    val contactEmail: String,
    val contactPhone: String,
    val advertiser: String?,
    val startDate: String,
    val endDate: String
)