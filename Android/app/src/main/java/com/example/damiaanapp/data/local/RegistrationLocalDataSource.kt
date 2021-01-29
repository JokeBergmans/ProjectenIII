package com.example.damiaanapp.data.local

import com.example.damiaanapp.model.RegistrationModel

// Authors: Joke Bergmans, Thibaud Steenhaut
class RegistrationLocalDataSource(val registrationDao: RegistrationDao, val routeDao: RouteDao, val nodeDao: NodeDao) {

    fun getRegistrations() = registrationDao.getAllRegistrations()

    fun getRegistrationByRouteId(routeId: Int) = registrationDao.getRegistrationByRouteId(routeId)

    fun saveRegistrations(list: List<RegistrationModel>) {
        val registrationList = ArrayList<Registration>()
        list.forEach { registrationModel -> registrationList.add(registrationModel.toDatabaseModel())}
        registrationDao.insertAll(registrationList)

        val routeList = ArrayList<Route>()
        list.forEach { registrationModel -> routeList.add(registrationModel.route.toDatabaseModel(registrationModel.id))}
        routeDao.insertAll(routeList)

        val nodeList = ArrayList<Node>()
        list.forEach { registrationModel ->
            val nodes = registrationModel.route.nodes
            nodes.forEach { node ->
                nodeList.add(node.toDatabaseModel(registrationModel.route.id))
            }
        }
        nodeDao.insertAll(nodeList)
    }
}