package com.nsu499.nsuts;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNsuID;
    private EditText mRegisterEmail;
    private EditText mContact;
    private EditText mRegisterPassword;
    private Button mRegisterButton;
    private Button mRegisterToLigin;
    private EditText mConfirmPasswordView;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegister();
            }
        });

        mRegisterToLigin = (Button) findViewById(R.id.RegisterToLogin);
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
        mDatabase = FirebaseDatabase.getInstance().getReference().child("message");

        mDatabase.setValue("Hello, World!");
    }

    private void showErrorDialog(String registration_attempt_failed) {
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage("message")
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
