package com.example.offroadhudandroid1.ViewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.offroadhudandroid1.TelemetryRepository
import com.example.offroadhudandroid1.Model.LocationModel

class DashboardViewModel @ViewModelInject constructor(
        private val telemetryRepository: TelemetryRepository
) : ViewModel() {

    fun getLocationData() = telemetryRepository.getLocationData()

    fun getInclineData() = telemetryRepository.getInclineData()

    fun saveLocationData(location: LocationModel) = telemetryRepository.saveNewLocation(location)

    fun startNewRoute(routeName: String) = telemetryRepository.startNewRoute(routeName)

    fun endCurrentRoute() = telemetryRepository.endCurrentRoute()
}
