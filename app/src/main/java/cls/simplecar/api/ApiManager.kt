package cls.simplecar.api;
import java.util.*

class ApiManager {
    companion object{
        public val SUCCESS_CARS_XE = "success"
        public val RETAIL = "retail"
        public val TRADE_IN = "tradeIn"
        public val ROUGH_TRADE_IN = "roughTradeIn"
        public val AVERAGE_TRADE_IN = "averageTradeIn"
        public val LOAN_VALUE = "loanValue"
        public val MSRP = "msrp"


        public val VEHICLE_ID = "vehicle_id"
        public val PHONE = "phone"
        public val EMAIL = "email"
        public val FIRST_NAME = "first_name"
        public val SECOND_NAME = "second_name"

        public val EXCEPTION_MSG = "error_msg"
        public val EXCEPTION_CODE = "error_code"


        public val VEHICLE_MAKE = "vehicle_make"
        public val VEHICLE_MODEL = "vehicle_model"
        public val VEHICLE_YEAR = "vehicle_year"
        public val ACCESS_TOKEN = "access_token"
        public val REFRESH_TOKEN = "refresh_token"

        public val UID_SMART = "smartcar_uid"

        public val AUTH_CLIENT = "auth_client"
        public val AUTH = "auth"
        public val LATITUDE = "latitude"
        public val LONGITUDE = "longitude"
        public val VEHICLE_IDS = "vehicle_ids"
        public val ODOMETER = "odometer"
        public val VIN = "vin"
        public val RANGE_PERCENT = "range_percent"
        public val RANGE_RADIUS = "radius"
        public val RANGE_AMOUNT = "product_amount"
        public val ACTION_MSG = "status_msg"
        public val IS_ELECTRIC = "is_electric"
        public val SUCCESSFUL_ACTION = "successful_action"
        public val ERROR_MSG = "error_msg"
        public val ERROR_CODE = "error_code"
        public val USER = "user"
        public val PERMISSIONS = "permissions"
        public val BATCH = "batch"
        public val OIL = "oil"

        public val BATCH_ARRAY_ALL = Arrays.asList("/location", "/odometer", "/attributes")
        public val SELECTION_BATCH = "selection"}
}