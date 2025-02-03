package com.ariston.training_module.networking;

public class RetrofitConstants {

    static final String BASE_URL = "http://harman.infield.co.in/ispmobile/";
    //https://aristonfieldforce.infield.co.in/
    public static final String IMAGE_PREFIX = "https://aristonfieldforce.infield.co.in/ispmobile";

    private RetrofitConstants() {

    }

    static final int CONNECTION_TIMEOUT = 20;//10 sec
    static final int READ_TIMEOUT = 20;//10 sec
    static final int WRITE_TIMEOUT = 20;//10 sec
}
