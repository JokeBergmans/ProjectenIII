package com.example.damiaanapp.data.remote

import com.example.damiaanapp.data.BaseDataSource

// Authors: Joke Bergmans, Thibaud Steenhaut
class RegistrationRemoteDataSource(val apiService: DamiaanService) : BaseDataSource() {
    suspend fun getRegistrations(id: Int) = getResult { apiService.getRegistrations(id) }
}