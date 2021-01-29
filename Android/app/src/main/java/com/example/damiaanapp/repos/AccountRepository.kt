package com.example.damiaanapp.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.damiaanapp.data.remote.AccountRemoteDataSource
import com.example.damiaanapp.data.remote.LoginRequest
import com.example.damiaanapp.data.remote.LoginResponse
import com.example.damiaanapp.util.Resource
import com.example.damiaanapp.util.Status
import kotlinx.coroutines.Dispatchers

class AccountRepository(val accountRemoteDataSource: AccountRemoteDataSource) {

    fun login(request: LoginRequest) : LiveData<Resource<LoginResponse>> = liveData(Dispatchers.IO) {
        val response = accountRemoteDataSource.login(request)
        emit(response)
    }
}