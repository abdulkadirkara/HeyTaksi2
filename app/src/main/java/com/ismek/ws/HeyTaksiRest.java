package com.ismek.ws;

import com.ismek.entity.ActiveLocationInfo;
import com.ismek.entity.BaseReturn;
import com.ismek.entity.LoginRequest;
import com.ismek.entity.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface HeyTaksiRest {

    @POST("loginRest/login")
    Call<BaseReturn<LoginResponse>> login(@Body LoginRequest loginRequest);

    @POST("loginRest/saveOrUpdate")
    Call<BaseReturn<String>> signIn(@Body LoginResponse user);

    @POST("activelocationinfo/save")
    Call<BaseReturn<String>> activeLocationInfoSave(@Body ActiveLocationInfo info);

    @GET("activelocationinfo/findAll")
    Call<BaseReturn<List<ActiveLocationInfo>>> activeLocationInfoFindAll();

}
