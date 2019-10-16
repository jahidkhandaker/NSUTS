package com.nsu499.nsuts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button mLoginbutton;

    private Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {

            mEmailView = (AutoCompleteTextView) findViewById(R.id.editTextEmail);
            mPasswordView = (EditText) findViewById(R.id.editTextPassword);
            mLoginbutton = (Button) findViewById(R.id.LoginButton);

            mLoginbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attemptLogin();
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    finish();
//                    startActivity(intent);
                }
            });

            mRegister = (Button) findViewById(R.id.LoginToRegister);
            mRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });

        }

        else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        }

    }
        private void attemptLogin(){

            String email = mEmailView.getText().toString();
            String password = mPasswordView.getText().toString();

            if (email.isEmpty())
                if (email.equals("") || password.equals("")) return;
            Toast.makeText(this, "Login in progress...", Toast.LENGTH_SHORT).show();

            // TODO: Use FirebaseAuth to sign in with email & password
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    if (!task.isSuccessful()) {
//                    Log.d("FlashChat", "Problem signing in: " + task.getException());
                        showErrorDialog("There was a problem signing in");
                    } else {
                        if (mAuth.getCurrentUser().isEmailVerified()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            finish();
                            startActivity(intent);
                        }else {
                            Toast.makeText(LoginActivity.this,
                                    "please verify your email",Toast.LENGTH_LONG).show();
                        }

                    }

                }
            });

        }


    private void showErrorDialog(String s) {
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(s)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        }
    }

