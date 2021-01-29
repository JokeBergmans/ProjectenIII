package com.example.damiaanapp.data.local

import androidx.room.Embedded
import androidx.room.Relation

// Authors: Joke Bergmans, Thibaud Steenhaut
data class RouteAndNodes (
    @Embedded
    var route: Route,

    @Relation(parentColumn = "id", entityColumn = "routeId")
    val nodes: List<Node> = emptyList()
)