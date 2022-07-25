package cls.simplecar.api

interface VehicleOdometerCallback {
    fun result(result: Odometer?)
    fun exception(exception: Exception)
}