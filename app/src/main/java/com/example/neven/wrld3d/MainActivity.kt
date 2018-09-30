package com.example.neven.wrld3d

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.RelativeLayout
import com.eegeo.indoors.IndoorMapView
import com.eegeo.mapapi.EegeoMap
import com.eegeo.mapapi.geometry.LatLng
import com.eegeo.mapapi.indoors.OnFloorChangedListener
import com.eegeo.mapapi.map.OnInitialStreamingCompleteListener
import com.eegeo.mapapi.markers.Marker
import com.eegeo.mapapi.markers.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import com.eegeo.mapapi.camera.CameraUpdateFactory
import com.eegeo.mapapi.camera.CameraPosition


class MainActivity : AppCompatActivity(), OnInitialStreamingCompleteListener, OnFloorChangedListener, View.OnClickListener {

    private var map: EegeoMap? = null
    private var listMarkers = listOf<Marker>()

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeMarkers()
        mapView.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map = it
            map?.addInitialStreamingCompleteListener(this)
            map?.addOnFloorChangedListener(this)
        }
    }

    override fun onClick(pView: View?) {
        when (pView?.id) {
            bNavigationCoffee.id -> {
                goToGetSomeCoffee()
            }
        }
    }

    override fun onInitialStreamingComplete() {
        setupIndoorMapView()
        bNavigationCoffee.visibility = View.VISIBLE
        bNavigationCoffee.setOnClickListener(this)
    }

    override fun onFloorChanged(selectedFloor: Int) {
        if (selectedFloor == 1) {
            addIndoorMarker(0)
            bNavigationCoffee.visibility = View.GONE
        } else {
            bNavigationCoffee.visibility = View.VISIBLE
        }
    }

    private fun setupIndoorMapView() {
        val uiContainer = findViewById<View>(R.id.eegeo_ui_container) as RelativeLayout
        IndoorMapView(mapView, uiContainer, map)
    }

    /**
     * adds an indoor marker in the Academy Cafe (1st floor)
     */
    private fun addIndoorMarker(floorId: Int) {
        val mutableListMarkers = mutableListOf<Marker>()
        listMarkers = mutableListMarkers
        if (map?.activeIndoorMap != null) {
            val marker = map?.addMarker(MarkerOptions()
                    .position(LatLng(ConstantsWrld3d.MARKER_COFFEE_LATITUDE, ConstantsWrld3d.MARKER_COFFEE_LONGITUDE))
                    .indoor(map?.activeIndoorMap?.id, floorId)
                    .labelText(ConstantsWrld3d.MARKER_COFFEE))
            marker?.let {
                mutableListMarkers.add(marker)
            }
        }
    }

    private fun removeMarkers() {
        map?.let {
            listMarkers.forEach { marker ->
                map?.removeMarker(marker)
            }
        }
    }

    private fun goToGetSomeCoffee() {
        val cameraPosition = CameraPosition.Builder()
                .target(ConstantsWrld3d.MARKER_COFFEE_LATITUDE, ConstantsWrld3d.MARKER_COFFEE_LONGITUDE)
                .indoor(ConstantsWrld3d.INDOOR_MAP_ID, 0)
                .build()
        map?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}
