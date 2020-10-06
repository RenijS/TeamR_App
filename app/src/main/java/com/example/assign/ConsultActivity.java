package com.example.assign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ConsultActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button makeBtn, viewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Consult");

        makeBtn = findViewById(R.id.makeBtn);
        viewBtn = findViewById(R.id.viewBtn);

        makeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(ConsultActivity.this, mAppointmentActivity.class);
                startActivity(mIntent);
            }
        });
    }
}