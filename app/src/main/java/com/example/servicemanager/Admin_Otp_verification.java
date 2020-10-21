package com.example.servicemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class Admin_Otp_verification extends AppCompatActivity {

    EditText edtVerifyOtp;
    Button btnVerify;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    String mobile, name;
    String mVerificationId;
    String resendToken;
    FirebaseFirestore db;
    private FirebaseUser mUser;
    private DocumentReference mDataRef;
    private FirebaseAuth mAuth;
    private static final String TAG = "Admin_Otp_verification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__otp_verification);
        mobile = getIntent().getStringExtra("mobile");
        name=getIntent().getStringExtra("name");
        edtVerifyOtp = findViewById(R.id.verify_otp);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        btnVerify = findViewById(R.id.verify_otp_btn);
        initFireBaseCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                callbacks);
    }

    void initFireBaseCallbacks() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                String code = credential.getSmsCode();
                if (code != null) {
                    edtVerifyOtp.setText(code);
                    verifyPhoneNumberWithCode(mVerificationId, code);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getApplicationContext(), "Verification Failed\n"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Toast.makeText(Admin_Otp_verification.this, "Code Sent", Toast.LENGTH_SHORT).show();
                mVerificationId = verificationId;

            }
        };
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
        } catch (Exception e) {


        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = task.getResult().getUser();
                            String UID = task.getResult().getUser().getUid();
                            Log.d(TAG, "uid-" + UID);
                            db.collection("Users").document(UID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            User user=document.toObject(User.class);
                                            if(user.name.equals("Admin"))
                                                Toast.makeText(Admin_Otp_verification.this, "You are already registered as admin", Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(Admin_Otp_verification.this, "You are already registered as customer\nPlease contact our techanical team", Toast.LENGTH_SHORT).show();
                                        } else {
                                            User user = new User(name, mobile);
                                            mDataRef = db.collection("Users").document(mUser.getUid());
                                            mDataRef.set(user)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(Admin_Otp_verification.this, "Success", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }

                                }
                            });


                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                edtVerifyOtp.setError("Invalid code.");
                                edtVerifyOtp.requestFocus();
                            }
                            Toast.makeText(getApplicationContext(), "Check Phone Number", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}