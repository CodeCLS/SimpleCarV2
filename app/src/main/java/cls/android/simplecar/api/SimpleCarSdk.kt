package cls.android.simplecar.api;

import android.util.Log
import cls.android.simplecar.UserRepository
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class SimpleCarSdk {
    private var hasFunctionalApiCode: Boolean = true
    private var hasFunctionalAttributes: Boolean = true
    private var apiCode: String?
    private var smartCarCode: String?
    private var uid: String?
    private val version : Int = 10//asd

    private lateinit var service : ApiService
    constructor(apiCode: String?, smartCarCode : String?, uid : String?){
        this.apiCode = apiCode
        this.smartCarCode = smartCarCode
        this.uid = SimpleCarSdk.NO_ACCOUNT;
        if (uid != null)
            this.uid = uid
       if (apiCode != null && (smartCarCode == null || uid == null)){
           hasFunctionalAttributes = false
           hasFunctionalApiCode = true
           this.apiCode = apiCode

           return
       }
       else if(apiCode == null || smartCarCode == null || uid == null){
           hasFunctionalAttributes = false
           hasFunctionalApiCode = false
       }


    }
    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://pacific-dawn-46907.herokuapp.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        service = retrofit.create(ApiService::class.java)
    }
    public fun getVersion() : Int {
        return version
    }

    public companion object {
        const val NO_ACCOUNT: String = "NO_ACCOUNT"
        var instance: SimpleCarSdk? = null
        public fun get(apiCode: String?, smartCarCode: String?, uid:String?) : SimpleCarSdk{
            if(instance == null)
                instance = SimpleCarSdk(apiCode,smartCarCode,uid)
            return instance as SimpleCarSdk
        }
    }
    fun getVehicleIds(vehicleIdListCallback: VehicleIdListCallback) {
        service.getCars(apiCode,smartCarCode,uid).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                vehicleIdListCallback.getVehicles(Converter().convertVehicleList(response.body()?.string()))
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                vehicleIdListCallback.exception(Exception(t.message,ExceptionManager.EXCEPTION_API_CALL_EXTERNAL))

            }

        })

    }


    fun getRange(id : String,rangeCallback: RangeCallback) {
        service.getVehicleRange(apiCode,smartCarCode,id,uid).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                rangeCallback.range(Converter().convertRange(response.body()?.string()))
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                rangeCallback.exception(Exception(t.message,ExceptionManager.EXCEPTION_API_CALL_EXTERNAL))

            }

        })

    }

    private val TAG = "SimpleCarSdk"
    fun isTokenValid(apiResult: ApiResult) {
        service.isTokenValid(apiCode,smartCarCode,uid).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                apiResult.result(Converter().convertApiResult(response.body()?.string()))
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "onFailure: " + t.message)
                apiResult.result(false)

            }

        })

    }
    fun getLocation(locationCallback: LocationCallback) {
        service.getCars(apiCode,smartCarCode,uid).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                locationCallback.location(Converter().convertLocation(response.body()?.string()))
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                locationCallback.exception(Exception(t.message,ExceptionManager.EXCEPTION_API_CALL_EXTERNAL))

            }

        })

    }
    fun unlockVehicle(id:String,apiResult: ApiResult) {
        service.unlockVehicle(apiCode,smartCarCode,id,uid).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                apiResult.result(Converter().convertApiResult(response.body()?.string()))
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                apiResult.result(false)

            }

        })

    }
    fun lockVehicle(id:String,apiResult: ApiResult) {
        service.lockVehicle(apiCode,smartCarCode,id,uid).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                apiResult.result(Converter().convertApiResult(response.body()?.string()))
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                apiResult.result(false)

            }

        })

    }
    fun getVehicleAttributes(id:String,apiResult: VehicleCallback) {
        service.getVehicleAttributes(apiCode,smartCarCode,id,uid).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                apiResult.getVehicle(Converter().convertVehicleAttributes(response.body()?.string()))
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                apiResult.exception(Exception(t.message, ExceptionManager.EXCEPTION_API_CALL_EXTERNAL))

            }

        })

    }
    fun getAccessTokenWithAuthToken(token:String,apiResult: ApiAuthPackageCallback) {
        service.getAccessWithAuthToken(apiCode,token,"NO_ACCOUNT").enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                apiResult.result(Converter().convertAuthResult(response.body()?.string()))
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                apiResult.exception(Exception(t.message, ExceptionManager.EXCEPTION_API_CALL_EXTERNAL))

            }

        })

    }


    fun signup(
        phone: String,
        email: String,
        firstName: String,
        secondName: String,
        simpleCarSignUpFeedback: OnSimpleCarSignUpFeedback
    ) {
        val jsonObject = JSONObject()
        jsonObject.put(ApiManager.PHONE,phone)
        jsonObject.put(ApiManager.EMAIL,email)

        jsonObject.put(ApiManager.FIRST_NAME,firstName)
        jsonObject.put(ApiManager.SECOND_NAME,secondName)

        service.signup(apiCode,smartCarCode,jsonObject.toString()).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                simpleCarSignUpFeedback.result(Converter().convertApiResult(response.body()?.string()),
                    Status()
                )
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                simpleCarSignUpFeedback.result(false,Status())

            }

        })

    }

    interface OnSimpleCarSignUpFeedback{
        fun result(apiResponse: Boolean, status: Status)

    }

}
