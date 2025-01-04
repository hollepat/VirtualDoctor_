package cvut.fel.cz.healthdataprovider

import org.json.JSONObject
import org.threeten.bp.LocalDateTime

/**
 * Represents a snapshot of health data, which is to be sent to the rest of the system.
 */
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
