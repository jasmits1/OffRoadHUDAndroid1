package com.example.offroadhudandroid1.Network

import com.example.offroadhudandroid1.Model.LocationModel
import com.example.offroadhudandroid1.Model.RouteModel
import okhttp3.Route
import retrofit2.Call
import retrofit2.http.*

interface RestApi {

    @Headers("Content-Type: application/json")
    @POST("location")
    fun postNewLocation(@Body location: LocationModel): Call<LocationModel>

    @Headers("Content-Type: application/json")
    @POST("route")
    fun postNewRoute(@Body route: RouteModel): Call<RouteModel>

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("route/routeName/{routeName}")
    fun postEndRoute(@Path("routeName") routeName: String, @Field("endTime") endTime: String): Call<RouteModel>
}