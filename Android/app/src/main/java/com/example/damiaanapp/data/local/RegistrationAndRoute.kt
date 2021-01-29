package com.example.damiaanapp.data.local

import androidx.room.Embedded
import androidx.room.Relation

// Authors: Joke Bergmans, Thibaud Steenhaut
data class RegistrationAndRoute (
    @Embedded
    var registration: Registration,

    @Relation(parentColumn = "id", entityColumn = "registrationId")
    val route: Route
)
