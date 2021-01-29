package com.example.damiaanapp.UI

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.damiaanapp.R
import com.example.damiaanapp.databinding.ActivityLoginBinding
import com.example.damiaanapp.data.remote.LoginRequest
import com.example.damiaanapp.data.remote.LoginResponse
import com.example.damiaanapp.util.SessionManager
import com.example.damiaanapp.repos.ParticipantRepository
import com.example.damiaanapp.util.Status
import com.example.damiaanapp.viewmodels.LoginViewModel
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager
    private var currentLanguage = "nl"
    private var currentLang: String? = null
    private lateinit var _viewModel: LoginViewModel

    //Author: Tom Van der Weeën

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: LoginViewModel by inject()
        _viewModel = viewModel
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding =  DataBindingUtil.setContentView(this,
            R.layout.activity_login
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.progressBar.visibility = View.GONE
        binding.login.setOnClickListener { login() }
        binding.register.setOnClickListener {register()}
        binding.nl.setOnClickListener{ setLocale("nl") }
        binding.fr.setOnClickListener{ setLocale("fr") }
        //underlineButton(currentLanguage)

        currentLanguage = intent.getStringExtra(currentLang).toString()
        sessionManager = SessionManager(this)
        tryLogin()
    }

    override fun onStart() {
        super.onStart()
        showLogin(true)
    }

    private fun login() {
        showLogin(false)

        _viewModel.login().observe(
            this,
            Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            val id: Int = resource.data!!.id
                            sessionManager.saveUserId(id)
                            sessionManager.saveAuthToken(resource.data.token)
                            tryLogin()
                        }
                        Status.LOADING -> {
                            showLogin(false)
                        }
                        Status.ERROR -> {
                            showToast(getString(R.string.invalid_login_request) + resource.message!!)
                            showLogin(true)
                        }
                    }
                }
            }
        )
    }

    private fun tryLogin(): Boolean {
        return if (sessionManager.fetchAuthToken() != null) {
            finalizeLogin()
            true
        } else
            false
    }

    private fun finalizeLogin() {
        _viewModel.getParticipant(sessionManager.fetchUserId()).observe(this, Observer {part ->
            part?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        _viewModel.getRegistrations(sessionManager.fetchUserId()).observe(this, Observer {
                            it?.let { resource ->
                                when (resource.status) {
                                    Status.SUCCESS -> {
                                        startActivity(Intent(this, MainActivity::class.java))
                                    }
                                    Status.LOADING -> {
                                        showLogin(false)
                                    }
                                    Status.ERROR -> {
                                        showToast(getString(R.string.invalid_login_request) + resource.message!!)
                                        showLogin(true)
                                    }
                                }
                            }
                        })
                    }
                    Status.LOADING -> {
                        showLogin(false)
                    }
                    Status.ERROR -> {
                        showToast(getString(R.string.invalid_login_request) + resource.message!!)
                        showLogin(true)
                    }
                }
            }
        })


    }

    private fun register() {
        val url = "https://evening-hamlet-46004.herokuapp.com/en-US/"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    //Author: Tom Van der Weeën
    private fun setLocale(localeName: String) {
        if (localeName != currentLanguage) {
            underlineButton(localeName)
            val locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(
                this,
                LoginActivity::class.java
            )
            refresh.putExtra(currentLang, localeName)
            startActivity(refresh)
        } else {
            Toast.makeText(
                this@LoginActivity, getString(R.string.lang_already_selected), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
        exitProcess(0)
    }

    //Author: Tom Van der Weeën
    private fun underlineButton(lang: String) {
        when (lang) {
            "nl"-> binding.nl.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            "fr" -> binding.fr.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }
    }

    //Author: Tom Van der Weeën
    private fun showLogin(show: Boolean) {
        if(show)
        {
            binding.progressBar.visibility = View.GONE
            binding.login.visibility = View.VISIBLE
        }
        else {
            binding.progressBar.visibility = View.VISIBLE
            binding.login.visibility = View.GONE
        }
    }
}