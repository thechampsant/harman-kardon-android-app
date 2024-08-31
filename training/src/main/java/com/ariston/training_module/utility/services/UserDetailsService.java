package com.ariston.training_module.utility.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class UserDetailsService extends Service {
    private Bundle userBundle;

    @Nullable
    private final IBinder localBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        userBundle = intent.getExtras();
        return localBinder;
    }


    public Bundle getUserBundle() {
        return userBundle;
    }

    public void setUserBundle(Bundle userBundle) {
        this.userBundle = userBundle;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        if (intent.getExtras() != null) {
            userBundle = intent.getExtras();
        }
    }

    public class MyBinder extends Binder {

        public UserDetailsService getService() {
            return UserDetailsService.this;

        }
    }
}
