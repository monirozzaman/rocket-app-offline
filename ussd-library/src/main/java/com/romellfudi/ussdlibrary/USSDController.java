package com.romellfudi.ussdlibrary;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telecom.PhoneAccountHandle;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Romell Dominguez
 * @version 1.1.c 27/09/2018
 * @since 1.0.a
 */
public class USSDController implements USSDInterface, USSDApi {

    protected static final String KEY_LOGIN = "KEY_LOGIN";
    protected static final String KEY_ERROR = "KEY_ERROR";
    protected static USSDController instance;
    protected Context context;
    protected HashMap<String, HashSet<String>> map;
    protected CallbackInvoke callbackInvoke;
    protected CallbackMessage callbackMessage;
    protected Boolean isRunning = false;

    private USSDInterface ussdInterface;
    private int simSlotNumber;

    private USSDController(Context context, int simSlot) {
        ussdInterface = this;
        this.simSlotNumber = simSlot;
        this.context = context;
    }

    /**
     * The Singleton building method
     *
     * @param context An activity that could call
     * @return An instance of USSDController
     */
    public static USSDController getInstance(Context context,int simSlot) {
        if (instance == null)
            instance = new USSDController(context,simSlot);
        return instance;
    }

    public static boolean verifyAccesibilityAccess(Context context) {
        boolean isEnabled = USSDController.isAccessiblityServicesEnable(context);
        if (!isEnabled) {
            if (context instanceof Activity) {
                openSettingsAccessibility((Activity) context);
            } else {
                Toast.makeText(
                        context,
                        "voipUSSD accessibility service is not enabled",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
        return isEnabled;
    }

    public static boolean verifyOverLay(Context context) {
        boolean m_android_doesnt_grant = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !Settings.canDrawOverlays(context);
        if (m_android_doesnt_grant) {
            if (context instanceof Activity) {
                openSettingsOverlay((Activity) context);
            } else {
                Toast.makeText(context,
                        "Overlay permission have not grant permission.",
                        Toast.LENGTH_LONG).show();
            }
            return false;
        } else
            return true;
    }

    private static void openSettingsAccessibility(final Activity activity) {
        android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(activity).create();
        alertDialog.setTitle("USSD Accessibility permission");
        alertDialog.setCancelable(false);
        ApplicationInfo applicationInfo = activity.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        String name = applicationInfo.labelRes == 0 ?
                applicationInfo.nonLocalizedLabel.toString() : activity.getString(stringId);
        alertDialog.setMessage("You must enable accessibility permissions for the app '" + name + "'");
        alertDialog.setIcon(R.drawable.logofinal);

        alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), 1);
            }
        });
        if (alertDialog != null) {
            alertDialog.show();
        }
        alertDialog.show();
    }

    private static void openSettingsOverlay(final Activity activity) {
        android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(activity).create();
        alertDialog.setTitle("USSD Overlay permission");
        alertDialog.setCancelable(false);
        ApplicationInfo applicationInfo = activity.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        String name = applicationInfo.labelRes == 0 ?
                applicationInfo.nonLocalizedLabel.toString() : activity.getString(stringId);
        alertDialog.setMessage("You must allow for the app to appear '" + name + "' on top of other apps.");
        alertDialog.setIcon(R.drawable.logofinal);

        alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + activity.getPackageName()));
                activity.startActivity(intent);
            }
        });
        if (alertDialog != null) {
            alertDialog.show();
        }
        alertDialog.show();
    }

    protected static boolean isAccessiblityServicesEnable(Context context) {
        AccessibilityManager am = (AccessibilityManager) context
                .getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (am != null) {
            for (AccessibilityServiceInfo service : am.getInstalledAccessibilityServiceList()) {
                if (service.getId().contains(context.getPackageName())) {
                    return USSDController.isAccessibilitySettingsOn(context, service.getId());
                }
            }
        }
        return false;
    }

    protected static boolean isAccessibilitySettingsOn(Context context, final String service) {
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

    /**
     * Invoke a dial-up calling a ussd number
     *
     * @param ussdPhoneNumber ussd number
     * @param map             Map of Login and problem messages
     * @param callbackInvoke  a callback object from return answer
     */
    public void callUSSDInvoke(String ussdPhoneNumber, HashMap<String, HashSet<String>> map, CallbackInvoke callbackInvoke) {
        callUSSDInvoke(ussdPhoneNumber, simSlotNumber, map, callbackInvoke);
    }

    /**
     * Invoke a dial-up calling a ussd number and
     * you had a overlay progress widget
     *
     * @param ussdPhoneNumber ussd number
     * @param map             Map of Login and problem messages
     * @param callbackInvoke  a callback object from return answer
     */
    public void callUSSDOverlayInvoke(String ussdPhoneNumber, HashMap<String, HashSet<String>> map, CallbackInvoke callbackInvoke) {
        callUSSDOverlayInvoke(ussdPhoneNumber, simSlotNumber, map, callbackInvoke);
    }

    /**
     * Invoke a dial-up calling a ussd number
     *
     * @param ussdPhoneNumber ussd number
     * @param simSlot         simSlot number to use
     * @param map             Map of Login and problem messages
     * @param callbackInvoke  a callback object from return answer
     */
    @SuppressLint("MissingPermission")
    public void callUSSDInvoke(String ussdPhoneNumber, int simSlot, HashMap<String, HashSet<String>> map, CallbackInvoke callbackInvoke) {
        this.callbackInvoke = callbackInvoke;
        this.map = map;
        if (verifyAccesibilityAccess(context)) {
            dialUp(ussdPhoneNumber, simSlot);
        } else {
            this.callbackInvoke.over("Check your accessibility");
        }
    }

    /**
     * Invoke a dial-up calling a ussd number and
     * you had a overlay progress widget
     *
     * @param ussdPhoneNumber ussd number
     * @param simSlot         simSlot number to use
     * @param map             Map of Login and problem messages
     * @param callbackInvoke  a callback object from return answer
     */
    @SuppressLint("MissingPermission")
    public void callUSSDOverlayInvoke(String ussdPhoneNumber, int simSlot, HashMap<String, HashSet<String>> map, CallbackInvoke callbackInvoke) {
        this.callbackInvoke = callbackInvoke;
        this.map = map;
        if (verifyAccesibilityAccess(context)) {
            dialUp(ussdPhoneNumber, simSlot);
        } else {
            this.callbackInvoke.over("Check your accessibility | overlay permission");
        }
    }

    private void dialUp(String ussdPhoneNumber, int simSlot) {
        if (map == null || (!map.containsKey(KEY_ERROR) || !map.containsKey(KEY_LOGIN))) {
            this.callbackInvoke.over("Bad Mapping structure");
            return;
        }
        if (ussdPhoneNumber.isEmpty()) {
            this.callbackInvoke.over("Bad ussd number");
            return;
        }
        String uri = Uri.encode("#");
        if (uri != null)
            ussdPhoneNumber = ussdPhoneNumber.replace("#", uri);
        Uri uriPhone = Uri.parse("tel:" + ussdPhoneNumber);
        if (uriPhone != null)
            isRunning = true;
        this.context.startActivity(getActionCallIntent(uriPhone, simSlot));
    }

    /**
     * get action call Intent
     *
     * @param uri     parsed uri to call
     * @param simSlot simSlot number to use
     */

    private Intent getActionCallIntent(Uri uri, int simSlot) {
        // https://stackoverflow.com/questions/25524476/make-call-using-a-specified-sim-in-a-dual-sim-device
   /*     final String simSlotName[] = {
                "extra_asus_dial_use_dualsim",
                "com.android.phone.extra.slot",
                "slot",
                "simslot",
                "sim_slot",
                "subscription",
                "Subscription",
                "phone",
                "com.android.phone.DialingMode",
                "simSlot",
                "slot_id",
                "simId",
                "simnum",
                "phone_type",
                "slotId",
                "slotIdx"
        };
  *//*
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("com.android.phone.force.slot", true);
        intent.putExtra("Cdma_Supp", true);

        for (String s : simSlotName)
            intent.putExtra(s, simSlot);

        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        if (telecomManager != null) {
            List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();
            if (phoneAccountHandleList != null && phoneAccountHandleList.size() > simSlot)
                intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandleList.get(simSlot));
        }*//*
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();


        Intent intent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(uri);
        intent.putExtra("com.android.phone.force.slot", true);
        intent.putExtra("Cdma_Supp", true);
        if (simSlot == 0) {   //0 for sim1
            for (String s : simSlotName)
                intent.putExtra(s, 0); //0 or 1 according to sim.......

            if (phoneAccountHandleList != null && phoneAccountHandleList.size() > 0)

                intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandleList.get(0));


        } else {     // 1 for sim2
            for (String s : simSlotName)
                intent.putExtra(s, 1); //0 or 1 according to sim.......

            if (phoneAccountHandleList != null && phoneAccountHandleList.size() > 1)
                intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandleList.get(1));

        }
        context.startActivity(intent);*/
        final Intent intent = new Intent(Intent.ACTION_CALL, uri);
        final int simSlotIndex = 1; //Second sim slot
        try {
            final Method getSubIdMethod = SubscriptionManager.class.getDeclaredMethod("getSubId", int.class);

            getSubIdMethod.setAccessible(true);
            final long subIdForSlot = ((long[]) getSubIdMethod.invoke(SubscriptionManager.class, simSlotIndex))[0];
            final ComponentName componentName = new ComponentName("com.android.phone", "com.android.services.telephony.TelephonyConnectionService");
            final PhoneAccountHandle phoneAccountHandle = new PhoneAccountHandle(componentName, String.valueOf(subIdForSlot));
            intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandle);

        } catch (Exception e) {
            e.printStackTrace();
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return intent;
    }

    public void sendData(String text) {
        USSDService.send(text);
    }

    public void send(String text, CallbackMessage callbackMessage) {
        this.callbackMessage = callbackMessage;
        ussdInterface.sendData(text);
    }

    public interface CallbackInvoke {
        void responseInvoke(String message);

        void over(String message);
    }

    public interface CallbackMessage {
        void responseMessage(String message);
    }
}
