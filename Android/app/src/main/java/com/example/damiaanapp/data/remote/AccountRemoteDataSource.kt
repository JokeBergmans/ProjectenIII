package com.example.damiaanapp.data.remote

import com.example.damiaanapp.data.BaseDataSource

class AccountRemoteDataSource(val apiService: DamiaanService) : BaseDataSource() {

    suspend fun login(request: LoginRequest) = getResult { apiService.login(request) }
}