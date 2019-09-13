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
import com.nsu499.nsuts.R;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {


    private Spinner spinner;
    private Spinner spinnerToHome;
    private DatabaseReference mDatabaseReference;
    private TextView mAseat;

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

                               String availableSeat = String.valueOf(dataSnapshot.child(bus).child("Available Seat").getValue());
                               mAseat = (TextView) root.findViewById(R.id.Aseat);
                               mAseat.setText(availableSeat);
                           }

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });


                   //-------------------------------



               }


           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });



// -------------End TO NSU Tab-----------------------------------------------

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

}