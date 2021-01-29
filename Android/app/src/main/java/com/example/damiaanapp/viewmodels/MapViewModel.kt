package com.example.damiaanapp.viewmodels


import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.damiaanapp.data.local.Node
import com.example.damiaanapp.data.local.Route
import com.example.damiaanapp.repos.NodeRepository
import com.example.damiaanapp.repos.RouteRepository
import com.google.android.gms.maps.GoogleMap
import java.io.ByteArrayInputStream
import java.io.InputStream

//Author: Brent Goubert
class MapViewModel(private val routeRepository: RouteRepository, private val nodeRepository: NodeRepository) : ViewModel()
{
    private var _locationPermissionGranted = MutableLiveData<Boolean>(false)
    val locationPermissionGranted : LiveData<Boolean>
        get() = _locationPermissionGranted

    private var _googleMap = MutableLiveData<GoogleMap>(null)
    val googleMap : MutableLiveData<GoogleMap>
        get() = _googleMap

    private lateinit var _route: LiveData<Route>
    val route : LiveData<Route>
        get() = _route

    private var _inputStream = MutableLiveData<InputStream>()
    val inputStream: LiveData<InputStream>
        get() = _inputStream

    private lateinit var _nodes: LiveData<List<Node>>
    val nodes : LiveData<List<Node>>
        get() = _nodes

    //Author: Brent Goubert
    fun updateLocationPermission(isGranted : Boolean)
    {
        _locationPermissionGranted.value = isGranted
    }

    //Author: Brent Goubert
    fun updateMap(map: GoogleMap)
    {
        _googleMap.value = map
    }

    fun updateRoute(routeId: Int) {
        _route = routeRepository.getRoute(routeId)
        _nodes = nodeRepository.getNodesFromRoute(routeId)

    }

    //Author: Brent Goubert
    fun updateInputStream(stream: ByteArrayInputStream) {
        _inputStream.value = stream
    }
}