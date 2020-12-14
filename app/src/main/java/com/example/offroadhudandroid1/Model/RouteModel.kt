package com.example.offroadhudandroid1.Model

import com.google.gson.annotations.SerializedName

data class RouteModel(
        @SerializedName("routeName")    val routeName: String?,
        @SerializedName("user")         val user: String?,
        @SerializedName("startTime")    val startTime: String? = null,
        @SerializedName("endTime")      val endTime: String? = null
)