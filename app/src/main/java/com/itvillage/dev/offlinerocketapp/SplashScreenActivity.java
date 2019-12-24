package com.itvillage.dev.offlinerocketapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen2);
        final Context me = this;
        Intent mIntent = getIntent();
        final int time = mIntent.getIntExtra("time", 3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(me, FragmentShowActivity.class);
                intent.putExtra("stop", true);
                startActivity(intent);
                finish();
            }
        }, time);
    }
}
