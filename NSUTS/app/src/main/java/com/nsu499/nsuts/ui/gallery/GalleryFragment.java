package com.nsu499.nsuts.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nsu499.nsuts.BookingActivity;
import com.nsu499.nsuts.LoginActivity;
import com.nsu499.nsuts.MainActivity;
import com.nsu499.nsuts.R;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {


    private Spinner spinner;
    private Spinner spinnerToHome;
    private Spinner mPickUp;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mPickupReference;
    private DatabaseReference mConfirmReference;
    private DatabaseReference mConfirmReferenceReq;

    private TextView mAseat;
    private Button mConfirmPickup;

    private int data;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        final View root = inflater.inflate(R.layout.fragment_gallery, container, false);



        final TabHost tabhost = root.findViewById(R.id.tabHost);
            tabhost.setup();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("busId");

// -------------Start TO NSU Tab-----------------------------------------------
        spinner =  root.findViewById(R.id.spinner);
            TabHost.TabSpec spec = tabhost.newTabSpec("To NSU");
            spec.setContent(R.id.ToNSU);
            spec.setIndicator("To NSU");
            tabhost.addTab(spec);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> busList = new ArrayList<String>();
                busList.add("Select Bus");
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        String titlename = dataSnapshot1.getKey();
                       //if (dataSnapshot1.child("tonsu").getValue(boolean.class) == true) {
                           busList.add(titlename);
                      // }

                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, busList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
            }


        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               if (parent.getItemAtPosition(position).equals("Select Bus")){
                   //Do Nothing
               }
               else{
                   final String bus = parent.getItemAtPosition(position).toString();

                   //-------------------------------

                   mDatabaseReference.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {

                           if(dataSnapshot.exists()) {
                               mAseat = (TextView) root.findViewById(R.id.Aseat);

                               String availableSeat = String.valueOf(dataSnapshot.child(bus).child("Available Seat").getValue());
                               int check = Integer.valueOf(availableSeat) ;
                               if (check > 0) {

                                   mAseat.setText(availableSeat);
                                   pickup(bus,root,check);

                               }
                               else {
                                   mAseat.setText("FULL");
                               }
                           }

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });

               }

           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });



// -------------End TO NSU Tab-----------------------------------------------

        mPickUp = root.findViewById(R.id.pickUp);
        mPickUp.setVisibility(View.INVISIBLE);


        //--------- Start Confirm PickUp--------------------
       mConfirmPickup = root.findViewById(R.id.ConfirmPickUpButton);
       mConfirmPickup.setVisibility(View.INVISIBLE);

        //--------- End Confirm PickUp--------------------


// -------------Start TO Home Tab-----------------------------------------------
        spinnerToHome =  root.findViewById(R.id.spinnerToHome);
            TabHost.TabSpec spec2 = tabhost.newTabSpec("To Home");
            spec2.setContent(R.id.ToHome);
            spec2.setIndicator("To Home");
            tabhost.addTab(spec2);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> busList = new ArrayList<String>();
                final List<String> busavailableList = new ArrayList<String>();
                busList.add("Select Bus");
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        String titlename = dataSnapshot1.getKey();
                        if (dataSnapshot1.child("tohome").getValue(boolean.class) == true) {
                            busList.add(titlename);
                        }

//                        String availableSeat = String.valueOf(dataSnapshot.child(titlename).child("Available Seat").getValue());
//                        busavailableList.add(availableseat);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, busList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerToHome.setAdapter(arrayAdapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinnerToHome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("Select Bus")){
                    //Do Nothing
                }
                else{
                    String bus = parent.getItemAtPosition(position).toString();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




// -------------End TO Home Tab-----------------------------------------------



        return root;
    }

    private void pickup(final String Pbus, final View root, final int check) {


        mPickUp.setVisibility(View.VISIBLE);
        mPickupReference = FirebaseDatabase.getInstance().getReference().child("busId").child(Pbus).child("stopage");
        mPickupReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> stopageList = new ArrayList<String>();
                stopageList.add("Select PickUp Point");
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshotPick : dataSnapshot.getChildren()) {

                        String stopagename =  dataSnapshotPick.getKey();

                        stopageList.add(stopagename);


                    }
                    ArrayAdapter<String> arrayAdapterr = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, stopageList);
                    arrayAdapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mPickUp.setAdapter(arrayAdapterr);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mPickUp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("Select PickUp Point")){
                    //Do Nothing
                }
                else{
                    final String pickpoint = parent.getItemAtPosition(position).toString();

                    confirmPickUp(Pbus, pickpoint, root, check);



                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void confirmPickUp(String pbus, String pickpoint, View root, final int check) {
        mConfirmPickup.setVisibility(View.VISIBLE);
        mConfirmReference = FirebaseDatabase.getInstance().getReference().child("busId").child(pbus).child("Available Seat");
        mConfirmReferenceReq = FirebaseDatabase.getInstance().getReference().child("busId").child(pbus).child("stopage").child(pickpoint);

      // final String reqNo = String.valueOf(SendReq());

       mConfirmPickup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int dec = check -1 ;
               String decSeat = String.valueOf(dec) ;
               mConfirmReference.setValue(decSeat);
               mConfirmReferenceReq.setValue(+1);
               Intent intent = new Intent(getActivity(), MainActivity.class);
               startActivity(intent);
           }
       });
    }

//    private int SendReq() {
//        final int[] dd = new int[1];
//        mConfirmReferenceReq.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                if(dataSnapshot.exists()) {
//
//                    String Req = (String) dataSnapshot.getValue();
//                    int increm = 0;
//                    increm = Integer.valueOf(Req) ;
//                    increm = increm + 1;
//
//                   dd[0] =  increm;
//
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        return dd[0];
//    }


}