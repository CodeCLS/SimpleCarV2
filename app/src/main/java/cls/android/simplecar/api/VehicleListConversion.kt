package cls.android.simplecar.api;

interface VehicleListConversion {
    fun convertVehicleList(body: String?): List<String>?
    fun convertVehicleAttributes(string: String?): VehicleAttributes?


}