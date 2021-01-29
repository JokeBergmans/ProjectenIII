package com.example.damiaanapp.UI

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.damiaanapp.viewmodels.MapViewModel
import com.example.damiaanapp.R
import com.example.damiaanapp.databinding.FragmentMapBinding
import com.example.damiaanapp.viewmodels.LoginViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.data.kml.KmlLayer
import com.google.maps.android.data.kml.KmlLineString
import com.google.maps.android.data.kml.KmlPoint
import org.koin.android.ext.android.inject
import java.io.ByteArrayInputStream


class MapFragment : Fragment(), OnMapReadyCallback {

    private var cameraPermissionGranted: Boolean = false
    private var lastKnownLocation: Location? = null

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var _viewModel: MapViewModel

    //Author: Brent Goubert
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMapBinding.inflate(inflater, container, false)
        binding.buttonQrscan.setOnClickListener {
            this.findNavController().navigate(
                MapFragmentDirections.actionMapFragmentToQrScanFragment())
        }


        var args = MapFragmentArgs.fromBundle(requireArguments())

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireContext())


        val viewModel: MapViewModel by inject()
        _viewModel = viewModel
        _viewModel.updateRoute(args.routeID)

        binding.buttonQrscan.setOnClickListener{ startQrScan()}


        _viewModel.route.observe(viewLifecycleOwner, Observer {
            _viewModel.updateInputStream(ByteArrayInputStream(Base64.decode(it.kml, Base64.DEFAULT)))
            binding.mapView.getMapAsync(this)
        })

        _viewModel.nodes.observe(viewLifecycleOwner, Observer {
            _viewModel.googleMap.observe(viewLifecycleOwner, Observer {
                if (_viewModel.googleMap.value != null)
                    addMarkers(_viewModel.googleMap.value!!)
            })
        })

        return binding.root

    }

    private fun startQrScan() {
        getCameraPermission()
        val intent = Intent(activity,  QRscanActivity::class.java)
        _viewModel.route.observe(viewLifecycleOwner, Observer { route ->
            Log.i("MapFragment", "" + route.id)
            intent.putExtra("routeid", route.id)
            startActivity(intent)
        })

    }

    //Author: Brent Goubert
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMapReady(map: GoogleMap) {
        map.let {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            _viewModel.updateMap(map)
            getLocationPermission()
            val layer = KmlLayer(_viewModel.googleMap.value,  _viewModel.inputStream.value, this.requireContext())
            layer.addLayerToMap()
            updateLocationUI()
        }
    }

    //Author: Brent Goubert
    private fun addMarkers(googleMap: GoogleMap) {
        //bitmaps voor custom icons ophalen
        val icons: Map<String, Bitmap> = getMarkerIcons()

        val builder = LatLngBounds.builder()
            for (node in _viewModel.nodes.value!!) {
                val latLng = LatLng(node.latitude, node.longitude)
                builder.include(latLng)
                val marker = googleMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            node.latitude,
                            node.longitude
                        )
                    ).title(node.street).snippet(node.info)
                )
                val string = changeInfoToIconString(node.info)
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(icons[string]))
            }
            _viewModel.googleMap.value?.moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                    builder.build(),
                    100
                )
            )
    }

    //Author: Brent Goubert
    private fun getMarkerIcons(): Map<String, Bitmap> {
        val listIcons: MutableMap<String, Bitmap> = mutableMapOf()
        val iconsNames: List<String> = mutableListOf("qr", "food", "start", "stop")

        //Iterate over list of icon names and add each of them to the bitmap map
        iconsNames.forEach {
            val qbitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(
                    resources,
                    resources.getIdentifier(it, "drawable", this.requireContext().packageName)
                ), 100, 100, false
            )
            listIcons[it] = qbitmap
        }
        return listIcons
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            _viewModel.updateLocationPermission(true)
        } else {
            ActivityCompat.requestPermissions(
                this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }
    private fun getCameraPermission() {
        /*
         * Request camera permission to scan a QR code.
         */
        if (ContextCompat.checkSelfPermission(this.requireContext(),
                Manifest.permission.CAMERA
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            cameraPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this.requireActivity(), arrayOf(Manifest.permission.CAMERA),
                PERMISSIONS_REQUEST_ACCESS_CAMERA
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        _viewModel.updateLocationPermission(false)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    _viewModel.updateLocationPermission(true)
                }
            }
        }
        updateLocationUI()
    }

    private fun updateLocationUI() {
        if (_viewModel.googleMap.value == null) {
            return
        }
        try {
            if (_viewModel.locationPermissionGranted.value!!) {
                _viewModel.googleMap.value?.isMyLocationEnabled = true
                _viewModel.googleMap.value?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                _viewModel.googleMap.value?.isMyLocationEnabled = false
                _viewModel.googleMap.value?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private const val PERMISSIONS_REQUEST_ACCESS_CAMERA = 1
    }

    //Author: Brent Goubert
    private fun changeInfoToIconString(info : String) : String
    {
        return when (info) {
            "Eetstand" -> "food"
            "Start" -> "start"
            "Einde" -> "stop"
            else -> "qr"
        }
    }
}