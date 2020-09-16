package com.example.assign;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "profile";
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageRef;

    ImageView profileImage;
    Button photoBtn, saveBtn, cancelBtn;
    TextView nameTxt, dobTxt, contactTxt, addressTxt;
    EditText nameEditTxt, dobEditTxt, numEditTxt, addressEditTxt;

    private DatePickerDialog.OnDateSetListener myDateSetListener;
    public Uri photoUri;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null){
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();

        profileImage = findViewById(R.id.profileImage);
        photoBtn = findViewById(R.id.photoBtn);
        saveBtn = findViewById(R.id.saveBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        nameTxt = findViewById(R.id.nameTxt);
        nameEditTxt = findViewById(R.id.nameEditTxt);
        dobTxt = findViewById(R.id.dobTxt);
        dobEditTxt = findViewById(R.id.dobEditTxt);
        contactTxt = findViewById(R.id.contactTxt);
        numEditTxt = findViewById(R.id.numEditTxt);
        addressTxt = findViewById(R.id.addressTxt);
        addressEditTxt = findViewById(R.id.addressEditTxt);

        loadUserInfo();

        dobEditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ProfileActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
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
                dobEditTxt.setText(date);

            }
        };

        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancelIntent = new Intent(ProfileActivity.this, StartActivity.class);
                startActivity(cancelIntent);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
            }
        });
    }



    private void loadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();

        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo userInfo = snapshot.getValue(UserInfo.class);
                if (userInfo != null) {
                    nameEditTxt.setText(userInfo.getName());
                    dobEditTxt.setText(userInfo.getDob());
                    numEditTxt.setText(userInfo.getNumber());
                    addressEditTxt.setText(userInfo.getAddress());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveUserInfo(){
        String name = nameEditTxt.getText().toString().trim();
        String birth = dobEditTxt.getText().toString().trim();
        String number = numEditTxt.getText().toString().trim();
        String address = addressEditTxt.getText().toString().trim();

        UserInfo userInfo = new UserInfo(name,birth,number,address);

        FirebaseUser user = mAuth.getCurrentUser();

        databaseReference.child(Objects.requireNonNull(user).getUid()).setValue(userInfo);

        Toast.makeText(ProfileActivity.this, "Info saved", Toast.LENGTH_LONG).show();

    }
}