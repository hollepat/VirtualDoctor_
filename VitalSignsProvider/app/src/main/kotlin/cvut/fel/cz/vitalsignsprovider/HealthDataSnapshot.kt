package cvut.fel.cz.vitalsignsprovider

import org.json.JSONObject
import java.time.LocalDateTime

data class HealthDataSnapshot(
    val skinTemperature: Double,
    val bloodPressure: Double,
    val heartRate: Int,
    val time: LocalDateTime = LocalDateTime.now(), // TODO why this warning??
) {
    fun toJson(): JSONObject =
        JSONObject().apply {
            put("skinTemperature", skinTemperature)
            put("bloodPressure", bloodPressure)
            put("heartRate", heartRate)
            put("time", time.toString())
        }

}
