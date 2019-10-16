package com.nsu499.nsudriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopage_req);


        Intent busIdIntent = getIntent();
        BusId = busIdIntent.getStringExtra("busIdPass");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("busId").child(BusId);
        mUserReference = FirebaseDatabase.getInstance().getReference().child("userId");
        mRfidReference = FirebaseDatabase.getInstance().getReference().child("rfid");

        toNsuHome = busIdIntent.getStringExtra("toNsuHome");

        mStopRide = findViewById(R.id.StopRide);
        mStopRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mDatabaseReference.child("stopage").setValue("0");
                mDatabaseReference.child(toNsuHome).setValue(false);
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

        mRfidReference.orderByChild("truth").equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                        int passengerNo = Integer.valueOf(mOnBus) ;
                        passengerNo = passengerNo + 1;
                        String onBus = String.valueOf(passengerNo);
                        mDatabaseReference.child("onBus").setValue(onBus);


                        String uid= snapshot.child("uid").getValue(String.class);
                        Toast.makeText(StopageReqActivity.this,uid,Toast.LENGTH_SHORT).show();
                        mUserReference.child(uid).child("booking").setValue(false);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerForStopageList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //RecyclerView Start---------------------------------------------------------

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerForStopageList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mDatabaseReference.child("stopage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list= new ArrayList<StopageList>();
                if(dataSnapshot.exists()) {
                    int i=1;
                    for (DataSnapshot resultSnapshot : dataSnapshot.getChildren()) {
                        String stopage = resultSnapshot.getKey();
                        String waitingNo = resultSnapshot.getValue(String.class);
                        StopageList p = new StopageList(stopage, waitingNo);
                        Toast.makeText(StopageReqActivity.this,"Opps "+i+" ",Toast.LENGTH_SHORT).show();
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
}
