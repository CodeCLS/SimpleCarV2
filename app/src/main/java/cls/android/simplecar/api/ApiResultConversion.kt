package cls.android.simplecar.api;

import org.json.JSONObject

interface ApiResultConversion {
    fun convertApiResult(body: String?) : Boolean
    fun convertAdditional(parent: String?, body: String?) : JSONObject
}