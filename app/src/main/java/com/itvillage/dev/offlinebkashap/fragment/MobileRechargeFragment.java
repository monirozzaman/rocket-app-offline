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


@SuppressLint("ValidFragment")
public class MobileRechargeFragment extends Fragment {

    private EditText getAmounts, getPin;
    private ImageButton send;
    private TextView acNo;
    private String acNumber, amount, pin, simType;

    private HashMap<String, HashSet<String>> map;
    private USSDApi ussdApi;

    private ArrayList<String> inputValue;
    private Context context;
    private int count = 0;
    private ImageView simIcon;
    private PermissionService.Callback callback = new PermissionService.Callback() {
        @Override
        public void onRefuse(ArrayList<String> RefusePermissions) {
            Toast.makeText(getContext(), "dinay", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        @Override
        public void onFinally() {

        }
    };

    @SuppressLint("ValidFragment")
    public MobileRechargeFragment(String acNumber, String simName, FragmentShowActivity fragmentShowActivity) {
        this.acNumber = acNumber;
        this.simType = simName;
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
        View view = inflater.inflate(R.layout.sim_recharge_freagment, container, false);

        simIcon = view.findViewById(R.id.simIconSimRecharge);

        getAmounts = view.findViewById(R.id.amountSimRecharge);
        getPin = view.findViewById(R.id.passwordSimRecharge);

        //Button
        send = view.findViewById(R.id.sandSimRecharge);

        //Text View
        acNo = view.findViewById(R.id.numberSimRecharge);
        acNo.setText(acNumber);
        setHasOptionsMenu(false);

        if (simType.equals("1")) {
            simIcon.setBackgroundResource(R.drawable.robi);
        } else if (simType.equals("2")) {
            simIcon.setBackgroundResource(R.drawable.airtel);
        } else if (simType.equals("3")) {
            simIcon.setBackgroundResource(R.drawable.banglalink);
        } else if (simType.equals("4")) {
            simIcon.setBackgroundResource(R.drawable.gp);
        } else if (simType.equals("5")) {
            simIcon.setBackgroundResource(R.drawable.teletalk);
        }


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getAmounts.getText().toString().equals("")  && getPin.getText().toString().equals("")) {
                    getAmounts.setError("Amount is Required");
                    getPin.setError("PIN is Required");
                }
                else {
                    if (getAmounts.getText().toString().matches("\\d+") && getPin.getText().toString().matches("\\d+")) {

                        inputValue = new ArrayList<>();
                        amount = getAmounts.getText().toString();

                        pin = getPin.getText().toString();
                        inputValue.add("3");
                        inputValue.add("1");
                        inputValue.add("2");
                        inputValue.add(acNumber);
                        inputValue.add(simType);
                        inputValue.add(amount);
                        inputValue.add(pin);

                        closeKeyBoard();
                        MobileRecharge(0);

                    } else {
                        getAmounts.setError("Invalid Amount");
                        getPin.setError("Invalid PIN");
                    }
                }


            }
        });
        return view;
    }

    private void MobileRecharge(int simslot) {
        ArrayList<String> phone = new ArrayList<>();
        phone.add(acNumber);
        SQLiteDB.insert("cashout", phone);
        ussdApi = USSDController.getInstance(getActivity(), simslot);

        //  if (USSDController.verifyOverLay(getActivity())) {
        //final Intent[] svc = {new Intent(getActivity(), SplashLoadingService.class)};
        //getActivity().startService(svc[0]);
            Log.d("APP", "START SPLASH DIALOG");
        String phoneNumber = "*322#";

            ussdApi.callUSSDOverlayInvoke(phoneNumber, map, new USSDController.CallbackInvoke() {
                @Override
                public void responseInvoke(String message) {
                              /*  Feed Back Mag*/
                    if (count <= 3) {
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
                        if (count == 2) {
                            ussdApi.send(inputValue.get(4), new USSDController.CallbackMessage() {
                                @Override
                                public void responseMessage(String message) {
                                    ++count;
                                    ussdApi.send(inputValue.get(5), new USSDController.CallbackMessage() {
                                        @Override
                                        public void responseMessage(String message) {
                                        }
                                    });
                                }
                            });
                        }
                        if (count == 3) {
                            ussdApi.send(inputValue.get(6), new USSDController.CallbackMessage() {
                                @Override
                                public void responseMessage(String message) {
                                    ++count;

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
                        // getActivity().stopService(svc[0]);
                    } else {
                        //  getActivity().stopService(svc[0]);
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
            alertDialog.setTitle("Dear Customer,");
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
            alertDialog.setTitle("Dear Sir,");
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
          /*
           TODO: add rating code
           */
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
        confirmationText.setVisibility(View.INVISIBLE);

        sim0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobileRecharge(0);
            }
        });
        sim1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobileRecharge(1);
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
        MobileRecharge(0);
    }
}

