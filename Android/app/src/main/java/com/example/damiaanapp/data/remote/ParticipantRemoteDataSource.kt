package com.example.damiaanapp.data.remote

import com.example.damiaanapp.data.BaseDataSource
import com.example.damiaanapp.data.local.Participant
import com.example.damiaanapp.model.ParticipantModel

// Authors: Joke Bergmans, Thibaud Steenhaut, Tom Van der Weeën
class ParticipantRemoteDataSource(val apiService: DamiaanService) : BaseDataSource() {
    suspend fun getParticipant(id: Int) = getResult { apiService.getParticipant(id) }

    suspend fun updatePreferences(participant: Participant) = getResult {
        apiService.updatePreferences(
            participant.id,
            ParticipantModel(
                participant.id,
                participant.canBeFollowed,
                participant.name,
                participant.firstName,
                participant.email
            )
        )
    }
}