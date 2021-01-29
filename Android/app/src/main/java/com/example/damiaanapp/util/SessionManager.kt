package com.example.damiaanapp.util

import android.content.Context
import android.content.SharedPreferences
import com.example.damiaanapp.R

// Authors: Joke Bergmans
class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_ID = "user_id"
    }

    // Function to save auth token
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }


    // Function to fetch auth token
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    // Function to clear values
    fun clear() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    // Function to save user id
    fun saveUserId(id: Int) {
        val editor = prefs.edit()
        editor.putInt(USER_ID, id)
        editor.apply()
    }

    // Function to fetch user id
    fun fetchUserId(): Int {
        return prefs.getInt(USER_ID, 0)
    }
}