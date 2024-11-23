package cvut.fel.cz.vitalsignsprovider

interface Observer {
    fun update(healthDataSnapshot: HealthDataSnapshot)
}