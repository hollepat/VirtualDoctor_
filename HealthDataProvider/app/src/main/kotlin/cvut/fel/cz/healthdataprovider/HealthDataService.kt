package cvut.fel.cz.healthdataprovider

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlin.random.Random

class HealthDataService : Service(), Subject {
    private val binder = LocalBinder()
    private val random = Random
    private val handler = Handler(Looper.getMainLooper())
    private var healthDataSnapshot = HealthDataSnapshot(0.0, 0.0, 0)
    private val observers = mutableListOf<Observer>()

    inner class LocalBinder : Binder() {
        fun getService(): HealthDataService = this@HealthDataService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        Log.d("HealthDataService", "Service created")
        startForeground(1, createNotification())
        simulateData()
    }

    /**
     * Simulates the data by generating random values for heartbeat.
     */
    private fun simulateData() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                healthDataSnapshot = generateHealhtDataSnapshot()
                notifyObservers()
                handler.postDelayed(this, 5000)
            }
        }, 5000)
    }

    private fun generateHealhtDataSnapshot(): HealthDataSnapshot {
        val heartbeat = 60 + random.nextInt(41)
        val bloodPressure = 80.0 + random.nextDouble() * 40
        val skinTemperature = 36.0 + random.nextDouble() * 2
        Log.d("HealthDataService", "Heartbeat: $heartbeat bpm, Blood pressure: $bloodPressure mmHg, Skin temperature: $skinTemperature °C")
        return HealthDataSnapshot(skinTemperature, bloodPressure, heartbeat)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)

        Log.d("HealthDataService", "Service destroyed")
    }

    private fun createNotification(): Notification {
        val channelId = "health_data_service"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Health Data Service",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Health Data Service")
            .setContentText("Simulating health data...")
            .setSmallIcon(androidx.loader.R.drawable.notification_bg)
            .build()
    }

    override fun registerObserver(observer: Observer) {
        observers.add(observer)
        Log.d("HealthDataService", "Observer registered: $observer")
    }

    override fun removeObserver(observer: Observer) {
        observers.remove(observer)
        Log.d("HealthDataService", "Observer removed: $observer")
    }

    override fun notifyObservers() {
        for (observer in observers) {
            observer.update(healthDataSnapshot)
        }
    }
}