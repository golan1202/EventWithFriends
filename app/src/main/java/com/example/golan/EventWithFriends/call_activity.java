package com.example.golan.EventWithFriends;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.golan.EventsWithFriends.R;
import com.shashank.sony.fancytoastlib.FancyToast;

public class call_activity extends Activity implements View.OnClickListener {

    SharedPreferences sp;
    TextView numberTv;
    final int CALL_PERMISSION_REQUEST  = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_activity);

        sp =getSharedPreferences("details",MODE_PRIVATE);
        numberTv = findViewById(R.id.phone_et);
        numberTv.setText(sp.getString("phone",""));
        Button btn1 = findViewById(R.id.btn_1);
        Button btn2 = findViewById(R.id.btn_2);
        Button btn3 = findViewById(R.id.btn_3);
        Button btn4 = findViewById(R.id.btn_4);
        Button btn5 = findViewById(R.id.btn_5);
        Button btn6 = findViewById(R.id.btn_6);
        Button btn7 = findViewById(R.id.btn_7);
        Button btn8 = findViewById(R.id.btn_8);
        Button btn9 = findViewById(R.id.btn_9);
        Button btn0 = findViewById(R.id.btn_0);

        Button star = findViewById(R.id.btn_star);
        Button sulamit = findViewById(R.id.btn_sulamit);

        ImageButton deleteBtn = findViewById(R.id.btn_delete);
        ImageButton callBtn  = findViewById(R.id.call_btn);

        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        star.setOnClickListener(this);
        sulamit.setOnClickListener(this);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String current  = numberTv.getText().toString();
                if(current.length()>0) {
                    current = current.substring(0,current.length()-1);
                    numberTv.setText(current);
                }
            }
        });

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT < 23) {
                    makeCall();
                }
                else {
                    int hasCallPermission = checkSelfPermission(Manifest.permission.CALL_PHONE);
                    if(hasCallPermission== PackageManager.PERMISSION_GRANTED) {
                        makeCall();
                    }
                    else {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE},CALL_PERMISSION_REQUEST);
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == CALL_PERMISSION_REQUEST) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall();
            }
            else {
                FancyToast.makeText(this,"Must give permission to call, please go to settings",FancyToast.LENGTH_LONG,FancyToast.WARNING,false).show();
            }
        }
    }

    private void makeCall() {
        String number  = numberTv.getText().toString();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number));
        startActivity(intent);

    }

    @Override
    public void onClick(View view) {
        numberTv.setText(numberTv.getText().toString() + ((Button)view).getText().toString());
    }



}
