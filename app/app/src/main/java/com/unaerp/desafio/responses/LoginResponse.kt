package com.unaerp.desafio.responses

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("token")
    val token: String,
    @SerializedName("role")
    val role: String,
)