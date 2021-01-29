package com.example.damiaanapp.viewmodels

import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.damiaanapp.data.local.Node
import com.example.damiaanapp.data.local.Registration
import com.example.damiaanapp.data.local.RegistrationAndRoute
import com.example.damiaanapp.data.local.Route
import com.example.damiaanapp.repos.NodeRepository
import com.example.damiaanapp.repos.RegistrationRepository
import com.google.android.gms.maps.GoogleMap
import java.io.ByteArrayInputStream
import java.io.InputStream

class QRScanViewModel(val nodeRepository: NodeRepository, val registrationRepository: RegistrationRepository) : ViewModel() {
    private var _routeId = MutableLiveData(0)
    val routeId : LiveData<Int>
        get() = _routeId

    private var _participantId = MutableLiveData(0)
    val participantId : LiveData<Int>
        get() = _participantId

    private lateinit var _nodes: LiveData<List<Node>>
    val nodes : LiveData<List<Node>>
        get() = _nodes

    private var _nodeId = MutableLiveData(0)
    val nodeId : LiveData<Int>
        get() = _nodeId

    private lateinit var _registration: LiveData<RegistrationAndRoute>
    val registration: LiveData<RegistrationAndRoute>
        get() = _registration

    fun updateRouteId(id : Int)
    {
        _routeId.value = id
        _nodes = nodeRepository.getNodesFromRoute(id)
        _registration = registrationRepository.getRegistrationByRouteId(id)
    }


    fun updateParticipantId(id : Int)
    {
        _participantId.value = id
    }

    fun updateNodeId(id: Int)
    {
        _nodeId.value = id
    }

    fun validateNodeId(): Boolean {
        val node = nodes.value?.find { it.nodeId == nodeId.value }
        return node != null
    }

    fun addScan() = nodeRepository.addScan(routeId.value!!, registration.value!!.registration.id, nodeId.value!!)

}