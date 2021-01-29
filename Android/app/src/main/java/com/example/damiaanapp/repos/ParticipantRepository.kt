package com.example.damiaanapp.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.example.damiaanapp.data.local.Participant
import com.example.damiaanapp.data.local.ParticipantLocalDataSource
import com.example.damiaanapp.data.remote.LoginResponse
import com.example.damiaanapp.data.remote.ParticipantRemoteDataSource
import com.example.damiaanapp.model.ParticipantModel
import com.example.damiaanapp.util.Resource
import com.example.damiaanapp.util.Status
import com.example.damiaanapp.util.performGetOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//Author: Tom Van der Weeën
class ParticipantRepository(
    val participantRemoteDataSource: ParticipantRemoteDataSource,
    val participantLocalDataSource: ParticipantLocalDataSource
) {
    fun getParticipant(id : Int) = performGetOperation(
        databaseQuery = { participantLocalDataSource.getParticipant(id) },
        networkCall = { participantRemoteDataSource.getParticipant(id) },
        saveCallResult = { participantLocalDataSource.saveParticipant(it) }
    )
    fun getParticipant() = participantLocalDataSource.getParticipant()

    fun updatePreferences(participant: Participant) = liveData(Dispatchers.IO) {
        val response = participantRemoteDataSource.updatePreferences(participant)
        emit(response)
        participantLocalDataSource.updatePreferences(participant)
    }


}