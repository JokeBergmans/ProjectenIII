package com.example.damiaanapp.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.damiaanapp.R
import com.example.damiaanapp.databinding.ActivityQRscanBinding
import com.example.damiaanapp.util.SessionManager
import com.example.damiaanapp.util.Status
import com.example.damiaanapp.viewmodels.QRScanViewModel
import com.google.zxing.integration.android.IntentIntegrator
import org.koin.android.ext.android.inject

class QRscanActivity : AppCompatActivity() {

    private var qrScan: IntentIntegrator? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityQRscanBinding
    private lateinit var _viewModel: QRScanViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: QRScanViewModel by inject()
        _viewModel = viewModel
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_q_rscan
        )
        sessionManager = SessionManager(this)

        _viewModel.updateRouteId(intent.getIntExtra("routeid", 0))
        _viewModel.updateParticipantId(sessionManager.fetchUserId())

        _viewModel.nodes.observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                qrScan = IntentIntegrator(this)
                qrScan!!.setOrientationLocked(false)
                qrScan!!.initiateScan()
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result.contents != null) {
            val nodeId = result.contents.toInt()
            _viewModel.updateNodeId(nodeId)
            if (_viewModel.validateNodeId()) {
                _viewModel.registration.observe(this, Observer {
                    if (it != null) {
                        _viewModel.addScan().observe(this, Observer {res ->
                            if (res.status == Status.SUCCESS)
                                showToast(getString(R.string.updated_location))
                            else
                                showToast(getString(R.string.error))
                        })
                    }
                })
            }
            else
                showToast(getString(R.string.invalid_qr_code))
            val intent = Intent(this, QRscanActivity::class.java)
            intent.putExtra("routeid", _viewModel.routeId.value)
            startActivity(intent)

        } else {
            showToast(getString(R.string.error))
        }
        startActivity(Intent(this, MainActivity::class.java))
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}