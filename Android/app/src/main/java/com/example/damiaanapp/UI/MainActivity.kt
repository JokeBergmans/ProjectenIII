package com.example.damiaanapp.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.damiaanapp.R
import com.example.damiaanapp.databinding.ActivityMainBinding
import com.example.damiaanapp.util.SessionManager
import com.example.damiaanapp.util.Status
import com.example.damiaanapp.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.nav_header.view.*
import org.koin.android.ext.android.inject
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var _viewModel: LoginViewModel
    private var currentLang: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        drawerLayout = binding.drawerLayout

        // navigation
        val navController = this.findNavController(R.id.myNavHostFragment)
        // add drawer to actionBar
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, args: Bundle? ->
            if (nd.id == nc.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
        // add drawer functionality
        NavigationUI.setupWithNavController(binding.navView, navController)


        val header = binding.navView.getHeaderView(0)
        val viewModel: LoginViewModel by inject()
        _viewModel = viewModel
        viewModel.getParticipant().observe(this, Observer {
            if (it != null) {
                header.account_email.text = it.email
                header.account_firstname.text = "${it.firstName} ${it.name}"
            }

        })

        // logout when menu item "Logout" is selected
        binding.navView.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            logout()
            true
        }
    }

    // add up button to actionBar
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    private fun logout() {
        sessionManager.clear()
        // TODO: empty db
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    fun changeLanguage(lang: String) {
        val locale = Locale(lang)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)
        val refresh = Intent(
            this,
            MainActivity::class.java
        )
        refresh.putExtra(currentLang, lang)
        startActivity(refresh)
    }

}