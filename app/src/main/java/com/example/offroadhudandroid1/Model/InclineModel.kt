package com.example.offroadhudandroid1.Model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["dateString"])
data class InclineModel(
    @field:SerializedName("x")
    val x: Float,
    @field:SerializedName("y")
    val y: Float,
    @field:SerializedName("z")
    val z: Float,
    @field:SerializedName("pitchDegrees")
    val pitchDegrees: Int,
    @field:SerializedName("rollDegrees")
    val rollDegrees: Int,
    @field:SerializedName("dateString")
    val dateString: String
)