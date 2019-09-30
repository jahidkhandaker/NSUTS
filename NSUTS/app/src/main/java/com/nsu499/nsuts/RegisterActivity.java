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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNsuID;
    private EditText mRegisterEmail;
    private EditText mContact;
    private EditText mRegisterPassword;
    private EditText mConfirmPasswordView;
    private Button mRegisterButton;
    private Button mRegisterToLigin;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
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
        final String email = mRegisterEmail.getText().toString();
        final String password = mRegisterPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Log.d("FlashChat", "createUser onComplete: " + task.isSuccessful());

                if(!task.isSuccessful()){
                    // Log.d("FlashChat", "user creation failed");
                    showErrorDialog("Registration attempt failed");
                } else {
                    mAuth.getCurrentUser().sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,
                                                "register is successful please verify ur email",Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        finish();
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(RegisterActivity.this,
                                                task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
//                    saveDisplayName();
                }
            }
        });
    }

    private void getValues(){
        mUser.setNsuId(mNsuID.getText().toString());
        mUser.setEmail(mRegisterEmail.getText().toString());
        mUser.setContact(mContact.getText().toString());
        mUser.setBalance("00");
        mUser.setBooking(false);
    }

    private void InsertUserData() {

        mUser = new RegisterUserClass();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getValues();
                mDatabase.child(mNsuID.getText().toString()).setValue(mUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
