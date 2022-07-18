package cls.android.simplecar.api;

interface VehicleIdListCallback {
    fun getVehicles(list : List<String>?)
    fun exception(exception: Exception)
}