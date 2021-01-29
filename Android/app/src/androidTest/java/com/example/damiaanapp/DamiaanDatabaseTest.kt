package com.example.damiaanapp

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.damiaanapp.data.local.*
import com.example.damiaanapp.data.remote.ParticipantRemoteDataSource
import com.example.damiaanapp.model.NodeModel
import com.example.damiaanapp.model.ParticipantModel
import com.example.damiaanapp.model.RouteModel
import com.example.damiaanapp.repos.ParticipantRepository
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*
import kotlin.jvm.Throws

//Author: Tom Van der Weeën
@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {

    private lateinit var participantDao: ParticipantDao
    private lateinit var routeDao: RouteDao
    private lateinit var nodeDao: NodeDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        participantDao = db.participantDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetParticipantDirectlyFromDaoGivesNull() {
        val participant = ParticipantModel(100, false, "test", "test", "test@test.com")
        participantDao.insert(participant.toDatabaseModel())
        val currentParticipant = participantDao.getParticipant(100)
        assertEquals(null, currentParticipant.value?.canBeFollowed)
        assertEquals(null, currentParticipant.value?.email)
    }
}