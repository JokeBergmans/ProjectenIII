package com.example.damiaanapp

import android.view.Gravity
import android.view.View
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.damiaanapp.UI.MainActivity
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


private const val EMAIL = "tom@vdw.com"
private const val PWD = "Tom123456$"
private const val PACKAGE_NAME = "com.example.damiaanapp"

//Author: Tom Van der Weeën
@RunWith(AndroidJUnit4::class)
class UiTests {

    /* Instantiate an ActivityScenarioRule object. */
    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun checkEmptyListMessage() {

        onView(withId(R.id.empty_view))
            .check(isDisplayed(withText("U ben nog niet ingeschreven voor een route. Schrijf u in op Onze website")))

    }

    @Test
    fun checkLogout() {

        onView(withId(R.id.drawerLayout))
            .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
            .perform(DrawerActions.open()); // Open Drawer

        onView(withId(R.id.logout))
            .perform(click())

        intended(
            allOf(
                hasComponent(hasShortClassName(".UI.LoginActivity")),
                toPackage(PACKAGE_NAME)
            )
        )
    }

    @Test
    fun checkNavDrawer() {
        onView(withId(R.id.sosFragment))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.drawerLayout))
            .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
            .perform(DrawerActions.open()); // Open Drawer

        onView(withId(R.id.sosFragment))
            .perform(click())

        onView(withId(R.id.sos_call_em))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    private fun isDisplayed(matcher: Matcher<View> = Matchers.any(View::class.java)) = matches(
        allOf(
            ViewMatchers.isDisplayed(),
            matcher
        )
    )
}