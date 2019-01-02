package com.example.golan.EventWithFriends;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.golan.EventsWithFriends.R;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SMSActivity  extends Activity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Button done;
    SharedPreferences sp;
    private  final int  PICK_CONTACT = 0;
    private static final String DEBUG_TAG ="TAG" ;
    Button phone_et;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_activity);

        sp = getSharedPreferences("details", MODE_PRIVATE);
        final EditText body_et = findViewById(R.id.body_et);
        done =findViewById(R.id.done_btn);
        phone_et = findViewById(R.id.phone_et);

        body_et.setText(sp.getString("event_name","")+"\n\n"+getString(R.string.from_date)+" "+sp.getString("from_date","")+ " " +getString(R.string.from_time)+ sp.getString("from_time","")
                +" "+ getString(R.string.till_date)+" "+ sp.getString("till_date","")+" "+ getString(R.string.till_time)+ " " + sp.getString("till_time","")+ "\n\n" + getString(R.string.location)+ ": "+sp.getString("location","")
                + "\n\n"+ getString(R.string.details) + " :" + sp.getString("body","")+"\n\n"+getString(R.string.event_link)+ " :"+ sp.getString("link","")+"\n\n"+ getString(R.string.phone_for_info) + " :"+sp.getString("phone",""));

        if(sp.getString("event_name","").matches("")||sp.getString("from_date","").matches("")||sp.getString("till_date","").matches("")
                ||(sp.getString("from_time","").matches(""))||sp.getString("till_time","").matches("")||sp.getString("link","").matches("")||sp.getString("body","").matches("")
                ||sp.getString("phone","").matches("")||sp.getString("location","").matches("")){
            FancyToast.makeText(SMSActivity.this,"You didn't fill all the details. Go to event screen",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();
        }


        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                Log.e("permission", "Permission already granted.");
            } else {
                requestPermission();
            }
        }

        phone_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT < 23) {
                    pickContact();
                }
                else {
                    int hasCallPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
                    if(hasCallPermission== PackageManager.PERMISSION_GRANTED) {
                        pickContact();
                    }
                    else {
                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},PICK_CONTACT);
                    }
                }
            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sms = body_et.getText().toString();
                String phoneNum = phone_et.getText().toString();
                if(phoneNum.matches("")){
                    FancyToast.makeText(SMSActivity.this,"Please pick phone number",FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                    return;
                }
                if(sms.matches("")){
                    FancyToast.makeText(SMSActivity.this,"Please add text to send",FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                    return;
                }

                if (!TextUtils.isEmpty(sms) && !TextUtils.isEmpty(phoneNum)) {
                    if (checkPermission()) {
                        //Get the default SmsManager//
                        SmsManager smsManager = SmsManager.getDefault();
                        //Send the SMS//
                        smsManager.sendTextMessage(phoneNum, null, sms, null, null);
                        FancyToast.makeText(SMSActivity.this,"SMS sent",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                    } else {
                        FancyToast.makeText(SMSActivity.this,"Permission denied",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                    }
                }
            }
        });
    }

    private boolean checkPermission() {
        int result = checkSelfPermission(Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){

            case  PICK_CONTACT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickContact();
                } else {
                    FancyToast.makeText(this,"Must give permission to READ CONTACTS",FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
                }

            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    FancyToast.makeText(SMSActivity.this,"Permission accepted",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                }
                else{
                    FancyToast.makeText(SMSActivity.this,"Permission denied",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();

                }
                break;
        }
    }
    public void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_CONTACT:
                    Cursor cursor = null;
                    String phone = "";
                    try {
                        Uri result = data.getData();

                        // get the contact id from the Uri
                        String id = result.getLastPathSegment();

                        // query for everything email
                        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{id},
                                null);

                        int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

                        // let's just get the first phone
                        if (cursor.moveToFirst()) {
                            phone = cursor.getString(emailIdx);
                            Log.v(DEBUG_TAG, "Got phone: " + phone);
                        } else {
                            Log.w(DEBUG_TAG, "No results");
                        }
                    } catch (Exception e) {
                        Log.e(DEBUG_TAG, "Failed to get phone data", e);
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                        phone_et.setText(phone);
                        if (phone.length() == 0) {
                            FancyToast.makeText(this,"No phone found for contact",FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
                        }

                    }

                    break;
            }

        } else {
            Log.w(DEBUG_TAG, "Warning: activity result not ok");
        }

    }
}
