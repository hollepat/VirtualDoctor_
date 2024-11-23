package cvut.fel.cz.vitalsignsprovider

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
        private var healthDataService: HealthDataService? = null
        private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as HealthDataService.LocalBinder
            healthDataService = binder.getService()
            isBound = true
            val url = "http://10.0.2.2:8085/api/v1/vitalSignsObserver/vitalSigns" // TODO move to configuration
            healthDataService?.registerObserver(RestObserver(url))
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(androidx.core.R.layout.custom_dialog)

        // Start the service
        val intent = Intent(this, HealthDataService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent) // For a foreground service
        } else {
            startService(intent) // For a background service
        }

        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }
}