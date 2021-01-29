package com.example.damiaanapp.model

import android.os.Parcelable
import com.example.damiaanapp.data.local.Node
import com.example.damiaanapp.data.local.Route
import kotlinx.android.parcel.Parcelize

// Authors: Joke Bergmans, Thibaud Steenhaut
@Parcelize
data class NodeModel (
    val id: Int,
    val street: String,
    val number: String?,
    val city: String?,
    val postalCode: Int,
    val longitude: Double,
    val latitude: Double,
    val info: String
) : Parcelable {
    fun toDatabaseModel(routeId: Int) : Node {
        return Node(id, street, number, city, postalCode, longitude, latitude, info, routeId)
    }
}