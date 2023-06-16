package com.unaerp.desafio.responses

import com.google.gson.annotations.SerializedName
import java.util.Date

data class AnnouncementResponse (
    @SerializedName("idannouncement")
    val idannouncement : Int,
    @SerializedName("area")
    val area: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("advertiser")
    val advertiser: String,
    @SerializedName("showadvertiser")
    val showAdvertiser: String,
    @SerializedName("remuneration")
    val remuneration: Double,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("startdate")
    val startDate: String,
    @SerializedName("enddate")
    val endDate: String
)