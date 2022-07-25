package cls.simplecar.api

interface VehiclePermissionsCallback {
    fun result(result: ArrayList<String>)
    fun exception(exception: Exception)

}