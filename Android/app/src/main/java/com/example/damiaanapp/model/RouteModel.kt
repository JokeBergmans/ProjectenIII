package com.example.damiaanapp.model

import android.os.Parcelable
import com.example.damiaanapp.data.local.Node
import com.example.damiaanapp.data.local.Route
import kotlinx.android.parcel.Parcelize
import java.util.*

// Authors: Joke Bergmans, Thibaud Steenhaut, Tom Van der Weeën
@Parcelize
data class RouteModel(
    val id: Int,
    val name: String,
    val distance: Int,
    val start: Date,
    val kml: String,
    val nodes : List<NodeModel>
) : Parcelable {
    fun toDatabaseModel(registrationId: Int) : Route {
        return Route(id, name, distance, start, kml, registrationId)
    }
}