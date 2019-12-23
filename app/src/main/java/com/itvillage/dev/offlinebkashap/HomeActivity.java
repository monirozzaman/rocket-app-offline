package com.itvillage.dev.offlinebkashap;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.itvillage.dev.sqlite.SQLiteDB;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ArrayList<String> rows = new ArrayList<>();
    private CardView cashout, sendmoney, payment, mobileRecharge, bill, moneyRequest;
    private Button balance;
    private TextView deviceName, chargeLevel;
    private Boolean firstStart;
    private BottomSheetDialog bottomSheetDialog;
    private InterstitialAd mInterstitialAd1, mInterstitialAd2, mInterstitialAd3, mInterstitialAd4, mInterstitialAd5;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    return true;
                case R.id.menu:
                    calculatorBottomSheetShow();
                    return true;
                case R.id.help:
                    settingBottomSheetShow();

                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cashout = findViewById(R.id.cashOut);
        sendmoney = findViewById(R.id.sandMoney);
        payment = findViewById(R.id.makePayment);
        mobileRecharge = findViewById(R.id.mobileRecharge);
        bill = findViewById(R.id.payBill);
        moneyRequest = findViewById(R.id.moneyRequst);

        TextView mTextMessage = (TextView) findViewById(R.id.message);
        deviceName = (TextView) findViewById(R.id.name);
        chargeLevel = (TextView) findViewById(R.id.acNo);

        balance = (Button) findViewById(R.id.balance);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            SharedPreferences sharedPreferences = getSharedPreferences("PREFS", 0);
            firstStart = sharedPreferences.getBoolean("first_time_start", true);
            if (firstStart) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("first_time_start", false);
                editor.commit();

                startActivity(new Intent(HomeActivity.this, SplashScreenHomeActivity.class));

            }
        }
        //Ads
        MobileAds.initialize(this, "ca-app-pub-5203976193543346~5370168648");
        // MobileAds.initialize(view.getContext(), "ca-app-pub-3940256099942544~3347511713");

        // Interstitial Ads One
        mInterstitialAd1 = new InterstitialAd(this);
        mInterstitialAd1.setAdUnitId("ca-app-pub-5203976193543346/1430923638");
        mInterstitialAd1.loadAd(new AdRequest.Builder().build());
        // Interstitia2 Ads One
        mInterstitialAd2 = new InterstitialAd(this);
        mInterstitialAd2.setAdUnitId("ca-app-pub-5203976193543346/4458318326");
        mInterstitialAd2.loadAd(new AdRequest.Builder().build());
        // Interstitia3 Ads One
        mInterstitialAd3 = new InterstitialAd(this);
        mInterstitialAd3.setAdUnitId("ca-app-pub-5203976193543346/6300106931");
        mInterstitialAd3.loadAd(new AdRequest.Builder().build());
        // Interstitia4 Ads One
        mInterstitialAd4 = new InterstitialAd(this);
        mInterstitialAd4.setAdUnitId("ca-app-pub-5203976193543346/6892909974");
        mInterstitialAd4.loadAd(new AdRequest.Builder().build());
        // Interstitia5 Ads One
        mInterstitialAd5 = new InterstitialAd(this);
        mInterstitialAd5.setAdUnitId("ca-app-pub-5203976193543346/2953664966");
        mInterstitialAd5.loadAd(new AdRequest.Builder().build());

        deviceName.setText(getDeviceName());

        chargeLevel.setText("Charge " + getBatteryCrgLevel() + "%");

        cashout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!permissionAlreadyGranted()) {
                    if (mInterstitialAd2.isLoaded()) {
                        mInterstitialAd2.show();
                    } else {
                        Log.e("Ads", "not showing");
                    }
                    startActivity(new Intent(HomeActivity.this, PermissionActivity.class));
                } else {
                    if (mInterstitialAd2.isLoaded()) {
                        mInterstitialAd2.show();
                    } else {
                        Log.e("Ads", "not showing");
                    }
                    CashOut();
                }

            }
        });

        sendmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!permissionAlreadyGranted()) {
                    if (mInterstitialAd3.isLoaded()) {
                        mInterstitialAd3.show();
                    } else {
                        Log.e("Ads", "not showing");
                    }
                    startActivity(new Intent(HomeActivity.this, PermissionActivity.class));
                } else {
                    if (mInterstitialAd3.isLoaded()) {
                        mInterstitialAd3.show();
                    } else {
                        Log.e("Ads", "not showing");
                    }
                    SendMoney();
                }
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!permissionAlreadyGranted()) {
                    startActivity(new Intent(HomeActivity.this, PermissionActivity.class));
                } else {
                    Payment();
                }
            }
        });

        mobileRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!permissionAlreadyGranted()) {
                    if (mInterstitialAd4.isLoaded()) {
                        mInterstitialAd4.show();
                    } else {
                        Log.e("Ads", "not showing");
                    }
                    startActivity(new Intent(HomeActivity.this, PermissionActivity.class));
                } else {
                    if (mInterstitialAd4.isLoaded()) {
                        mInterstitialAd4.show();
                    } else {
                        Log.e("Ads", "not showing");
                    }
                    MobileRecharge();
                }
            }
        });

        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!permissionAlreadyGranted()) {
                    startActivity(new Intent(HomeActivity.this, PermissionActivity.class));
                } else {
                    Bill();
                }
            }
        });

        moneyRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!permissionAlreadyGranted()) {
                    if (mInterstitialAd5.isLoaded()) {
                        mInterstitialAd5.show();
                    } else {
                        Log.e("Ads", "not showing");
                    }
                    startActivity(new Intent(HomeActivity.this, PermissionActivity.class));
                } else {
                    if (mInterstitialAd5.isLoaded()) {
                        mInterstitialAd5.show();
                    } else {
                        Log.e("Ads", "not showing");
                    }
                    MoneyRequest();
                }
            }
        });

        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!permissionAlreadyGranted()) {
                    if (mInterstitialAd1.isLoaded()) {
                        mInterstitialAd1.show();
                    } else {
                        Log.e("Ads", "not showing");
                    }
                    startActivity(new Intent(HomeActivity.this, PermissionActivity.class));
                } else {
                    if (mInterstitialAd1.isLoaded()) {
                        mInterstitialAd1.show();
                    } else {
                        Log.e("Ads", "not showing");
                    }
                    Balance();
                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        createSqliteDbAndTable();
    }

    private void createSqliteDbAndTable() {
        SQLiteDB.createDatabase(this, "bkashoffline");
        rows.add("number");
        SQLiteDB.createTable("cashout", rows);
    }

    private void Balance() {

        Intent sendMoney = new Intent(HomeActivity.this, FragmentShowActivity.class);
        sendMoney.putExtra("getOptionName", "Balance");
        startActivity(sendMoney);

    }

    private void MoneyRequest() {
        Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
    }

    private void Bill() {
        Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
    }

    private void MobileRecharge() {
        Intent sendMoney = new Intent(HomeActivity.this, GetPhoneNumberActivity.class);
        sendMoney.putExtra("getOptionName", "Mobile Recharge");
        sendMoney.putExtra("headingOfNumberString", "For");
        startActivity(sendMoney);
    }

    private void Payment() {
        Intent cashOut = new Intent(HomeActivity.this, GetPhoneNumberActivity.class);
        cashOut.putExtra("headingOfNumberString", "Merchant Number");
        cashOut.putExtra("getOptionName", "Payment");
        startActivity(cashOut);

    }

    private void SendMoney() {
        Intent sendMoney = new Intent(HomeActivity.this, GetPhoneNumberActivity.class);
        sendMoney.putExtra("getOptionName", "Send Money");
        sendMoney.putExtra("headingOfNumberString", "To");
        startActivity(sendMoney);
    }

    private void CashOut() {
        Intent cashOut = new Intent(HomeActivity.this, GetPhoneNumberActivity.class);
        cashOut.putExtra("getOptionName", "Cash Out");
        cashOut.putExtra("headingOfNumberString", "Agent Number");
        startActivity(cashOut);

    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public String getBatteryCrgLevel() {
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, iFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level / (float) scale;

        int crgLevel = (int) (batteryPct * 100);
        return String.valueOf(crgLevel);

    }

    public void calculatorBottomSheetShow() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_calculator, null);
        final EditText amount = view.findViewById(R.id.editText2);

        Button calculate = view.findViewById(R.id.calculate);

        final TextView sandMonayCost = view.findViewById(R.id.sendMoneyCost);
        final TextView cashOutCost = view.findViewById(R.id.cashOutCost);

        final CardView cardView = view.findViewById(R.id.cardView);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!amount.getText().toString().equals("")) {
                    double amountDouble = Double.parseDouble(amount.getText().toString());
                    cardView.setVisibility(View.VISIBLE);

                    double sentMoneyCost = 5.00;
                    double cashOutCostd = amountDouble * 0.0185;
                    sandMonayCost.setText(String.valueOf("For SendMoney: " + sentMoneyCost));
                    cashOutCost.setText(String.valueOf("For CashOut: " + cashOutCostd));
                } else {
                    Toast.makeText(getApplicationContext(), "Amount Needed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

    }

    private void settingBottomSheetShow() {
        View view = getLayoutInflater().inflate(R.layout.setting_bottom_sheet, null);

        Button about = view.findViewById(R.id.about);
        Button video_tutorial = view.findViewById(R.id.video_tutorial);
        Button notification = view.findViewById(R.id.notification);
        Button reatUs = view.findViewById(R.id.reatUs);
        Button Sugg = view.findViewById(R.id.Sugg);
        Button howtouseit = view.findViewById(R.id.howtouseit);
        Button report = view.findViewById(R.id.report1);
        Button privacy = view.findViewById(R.id.privecy);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AboutActivity.class));
            }
        });
        video_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=iBrzJIa-_64&list=PLc9fjuvaiz3GJ2DYogiJqDwIwBTwOpAIr"));
                startActivity(i);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        reatUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.itvillage.dev.offlinebkashap")));
            }
        });
        Sugg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "itvillage029@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Offline bkash App");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Type your Valuable Suggestion:");
                startActivity(Intent.createChooser(emailIntent, "Send Suggestion..."));
            }
        });
        howtouseit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGetStartVideoDemo();
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "itvillage029@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Offline bkash App");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Type your report:");
                startActivity(Intent.createChooser(emailIntent, "Send report..."));
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.shorturl.at/hEIJS"));
                startActivity(i);
            }
        });

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    public void showGetStartVideoDemo() {

        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/siyam_rupali.ttf");
        View view = getLayoutInflater().inflate(R.layout.dialog_show_getstart_video_demo, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.basic_tutorial_promo;
        Uri uri = Uri.parse(videoPath);

        Button skipbut = view.findViewById(R.id.getstart);
        VideoView videoView = view.findViewById(R.id.showDemo);
        TextView instrction = view.findViewById(R.id.instraction);

        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.start();

        instrction.setTypeface(typeface);
        instrction.setText("টিউটোরিয়াল ক্লিপ");
        skipbut.setVisibility(View.INVISIBLE);

        alertDialog.setView(view);
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        exit();
    }

    public void exit() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit");
        alertDialogBuilder.setIcon(R.drawable.exit_icon);
        alertDialogBuilder.setMessage("Are you sure,You want to exit");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                ActivityCompat.finishAffinity((Activity) HomeActivity.this);
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private boolean permissionAlreadyGranted() {
        if (isAccessiblityServicesEnable(this)) {
            return true;
        }
        return false;
    }

    public boolean verifyOverLay(Context context) {
        boolean m_android_doesnt_grant = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !Settings.canDrawOverlays(context);
        if (m_android_doesnt_grant) {
            if (context instanceof Activity) {
                return false;
            } else {
                Toast.makeText(context,
                        "Overlay permission have not grant permission.",
                        Toast.LENGTH_LONG).show();
            }
            return false;
        } else
            return true;
    }

    public boolean isAccessiblityServicesEnable(Context context) {
        AccessibilityManager am = (AccessibilityManager) context
                .getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (am != null) {
            for (AccessibilityServiceInfo service : am.getInstalledAccessibilityServiceList()) {
                if (service.getId().contains(context.getPackageName())) {
                    return isAccessibilitySettingsOn(context, service.getId());
                }
            }
        }
        return false;
    }

    public boolean isAccessibilitySettingsOn(Context context, final String service) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    context.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            //
        }
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                    context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(':');
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();
                    if (accessabilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
