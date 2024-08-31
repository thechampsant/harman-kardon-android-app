package com.vspl.docopd.API

import com.APIService.SubmitEmpDetail
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface Apiintefacec{

    @FormUrlEncoded
    @POST("spreadsheet/submit_employee_details")
    fun submit_employee_details(
        @Field("empcode") empcode: String?,
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("mobile") mobile: String?,
        @Field("designation") designation: String?,
            @Field("gender") gender: String?,
        //@Field("date") date: String?,
        @Field("project") project: String?

    ): Call<SubmitEmpDetail>

    @Headers("Content-Type: application/json")
    @POST("SubmitBulkDisplayModel")
    fun SubmitBulkDisplayModel(
        @Body trim:String
    ): Call<String>


}