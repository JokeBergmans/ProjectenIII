package com.example.damiaanapp.repos

import androidx.lifecycle.liveData
import com.example.damiaanapp.data.local.NodeLocalDataSource
import com.example.damiaanapp.data.remote.NodeScanRemoteDataSource
import kotlinx.coroutines.Dispatchers

class NodeRepository(
    val nodeScanRemoteDataSource: NodeScanRemoteDataSource,
    val nodeLocalDataSource: NodeLocalDataSource
) {
    fun getNodesFromRoute(routeId: Int) = nodeLocalDataSource.getNodesFromRoute(routeId)

    fun addScan(routeId: Int, registrationId: Int, nodeId: Int) = liveData(Dispatchers.IO) {
        val response = nodeScanRemoteDataSource.addScan(routeId, registrationId, nodeId)
        emit(response)
    }
}