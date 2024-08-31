package com.ariston.training_module.networking;

import com.ariston.training_module.modules.dashboard.apis.DashboardApis;
import com.ariston.training_module.modules.training_module.apis.TrainingModuleApis;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {


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

    private static TrainingModuleApis trainingModuleApi = retrofit.create(TrainingModuleApis.class);

    public static TrainingModuleApis getTrainingModuleApi() {
        return trainingModuleApi;
    }

    private static DashboardApis dashboardApis = retrofit.create(DashboardApis.class);

     public static DashboardApis getDashboardModuleApis(){
        return dashboardApis;
    }

}
