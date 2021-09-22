package com.example.psodf2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {
    @GET("api_test")
    Call<TestApi> api_test();

    @POST("app/login")
    Call<Login> app_login(@Body Login login);

    @GET("api/batch")
    Call<List<Batch>> get_batch(@Query("api_token") String api_token);

    @GET("api/batch/{id}/class")
    Call<List<Classs>> batch_find_class(@Path("id") int id, @Query("api_token") String api_token);

    @GET("api/class/{id}/student")
    Call<List<Student>> class_find_student(@Path("id") int id, @Query("api_token") String api_token);
}
