package com.example.damiaanapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.damiaanapp.data.local.Route
import com.example.damiaanapp.repos.RouteRepository
import com.example.damiaanapp.util.Status
import kotlinx.coroutines.*

//Author: Tom Van der Weeën, Brent Goubert
class RoutesViewModel(val routeRepository: RouteRepository) : ViewModel() {
    val routes: LiveData<List<Route>> = routeRepository.getRoutes()

    private val _navigateToMapRouteId = MutableLiveData<Int>()
    val navigateToMapRouteId : LiveData<Int>
        get() = _navigateToMapRouteId

    private val _navigateToMapRouteKml = MutableLiveData<String>()
    val navigateToMapRouteKml : LiveData<String>
        get() = _navigateToMapRouteKml


    //Author: Brent Goubert
    fun onRouteClicked(routeId : Int, kml: String){
        _navigateToMapRouteKml.value = kml
        _navigateToMapRouteId.value = routeId
    }

    //Author: Brent Goubert
    fun onMapFragmentNavigated() {
        _navigateToMapRouteKml.value = null
        _navigateToMapRouteId.value = null
    }
}