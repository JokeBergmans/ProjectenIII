package com.example.damiaanapp.model

import android.os.Parcelable
import com.example.damiaanapp.data.local.Registration
import kotlinx.android.parcel.Parcelize

// Authors: Joke Bergmans, Thibaud Steenhaut
@Parcelize
data class RegistrationModel (
    val id: Int,
    val route: RouteModel
) : Parcelable {
    fun toDatabaseModel() : Registration {
        return Registration(id)
    }
}