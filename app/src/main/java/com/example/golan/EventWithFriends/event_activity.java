package com.example.golan.EventWithFriends;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.example.golan.EventsWithFriends.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class event_activity extends Activity  {

    EditText event_name_et;
    Button from_date_et;
    Button from_time_et;
    Button till_date_et;
    Button till_time_et;
    EditText location_et;
    EditText details_et;
    EditText link_et;
    EditText phone_et;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);


        event_name_et = findViewById(R.id.event_name_et);
        from_date_et = findViewById(R.id.from_date_et);
         from_time_et= findViewById(R.id.from_time_et);
         till_date_et= findViewById(R.id.till_date_et);
         till_time_et= findViewById(R.id.till_time_et);
        location_et = findViewById(R.id.location_et);
        details_et = findViewById(R.id.details_et);
        link_et = findViewById(R.id.link_et);
        phone_et = findViewById(R.id.phone_et);
        Button back_btn = findViewById(R.id.back_btn);
        Button clean_btn = findViewById(R.id.clean_btn);
         sp = getSharedPreferences("details",MODE_PRIVATE);
        Button link_btn = findViewById(R.id.link_btn);





         link_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String site = link_et.getText().toString();
                 if (site.matches("")){
                         FancyToast.makeText(event_activity.this,"Please add link",FancyToast.LENGTH_SHORT,FancyToast.WARNING,false).show();
                         return;
                 }
                 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+site));
                 startActivity(intent);
             }
         });

       from_date_et.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(event_activity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;
                        from_date_et.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("from_year",selectedyear);
                        editor.putInt("from_month",selectedmonth);
                        editor.putInt("from_day",selectedday);
                        editor.commit();
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });

        till_date_et.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(event_activity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;
                        till_date_et.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("till_year",selectedyear);
                        editor.putInt("till_month",selectedmonth);
                        editor.putInt("till_day",selectedday);
                        editor.commit();
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });

        from_time_et.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(event_activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        from_time_et.setText( selectedHour + ":" + selectedMinute);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("from_hour",selectedHour);
                        editor.putInt("from_minute",selectedMinute);
                        editor.commit();
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        till_time_et.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(event_activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        till_time_et.setText( selectedHour + ":" + selectedMinute);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("till_hour",selectedHour);
                        editor.putInt("till_minute",selectedMinute);
                        editor.commit();
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        if(sp.getBoolean("not_first_run",false)) {
            event_name_et.setText(sp.getString("event_name", ""));
            from_date_et.setText(sp.getString("from_date", ""));
            from_time_et.setText(sp.getString("from_time", ""));
            till_date_et.setText(sp.getString("till_date", ""));
            till_time_et.setText(sp.getString("till_time", ""));
            location_et.setText(sp.getString("location", ""));
            details_et.setText(sp.getString("body", ""));
            link_et.setText(sp.getString("link", ""));
            phone_et.setText(sp.getString("phone", ""));
        }
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(event_activity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        clean_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event_name_et.setText("");
                from_date_et.setText("");
                from_time_et.setText("");
                till_date_et.setText("");
                till_time_et.setText("");
                location_et.setText("");
                details_et.setText("");
                link_et.setText("");
                phone_et.setText("");
                FancyToast.makeText(event_activity.this,"Event initialized",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("event_name",event_name_et.getText().toString());
        editor.putString("from_date",from_date_et.getText().toString());
        editor.putString("till_date",till_date_et.getText().toString());
        editor.putString("from_time",from_time_et.getText().toString());
        editor.putString("till_time",till_time_et.getText().toString());
        editor.putString("location",location_et.getText().toString());
        editor.putString("body",details_et.getText().toString());
        editor.putString("link",link_et.getText().toString());
        editor.putString("phone",phone_et.getText().toString());
        editor.putBoolean("not_first_run",true);
        editor.commit();
        FancyToast.makeText(this,"The event is saved",FancyToast.LENGTH_LONG,FancyToast.INFO,false).show();


    }
}