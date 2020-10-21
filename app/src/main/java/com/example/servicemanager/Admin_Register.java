package com.example.servicemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class Admin_Register extends AppCompatActivity {

    EditText Admin_num;
    Button send_otp;
    private FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__register);
        mAuth = FirebaseAuth.getInstance();

        Admin_num = findViewById(R.id.Admin_num);
        send_otp = findViewById(R.id.send_otp);

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Admin_Register.this, Admin_Otp_verification.class);
                i.putExtra("mobile", "+91"+Admin_num.getText().toString().trim());
                i.putExtra("name", "Admin");
                startActivity(i);
            }
        });
    }

    void initFireBaseCallbacks() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Toast.makeText(Admin_Register.this, "Verification Complete", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(Admin_Register.this, "Verification Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Toast.makeText(Admin_Register.this, "Code Sent", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Admin_Register.this, Admin_Otp_verification.class));
            }
        };
    }
}