package com.itvillage.dev.offlinebkashap;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.itvillage.dev.offlinebkashap.fragment.BalanceShowFragment;
import com.itvillage.dev.offlinebkashap.fragment.CashOutFragment;
import com.itvillage.dev.offlinebkashap.fragment.MobileRechargeFragment;
import com.itvillage.dev.offlinebkashap.fragment.PaymentFragment;
import com.itvillage.dev.offlinebkashap.fragment.SendMoneyFragment;

public class FragmentShowActivity extends AppCompatActivity {
    private String getOptionName,simName,acNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_and_another_input);

        getOptionName = getIntent().getExtras().getString("getOptionName");
        simName = getIntent().getExtras().getString("simName");
        acNumber=getIntent().getExtras().getString("acNo");

        View view = getLayoutInflater().inflate(R.layout.action_bar_costom, null);
        TextView textView = view.findViewById(R.id.action_bar_title);
        textView.setText(getOptionName);
        textView.setGravity(Gravity.CENTER);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

           /*-------------------------------------- Option Selection----------------------------------------*/
        if (getOptionName.equals("Send Money")) {
            FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
            fragment.replace(R.id.fragment_layout, new SendMoneyFragment(acNumber, this));
            fragment.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragment.addToBackStack(null);
            fragment.commit();
        } else if (getOptionName.equals("Balance")) {
            FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
            fragment.replace(R.id.fragment_layout, new BalanceShowFragment(this));
            fragment.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragment.addToBackStack(null);
            fragment.commit();
        } else if (getOptionName.equals("Mobile Recharge")) {
            FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
            fragment.replace(R.id.fragment_layout, new MobileRechargeFragment(acNumber, simName, this));
            fragment.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragment.addToBackStack(null);
            fragment.commit();
        } else if (getOptionName.equals("Cash Out")) {
            FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
            fragment.replace(R.id.fragment_layout, new CashOutFragment(acNumber, this));
            fragment.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragment.addToBackStack(null);
            fragment.commit();
        } else if (getOptionName.equals("Payment")) {
            FragmentTransaction fragment = getSupportFragmentManager().beginTransaction();
            fragment.replace(R.id.fragment_layout, new PaymentFragment(acNumber, this));
            fragment.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragment.addToBackStack(null);
            fragment.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void closeKeyboard() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
    }
}
