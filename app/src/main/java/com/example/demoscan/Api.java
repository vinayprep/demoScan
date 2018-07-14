package com.example.demoscan;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("check-prod")
    Call<CheckSerial> postCheckSerial(@Header("Authorization") String api_token, @Field("brand_id") String brandId, @Field("serial_number") String serialNumber, @Field("location") String location);


}
