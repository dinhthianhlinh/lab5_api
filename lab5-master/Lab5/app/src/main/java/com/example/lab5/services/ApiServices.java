package com.example.lab5.services;

import com.example.lab5.model.Distributor;
import com.example.lab5.model.Response;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServices {

//    *****
//    Trường hợp dùng JSON server trên máy cá nhân:
//            - Nếu máy ảo android studio thì: Chạy json server không cho tham số -h , trong code app thì viết link:  http://10.0.2.2/xxxxxx
//            - Nếu máy ảo genymotion thì: Chạy json server không cho tham số -h , trong code app thì viết link:  http://10.0.3.2/xxxxxx
//            - Nếu máy ảo iphone (với macbook) thì: Chạy json server không cho tham số -h , trong code app thì viết link:  http://127.0.0.1/xxxxxx  hoặc  http://localhost/xxxxxx
//    không cần lấy IP wifi

    public static String BASE_URL = "http://10.0.2.2:3000/api/";

//    annotation @GET cho method và url phương gọi

    @GET("get-list-distributor")
    Call<Response<ArrayList<Distributor>>> getListDistributor();

    @GET("search-distributor")
    Call<Response<ArrayList<Distributor>>> searchDistributor(@Query("key") String key);

    @POST("add-distributors")
    Call<Response<Distributor>> addDistributor(@Body Distributor distributor);

    //    Param url sẽ bỏ vào {}
    @GET("get-distributor-by-id/{id}")
    Call<Response<Distributor>> getDistributorById(@Path("id") String id);

    @DELETE("delete-distributor-by-id/{id}")
    Call<Response<Distributor>> deleteDistributorById(@Path("id") String id);

    @PUT("update-distributor-by-id/{id}")
    Call<Response<Distributor>> updateDistributorById(@Path("id") String id, @Body Distributor distributor);


}
