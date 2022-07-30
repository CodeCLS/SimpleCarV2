package cls.simplecar.api;

import android.os.Build
import android.util.Log
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
    var smartCarCode: String?
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
                var body = response.body()?.string()
                var vehicleIds = Converter().convertVehicleList(body)
                if(vehicleIds != null){
                    vehicleIdListCallback.getVehicles(vehicleIds)
                }
                else{
                    vehicleIdListCallback.exception(Converter().convertException(body))
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                vehicleIdListCallback.exception(Exception(t.message,ExceptionManager.EXCEPTION_API_CALL_EXTERNAL))

            }

        })

    }
    fun getOdometer(id : String,odometerCallback: VehicleOdometerCallback) {
        service.getOdometer(apiCode,smartCarCode,uid,id).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var body = response.body()?.string()
                var odometer = Converter().convertOdometer(body)
                if(odometer != null){
                    odometerCallback.result(odometer)
                }
                else{
                    odometerCallback.exception(Converter().convertException(body))
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                odometerCallback.exception(Exception(t.message,ExceptionManager.EXCEPTION_API_CALL_EXTERNAL))

            }

        })

    }


    fun getRange(id : String,rangeCallback: RangeCallback) {
        service.getVehicleRange(apiCode,smartCarCode,uid,id).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var body = response.body()?.string()
                var range = Converter().convertRange(body)
                if(range != null){
                    rangeCallback.range(range)
                }
                else{
                    rangeCallback.exception(Converter().convertException(body))
                }            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                rangeCallback.exception(Exception(t.message,ExceptionManager.EXCEPTION_API_CALL_EXTERNAL))

            }

        })

    }

    private val TAG = "SimpleCarSdk"
    fun isTokenValid(apiResult: ApiResult) {
        Log.d(TAG, "isTokenValid: " + apiCode + " "  + smartCarCode)
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
    fun getLocation(id: String,locationCallback: LocationCallback) {
        service.getVehicleLocation(apiCode,smartCarCode,uid,id).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var body = response.body()?.string()
                var location = Converter().convertLocation(body)
                if(location != null){
                    locationCallback.location(location)
                }
                else{
                    locationCallback.exception(Converter().convertException(body))
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                locationCallback.exception(Exception(t.message,ExceptionManager.EXCEPTION_API_CALL_EXTERNAL))

            }

        })

    }
    fun unlockVehicle(id:String,apiResult: ApiResult) {
        service.unlockVehicle(apiCode,smartCarCode,uid,id).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var body = response.body()?.string()
                var result = Converter().convertApiResult(body)
                if(!result){
                    apiResult.exception(Converter().convertException(body))
                }
                apiResult.result(result)

            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                apiResult.result(false)

            }

        })

    }
    fun lockVehicle(id:String,apiResult: ApiResult) {
        service.lockVehicle(apiCode,smartCarCode,uid,id).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var body = response.body()?.string()
                var result = Converter().convertApiResult(body)
                if(!result){
                    apiResult.exception(Converter().convertException(body))
                }
                apiResult.result(result)
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                apiResult.result(false)

            }

        })

    }
    fun getVehicleAttributes(id:String,apiResult: VehicleCallback) {
        service.getVehicleAttributes(apiCode,smartCarCode,uid,id).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var body = response.body()?.string()
                var attrs = Converter().convertVehicleAttributes(body)
                if(attrs != null){
                    apiResult.getVehicle(attrs)
                }
                else{
                    apiResult.exception(Converter().convertException(body))
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                apiResult.exception(Exception(t.message, ExceptionManager.EXCEPTION_API_CALL_EXTERNAL))

            }

        })

    }
    fun getOil(id:String,apiResult: OilCallback) {
        service.getOil(apiCode,smartCarCode,uid,id).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var body = response.body()?.string()
                var attrs = Converter().convertOil(body)
                Log.d(TAG, "onResponsOile: " + body)
                if(attrs != null){
                    apiResult.oil(attrs)
                }
                else{
                    apiResult.exception(Converter().convertException(body))
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                apiResult.exception(Exception(t.message, ExceptionManager.EXCEPTION_API_CALL_EXTERNAL))

            }

        })

    }
    fun getMarketValue(id:String,apiResult: CarMarketValueCallback) {
        service.getCarMarketValue(apiCode,smartCarCode,uid,id).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var body = response.body()?.string()
                var attrs = Converter().convertMarketValue(body)
                Log.d(TAG, "onResponsOile: " + body)
                if(attrs != null){
                    apiResult.result(attrs)
                }
                else{
                    apiResult.exception(Converter().convertException(body))
                }
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
    fun refreshToken(auth:String, authClient:String,apiResult: ApiAuthPackageCallback) {
        Log.d(TAG, "refreshToken: " + auth + " " + authClient + " " + apiCode)
        service.refreshToken(apiCode,smartCarCode,"NO_ACCOUNT",auth,authClient).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d(TAG, "onResponse123: " + call.toString())
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
        Log.d(TAG, "signup: $jsonObject")

        service.signup(apiCode,smartCarCode,jsonObject.toString()).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var body: String? = response.body()?.string()
                var resultApi : Boolean = Converter().convertApiResult(body)
                simpleCarSignUpFeedback.result(resultApi,
                    Status(
                        resultApi,
                        -1,
                        Converter().convertAdditional("user", body)
                    )
                )
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                simpleCarSignUpFeedback.result(false, Status())
                Log.e(TAG, "onFailure: " + t.message )

            }

        })

    }

    fun getPermissions(smartCarId: String, vehiclePermissionsCallback: VehiclePermissionsCallback) {
        service.getPermissions(apiCode,smartCarCode,uid,smartCarId).enqueue(object :
            Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                var body = response.body()?.string()
                var attrs = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Converter().convertPermissions(body)
                } else {
                    TODO("VERSION.SDK_INT < KITKAT")
                }
                if(attrs != null){
                    vehiclePermissionsCallback.result(attrs)
                }
                else{
                    vehiclePermissionsCallback.exception(Converter().convertException(body))
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                vehiclePermissionsCallback.exception(Exception(t.message, ExceptionManager.EXCEPTION_API_CALL_EXTERNAL))

            }

        })
    }


    interface OnSimpleCarSignUpFeedback{
        fun result(apiResponse: Boolean, status: Status)

    }

}
