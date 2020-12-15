package com.example.offroadhudandroid1.Model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["dateString"])
data class LocationModel(
        @field:SerializedName("latitude")
        val latitude: Double?,
        @field:SerializedName("longitude")
        val longitude: Double?,
        @field:SerializedName("speedMS")
        val speedMS: Float?,
        @field:SerializedName("dateString")
        val dateString: String,
        @field:SerializedName("routeName")
        var routeName: String? = null
)