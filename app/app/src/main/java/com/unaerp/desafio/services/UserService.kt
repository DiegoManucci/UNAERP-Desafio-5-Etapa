package com.unaerp.desafio.services

import com.unaerp.desafio.responses.LoginResponse
import com.unaerp.desafio.responses.RecuperarSenhaResponse
import com.unaerp.desafio.responses.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @FormUrlEncoded
    @POST("users/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("users")
    fun cadastrar(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("type") type: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("users/recuperar-senha")
    fun recuperarSenha(
        @Field("email") email: String,
    ): Call<RecuperarSenhaResponse>

    @GET("users/{id}")
    fun getUser(@Path("id") id: Int): Call<UserResponse>

    @FormUrlEncoded
    @PUT("users/{id}")
    fun updateUser(
        @Path("id") id: Int,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<UserResponse>
}