package com.example.assign;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;
import java.util.Objects;

public class mAppointmentActivity extends AppCompatActivity {
    private static final String TAG = "make";
    Toolbar toolbar;
    TextView therapistTxt, dateTxt, timeTxt;
    EditText dateEditTxt, timeEditTxt;
    Spinner spinner;
    Button confirmBtn;

    private DatePickerDialog.OnDateSetListener myDateSetListener;
    private TimePickerDialog.OnTimeSetListener myTimeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_appointment);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Make Appointment");

        therapistTxt = findViewById(R.id.therapistTxt);
        dateTxt = findViewById(R.id.dateTxt);
        timeTxt = findViewById(R.id.timeTxt);
        dateEditTxt = findViewById(R.id.dateEditTxt);
        timeEditTxt = findViewById(R.id.timeEditTxt);
        spinner = findViewById(R.id.spinner);
        confirmBtn = findViewById(R.id.confirmBtn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.therapist_name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cIntent = new Intent(mAppointmentActivity.this,StartActivity.class);
                startActivity(cIntent);
            }
        });

        timeEditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(mAppointmentActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,myTimeSetListener,hour,minute, true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        myTimeSetListener = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker timePicker, int hr, int min) {
                Log.d(TAG, "TimeSet: hr/min" + hr + ":"+ min);
                String time =  hr + ":"+ min;
                timeEditTxt.setText(time);
            }
        };

        dateEditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        mAppointmentActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        myDateSetListener, year,month,day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        myDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "DateSet: mm/dd/yy" + month + "/" + day + "/"+ year);
                String date = month + "/" + day + "/" + year;
                dateEditTxt.setText(date);

            }
        };
    }

}