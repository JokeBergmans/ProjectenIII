package com.example.damiaanapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.damiaanapp.util.DateConverter
import java.util.*

// Authors: Joke Bergmans, Thibaud Steenhaut, Tom Van der Weeën
@Entity(tableName = "routes")
@TypeConverters(DateConverter::class)
data class Route(
    @PrimaryKey
    val id: Int,
    val name: String,
    val distance: Int,
    val start: Date,
    val kml: String,
    val registrationId: Int
)