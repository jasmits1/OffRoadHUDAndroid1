package com.example.offroadhudandroid1.Network

import com.example.offroadhudandroid1.Model.LocationModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ApiService {
    fun postNewLocation(location: LocationModel, onResult: (LocationModel?) -> Unit) {
        val retrofit = ApiServiceBuilder.buildService(RestApi::class.java)
        retrofit.postNewLocation(location).enqueue(
            object : Callback<LocationModel> {
                override fun onFailure(
                    call: Call<LocationModel>,
                    t: Throwable
                ) {
                    onResult(null)
                }

                override fun onResponse(
                    call: Call<LocationModel>,
                    response: Response<LocationModel>
                ) {
                    val postedLocation = response.body()
                    onResult(postedLocation)
                }
            }
        )
    }
}