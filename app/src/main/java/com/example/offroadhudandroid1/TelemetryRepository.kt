package com.example.offroadhudandroid1

import android.content.Context
import com.example.offroadhudandroid1.LiveData.InclineLiveData
import com.example.offroadhudandroid1.LiveData.LocationLiveData
import com.example.offroadhudandroid1.Model.LocationModel
import com.example.offroadhudandroid1.Model.RouteModel
import com.example.offroadhudandroid1.Network.ApiService
import com.example.offroadhudandroid1.Util.DateUtil
import com.example.offroadhudandroid1.db.LocationDao
import com.example.offroadhudandroid1.db.RouteDao
import dagger.hilt.android.qualifiers.ActivityContext
import java.util.*
import javax.inject.Inject


class TelemetryRepository @Inject constructor(
        @ActivityContext private val context : Context,
        private val locationDao: LocationDao,
        private val routeDao: RouteDao
) {

    var currentRouteName: String? = null

    /**
     * @return A LiveData instance containing the most recently received location update.
     */
    fun getLocationData(): LocationLiveData {
        return LocationLiveData(context)
    }

    /**
     * @return A LiveData instance containing the most recently received incline update.
     */
    fun getInclineData(): InclineLiveData {
        return InclineLiveData(context)
    }

    /**
     * Start a new route and save its information in the database.
     *
     * @param routeName the name of the newly started route.
     */
    suspend fun startNewRoute(routeName: String) {
        currentRouteName = routeName
        val dateString = DateUtil.formatDate(Calendar.getInstance().timeInMillis)
        val route = RouteModel(routeName, "jason", true, dateString)
        routeDao.insert(route)
    }

    /**
     * End a current route, and then post the route and associated telemetry to the server.
     */
    suspend fun endCurrentRoute() {
        val activeRoute = routeDao.findActiveRoute()
        activeRoute?.isActive = false
        activeRoute?.endTime = DateUtil.formatDate(Calendar.getInstance().timeInMillis)
        if (activeRoute != null) {
            routeDao.update(activeRoute)
            postRoute(activeRoute)
            postRouteLocations(activeRoute.routeName)
        }
        currentRouteName = null
    }

    /**
     * Save a new location to the database.
     *
     * @param location the newly received location.
     */
    suspend fun saveNewLocation(location: LocationModel) {
        location.routeName = currentRouteName
        locationDao.insert(location)
    }

    /**
     * Post a completed route to the server.
     *
     * @param route the completed route.
     */
    private fun postRoute(route: RouteModel) {
        val apiService = ApiService()
        apiService.postRoute(route) {
            if(it != null)
                println("PostRoute API Call Success!")
            else
                println("PostRoute API Call Failed.")
        }
    }

    /**
     * Post all locations associated with a given route to the server.
     *
     *
     */
    private suspend fun postRouteLocations(routeName: String) {
        val locations = locationDao.getLocationsForRoute(routeName)
        val apiService = ApiService()
        for(location in locations) {
            apiService.postNewLocation(location) {
                if (it == null)
                    println("PostLocation API call success!")
                else
                    println("PostLocation API call error.")
            }
        }
    }

}