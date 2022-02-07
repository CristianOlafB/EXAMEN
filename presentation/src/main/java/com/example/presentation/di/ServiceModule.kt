package com.example.presentation.di

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.presentation.MainActivity
import com.example.presentation.R
import com.example.presentation.extension.Constants.Constants.NOTIFICATION_CHANNEL_ID
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import androidx.lifecycle.Observer
import com.example.presentation.extension.Constants.Constants.NOTIFICATION_ID

@Module
@InstallIn(ServiceComponent::class)
class ServiceModule {

    @ServiceScoped
    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext app: Context
    ) = FusedLocationProviderClient(app)

    @ServiceScoped
    @Provides
    fun provideMainActivityPendingIntent(
        @ApplicationContext app: Context
    ): PendingIntent = PendingIntent.getActivity(
        app, 0, Intent(app, MainActivity::class.java), 0
    )

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext app: Context
    ) = app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext app: Context,
        pendingIntent: PendingIntent
    ) = NotificationCompat.Builder(app, NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(true)
        .setOngoing(false)
        .setSmallIcon(R.drawable.ic_location)
        .setContentTitle(app.getString(R.string.real))
        .setContentText("Close")
        .setContentIntent(pendingIntent)

    @ServiceScoped
    @Provides
    fun provideNotificationObserver(
        notificationManager: NotificationManager,
        notificationBuilder: NotificationCompat.Builder
    ) = Observer<Long> {
        val notification = notificationBuilder.setContentText("For the location go to the map")
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

}