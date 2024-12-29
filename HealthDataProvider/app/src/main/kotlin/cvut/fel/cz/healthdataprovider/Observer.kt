package cvut.fel.cz.healthdataprovider

interface Observer {
    fun update(healthDataSnapshot: HealthDataSnapshot)
}