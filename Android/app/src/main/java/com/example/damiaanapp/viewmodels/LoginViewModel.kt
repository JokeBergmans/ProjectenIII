package com.example.damiaanapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.damiaanapp.data.remote.LoginRequest
import com.example.damiaanapp.data.remote.LoginResponse
import com.example.damiaanapp.repos.AccountRepository
import com.example.damiaanapp.repos.ParticipantRepository
import com.example.damiaanapp.repos.RegistrationRepository
import com.example.damiaanapp.util.Resource
import com.example.damiaanapp.util.Status

class LoginViewModel(val participantRepository: ParticipantRepository, val registrationRepository: RegistrationRepository, val accountRepository: AccountRepository) : ViewModel() {

    var _email = MutableLiveData<String>()

    var _password = MutableLiveData<String>()

    fun login() = accountRepository.login(LoginRequest(_email.value!!, _password.value!!))

    fun getParticipant(userId: Int) = participantRepository.getParticipant(userId)

    fun getParticipant() = participantRepository.getParticipant()

    fun getRegistrations(userId: Int) = registrationRepository.getRegistrations(userId)

}