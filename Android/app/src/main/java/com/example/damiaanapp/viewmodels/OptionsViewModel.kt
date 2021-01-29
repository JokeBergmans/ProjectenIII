package com.example.damiaanapp.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.damiaanapp.R
import com.example.damiaanapp.data.local.Participant
import com.example.damiaanapp.util.SessionManager
import com.example.damiaanapp.repos.ParticipantRepository
import com.example.damiaanapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//Author: Tom Van der Weeën
class OptionsViewModel(val participantRepository: ParticipantRepository) : ViewModel() {

    private lateinit var _participant: LiveData<Participant>
    val participant: LiveData<Participant>
        get() = _participant

    private var _participantId = MutableLiveData(0)
    val participantId : LiveData<Int>
        get() = _participantId

    var _canBeFollowed = MutableLiveData<Boolean>()
    val canBeFollowed: LiveData<Boolean>
        get() = _canBeFollowed

    fun updateParticipantId(id: Int) {
        _participantId.value = id
        _participant = participantRepository.getParticipant()
    }

    fun updatePreferences() = participantRepository.updatePreferences(_participant.value!!)

}