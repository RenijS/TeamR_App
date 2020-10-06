package com.example.assign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    Button profileBtn, consultBtn,chatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        profileBtn = findViewById(R.id.profileBtn);
        consultBtn = findViewById(R.id.consultBtn);
        chatBtn = findViewById(R.id.chatBtn);

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(StartActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });
        consultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent consultIntent = new Intent(StartActivity.this, ConsultActivity.class);
                startActivity(consultIntent);
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cIntent = new Intent(StartActivity.this, ChatActivity.class);
                startActivity(cIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(StartActivity.this, "Cant Go Back",Toast.LENGTH_SHORT).show();
    }
}