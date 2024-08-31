package com.fieldforce.retrofit_setup;


import com.fieldforce.harmonkardonff.corona_survey_module.network.CoronaSurveyApi;
import com.suveyform.network.SurveyFormApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    //https://futurestud.io/tutorials/retrofit-2-log-requests-and-responses

    private ServiceGenerator() {
        //restriction for the instantiation of this SingleTon class.
    }

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    private static OkHttpClient.Builder setUpRetrofitLogger() {
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …

        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        //esatblish connection to server for eg -> handshaking with server and during this server decides server allow your application or not.
        httpClient.connectTimeout(RetrofitConstants.CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        //time between each byte read from the server
        httpClient.readTimeout(RetrofitConstants.READ_TIMEOUT,TimeUnit.SECONDS);
        // time between each byte sent to server
        httpClient.writeTimeout(RetrofitConstants.WRITE_TIMEOUT,TimeUnit.SECONDS);
        httpClient.retryOnConnectionFailure(false);

        return httpClient;
    }

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(RetrofitConstants.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(setUpRetrofitLogger()
                    .build());
    private static Retrofit retrofit = retrofitBuilder.build();

    private static CoronaSurveyApi coronaSurveyApi = retrofit.create(CoronaSurveyApi.class);
    public static CoronaSurveyApi getCoronaSurveyApi() {
        return coronaSurveyApi;
    }
    private static SurveyFormApi coronaSurveyApi1 = retrofit.create(SurveyFormApi.class);
    public static SurveyFormApi getCoronaSurveyApi1() {
        return coronaSurveyApi1;
    }
}
