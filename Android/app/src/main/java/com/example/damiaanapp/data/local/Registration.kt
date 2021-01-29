package com.example.damiaanapp.data.local

import androidx.room.*

// Authors: Joke Bergmans, Thibaud Steenhaut
@Entity(tableName = "registrations")
class Registration(
    @PrimaryKey
    val id: Int,
)

