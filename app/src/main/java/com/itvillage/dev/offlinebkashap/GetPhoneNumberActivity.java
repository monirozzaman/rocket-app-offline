package com.itvillage.dev.offlinebkashap;

import android.Manifest;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.itvillage.dev.offlinebkashap.adapter.Contact;
import com.itvillage.dev.offlinebkashap.adapter.ContactFetcher;
import com.itvillage.dev.offlinebkashap.adapter.ContactsAdapter;
import com.itvillage.dev.sqlite.SQLiteDB;

import java.util.ArrayList;

public class GetPhoneNumberActivity extends AppCompatActivity {
    private EditText numberOrName;
    // private Button  scaner;
    private ImageButton next;
    private ListView rvContacts;
    private ProgressDialog progressDialog;
    private ContactsAdapter adapterContacts;
    private String getOptionName, acNumber, headingOfNumberString;
    private int READ_CONTACTS_CODE = 1;
    private TextView headingOfNumber;
    private ArrayList<Contact> listContacts;
    private Button sugg1But, sugg2But, sugg3But;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_number);

        numberOrName = findViewById(R.id.numberOrName);
        next = findViewById(R.id.nextAction);
        // scaner = findViewById(R.id.scaner);
        rvContacts = findViewById(R.id.rvContacts);
        headingOfNumber = findViewById(R.id.headingOfNumber);
        sugg1But = findViewById(R.id.sugg1);
        sugg2But = findViewById(R.id.sugg2);
        sugg3But = findViewById(R.id.sugg3);


        getOptionName = getIntent().getExtras().getString("getOptionName");
        headingOfNumberString = getIntent().getExtras().getString("headingOfNumberString");
        headingOfNumber.setText(headingOfNumberString);


        // Set Toolbar level name
        View view = getLayoutInflater().inflate(R.layout.action_bar_costom, null);
        TextView textView = view.findViewById(R.id.action_bar_title);
        textView.setText(getOptionName);
        textView.setGravity(Gravity.CENTER);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(view);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(GetPhoneNumberActivity.this);
        progressDialog.setMessage("Contact List getting.."); // Setting Message
        progressDialog.setTitle("Please Wait.."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (permissionAlreadyGranted()) {
                    getAllContacts();
                    progressDialog.dismiss();
                    return;
                }


            }
        }, 100);
