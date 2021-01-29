package com.example.damiaanapp

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.damiaanapp.model.NodeModel
import com.example.damiaanapp.model.RouteModel
import com.example.damiaanapp.util.DateConverter
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.Calendar.getInstance

//Author: Tom Van der Weeën
@RunWith(AndroidJUnit4::class)
class AdapterTest {

    private val dateConverter: DateConverter = DateConverter()

    @Test
    fun testAddKmToDistance() {
        val node = NodeModel(4, "test", "7", "Gent", 9000, 23.7467, 76.34563, "testinfo")
        val nodes = listOf(node)
        val route = RouteModel(1, "testroute", 15, Date(), "testkml", nodes)
    }

    @Test
    fun testDateConverter() {
        val value: Long = 1510500494000
        val comp: Date = Date.from(Instant.ofEpochSecond(1510500494)
            .atZone(ZoneId.systemDefault())
            .toInstant())
        val result = dateConverter.toDate(value)

        assertEquals(comp.time, result.time)
    }
}