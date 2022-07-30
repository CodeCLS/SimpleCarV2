package cls.simplecar.api

interface CarMarketValueCallback {
    fun result(value:CarMarketValue)
    fun exception(exception: Exception)
}