/* For Scenner  */

   /*     if (getOptionName.equals("Cash Out")) {
            scaner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new IntentIntegrator(GetPhoneNumberActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
                    Toast.makeText(getApplicationContext(), "Scanner Opening..", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            scaner.setEnabled(false);
            scaner.setTextColor(Color.parseColor("#FF545354"));
        }*/
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                acNumber = numberOrName.getText().toString();
                if (acNumber.equals("")) {
                    numberOrName.setError("Phone Number is Required");
                } else {
                    if (acNumber.length() == 15) {

                        acNumber = acNumber.substring(3, 15).replaceAll("[-.^:,]", "");
                    } else if (acNumber.length() == 11) {

                        acNumber = acNumber.replaceAll("[-.^:,]", "");
                    }
                    if (acNumber.length() == 12 || acNumber.length() == 11) {
/* -------------------------------------- Option Selection----------------------------------------*/

                        if (getOptionName.equals("Send Money")) {

                            Intent intent = new Intent(GetPhoneNumberActivity.this,
                                    FragmentShowActivity.class);
                            intent.putExtra("acNo", acNumber);
                            intent.putExtra("getOptionName", getOptionName);
                            startActivity(intent);
                        } else if (getOptionName.equals("Mobile Recharge")) {

                            Intent intent = new Intent(GetPhoneNumberActivity.this,
                                    SimOparetorSelectActivity.class);
                            intent.putExtra("acNo", acNumber);
                            intent.putExtra("getOptionName", getOptionName);
                            startActivity(intent);
                        } else if (getOptionName.equals("Cash Out")) {

                            onClickSuggetionButton("cashout");
                            Intent intent = new Intent(GetPhoneNumberActivity.this,
                                    FragmentShowActivity.class);
                            intent.putExtra("acNo", acNumber);
                            intent.putExtra("getOptionName", getOptionName);
                            startActivity(intent);
                        } else if (getOptionName.equals("Payment")) {

                            Intent intent = new Intent(GetPhoneNumberActivity.this,
                                    FragmentShowActivity.class);
                            intent.putExtra("acNo", acNumber);
                            intent.putExtra("getOptionName", getOptionName);
                            startActivity(intent);
                        }

                    } else {
                        numberOrName.setError("Phone Number is Invalid");

                    }
                }
            }
        });

        numberOrName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!numberOrName.getText().equals("")) {
                    SearchInPhonebook(s.toString());
                } else {
                    listContacts = new ContactFetcher(GetPhoneNumberActivity.this).fetchAll();
                    adapterContacts = new ContactsAdapter(GetPhoneNumberActivity.this, listContacts, numberOrName);
                    rvContacts.setAdapter(adapterContacts);
                }
            }
        });
        if (!permissionAlreadyGranted()) {
            requestPermission();
            return;
        }
        onClickSuggetionButton("cashout");
    }

    private void onClickSuggetionButton(final String tableName) {
        setRecentCashOut(getCashOutRecentNumber(tableName));
        sugg1But.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOrName.setText("");
                numberOrName.setText(getCashOutRecentNumber(tableName)
                        .get(getCashOutRecentNumber(tableName).size() - 1));
            }
        });
        sugg2But.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOrName.setText("");
                numberOrName.setText(getCashOutRecentNumber(tableName)
                        .get(getCashOutRecentNumber(tableName).size() - 2));
            }
        });
        sugg3But.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOrName.setText("");
                numberOrName.setText(getCashOutRecentNumber(tableName)
                        .get(getCashOutRecentNumber(tableName).size() - 3));
            }

        });
    }

    private void setRecentCashOut(ArrayList<String> cashOutRecentNumber) {
        if (cashOutRecentNumber.size() >= 3) {
            sugg1But.setText(cashOutRecentNumber.get(cashOutRecentNumber.size() - 1));
            sugg2But.setText(cashOutRecentNumber.get(cashOutRecentNumber.size() - 2));
            sugg3But.setText(cashOutRecentNumber.get(cashOutRecentNumber.size() - 3));
        } else if (cashOutRecentNumber.size() >= 2) {
            sugg1But.setText(cashOutRecentNumber.get(cashOutRecentNumber.size() - 1));
            sugg2But.setText(cashOutRecentNumber.get(cashOutRecentNumber.size() - 2));
            sugg3But.setVisibility(View.INVISIBLE);
        } else if (cashOutRecentNumber.size() >= 1) {
            sugg1But.setText(cashOutRecentNumber.get(cashOutRecentNumber.size() - 1));
            sugg2But.setVisibility(View.INVISIBLE);
            sugg3But.setVisibility(View.INVISIBLE);
        } else if (cashOutRecentNumber.size() >= 0) {
            sugg1But.setVisibility(View.INVISIBLE);
            sugg2But.setVisibility(View.INVISIBLE);
            sugg3But.setVisibility(View.INVISIBLE);
        } else {

        }
    }

    private ArrayList<String> getCashOutRecentNumber(String tableName) {
        ArrayList<String> cashOutNumberList = new ArrayList<>();
        Cursor cursor = SQLiteDB.findAll(tableName);
        if (cursor.getCount() == 0) {

            Toast.makeText(this, "No history found", Toast.LENGTH_SHORT).show();
            return cashOutNumberList;
        } else {
            while (cursor.moveToNext()) {

                Log.i("message", "Data got");
                cashOutNumberList.add(cursor.getString(1)); // Adding data received to a Listview
            }

            return cashOutNumberList;

        }
    }

    private void SearchInPhonebook(String inputTaxt) {
        if (listContacts.size() > 0) {
            ArrayList<Contact> serchableList = new ArrayList();
            for (Contact contactVOItem : listContacts) {
                if (contactVOItem.getName().toLowerCase().contains(inputTaxt.toLowerCase())) {
                    serchableList.add(contactVOItem);
                }
            }
            adapterContacts = new ContactsAdapter(this, serchableList, numberOrName);
            rvContacts.setAdapter(adapterContacts);
        } else {
            listContacts = new ContactFetcher(this).fetchAll();
            adapterContacts = new ContactsAdapter(this, listContacts, numberOrName);
            rvContacts.setAdapter(adapterContacts);
        }
    }

    private void getAllContacts() {

        listContacts = new ContactFetcher(this).fetchAll();
        adapterContacts = new ContactsAdapter(this, listContacts, numberOrName);
        rvContacts.setAdapter(adapterContacts);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            } else {
                showResultDialogue(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void showResultDialogue(final String result) {
        numberOrName.setText("");
        numberOrName.setText(result);
        Toast.makeText(getApplicationContext(), "Scan Successfully completed" + result, Toast.LENGTH_SHORT).show();
    }

    private boolean permissionAlreadyGranted() {

        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_CONTACTS_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAllContacts();
                progressDialog.dismiss();
                Toast.makeText(this, "Permission granted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission is denied!", Toast.LENGTH_SHORT).show();
                boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS);
                if (!showRationale) {
                    Toast.makeText(GetPhoneNumberActivity.this, "Setting", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}