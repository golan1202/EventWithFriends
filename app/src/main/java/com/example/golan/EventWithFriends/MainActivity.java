package com.example.golan.EventWithFriends;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;

import com.example.golan.EventsWithFriends.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends Activity {

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button calling_btn = findViewById(R.id.calling_btn);
        Button email_btn = findViewById(R.id.email_btn);
        Button new_event_btn = findViewById(R.id.new_event_btn);
        Button calender_btn = findViewById(R.id.calender_btn);
        Button alarm_btn = findViewById(R.id.alarm_btn);
        Button sms_btn = findViewById(R.id.SMS_btn);

        sp =getSharedPreferences("details",MODE_PRIVATE);

        sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,SMSActivity.class);
                startActivity(intent);
            }
        });

        calling_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,call_activity.class);
                startActivity(intent);
            }
        });

        email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,email_activity.class);
                startActivity(intent);

            }
        });
        new_event_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,event_activity.class);
                startActivity(intent);
            }
        });

        calender_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar beginTime = Calendar.getInstance();
                beginTime.set(sp.getInt("from_year", 0), sp.getInt("from_month",0), sp.getInt("from_day",0), sp.getInt("from_hour",0), sp.getInt("from_minute",0));
                Calendar endTime = Calendar.getInstance();
                endTime.set(sp.getInt("till_year", 0), sp.getInt("till_month",0), sp.getInt("till_day",0), sp.getInt("till_hour",0), sp.getInt("till_minute",0));
                if((sp.getString("from_date","").matches(""))||(sp.getString("till_date","").matches(""))){
                    FancyToast.makeText(MainActivity.this,"Please pick event date at new event",FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
                    return;
                }
                if((sp.getString("from_time","").matches(""))||(sp.getString("till_time","").matches(""))){
                    FancyToast.makeText(MainActivity.this,"Please pick event time at new event",FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra(CalendarContract.Events.TITLE, sp.getString("event_name",""));
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,beginTime.getTimeInMillis() );
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
                intent.putExtra(CalendarContract.Events.DESCRIPTION, sp.getString("body", ""));
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, sp.getString("location", ""));
                startActivity(intent);
            }
        });


        alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                startActivity(i);
            }
        });

    }
}
