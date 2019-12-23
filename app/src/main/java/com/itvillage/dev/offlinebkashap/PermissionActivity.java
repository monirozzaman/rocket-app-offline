package com.itvillage.dev.offlinebkashap;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class PermissionActivity extends AppCompatActivity {
    Handler handler;
    Runnable r;
    private ImageButton back;
    private Button accessibilityBut, save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        back = findViewById(R.id.back);
        accessibilityBut = findViewById(R.id.phoneCall45);
        save = findViewById(R.id.nextBut);
        handler = new Handler();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PermissionActivity.this, HomeActivity.class));
            }
        });
        accessibilityBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsAccessibility(PermissionActivity.this);
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PermissionActivity.this, HomeActivity.class));
            }
        });
        r = new Runnable() {
            public void run() {
                if (!permissionAlreadyGranted()) {
                    save.setEnabled(false);
                    save.setBackgroundColor(Color.parseColor("#bcbaba"));
                } else {
                    save.setEnabled(true);
                    save.setBackground(ContextCompat.getDrawable(PermissionActivity.this, R.drawable.but_gradient));
                }
                handler.postDelayed(r, 1000);
            }
        };
        handler.post(r);
    }

    private boolean permissionAlreadyGranted() {
        if (/*verifyOverLay(this)
                && */isAccessiblityServicesEnable(this)) {
            return true;
        }
        return false;
    }

    private void openSettingsAccessibility(final Activity activity) {
        activity.startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 1);
    }

    private void openSettingsOverlay(final Activity activity) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
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

    @Override
    public void onBackPressed() {
    }
}
