package com.example.damiaanapp.data.remote

import com.example.damiaanapp.data.BaseDataSource

class NodeScanRemoteDataSource(val apiService: DamiaanService) : BaseDataSource() {
    suspend fun addScan(routeId : Int, registrationId: Int, nodeId: Int) = getResult { apiService.addScanned(routeId, registrationId, nodeId) }
}