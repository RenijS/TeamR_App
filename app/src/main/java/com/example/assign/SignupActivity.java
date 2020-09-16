package com.example.assign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "signIn" ;
    private EditText emailEditTxt, passwordEditTxt, repassEditTxt;
    private Button signupBtn;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        emailEditTxt = (EditText) findViewById(R.id.emailEditTxt);
        passwordEditTxt = (EditText) findViewById(R.id.passwordEditTxt);
        repassEditTxt = (EditText) findViewById(R.id.repassEditTxt);
        signupBtn = (Button) findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = emailEditTxt.getText().toString().trim();
        String password = passwordEditTxt.getText().toString().trim();
        String rePassword = repassEditTxt.getText().toString().trim();

        if (email.isEmpty()){
            Toast.makeText(getApplicationContext(),"Email is required, please try again", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.isEmpty()){
            Toast.makeText(getApplicationContext(),"Password is required, please try again", Toast.LENGTH_LONG).show();
            return;
        }

        if (!rePassword.equals(password)){
            Toast.makeText(getApplicationContext(),"Password doesn't match, please try again", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                    Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_LONG).show();
                    Intent loginIntent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                } else {
                    Log.w(TAG, "signInWithEmail:failure");
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}