package cls.simplecar.api

interface UserConverter {
    fun convertUserId(body: String? ) : String?
}