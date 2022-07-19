package cls.android.simplecar.api

interface SmartCarAuthConversion {
    fun convertAuthResult(body: String?): ApiSmartCarAuthPackage?
}