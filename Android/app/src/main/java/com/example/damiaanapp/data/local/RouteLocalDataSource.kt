package com.example.damiaanapp.data.local

//Authors: Tom Van der Weeën
class RouteLocalDataSource(val routeDao: RouteDao) {

    fun getRoute(id: Int) = routeDao.getRoute(id)

    fun getRoutes() = routeDao.getRoutes()
}