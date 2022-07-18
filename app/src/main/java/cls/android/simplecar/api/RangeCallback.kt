package cls.android.simplecar.api;

interface RangeCallback {
    fun range(range: Range?)
    fun exception(exception: Exception)
}