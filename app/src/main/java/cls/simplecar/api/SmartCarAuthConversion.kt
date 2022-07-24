package cls.simplecar.api

interface SmartCarAuthConversion {
    fun convertAuthResult(body: String?): ApiSmartCarAuthPackage?
}