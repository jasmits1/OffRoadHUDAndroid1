package com.example.offroadhudandroid1

import android.app.Application
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.example.offroadhudandroid1.LiveData.InclineLiveData
import com.example.offroadhudandroid1.LiveData.LocationLiveData
import com.example.offroadhudandroid1.Model.LocationModel
import com.example.offroadhudandroid1.Model.RouteModel
import com.example.offroadhudandroid1.Network.ApiService
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ActivityContext
import java.util.*
import javax.inject.Inject

class TelemetryRepository @Inject constructor(
        @ActivityContext private val context : Context
) {

    var currentRouteName: String? = null

    fun getLocationData(): LocationLiveData {
        return LocationLiveData(context)
    }

    fun getInclineData(): InclineLiveData {
        return InclineLiveData(context)
    }

    fun startNewRoute(routeName: String) {
        // TODO: Redo the date properly
        val route = RouteModel(routeName, "jason", Calendar.getInstance().time.toString())
        currentRouteName = routeName
        val apiService = ApiService()
        apiService.postNewRoute(route) {
            if(it?.routeName != null) {
                println("New route API call success!")
            } else {
                println("New route API call failed.")
            }
        }
    }

    fun endCurrentRoute() {
        // TODO: Redo the date properly
        val apiService = ApiService()
        apiService.postRouteComplete(currentRouteName!!, Calendar.getInstance().time.toString()) {
            if(it?.routeName != null) {
                println("End route API call success!")
                currentRouteName = null
            } else {
                println("End route API call failed")
                currentRouteName = null
            }
        }
    }




    fun saveNewLocation(location: LocationModel) {
        if(!currentRouteName.isNullOrEmpty())
            location.routeName = currentRouteName
        val apiService = ApiService()
        apiService.postNewLocation(location) {
            if (it?.latitude != null) {
                println("Api Call Success!")
            } else {
                println("Api call error!")
            }
        }
    }
}