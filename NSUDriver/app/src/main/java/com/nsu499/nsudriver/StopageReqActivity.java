package com.nsu499.nsudriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StopageReqActivity extends AppCompatActivity{
    private String BusId;
    private String toNsuHome;
    private String mOnBus;
    private TextView mSeatNo;
    private Button mStopRide;
    DatabaseReference mDatabaseReference;
    DatabaseReference mUserReference;
    DatabaseReference mRfidReference;
    RecyclerView mRecyclerView;
    StopageListAdapter mStopageListAdapter;

    ArrayList<StopageList> list;

    LocationListener locationListener;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopage_req);


        Intent busIdIntent = getIntent();
        BusId = busIdIntent.getStringExtra("busIdPass");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("busId").child(BusId);
        mUserReference = FirebaseDatabase.getInstance().getReference().child("userId");
        mRfidReference = FirebaseDatabase.getInstance().getReference().child("rfid");

        //----Location---------------
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                String Lat = String.valueOf(location.getLatitude());
                String Lon = String.valueOf(location.getLongitude());
                if (Lat!=null && Lon!=null){
                    mDatabaseReference.child("location").child("latitude").setValue(Lat);
                    mDatabaseReference.child("location").child("longitude").setValue(Lon);
                }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 15, locationListener);
        }
        catch (SecurityException e){
            e.printStackTrace();
        }


        //----Location---------------

        toNsuHome = busIdIntent.getStringExtra("toNsuHome");

        mStopRide = findViewById(R.id.StopRide);
        mStopRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReference.child(toNsuHome).setValue(false);
                StopageReqDoFalse();
                UserBookingDoFalse(mUserReference);
                Intent intent=new Intent(StopageReqActivity.this, MainActivity.class);
                intent.putExtra("busIdPass", BusId);
                finish();
                startActivity(intent);
            }
        });

        mSeatNo = findViewById(R.id.seatNo);
        mDatabaseReference.child("onBus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    mOnBus = dataSnapshot.getValue(String.class);
                    mSeatNo.setText(mOnBus);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRfidReference.orderByChild("busRfid").equalTo(BusId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot result : dataSnapshot.getChildren()){
                        String key = result.getKey();
                        rfidProcessing(mRfidReference,key);
                        String uid = result.child("uid").getValue(String.class);
                        UserBalanceReduced(mUserReference,uid);
                        mRfidReference.child(key).child("busRfid").setValue("");
                        Toast.makeText(StopageReqActivity.this,key, Toast.LENGTH_LONG).show();
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        mRecyclerView =  findViewById(R.id.recyclerForStopageList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //RecyclerView Start---------------------------------------------------------

        mRecyclerView =  findViewById(R.id.recyclerForStopageList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mDatabaseReference.child("stopage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list= new ArrayList<>();
                if(dataSnapshot.exists()) {
                    int i=1;
                    for (DataSnapshot resultSnapshot : dataSnapshot.getChildren()) {
                        String stopage = resultSnapshot.getKey();
                        String waitingNo = resultSnapshot.getValue(String.class);
                        StopageList p = new StopageList(stopage, waitingNo);
                        //Toast.makeText(StopageReqActivity.this,"Opps "+i+" ",Toast.LENGTH_SHORT).show();
                        list.add(p);
                        i++;

                    }
                    mStopageListAdapter = new StopageListAdapter(StopageReqActivity.this, list);
                    mRecyclerView.setAdapter(mStopageListAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StopageReqActivity.this,"Opps....Something Wrong",Toast.LENGTH_SHORT).show();
            }
        });

        //RecyclerView End---------------------------------------------------------

    }

    private void rfidProcessing(final DatabaseReference mRfidReference, final String key) {
        mRfidReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int passengerNo = Integer.valueOf(mOnBus);
                passengerNo = passengerNo + 1;
                String onBus = String.valueOf(passengerNo);
                mDatabaseReference.child("onBus").setValue(onBus);
//                mRfidReference.child(key).child("busRfid").setValue("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void UserBalanceReduced(final DatabaseReference mUserReference, final String uid) {
        mUserReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int balance = Integer.valueOf(dataSnapshot.child("balance").getValue(String.class));
                int due = Integer.valueOf( dataSnapshot.child("due").getValue(String.class));
                balance = balance - due;
                String reducedBalance = String.valueOf(balance);
                mUserReference.child(uid).child("balance").setValue(reducedBalance);
                mUserReference.child(uid).child("due").setValue("00");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void StopageReqDoFalse(){

        mDatabaseReference.child("stopage").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot out: dataSnapshot.getChildren()){
                    mDatabaseReference.child("stopage").child(out.getKey()).setValue("00");
                    mDatabaseReference.child("AvailableSeat").setValue("30");


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void UserBookingDoFalse(final DatabaseReference mUserReference) {
        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot out : dataSnapshot.getChildren()) {
                    String key = out.getKey();
                    if ((out.child("booked").getValue(String.class)).equals(BusId)) {
                        mUserReference.child(key).child("booking").setValue(false);
                        mUserReference.child(key).child("booked").setValue("NotSelected");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
