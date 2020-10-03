package com.example.assign;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    Toolbar toolbar;

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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");

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
                choosePhoto();
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

    private void choosePhoto() {
        Intent photoIntent = new Intent();
        photoIntent.setType("image/*");
        photoIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(photoIntent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            photoUri = data.getData();
            profileImage.setImageURI(photoUri);
            uploadPhoto();
        }
    }

    private void uploadPhoto() {
        final String randomKey = UUID.randomUUID().toString();
        StorageReference riverRef = storageRef.child("images/" + randomKey);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();


        riverRef.putFile(photoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
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
                Log.w(TAG, "Failed to read value.", error.toException());
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
    private void deleteUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        databaseReference.child(user.getUid()).removeValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.one_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                logout();
                Toast.makeText(ProfileActivity.this,"logout selected",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete:
                deleteAcc();
                Toast.makeText(ProfileActivity.this,"delete account selected",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAcc() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileActivity.this);
        dialog.setTitle("Are you sure?");
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteUserInfo();
                FirebaseUser user = mAuth.getCurrentUser();
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent dIntent = new Intent(ProfileActivity.this, SignupActivity.class);
                            startActivity(dIntent);
                            Toast.makeText(getApplicationContext(), "Account Deleted",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }


    private void logout() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent outIntent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(outIntent);
    }
}