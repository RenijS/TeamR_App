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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "login";
    boolean firstTime = true;
    private EditText emailEditTxt, passwordEditTxt;
    private Button loginBtn;
    private TextView noAccount;

    private ProgressDialog progressDialog;
    //firebase auth object
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        emailEditTxt = (EditText) findViewById(R.id.emailEditTxt);
        passwordEditTxt = (EditText) findViewById(R.id.passwordEditTxt);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        noAccount = (TextView) findViewById(R.id.noAccount);

        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signIntent =new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signIntent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

    }

    private void loginUser() {
        String email = emailEditTxt.getText().toString();
        String password = passwordEditTxt.getText().toString();

        if (email.isEmpty()){
            Toast.makeText(getApplicationContext(),"Email is required, please try again", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.isEmpty()){
            Toast.makeText(getApplicationContext(),"Password is required, please try again", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("logging in...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    Log.d(TAG, "loginWithEmail:success");
                    Intent startIntent = new Intent(LoginActivity.this, StartActivity.class);
                    startActivity(startIntent);
                } else {
                    Log.w(TAG, "loginWithEmail:failure", task.getException());
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}