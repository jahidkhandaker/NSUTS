package com.nsu499.nsudriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText mNsuid;
    private EditText mPass;
    private Button mLoginButton;

    private String BusId="";
    private Spinner mSpinner;
    private DatabaseReference mBusDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mNsuid = findViewById(R.id.nsuid);
        mPass = findViewById(R.id.password);
        mLoginButton = findViewById(R.id.login_button);



        mBusDatabase = FirebaseDatabase.getInstance().getReference().child("busId");
        //--------Spinner------------------
        mSpinner = findViewById(R.id.spinnerbus);
        mBusDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> busList = new ArrayList<>();
                busList.add("Select Bus");
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                        String BusName = dataSnapshot1.getKey();
                        busList.add(BusName);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_item, busList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpinner.setAdapter(arrayAdapter);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Bus")){
                    //Do Nothing
                }else {
                    String bus = parent.getItemAtPosition(position).toString();
                    BusId = bus;
                    mLoginButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                            mBusDatabase.child(BusId).child("running").setValue(true);
                            intent.putExtra("busIdPass", BusId);
                            finish();
                            startActivity(intent);
                        }
                    });

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        //--------End Spinner--------------


    }


}
