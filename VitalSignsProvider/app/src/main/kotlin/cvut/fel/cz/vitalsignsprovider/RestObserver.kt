package cvut.fel.cz.vitalsignsprovider

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.time.LocalDateTime

class RestObserver(private val url: String) : Observer {
    private val client = OkHttpClient()
    private val scope = CoroutineScope(Dispatchers.IO)
    private val username = "john-doe"

    init {
        Log.d("RestObserver", "Created RestObserver with URL: $url")
        // Optional: Add logging to see request details during development
//        val logging = HttpLoggingInterceptor()
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
//        client = OkHttpClient.Builder()
//            .addInterceptor(logging)
//            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun update(healthDataSnapshot: HealthDataSnapshot) {
        scope.launch {
            Log.d("RestObserver", "Sending update: $healthDataSnapshot")

            // Create JSON object to send
            val jsonObject = healthDataSnapshot.toJson()
            jsonObject.put("username", username) // to match user in dbs

            // Convert JSON object to RequestBody
            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val requestBody = jsonObject.toString().toRequestBody(mediaType)

            // Build the request
            val request = Request.Builder().url(url)
                .post(requestBody)
                .build()

            withContext(Dispatchers.IO) {
                sendRequest(request)
            }
        }
    }

    private fun sendRequest(request: Request) {
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Log.e("RestObserver", "Failed to send data: $response")
            } else {
                Log.d("RestObserver", "Data sent successfully")
            }
        }
    }
}