package cvut.fel.cz.vitalsignsprovider

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import fi.iki.elonen.NanoHTTPD
import kotlin.random.Random

class HealthDataService : Service(), Subject {
    private val binder = LocalBinder()
    private val random = Random
    private val handler = Handler()
    private var heartbeat: Int = 0
    private var stepCount: Int = 0
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
     * TODO investigate how this works
     */
    private fun simulateData() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                heartbeat = 60 + random.nextInt(41)
                stepCount = random.nextInt(5000)
                notifyObservers()
                Log.d("HealthDataService", "Heartbeat: $heartbeat bpm, Steps: $stepCount")
                handler.postDelayed(this, 5000)
            }
        }, 5000)
    }

    /**
     * TODO
     */
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)

        Log.d("HealthDataService", "Service destroyed")
    }

    // Inner class for the HTTP server
    private inner class SimpleHttpServer(port: Int) : NanoHTTPD(port) {
        override fun serve(session: IHTTPSession): Response {
            val responseJson = """{"heartbeat": $heartbeat, "stepCount": $stepCount}"""
            return newFixedLengthResponse(Response.Status.OK, "application/json", responseJson)
        }
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
            // TODO create some kind of a snapshot of the current state rather then passing the values separately
            observer.update(heartbeat, stepCount)
        }
    }
}