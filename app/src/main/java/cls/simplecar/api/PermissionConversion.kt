package cls.simplecar.api

interface PermissionConversion {
    fun convertPermissions(body:String?) : ArrayList<String>?
}