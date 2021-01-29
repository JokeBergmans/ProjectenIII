package com.example.damiaanapp.data.local

import com.example.damiaanapp.model.ParticipantModel

//Author: Tom Van der Weeën
class ParticipantLocalDataSource(val participantDao: ParticipantDao) {
    fun getParticipant(id: Int) = participantDao.getParticipant(id)

    fun getParticipant() = participantDao.getParticipant()

    fun saveParticipant(participant: ParticipantModel) {
        participantDao.insert(participant.toDatabaseModel())
    }

    fun updatePreferences(participant: Participant) = participantDao.updateParticipant(participant)
}