package com.example.mysportickappdev43.di.modules

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.core.app.NotificationCompat
import com.example.mysportickappdev43.Main.MainActivity
import com.example.mysportickappdev43.R
import com.example.mysportickappdev43.utils.obj.GpsRunConst
import com.example.mysportickappdev43.utils.obj.GpsRunConst.ACTION_SHOW_TRACKING_FRAGMENT
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext app : Context
    ) = FusedLocationProviderClient(app)


    @ServiceScoped
    @Provides
    fun provideMainActivityPendingIntent(
        @ApplicationContext app : Context
    ): PendingIntent = PendingIntent.getActivity(
        app,
        0,
        Intent(app, MainActivity::class.java).also{
            it.action = ACTION_SHOW_TRACKING_FRAGMENT
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    @Provides
    @ServiceScoped
    fun provideBaseNotificationBuilder(
        @ApplicationContext app : Context,
        pendingIntent : PendingIntent
    ) = NotificationCompat.Builder(app, GpsRunConst.NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.ic__icon_run)
        .setContentTitle("Running App")
        .setContentText("00:00:00")
        .setContentIntent(pendingIntent)
}