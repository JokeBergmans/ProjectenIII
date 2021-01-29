package com.example.damiaanapp.repos

import com.example.damiaanapp.data.local.RegistrationLocalDataSource
import com.example.damiaanapp.data.remote.RegistrationRemoteDataSource
import com.example.damiaanapp.util.performGetOperation

// Authors: Joke Bergmans, Thibaud Steenhaut
class RegistrationRepository(
    val registrationRemoteDataSource: RegistrationRemoteDataSource,
    val registrationLocalDataSource: RegistrationLocalDataSource
    ) {

        fun getRegistrations(id: Int) = performGetOperation(
            databaseQuery = { registrationLocalDataSource.getRegistrations() },
            networkCall = { registrationRemoteDataSource.getRegistrations(id) },
            saveCallResult = { registrationLocalDataSource.saveRegistrations(it) }
        )

        fun getRegistrationByRouteId(routeId: Int) = registrationLocalDataSource.getRegistrationByRouteId(routeId)


    }
