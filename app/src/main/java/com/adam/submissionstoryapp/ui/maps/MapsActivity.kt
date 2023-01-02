package com.adam.submissionstoryapp.ui.maps

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.adam.submissionstoryapp.R
import com.adam.submissionstoryapp.databinding.ActivityMapsBinding
import com.adam.submissionstoryapp.network.responses.ListStoryItem
import com.adam.submissionstoryapp.ui.ViewModelFactory
import com.adam.submissionstoryapp.ui.home.StoryViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.adam.submissionstoryapp.utils.Result

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val viewModel: StoryViewModel by viewModels{ ViewModelFactory(this) }
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var listStoryData: List<ListStoryItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listStoryData = listOf()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true

        getMyLocation()
        setMapStyle()
        addManyMarker()
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun addManyMarker(){
        viewModel.getDataLocation().observe(this){ result ->
            if (result != null) {
                when(result) {
                    is Result.Success -> {
                        listStoryData = result.data.listStory
                        listStoryData.forEach { story ->
                            val latLng = LatLng(story.lat, story.lon)

                            mMap.addMarker(
                                MarkerOptions()
                                    .position(latLng)
                                    .title(story.name)
                                    .snippet(story.description)
                            )
                        }
                    }
                    is Result.Error -> {
                        val errorMessage = result.error
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                    }
                    is Result.Loading -> {

                    }
                }
            } else {
                viewModel.error.observe(this) { error ->
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}