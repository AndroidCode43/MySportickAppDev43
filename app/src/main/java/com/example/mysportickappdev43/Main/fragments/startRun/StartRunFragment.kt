package com.example.mysportickappdev43.Main.fragments.startRun

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import com.example.mysportickappdev43.R
import com.example.mysportickappdev43.databinding.FragmentStartRunBinding
import com.example.mysportickappdev43.service.GpsRunService
import com.example.mysportickappdev43.service.Polyline
import com.example.mysportickappdev43.utils.MAP_ZOOM
import com.example.mysportickappdev43.utils.POLYLINE_COLOR
import com.example.mysportickappdev43.utils.POLYLINE_WIDTH
import com.example.mysportickappdev43.utils.obj.GpsRunConst.ACTION_PAUSE_SERVICE
import com.example.mysportickappdev43.utils.obj.GpsRunConst.ACTION_START_OR_RESUME_SERVICE
import com.example.mysportickappdev43.utils.obj.LocationUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class StartRunFragment : Fragment(R.layout.fragment_start_run) , EasyPermissions.PermissionCallbacks{

    private var mBinding : FragmentStartRunBinding? = null
    private var currentMap : GoogleMap? = null

    private var isTracking = false
    private var currentTimeMillis = 0L
    private var pathPoints = mutableListOf<Polyline>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentStartRunBinding.bind(view).also {
            it.mapView.onCreate(savedInstanceState)
        }
        init()
    }

    override fun onStart() {
        super.onStart()
        mBinding!!.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mBinding!!.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mBinding!!.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mBinding!!.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding!!.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mBinding!!.mapView.onLowMemory()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }

    private fun subscribeToRunObservers(){
        GpsRunService.isTracking.observe(viewLifecycleOwner) {
            updateTracking(it)
        }

        GpsRunService.pathPoints.observe(viewLifecycleOwner){
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        }

        GpsRunService.timeRunInMillis.observe(viewLifecycleOwner){
            currentTimeMillis = it
            val formattedTime = LocationUtils.getFormattedStopWatchTime(currentTimeMillis,true)
            mBinding!!.tvTimer.text = formattedTime
        }

        GpsRunService.polylinePoints.observe(viewLifecycleOwner){
            for(i in pathPoints){
                getDistanceForText(LocationUtils.calculatePolylineDistance(i).toString())
            }
        }
    }

    private fun toggleRun(){
        if(isTracking){
            sendCommandToService(ACTION_PAUSE_SERVICE)
        }else{
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isTracking : Boolean){
        this.isTracking = isTracking
        if(!isTracking){
            mBinding?.btnStartRun?.text = "Start"
        }else{
            mBinding?.btnStartRun?.text = "Stop"
        }
    }

    private fun moveCameraToUser(){
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
            currentMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }

    private fun addAllPolylines(){
        for(polyline in pathPoints){
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            currentMap?.addPolyline(polylineOptions)

            GpsRunService.polylinePoints.value?.apply {
                add(polyline)
            }
        }
    }

    private fun addLatestPolyline(){
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1){
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            currentMap?.addPolyline(polylineOptions)
        }
    }

    private fun init(){
        requestLocationPermissions()
        initMap()
        subscribeToRunObservers()
        btnStartRun()
    }

    private fun initMap(){
        mBinding!!.mapView.getMapAsync {
            currentMap = it.apply {
                setMapStyle(
                    MapStyleOptions
                        .loadRawResourceStyle(requireContext(), R.raw.map_style)
                )
            }
            addAllPolylines()
        }
    }

    private fun requestLocationPermissions(){
        if(LocationUtils.hasLocationPermission(requireContext())){
            return
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(
                this,
                "Для использования приложения предоставьте доступ к геолокации!",
                0,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        }else{
            EasyPermissions.requestPermissions(
                this,
                "Для использования приложения предоставьте доступ к геолокации!",
                0,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    private fun sendCommandToService(action : String) = Intent(
        requireContext(), GpsRunService::class.java
    ).also {
        it.action = action
        requireContext().startService(it)
    }

    private fun btnStartRun(){
        mBinding!!.btnStartRun.setOnClickListener {
            toggleRun()
        }
    }

    private fun getDistanceForText(distnace : String){
        mBinding!!.startRunDistanceM.text = distnace + "m"
    }
}