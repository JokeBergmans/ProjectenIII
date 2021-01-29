package com.example.damiaanapp.model

import android.os.Parcelable
import com.example.damiaanapp.data.local.Participant
import kotlinx.android.parcel.Parcelize

//Authors: Tom Van der Weeën
@Parcelize
data class ParticipantModel(
    val id: Int,
    val canBeFollowed: Boolean,
    val name: String,
    val firstName: String,
    val email: String
) : Parcelable{
    fun toDatabaseModel(): Participant {
        return Participant(id, canBeFollowed, name, firstName, email)
    }
}