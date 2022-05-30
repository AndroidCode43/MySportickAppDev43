package com.example.mysportickappdev43.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.mysportickappdev43.R
import com.example.mysportickappdev43.utils.FASTEST_LOCATION_INTERVAL
import com.example.mysportickappdev43.utils.LOCATION_UPDATE_INTERVAL
import com.example.mysportickappdev43.utils.TIMER_UPDATE_INTERVAL
import com.example.mysportickappdev43.utils.obj.GpsRunConst.ACTION_PAUSE_SERVICE
import com.example.mysportickappdev43.utils.obj.GpsRunConst.ACTION_START_OR_RESUME_SERVICE
import com.example.mysportickappdev43.utils.obj.GpsRunConst.ACTION_STOP_SERVICE
import com.example.mysportickappdev43.utils.obj.GpsRunConst.NOTIFICATION_CHANNEL_ID
import com.example.mysportickappdev43.utils.obj.GpsRunConst.NOTIFICATION_CHANNEL_NAME
import com.example.mysportickappdev43.utils.obj.GpsRunConst.NOTIFICATION_ID
import com.example.mysportickappdev43.utils.obj.LocationUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

typealias  Polyline = MutableList<LatLng>
typealias  Polylines = MutableList<Polyline>

@AndroidEntryPoint
class GpsRunService : LifecycleService() {

    var isFirstRun = true

    @Inject
    lateinit var fusedLocationProviderClient : FusedLocationProviderClient

    @Inject
    lateinit var baseNotificationBuilder : NotificationCompat.Builder

    var currentNotificationBuilder : NotificationCompat.Builder? = null

    private val timeRunInSeconds = MutableLiveData<Long>()

    companion object{
        val timeRunInMillis = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<MutableList<MutableList<LatLng>>>()
        val polylinePoints = MutableLiveData<MutableList<Polyline>>()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking : Boolean){

        if(isTracking){
            if(LocationUtils.hasLocationPermission(this)){
                val request = com.google.android.gms.location.LocationRequest.create().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }

                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }else{
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }




    private fun updateNotificationTrackingState(isTracking: Boolean){
        val notificationActionText = if(isTracking) "Pause" else "Resume"
        val pendingIntent = if(isTracking){
            val pauseIntent = Intent(this, GpsRunService::class.java).apply{
                action = ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this,1,pauseIntent, FLAG_UPDATE_CURRENT)
        } else{
            val resumeIntent = Intent(this, GpsRunService::class.java).apply {
                action = ACTION_START_OR_RESUME_SERVICE
            }
            PendingIntent.getService(this, 2, resumeIntent, FLAG_UPDATE_CURRENT)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        currentNotificationBuilder!!.javaClass.getDeclaredField("mActions").apply {
             isAccessible = true
            set(currentNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }
        currentNotificationBuilder = baseNotificationBuilder
            .addAction(R.drawable.ic__fire_icon, notificationActionText, pendingIntent)
        notificationManager.notify(NOTIFICATION_ID, currentNotificationBuilder!!.build())
    }




    private var locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            if(isTracking.value!!){
                p0.locations.let {locations ->

                    for(location in locations){
                        addPathPoint(location)
                        Toast.makeText(applicationContext, "New location: ${location.latitude} ," +
                                " ${location.longitude} " , Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun postInitialValues(){
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
    }

    @SuppressLint("VisibleForTests")
    override fun onCreate() {
        super.onCreate()
        currentNotificationBuilder = baseNotificationBuilder
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this) {
            updateLocationTracking(it)
            updateNotificationTrackingState(it)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                ACTION_START_OR_RESUME_SERVICE -> {
                    if(isFirstRun){
                        startForegroundService()
                        isFirstRun = false
                    }else{
                        startForegroundService()
                        startTimer()
                    }
                }

                ACTION_PAUSE_SERVICE -> {
                    Log.d("gps:","pause service")
                    pauseService()
                }

                ACTION_STOP_SERVICE -> {
                    Log.d("gps","stop service")
                }
            }
            false
        }


        return super.onStartCommand(intent, flags, startId)
    }

    private fun addPathPoint(location : Location?){
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)

            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
                polylinePoints.postValue(this)
            }
        }
    }

    private var isTimerEnabled = false
    private var lapTime = 0L
    private var timeRun = 0L
    private var timeStarted = 0L
    private var lastSecondTimestamp = 0L

    private fun startTimer(){
        addEmptyPolyline()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!){
                //time difference between now and timeStarted
                lapTime = System.currentTimeMillis() - timeStarted
                //post new lap time
                timeRunInMillis.postValue(timeRun + lapTime)

                if(timeRunInMillis.value!! >= lastSecondTimestamp + 1000L){
                    timeRunInSeconds.postValue(timeRunInSeconds.value!! + 1)
                    lastSecondTimestamp += 1000L
                }
                delay(TIMER_UPDATE_INTERVAL)
            }
            timeRun += lapTime
        }
    }

    private fun pauseService(){
        isTracking.postValue(false)
        isTimerEnabled = false
    }

    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))


    private fun startForegroundService(){
        startTimer()
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())

        timeRunInSeconds.observe(this){
            val notification = currentNotificationBuilder
                ?.setContentText(LocationUtils.getFormattedStopWatchTime(it * 1000L))
            notificationManager.notify(NOTIFICATION_ID, notification?.build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}