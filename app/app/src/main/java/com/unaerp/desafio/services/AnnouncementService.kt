package com.unaerp.desafio.services

import com.unaerp.desafio.responses.AnnouncementResponse
import com.unaerp.desafio.responses.LoginResponse
import com.unaerp.desafio.responses.RecuperarSenhaResponse
import retrofit2.Call
import retrofit2.http.*

interface AnnouncementService {

    @GET("announcements")
    fun getAll(): Call<List<AnnouncementResponse>>

    @GET("announcements/{id}")
    fun getMy(@Path("id") id: Int): Call<List<AnnouncementResponse>>

    @FormUrlEncoded
    @POST("announcements")
    fun criar(
        @Field("area") area: String,
        @Field("remuneration") remuneration: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("description") description: String,
        @Field("city") city: String,
        @Field("advertiser") advertiser: Int,
        @Field("showadvertiser") showAdvertiser: String,
        @Field("startdate") startDate: String,
        @Field("enddate") endDate: String
    ): Call<AnnouncementResponse>

    @DELETE("announcements/{id}")
    fun deletar(@Path("id") id: Int): Call<Unit>
}