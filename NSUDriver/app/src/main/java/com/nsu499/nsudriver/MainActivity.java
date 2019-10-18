package com.nsu499.nsudriver;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String BusId;
    private Spinner mSpinner;
    private Button mtoNsu;
    private Button mtoHome;
    private DatabaseReference mBusDatabase;
    private DatabaseReference mRfidReference;
    private DatabaseReference mUserReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent busIdIntent = getIntent();
        BusId = busIdIntent.getStringExtra("busIdPass");

        mBusDatabase = FirebaseDatabase.getInstance().getReference().child("busId");
        mBusDatabase.child(BusId).child("onBus").setValue("00");
        mUserReference = FirebaseDatabase.getInstance().getReference().child("userId");
        mRfidReference = FirebaseDatabase.getInstance().getReference().child("rfid");

        mtoHome = findViewById(R.id.toHome);
        mtoNsu = findViewById(R.id.toNsu);

//        mBusDatabase.child(BusId).child("location").child("latitude").setValue("23.815121");
//        mBusDatabase.child(BusId).child("location").child("longitude").setValue("90.425460");

        mtoNsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBusDatabase.child(BusId).child("tonsu").setValue(true);
                Intent intent=new Intent(MainActivity.this, StopageReqActivity.class);
                intent.putExtra("busIdPass", BusId);
                intent.putExtra("toNsuHome", "tonsu");
                finish();
                startActivity(intent);

            }
        });

        mtoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBusDatabase.child(BusId).child("tohome").setValue(true);

                Intent intent=new Intent(MainActivity.this, StopageReqActivity.class);
                intent.putExtra("busIdPass", BusId);
                intent.putExtra("toNsuHome", "tohome");
                finish();
                startActivity(intent);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mBusDatabase.child(BusId).child("running").setValue(false);
            RfidDoFalse();


            Intent intent=new Intent(MainActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void RfidDoFalse() {
        mRfidReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot out: dataSnapshot.getChildren()){
                    mRfidReference.child(out.getKey()).child("truth").setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void BookingReqDoFalse(){

    }
}

