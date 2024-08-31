package com.fieldforce.harmonkardonff;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.fieldforce.utility.PermissionUtils;

import app.core.image.slider.AppConstant;

public class SplashScreen extends Activity implements ViewSwitcher.ViewFactory {
    // Splash screen timer


    private static final String TAG = "IntroActivity";

    private final int[] images = {R.drawable.splash_harman,
            R.drawable.splash_infinity};
    private int index = 0;
    private final int interval = 1500;
    private boolean isRunning = true;

    private String[] permissionsRequired = new String[]
            {
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
            };
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

       /* if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            checkForAllPermissions();
        } else {
            // do something for phones running an SDK before lollipop


        }*/

        splashHandler();

        /*new Handler().postDelayed(new Runnable() {

         *//*
         * Showing splash screen with a timer. This will be useful when you
         * want to show case your app logo / company
         *//*

				@Override
				public void run() {
					// This method will be executed once the timer is over
					// Start your app main activity
					Intent i = new Intent(SplashScreen.this, MainActivity.class);
					startActivity(i);

					// close this activity
					finish();
				}
				
			}, SPLASH_TIME_OUT);*/


    }

    private void splashHandler() {

        ImageView imageView = findViewById(R.id.jblImage);

        Animation animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_from_right);
        animSlide.setDuration(1000);
// Start the animation like this
        imageView.startAnimation(animSlide);

        animSlide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                RelativeLayout layout = findViewById(R.id.rlSplash);
                layout.setBackgroundColor(getResources().getColor(R.color.black));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
						imageView.setVisibility(View.GONE);
                        startAnimatedBackground();
                    }
                }, 500);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



        /*
         * Showing splash screen with a timer. This will be useful when you
         * want to show case your app logo / company
         */
	/*	new Handler().postDelayed(() -> {
			// This method will be executed once the timer is over
			// Start your app main activity
			//Intent i = new Intent(SplashScreen.this, MainActivity.class);
			//startActivity(i);
			// close this activity
		//	finish();
		}, SPLASH_TIME_OUT);*/
    }

    private void checkForAllPermissions() {
        boolean isAllPermissionGranted = false;
        isAllPermissionGranted = PermissionUtils.checkForPermission(this, AppConstant.PERM_REQ_CODE, permissionsRequired);
        if (isAllPermissionGranted) {
            splashHandler();
        } else {

            Toast.makeText(this, "All Permission not granted...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstant.PERM_REQ_CODE) {
            checkForAllPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConstant.PERM_REQ_CODE && PermissionUtils.permissionsGrantedCheck(grantResults)) {
            //ShowToast("Permissions Granted");
            Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show();
            splashHandler();
        } else {
            RelativeLayout layout = findViewById(R.id.rlSplash);
            layout.setBackground(getResources().getDrawable(R.drawable.splash_screen_background));
            checkForAllPermissions();
        }
    }


    private void startAnimatedBackground() {
        Animation aniIn = AnimationUtils.loadAnimation(this,
                R.anim.fragment_fade_enter);
        aniIn.setDuration(1500);
        Animation aniOut = AnimationUtils.loadAnimation(this,
                R.anim.fragment_fade_exit);
        aniOut.setDuration(200);

        final ImageSwitcher imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
        imageSwitcher.setInAnimation(aniIn);
        imageSwitcher.setOutAnimation(aniOut);
        imageSwitcher.setFactory(this);
        imageSwitcher.setImageResource(images[index]);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                if (isRunning) {
                    index++;
                    index = index % images.length;
                    Log.d("Intro Screen", "Change Image " + index);
                    imageSwitcher.setImageResource(images[index]);

                    if(index==1){

                    	new Handler().postDelayed(() -> {
							Intent i = new Intent(SplashScreen.this, MainActivity.class);
							startActivity(i);
							// close this activity
							finish();
						}, 2000);

					}else{

						handler.postDelayed(this, interval);
					}

                }
            }
        };
        handler.postDelayed(runnable, interval);

    }

    @Override
    public View makeView() {
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    @Override
    public void finish() {
        isRunning = false;
        Log.e("________", "FINISHED  111ß");
        super.finish();
    }
}
