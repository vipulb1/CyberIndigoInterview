package com.me.cyberindigointerview.remote

import com.me.cyberindigointerview.model.User
import com.me.cyberindigointerview.model.UsersData
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    fun LOGIN(@Field("email") email: String,
              @Field("password") password: String): Call<User>

    @GET("users?page=2")
    fun USERDATA(): Call<UsersData>
}