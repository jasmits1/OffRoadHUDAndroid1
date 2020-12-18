package com.example.offroadhudandroid1.Model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["routeName"])
data class RouteModel(
        @field:SerializedName("routeName")
        val routeName: String,
        @field:SerializedName("user")
        val user: String?,
        @field:SerializedName("isActive")
        var isActive: Boolean,
        @field:SerializedName("startTime")
        val startTime: String? = null,
        @field:SerializedName("endTime")
        var endTime: String? = null
)