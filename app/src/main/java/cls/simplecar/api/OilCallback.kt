package cls.simplecar.api

interface OilCallback {
    fun oil(oil:Oil)
    fun exception(exception: Exception)
}