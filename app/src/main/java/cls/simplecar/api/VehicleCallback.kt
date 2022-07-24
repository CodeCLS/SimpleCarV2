package cls.simplecar.api

interface VehicleCallback {
    fun getVehicle(vehicleAttributes: VehicleAttributes?)
    fun exception(exception: Exception)
}