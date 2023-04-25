package com.unaerp.desafio

import java.util.*

data class Vaga(
    val area: String,
    val description: String,
    val remuneration: Double,
    val city: String,
    val contactEmail: String,
    val contactPhone: String,
    val advertiser: String?,
    val startDate: Date,
    val endDate: Date
)