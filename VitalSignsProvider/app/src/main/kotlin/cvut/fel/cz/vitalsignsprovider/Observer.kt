package cvut.fel.cz.vitalsignsprovider

interface Observer {
    fun update(heartBeat: Int, stepCount: Int)
}