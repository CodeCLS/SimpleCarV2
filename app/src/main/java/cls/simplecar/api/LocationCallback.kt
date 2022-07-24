package cls.simplecar.api;

interface LocationCallback {
    fun location(location: Location?)
    fun exception(exception: Exception)
}