package com.vspl.docopd.API

import com.APIService.SubmitEmpDetail
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Apiintefacec1{

    @FormUrlEncoded
    @POST("spreadsheet/submit_no_details")
    fun submit_employee_details_NO(
            @Field("empcode") empcode: String?,
            @Field("name") name: String?,
            @Field("email") email: String?,
            @Field("mobile") mobile: String?,
            @Field("designation") designation: String?,
            @Field("gender") gender: String?,
            @Field("project") project: String?//,
            //@Field("date") date: String?

    ): Call<SubmitEmpDetail>


}