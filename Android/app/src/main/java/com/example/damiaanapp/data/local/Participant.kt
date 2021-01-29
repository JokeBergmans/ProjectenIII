package com.example.damiaanapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//Author: Tom Van der Weeën
@Entity(tableName = "participants")
data class Participant (
    @PrimaryKey
    val id: Int,
    var canBeFollowed: Boolean,
    val name: String,
    val firstName: String,
    val email: String
)