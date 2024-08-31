package com.fieldforce.floorhygiene.retrofitFloor;

import com.ariston.training_module.modules.dashboard.apis.DashboardApis;
import com.ariston.training_module.modules.training_module.apis.TrainingModuleApis;
import com.ariston.training_module.networking.RetrofitConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by deepakkanyan on 22/07/20 at 3:52 PM.
 */
public class MyRetrofitService {


    static final String BASE_URL = "http://harman.infield.co.in/ispmobile/";
    static final String BASE_URL_DOC = "http://harman.infield.co.in/FileUploader/ISD/Harman_Kardon/FloorHygieneHandler.ashx?UploadedBy=50&DocID=DOCID_1234XYZ&DocType=JPG";
    //http://harman.infield.co.in/
    public static final String IMAGE_PREFIX = "http://harman.infield.co.in/ispmobile";



    static final int CONNECTION_TIMEOUT = 20;//10 sec
    static final int READ_TIMEOUT = 20;//10 sec
    static final int WRITE_TIMEOUT = 20;//10 sec

    private MyRetrofitService() {
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
        httpClient.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        //time between each byte read from the server
        httpClient.readTimeout( READ_TIMEOUT,TimeUnit.SECONDS);
        // time between each byte sent to server
        httpClient.writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS);
        httpClient.retryOnConnectionFailure(false);

        return httpClient;
    }

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(setUpRetrofitLogger()
                    .build());
    private static Retrofit retrofit = retrofitBuilder.build();

    private static FloorApis floorApis = retrofit.create(FloorApis.class);
    public static FloorApis getFloorApis() {
        return floorApis;
    }


}
