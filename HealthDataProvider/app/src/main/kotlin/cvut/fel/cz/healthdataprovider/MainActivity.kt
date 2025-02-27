package cvut.fel.cz.healthdataprovider

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.threetenabp.AndroidThreeTen
import java.util.Properties

class MainActivity : AppCompatActivity() {
        private var healthDataService: HealthDataService? = null
        private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as HealthDataService.LocalBinder
            healthDataService = binder.getService()
            isBound = true
            val url =  loadUrlFromConfig()
            val userName = loadUsernameFromConfig()
            healthDataService?.registerObserver(RestObserver(url, userName))
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    /**
     * Create Service and bind it to the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
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

    private fun loadUrlFromConfig(): String {
        val properties = Properties()
        val inputStream = resources.openRawResource(R.raw.config)
        properties.load(inputStream)
        return properties.getProperty("url")
    }

    private fun loadUsernameFromConfig(): String {
        val properties = Properties()
        val inputStream = resources.openRawResource(R.raw.config)
        properties.load(inputStream)
        return properties.getProperty("user_name")
    }
}