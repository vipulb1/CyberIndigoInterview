package com.me.cyberindigointerview.model

import com.google.gson.annotations.SerializedName

data class UsersData(
    @SerializedName("support")
    val support: supports,
    @SerializedName("data")
    val userInfo: ArrayList<UserInfo>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("per_page")

    val per_page: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val total_pages: Int
){
    data class UserInfo(
        @SerializedName("avatar")
        val avatar: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("first_name")
        val first_name: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("last_name")
        val last_name: String
    )
    data class supports(
        @SerializedName("text")
        val text: String,
        @SerializedName("url")
        val url: String
    )
}