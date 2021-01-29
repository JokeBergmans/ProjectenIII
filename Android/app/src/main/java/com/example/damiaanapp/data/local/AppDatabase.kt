package com.example.damiaanapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Authors: Joke Bergmans, Thibaud Steenhaut, Tom Van der Weeën
@Database(entities = [Registration::class, Route::class, Node::class, Participant::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun registrationDao(): RegistrationDao
    abstract fun routeDao(): RouteDao
    abstract fun nodeDao(): NodeDao
    abstract fun participantDao(): ParticipantDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "damiaanDB")
                .fallbackToDestructiveMigration()
                .build()
    }
}