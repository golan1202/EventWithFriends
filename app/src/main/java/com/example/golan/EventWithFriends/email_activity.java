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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.golan.EventsWithFriends.R;
import com.shashank.sony.fancytoastlib.FancyToast;


public class email_activity extends Activity {

    private static final String DEBUG_TAG ="TAG" ;
    private  final int  PICK_CONTACT = 0;
    SharedPreferences sp;
    Button address_et;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_activity);

        sp = getSharedPreferences("details", MODE_PRIVATE);
        address_et = findViewById(R.id.address_et);
        final EditText subjectEt = findViewById(R.id.subject_et);
        final EditText bodyEt = findViewById(R.id.body_et);
        Button done = findViewById(R.id.done_btn);
        subjectEt.setText(sp.getString("event_name", ""));
        bodyEt.setText(sp.getString("event_name","")+"\n\n"+getString(R.string.from_date)+" "+sp.getString("from_date","")+ " " +getString(R.string.from_time)+ sp.getString("from_time","")
                +" "+ getString(R.string.till_date)+" "+ sp.getString("till_date","")+" "+ getString(R.string.till_time)+ " " + sp.getString("till_time","")+ "\n\n" + getString(R.string.location)+ ": "+sp.getString("location","")
                + "\n\n"+ getString(R.string.details) + " :" + sp.getString("body","")+"\n\n"+getString(R.string.event_link)+ " :"+ sp.getString("link","")+"\n\n"+ getString(R.string.phone_for_info) + " :"+sp.getString("phone",""));

        if(sp.getString("event_name","").matches("")||sp.getString("from_date","").matches("")||sp.getString("till_date","").matches("")
                ||(sp.getString("from_time","").matches(""))||sp.getString("till_time","").matches("")||sp.getString("link","").matches("")||sp.getString("body","").matches("")
                ||sp.getString("phone","").matches("")||sp.getString("location","").matches("")){
            FancyToast.makeText(email_activity.this,"You didn't fill all the details. Go to event screen",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();
        }


        address_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Build.VERSION.SDK_INT < 23) {
                    pickEmail();
                }
                else {
                    int hasCallPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
                    if(hasCallPermission== PackageManager.PERMISSION_GRANTED) {
                        pickEmail();
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
                String address = address_et.getText().toString();
                String body = bodyEt.getText().toString();
                String subject = subjectEt.getText().toString();

                if(address.matches("")){
                    FancyToast.makeText(email_activity.this,"Please pick email address",FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                    return;
                }
                if(subject.matches("")){
                    FancyToast.makeText(email_activity.this,"Please add subject",FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                    return;
                }
                if(body.matches("")){
                    FancyToast.makeText(email_activity.this,"Please add text to send",FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, body);
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});

                intent.setType("text/html");

                startActivity(Intent.createChooser(intent, "Send email with..."));


            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_CONTACT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickEmail();
            } else {
                FancyToast.makeText(this,"Must give permission to READ CONTACTS",FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_CONTACT:
                    Cursor cursor = null;
                    String email = "";
                    try {
                        Uri result = data.getData();

                        // get the contact id from the Uri
                        String id = result.getLastPathSegment();

                        // query for everything email
                        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?", new String[]{id},
                                null);

                        int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

                        // let's just get the first email
                        if (cursor.moveToFirst()) {
                            email = cursor.getString(emailIdx);
                            Log.v(DEBUG_TAG, "Got email: " + email);
                        } else {
                            Log.w(DEBUG_TAG, "No results");
                        }
                    } catch (Exception e) {
                        Log.e(DEBUG_TAG, "Failed to get email data", e);
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                         address_et.setText(email);
                        if (email.length() == 0) {
                            FancyToast.makeText(this,"No email found for contact",FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
                        }

                    }

                    break;
            }

        } else {
            Log.w(DEBUG_TAG, "Warning: activity result not ok");
        }
    }

    public void pickEmail() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);

    }
}