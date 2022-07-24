package cls.simplecar.api

interface ApiAuthPackageCallback {
    fun result(packageSmartCar: ApiSmartCarAuthPackage?)
    fun exception(exception: Exception)
}