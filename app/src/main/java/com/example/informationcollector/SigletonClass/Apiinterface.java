package com.example.informationcollector.SigletonClass;

import com.example.informationcollector.ModelClass.Clintdata;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Apiinterface {
    @Headers({"Accept: application/json"})
    @POST("index.php")
    Call<Clintdata> insertdata(@Body String jsonObject);
}
