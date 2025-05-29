package com.fieldforce.harmonkardonff.home.service;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {


    @GET("SubmitConfuguredData")
    Call<Bannerdata> SubmitConfuguredData(
            @Query("LoginId") String userName,
            @Query("DocId") String DocId);


}