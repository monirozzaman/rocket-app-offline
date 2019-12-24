package com.itvillage.dev.offlinebkashap.fragment;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.itvillage.dev.offlinebkashap.FragmentShowActivity;
import com.itvillage.dev.offlinebkashap.R;
import com.itvillage.dev.sqlite.SQLiteDB;
import com.romellfudi.permission.PermissionService;
import com.romellfudi.ussdlibrary.USSDApi;
import com.romellfudi.ussdlibrary.USSDController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;

/**
 * Created by monirozzamanroni on 7/14/2019.
 */

@SuppressLint("ValidFragment")
public class SendMoneyFragment extends Fragment {


    private EditText getAmounts, getPin;
    private ImageButton send;
    private TextView acNo;
    private String acNumber, amount, pin;

    private HashMap<String, HashSet<String>> map;
    private USSDApi ussdApi;
    private ArrayList<String> inputValue;

    private Context context;
    private int count = 0;
    private PermissionService.Callback callback = new PermissionService.Callback() {

        @Override
        public void onRefuse(ArrayList<String> RefusePermissions) {
            Toast.makeText(getContext(), "dinay", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        @Override
        public void onFinally() {
            // pass
        }
    };

    @SuppressLint("ValidFragment")
    public SendMoneyFragment(String acNumber, FragmentShowActivity fragmentShowActivity) {
        this.acNumber = acNumber;
        this.context = fragmentShowActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        map = new HashMap<>();
        map.put("KEY_LOGIN", new HashSet<>(Arrays.asList("espere", "waiting", "loading", "esperando")));
        map.put("KEY_ERROR", new HashSet<>(Arrays.asList("problema", "problem", "error", "null")));

        new PermissionService(getActivity()).request(
                new String[]{permission.CALL_PHONE, permission.READ_PHONE_STATE},
                callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_op1, container, false);

        getAmounts = view.findViewById(R.id.amountsSendMoney);
        getPin = view.findViewById(R.id.passwordSendMoney);
        send = view.findViewById(R.id.sendSendMoney);
        acNo = view.findViewById(R.id.numberSendMoney);

        acNo.setText(acNumber);
        setHasOptionsMenu(false);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getAmounts.getText().toString().equals("") && getPin.getText().toString().equals("")) {
                    getAmounts.setError("Amount is Required");

                    getPin.setError("PIN is Required");
                } else {
                    if (getAmounts.getText().toString().matches("\\d+") && getPin.getText().toString().matches("\\d+")) {
                        inputValue = new ArrayList<>();
                        amount = getAmounts.getText().toString();

                        pin = getPin.getText().toString();
                        inputValue.add("2");
                        inputValue.add(acNumber);
                        inputValue.add(amount);
                        inputValue.add(pin);
                        closeKeyBoard();
                        SendMoney(0);
                    } else {
                        getAmounts.setError("Invalid Amount");
                        getPin.setError("Invalid PIN");
                    }
                }
            }
        });
        return view;
    }

    private void SendMoney(int simslot) {
        ArrayList<String> phone = new ArrayList<>();
        phone.add(acNumber);
        SQLiteDB.insert("cashout", phone);
        ussdApi = USSDController.getInstance(getActivity(), simslot);
        //  if (USSDController.verifyOverLay(getActivity())) {

            Log.d("APP", "START SPLASH DIALOG");
        String phoneNumber = "*322#";

            ussdApi.callUSSDOverlayInvoke(phoneNumber, map, new USSDController.CallbackInvoke() {
                @Override
                public void responseInvoke(String message) {
                              /*  Feed Back Mag*/
                    if (count <= 1) {
                        if (count == 0) {
                            ussdApi.send(inputValue.get(0), new USSDController.CallbackMessage() {
                                @Override
                                public void responseMessage(String message) {
                                    ++count;
                                    ussdApi.send(inputValue.get(1), new USSDController.CallbackMessage() {
                                        @Override
                                        public void responseMessage(String message) {

                                        }
                                    });
                                }
                            });
                        }
                        if (count == 1) {
                            ussdApi.send(inputValue.get(2), new USSDController.CallbackMessage() {
                                @Override
                                public void responseMessage(String message) {
                                    ++count;
                                    ussdApi.send(inputValue.get(3), new USSDController.CallbackMessage() {
                                        @Override
                                        public void responseMessage(String message) {
                                        }
                                    });
                                }
                            });
                        }

                    }
                }

                @Override
                public void over(String message) {
                    Log.e("APP", message);
                    if (!String.valueOf(message).equals("Check your accessibility | overlay permission")) {
                        showDailog(String.valueOf(message));

                    } else {

                    }
                }
            });
        }
    // }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callback.handler(permissions, grantResults);
    }

    public void showDailog(String mgs) {

        if (mgs.equals(" ")) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();

            alertDialog.setTitle("Dear Sir,");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("The bKash Account No is invalid");
            alertDialog.setIcon(R.drawable.logofinal);

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    feedBackDialog();
                }
            });
            alertDialog.show();
        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(context).create();

            alertDialog.setTitle("Dear Customer,");
            alertDialog.setCancelable(false);
            alertDialog.setMessage(mgs);
            alertDialog.setIcon(R.drawable.logofinal);

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getContext(), "Thank You for Using", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(intent);
                    ActivityCompat.finishAffinity((Activity) context);
                    int pid = android.os.Process.myPid();
                    android.os.Process.killProcess(pid);
                }
            });
            alertDialog.show();
        }

    }

    public void feedBackDialog() {

        View view = getLayoutInflater().inflate(R.layout.custom_layout_feedback_dialog, null);
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCancelable(false);

        SmileRating smileRating = (SmileRating) view.findViewById(R.id.smile_rating);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                ActivityCompat.finishAffinity((Activity) context);
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
        });
        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                switch (smiley) {
                    case SmileRating.BAD:
                        Log.i("smaile", "Bad");
                        break;
                    case SmileRating.GOOD:
                        Log.i("smaile", "Good");
                        break;
                    case SmileRating.GREAT:
                        Log.i("smaile", "Great");
                        break;
                    case SmileRating.OKAY:
                        Log.i("smaile", "Okay");
                        break;
                    case SmileRating.TERRIBLE:
                        Log.i("smaile", "Terrible");
                        break;
                }
            }
        });
        smileRating.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            @Override
            public void onRatingSelected(int level, boolean reselected) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
                ActivityCompat.finishAffinity((Activity) context);
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);

                Toast.makeText(getContext(), "Thank You for this support", Toast.LENGTH_SHORT).show();
             /*          TODO: add rating code              */
            }
        });

        alertDialog.setView(view);
        alertDialog.show();
    }

    public void selectSimSlot() {

        View view = getLayoutInflater().inflate(R.layout.sim_selection_ui, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCancelable(false);

        ImageView sim0 = (ImageView) view.findViewById(R.id.sim0);
        ImageView sim1 = (ImageView) view.findViewById(R.id.sim1);
        TextView confirmationText = view.findViewById(R.id.details);

        double fee = 5.00;
        double total = Double.parseDouble(amount) + fee;
        confirmationText.setText("Confirm Please \n\nAC no: " + acNumber + "\nAmount: " + amount + " tk\nbKash Fee: " + fee + " tk\n\n Total Balance Need: " + total + " tk");

        sim0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendMoney(0);
            }
        });
        sim1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendMoney(1);
            }
        });

        ImageView close = (ImageView) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    private void closeKeyBoard() {
        FragmentShowActivity fragmentShowActivity = (FragmentShowActivity) getActivity();
        fragmentShowActivity.closeKeyboard();
    }

    public void selectSimSlotOne() {
        SendMoney(0);
    }
}

