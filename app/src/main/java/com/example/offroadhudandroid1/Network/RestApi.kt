package com.example.offroadhudandroid1.Network

import com.example.offroadhudandroid1.Model.LocationModel
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Call

interface RestApi {

    @Headers("Content-Type: application/json")
    @POST("location")
    fun postNewLocation(@Body location: LocationModel): Call<LocationModel>
}