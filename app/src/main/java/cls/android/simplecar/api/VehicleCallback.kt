package cls.android.simplecar.api;

interface VehicleCallback {
    fun getVehicles(list : List<String>?)
    fun exception(exception: Exception)
}