package cls.simplecar.api;

interface ApiResult {
    fun result(result: Boolean)
    fun exception(exception: Exception)

}