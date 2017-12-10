package com.ismek.ws;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ismek.util.UnixEpochDateTypeAdapter;

import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiClient {
 
    public static final String BASE_URL = "http://165.227.165.130:8081/HeyTaksi/rest/";
    private static Retrofit retrofit = null;
 
 
    public static Retrofit getClient() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, UnixEpochDateTypeAdapter.getUnixEpochDateTypeAdapter())
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}