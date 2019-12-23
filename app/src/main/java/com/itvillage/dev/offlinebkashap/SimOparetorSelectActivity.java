package com.itvillage.dev.offlinebkashap;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SimOparetorSelectActivity extends AppCompatActivity {
    private String getOptionName,acNo;
    private CardView airtel,robi,gp,banglalink,teletalk;
    private TextView acNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim_oparetor_select);

        acNumber= findViewById(R.id.number);
        getOptionName = getIntent().getExtras().getString("getOptionName");
        acNo= getIntent().getExtras().getString("acNo");

        airtel=findViewById(R.id.airtel);
        robi=findViewById(R.id.robi);
        gp=findViewById(R.id.gp);
        banglalink=findViewById(R.id.banglalink);
        teletalk=findViewById(R.id.teletalk);

        acNumber.setText(acNo);

        View view = getLayoutInflater().inflate(R.layout.action_bar_costom, null);
        TextView textView = view.findViewById(R.id.action_bar_title);
        textView.setText(getOptionName);
        textView.setGravity(Gravity.CENTER);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        airtel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SimOparetorSelectActivity.this,
                        FragmentShowActivity.class);
                intent.putExtra("acNo", acNo);
                intent.putExtra("simName", "2");
                intent.putExtra("getOptionName", getOptionName);
                startActivity(intent);
            }
        });
        robi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SimOparetorSelectActivity.this,
                        FragmentShowActivity.class);
                intent.putExtra("acNo", acNo);
                intent.putExtra("simName", "1");
                intent.putExtra("getOptionName", getOptionName);
                startActivity(intent);
            }
        });
        gp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SimOparetorSelectActivity.this,
                        FragmentShowActivity.class);
                intent.putExtra("acNo", acNo);
                intent.putExtra("simName", "4");
                intent.putExtra("getOptionName", getOptionName);
                startActivity(intent);
            }
        });
        banglalink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SimOparetorSelectActivity.this,
                        FragmentShowActivity.class);
                intent.putExtra("acNo", acNo);
                intent.putExtra("simName", "3");
                intent.putExtra("getOptionName", getOptionName);
                startActivity(intent);
            }
        });
        teletalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SimOparetorSelectActivity.this,
                        FragmentShowActivity.class);
                intent.putExtra("acNo", acNo);
                intent.putExtra("simName", "5");
                intent.putExtra("getOptionName", getOptionName);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
