package com.example.servicemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button Admin_btn,Submit;
    TextInputEditText u_name,u_pasword,u_email;
    private FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Admin_btn = findViewById(R.id.Admin_Button);
        Submit = findViewById(R.id.submit);
        u_name = findViewById(R.id.uname);
        u_email = findViewById(R.id.uemail);

        Admin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Admin_Register.class);
                startActivity(i);
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Admin_Otp_verification.class);
                i.putExtra("mobile","+91"+u_email.getText().toString().trim());
                i.putExtra("name",u_name.getText().toString().trim());
                startActivity(i);
            }
        });


    }

}