package com.equalinfotechuser.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.equalinfotechuser.AppSharedPreference;
import com.equalinfotechuser.R;

public class SplashActivity extends AppCompatActivity {
    AppSharedPreference sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_splash);
        sharedpreferences = AppSharedPreference.getsharedprefInstance(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {



                if (sharedpreferences.getuser_id().equals("")){
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else {

                    Intent intent = new Intent(SplashActivity.this, HomeActivityStudent.class);
                    startActivity(intent);




                }






            }
        },2000);

    }
}
