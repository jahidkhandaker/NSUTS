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
import com.nsu499.nsuts.LoginActivity;
import com.nsu499.nsuts.R;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    private Spinner spinner;
    private DatabaseReference mDatabaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
//        final TextView textView = root.findViewById(R.id.text_gallery);
//        galleryViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        final TabHost tabhost = root.findViewById(R.id.tabHost);
            tabhost.setup();
// -------------Start TO NSU Tab-----------------------------------------------
            TabHost.TabSpec spec = tabhost.newTabSpec("To NSU");
            spec.setContent(R.id.ToNSU);
            spec.setIndicator("To NSU");
            tabhost.addTab(spec);


        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("busId");
        spinner =  root.findViewById(R.id.spinner);


        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> busList = new ArrayList<String>();
                final List<String> busavailableList = new ArrayList<String>();
                busList.add("Select Bus");
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        String titlename = dataSnapshot1.getKey();
                        busList.add(titlename);

                        String availableseat = dataSnapshot1.child(titlename).getValue(String.class);
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
                   String bus = parent.getItemAtPosition(position).toString();


               }


           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });



// -------------End TO NSU Tab-----------------------------------------------

// -------------Start TO Home Tab-----------------------------------------------

            TabHost.TabSpec spec2 = tabhost.newTabSpec("To Home");
            spec2.setContent(R.id.ToHome);
            spec2.setIndicator("To Home");
            tabhost.addTab(spec2);


// -------------End TO Home Tab-----------------------------------------------


        return root;
    }

}