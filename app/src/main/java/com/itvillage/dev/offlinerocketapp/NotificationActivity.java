package com.itvillage.dev.offlinerocketapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {
    private TextView heading, body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        heading = findViewById(R.id.heading);
        body = findViewById(R.id.bodyofnotifi);
        heading.setText("\"" + "Details" + "\"");
        body.setText(getIntent().getExtras().getString("body"));
    }
}
