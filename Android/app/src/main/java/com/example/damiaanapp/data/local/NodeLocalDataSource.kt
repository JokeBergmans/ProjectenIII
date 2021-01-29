package com.example.damiaanapp.data.local

class NodeLocalDataSource(val nodeDao: NodeDao) {
    fun getNodesFromRoute(routeId: Int) = nodeDao.getNodesFromRoute(routeId)
}