package com.unaerp.desafio.responses

import com.google.gson.annotations.SerializedName

data class RecuperarSenhaResponse (
    @SerializedName("message")
    val message: String
)