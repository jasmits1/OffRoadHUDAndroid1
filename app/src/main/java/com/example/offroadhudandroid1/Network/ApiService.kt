package com.example.offroadhudandroid1.Network

import com.example.offroadhudandroid1.Model.LocationModel
import com.example.offroadhudandroid1.Model.RouteModel
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

    fun postNewRoute(route: RouteModel, onResult: (RouteModel?) -> Unit) {
        val retrofit = ApiServiceBuilder.buildService(RestApi::class.java)
        retrofit.postNewRoute(route).enqueue(
                object : Callback<RouteModel> {
                    override fun onFailure(
                            call: Call<RouteModel>,
                            t: Throwable
                    ) {
                        onResult(null)
                    }

                    override fun onResponse(
                            call: Call<RouteModel>,
                            response: Response<RouteModel>
                    ) {
                       val postedRoute = response.body()
                        onResult(postedRoute)
                    }
                }
        )
    }

    fun postRouteComplete(routeName: String, endTime: String, onResult: (RouteModel?) -> Unit) {
        val retrofit = ApiServiceBuilder.buildService(RestApi::class.java)
        val call = retrofit.postEndRoute(routeName, endTime)
        println(call.request().url())
        retrofit.postEndRoute(routeName, endTime).enqueue((
                object : Callback<RouteModel> {
                    override fun onFailure(call: Call<RouteModel>, t: Throwable) {
                        onResult(null)
                    }

                    override fun onResponse(call: Call<RouteModel>, response: Response<RouteModel>) {
                        val endedRoute = response.body()
                        onResult(endedRoute)
                    }
                }
                ))
    }
}