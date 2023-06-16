package com.unaerp.desafio.services.config

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8888/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    inline fun <reified T> createService() : T {
        return retrofit.create(T::class.java)
    }
}