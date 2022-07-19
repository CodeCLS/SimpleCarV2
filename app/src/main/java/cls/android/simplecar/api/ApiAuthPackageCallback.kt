package cls.android.simplecar.api

interface ApiAuthPackageCallback {
    fun result(packageSmartCar: ApiSmartCarAuthPackage?)
    fun exception(exception: Exception)
}