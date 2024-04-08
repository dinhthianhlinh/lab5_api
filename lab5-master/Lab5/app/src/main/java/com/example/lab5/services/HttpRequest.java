package com.example.lab5.services;

import static com.example.lab5.services.ApiServices.BASE_URL;

import com.google.gson.Gson;

import java.util.Base64;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class HttpRequest {
    //    biến interface ApiServices
    private ApiServices requestInterface;

    public HttpRequest() {
//        create retrofit
        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiServices.class);

    }

    public ApiServices callApi() {
        return requestInterface;
    }

}
