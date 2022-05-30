package com.example.mysportickappdev43.utils.obj

import android.content.Context
import android.location.Location
import android.os.Build
import com.example.mysportickappdev43.service.Polyline
import pub.devrel.easypermissions.EasyPermissions
import java.sql.Time
import java.util.concurrent.TimeUnit
import kotlin.time.TimeMark

object LocationUtils {

    fun hasLocationPermission(context : Context) =
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.hasPermissions(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }else{
            EasyPermissions.hasPermissions(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

    fun getFormattedStopWatchTime(ms : Long, includeMillis : Boolean = false) : String{
        var milliseconds = ms
        var hours = TimeUnit.MILLISECONDS.toHours(milliseconds)

        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        var minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)

        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        var second = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        if(!includeMillis){
            return "${if(hours < 10) "0" else ""}$hours:" +
                    "${if(minutes < 10) "0" else ""}$minutes:" +
                    "${if(second < 10) "0" else ""}$second"
        }

        milliseconds -= TimeUnit.SECONDS.toMillis(second)
        milliseconds /= 10
        return "${if(hours < 10) "0" else ""}$hours:" +
                "${if(minutes < 10) "0" else ""}$minutes:" +
                "${if(second < 10) "0" else ""}$second:" +
                "${if(milliseconds < 10) "0" else ""}$milliseconds"
    }

    fun calculatePolylineDistance(polyline : Polyline) : Int{
        var distance = 0f
        for(i in 0..polyline.size - 2){
            var pos1 = polyline[i]
            var pos2 = polyline[i + 1]

            val result = FloatArray(1)

            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result
            )
            distance += result[0]
        }
        return distance.toInt()
    }
}