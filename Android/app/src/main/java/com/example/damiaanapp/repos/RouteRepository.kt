package com.example.damiaanapp.repos

import com.example.damiaanapp.data.local.RouteLocalDataSource

//Authors: Tom Van der Weeën
class RouteRepository(
    val routeLocalDataSource: RouteLocalDataSource
) {
    fun getRoute(id: Int) = routeLocalDataSource.getRoute(id)

    fun getRoutes() = routeLocalDataSource.getRoutes()
}