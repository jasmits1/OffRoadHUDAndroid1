package com.example.offroadhudandroid1.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.offroadhudandroid1.LiveData.InclineLiveData
import com.example.offroadhudandroid1.LiveData.LocationLiveData

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val locationData = LocationLiveData(application)
    private val inclineData = InclineLiveData(application)

    fun getLocationData() = locationData

    fun getInclineData() = inclineData

}