package cls.simplecar.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("user/{uid}/vehicle/{id}/attributes")
    Call<ResponseBody> getVehicleAttributes(@Header("api-code") String apiCode,
                                            @Header("access-token-smart-car") String token,
                                            @Path("uid") String uid,
                                            @Path("id") String id);

    @GET("user/{uid}/vehicle/{id}/location")
    Call<ResponseBody> getVehicleLocation(@Header("api-code") String apiCode,
                                          @Header("access-token-smart-car") String token,
                                          @Path("uid") String uid,
                                          @Path("id") String id);
    @GET("user/{uid}/vehicle/{id}/range")
    Call<ResponseBody> getVehicleRange(@Header("api-code") String apiCode,
                                       @Header("access-token-smart-car") String token,
                                       @Path("uid") String uid,
                                       @Path("id") String id);
    @GET("user/{uid}/vehicle/{id}/odometer")
    Call<ResponseBody> getOdometer(@Header("api-code") String apiCode,
                                   @Header("access-token-smart-car") String token,
                                   @Path("uid") String uid,
                                   @Path("id") String id);
    @GET("user/{uid}/exchange/auth")
    Call<ResponseBody> exchangeAuthTokenForAccessToken(@Header("api-code") String apiCode,
                                                       @Header("access-token-smart-car") String token,
                                                       @Path("uid") String uid);
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("/user/{uid}/refresh")
    @FormUrlEncoded
    Call<ResponseBody> refreshToken(@Header("api-code") String apiCode,
                                    @Header("access-token-smart-car") String token,
                                    @Path("uid") String uid,@Field("auth") String auth
                                    ,@Field("client")String client
                                     );
    @GET("user/{uid}/validate/smartcar_token")
    Call<ResponseBody> validateToken(@Header("api-code") String apiCode,
                                     @Header("access-token-smart-car") String token,
                                     @Path("uid") String uid);
    @GET("user/{uid}/vehicle/{id}/logs")
    Call<ResponseBody> getVehicleLogs(@Header("api-code") String apiCode,
                                      @Header("access-token-smart-car") String token,
                                      @Path("uid") String uid,
                                      @Path("id") String id);
    @POST("user/{uid}/vehicle/{id}/lock")
    Call<ResponseBody> lockVehicle(@Header("api-code") String apiCode,
                                   @Header("access-token-smart-car") String token,
                                   @Path("uid") String uid,
                                   @Path("id") String id);
    @POST("user/{uid}/vehicle/{id}/unlock")
    Call<ResponseBody> unlockVehicle(@Header("api-code") String apiCode,
                                     @Header("access-token-smart-car") String token,
                                     @Path("uid") String uid,
                                     @Path("id") String id);
    @GET("user/{uid}/vehicle/{id}/is_electric")
    Call<ResponseBody> isElectric(@Header("api-code") String apiCode,
                                  @Header("access-token-smart-car") String token,
                                  @Path("uid") String uid,
                                  @Path("id") String id);
    @GET("user/{uid}/vehicle/")
    Call<ResponseBody> getCars(@Header("api-code") String apiCode,
                               @Header("access-token-smart-car") String token,
                               @Path("uid") String uid);
    @GET("user/{uid}/")
    Call<ResponseBody> getUser(
            @Header("api-code") String apiCode,
            @Header("access-token-smart-car") String token,
            @Path("uid") String uid);
    @POST("user/")
    Call<ResponseBody> signup(@Header("api-code") String apiCode,
                              @Header("access-token-smart-car") String token,
                              @Body String body);

    @GET("user/{uid}/exchange/auth")
    Call<ResponseBody> getAccessWithAuthToken(@Header("api-code") String apiCode,
                                  @Header("access-token-smart-car") String token,
                                  @Path("uid") String uid);


    @GET("/user/{uid}/validate/smartcar_token")
    Call<ResponseBody> isTokenValid(@Header("api-code") String apiCode,
                                              @Header("access-token-smart-car") String token,
                                              @Path("uid") String uid);

    @GET("user/{uid}/vehicle/{id}/oil")
    Call<ResponseBody> getOil(@Header("api-code") String apiCode,
                                  @Header("access-token-smart-car") String token,
                                  @Path("uid") String uid,
                                  @Path("id") String id);

    @GET("user/{uid}/vehicle/{id}/permissions")
    Call<ResponseBody> getPermissions(@Header("api-code") String apiCode,
                              @Header("access-token-smart-car") String token,
                              @Path("uid") String uid,
                              @Path("id") String id);

    @GET("user/{uid}/vehicle/{id}/market_value")
    Call<ResponseBody> getCarMarketValue(@Header("api-code") String apiCode,
                              @Header("access-token-smart-car") String token,
                              @Path("uid") String uid,
                              @Path("id") String id);

    @POST("user/{uid}/vehicle/{id}/start_charge")
    Call<ResponseBody> startCharge(@Header("api-code") String apiCode,
                                     @Header("access-token-smart-car") String token,
                                     @Path("uid") String uid,
                                     @Path("id") String id);
    @POST("user/{uid}/vehicle/{id}/stop_charge")
    Call<ResponseBody> stopCharge(@Header("api-code") String apiCode,
                                   @Header("access-token-smart-car") String token,
                                   @Path("uid") String uid,
                                   @Path("id") String id);
}