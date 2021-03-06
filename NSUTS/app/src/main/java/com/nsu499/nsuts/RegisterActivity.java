package com.nsu499.nsuts;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNsuID;
    private EditText mRegisterEmail;
    private EditText mContact;
    private EditText mRegisterPassword;
    private EditText mConfirmPasswordView;
    private Button mRegisterButton;
    private Button mRegisterToLigin;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    RegisterUserClass mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("userId");
        mNsuID = findViewById(R.id.nsuID) ;
        mRegisterEmail=findViewById(R.id.RegisterEmail);
        mContact = findViewById(R.id.contact);
        mRegisterPassword= findViewById(R.id.RegisterPassword);
        mConfirmPasswordView = findViewById(R.id.RegisterConfirmPassword);
        mRegisterButton =  findViewById(R.id.register_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsValid()){
                    userRegister();
                }
                else {

                }
            }
        });

        mRegisterToLigin =  findViewById(R.id.RegisterToLogin);
        mRegisterToLigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }

    private void userRegister() {
        final String email = mRegisterEmail.getText().toString();
        final String password = mRegisterPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    showErrorDialog("Registration attempt failed");
                } else {

                    mAuth.getCurrentUser().sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,
                                                "register is successful please verify ur email",Toast.LENGTH_LONG).show();
                                        InsertUserData();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        finish();
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(RegisterActivity.this,
                                                task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                     }
            }
        });
    }

    private boolean IsValid(){
        String email = mRegisterEmail.getText().toString();
        String pass = mRegisterPassword.getText().toString();
        String Cpass = mConfirmPasswordView.getText().toString();

        if (!email.contains("@northsouth.edu")){
            Toast.makeText(RegisterActivity.this,
                    "NSU Email Needed",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (pass.length() < 4){
            Toast.makeText(RegisterActivity.this,
                    "At least 4 character",Toast.LENGTH_LONG).show();
            return false;
        }
        else
           return true;
    }

    private void getValues(){
        mUser.setNsuId(mNsuID.getText().toString());
        mUser.setEmail(mRegisterEmail.getText().toString());
        mUser.setContact(mContact.getText().toString());
        mUser.setBalance("00");
        mUser.setDue("00");
        mUser.setBooking(false);
        mUser.setBooked("NotSelected");
    }

    private void InsertUserData() {

        mUser = new RegisterUserClass();
        getValues();
        mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(mUser);
        FirebaseAuth.getInstance().signOut();

    }

    private void showErrorDialog(String regfailed) {
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(regfailed)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}