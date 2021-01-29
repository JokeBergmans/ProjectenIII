package com.example.damiaanapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Authors: Joke Bergmans, Thibaud Steenhaut
@Entity(tableName = "nodes")
class Node(
    @PrimaryKey
    val nodeId: Int,
    val street: String,
    val number: String?,
    val city: String?,
    val postalCode: Int,
    val longitude: Double,
    val latitude: Double,
    val info: String,
    val routeId: Int
)