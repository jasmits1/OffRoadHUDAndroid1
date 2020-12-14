package com.example.offroadhudandroid1.Model

import com.google.gson.annotations.SerializedName

data class LocationModel(
        @SerializedName("latitude")     val latitude: Double?,
        @SerializedName("longitude")    val longitude: Double?,
        @SerializedName("speedMS")      val speedMS: Float?,
        @SerializedName("dateString")   val dateString: String? = "2020-09-09T00:40:20.861Z",
        @SerializedName("routeName")    var routeName: String? = null
)