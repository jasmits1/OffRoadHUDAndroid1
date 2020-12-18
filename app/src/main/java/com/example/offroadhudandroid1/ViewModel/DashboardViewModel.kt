package com.example.offroadhudandroid1.ViewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.offroadhudandroid1.TelemetryRepository
import com.example.offroadhudandroid1.Model.LocationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DashboardViewModel @ViewModelInject constructor(
        private val telemetryRepository: TelemetryRepository
) : ViewModel() {

    fun getLocationData() = telemetryRepository.getLocationData()

    fun getInclineData() = telemetryRepository.getInclineData()

    fun saveLocationData(location: LocationModel) = GlobalScope.launch(Dispatchers.Main) {  telemetryRepository.saveNewLocation(location)    }

    fun startNewRoute(routeName: String) = GlobalScope.launch(Dispatchers.Main) {   telemetryRepository.startNewRoute(routeName)    }

    fun endCurrentRoute() = GlobalScope.launch(Dispatchers.Main) {  telemetryRepository.endCurrentRoute() }
}
