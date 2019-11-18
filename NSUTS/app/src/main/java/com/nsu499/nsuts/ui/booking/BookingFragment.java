package com.nsu499.nsuts.ui.booking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nsu499.nsuts.MainActivity;
import com.nsu499.nsuts.R;
import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment {


    private Spinner spinner;
    private Spinner spinnerToHome;
    private Spinner mPickUp;
    private Spinner mDestination;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mUserReference;
    private DatabaseReference mFareReference;

    private TextView mSelectBusView;
    private TextView mSelectBusViewToHome;
    private TextView mAseatTextToNsu;
    private TextView mAseatToNsu;
    private TextView mAseatTextToHome;
    private TextView mAseatToHome;
    private Button mConfirmPickup;
    private Button mConfirmDestination;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_booking, container, false);


        final TabHost tabhost = root.findViewById(R.id.tabHost);
            tabhost.setup();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("busId");
        mUserReference = FirebaseDatabase.getInstance().getReference().child("userId").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        mSelectBusView = root.findViewById(R.id.SelectBusView) ;
        mSelectBusViewToHome = root.findViewById(R.id.SelectBusViewToHome) ;

        spinner =  root.findViewById(R.id.spinner);
        spinner.setVisibility(View.INVISIBLE);
        spinnerToHome =  root.findViewById(R.id.spinnerToHome);
        spinnerToHome.setVisibility(View.INVISIBLE);

        mAseatTextToNsu = root.findViewById(R.id.AseatTextToNsu);
        mAseatTextToNsu.setVisibility(View.INVISIBLE);

        mAseatToNsu =  root.findViewById(R.id.AseatToNsu);
        mAseatToNsu.setVisibility(View.INVISIBLE);

        mAseatTextToHome =  root.findViewById(R.id.AseatTextToHome);
        mAseatTextToHome.setVisibility(View.INVISIBLE);

        mAseatToHome =  root.findViewById(R.id.AseatToHome);
        mAseatToHome.setVisibility(View.INVISIBLE);

        mPickUp = root.findViewById(R.id.pickUp);
        mPickUp.setVisibility(View.INVISIBLE);

        mConfirmPickup = root.findViewById(R.id.ConfirmPickUpButton);
        mConfirmPickup.setVisibility(View.INVISIBLE);
// -------------Start TO Home Tab-----------------------------------------------

        mDestination = root.findViewById(R.id.DestinationSpinner);
        mDestination.setVisibility(View.INVISIBLE);

        mConfirmDestination = root.findViewById(R.id.ConfirmDestinationButton);
        mConfirmDestination.setVisibility(View.INVISIBLE);

        //my sec---------------------------


        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if ((Double.valueOf(dataSnapshot.child("balance").getValue(String.class)))< 30){
                    mSelectBusView.setText("Balance Low");
                    mSelectBusViewToHome.setText("Balance Low");
                    Toast.makeText(getActivity(),"Recharge your Balance",Toast.LENGTH_LONG).show();
                }
                else if (dataSnapshot.child("booking").getValue(boolean.class)){ //-----------------change

                    mSelectBusView.setText("Already Booked");
                    mSelectBusViewToHome.setText("Already Booked");
                    Toast.makeText(getActivity(),"Already Booked",Toast.LENGTH_LONG).show();

                } else{
                    TabHost.TabSpec spec = tabhost.newTabSpec("To NSU");
                    spec.setContent(R.id.ToNSU);
                    spec.setIndicator("To NSU");
                    tabhost.addTab(spec);

                    TabHost.TabSpec spec2 = tabhost.newTabSpec("To Home");
                    spec2.setContent(R.id.ToHome);
                    spec2.setIndicator("To Home");
                    tabhost.addTab(spec2);

// -------------Start TO NSU Tab-----------------------------------------------
                    spinner.setVisibility(View.VISIBLE);

                    mDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final List<String> busList = new ArrayList<>();
                            busList.add("Select Bus");
                            if(dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String titlename = dataSnapshot1.getKey();
                                    if (dataSnapshot1.child("tonsu").getValue(boolean.class)) {
                                        busList.add(titlename);
                                    }
                                }
                                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, busList);
                                try{
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, busList);
                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner.setAdapter(arrayAdapter);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

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

                                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()) {
                                            mAseatTextToNsu.setVisibility(View.VISIBLE);
                                            mAseatToNsu.setVisibility(View.VISIBLE);
                                            String availableSeat = String.valueOf(dataSnapshot.child(bus).child("AvailableSeat").getValue());
                                            int check = Integer.valueOf(availableSeat) ;
                                            if (check > 0) {
                                                mAseatToNsu.setText(availableSeat);
                                                pickup(bus,root,check);
                                            }
                                            else {
                                                mAseatToNsu.setText("FULL");
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
                    spinnerToHome.setVisibility(View.VISIBLE);

                    mDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final List<String> busList = new ArrayList<>();
                            busList.add("Select Bus");
                            if(dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String titlename = dataSnapshot1.getKey();
                                    if (dataSnapshot1.child("tohome").getValue(boolean.class) == true) {
                                        busList.add(titlename);
                                    }
                                }
                                try{
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, busList);
                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerToHome.setAdapter(arrayAdapter);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

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
                                final String bus = parent.getItemAtPosition(position).toString();

                                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange( DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()) {
                                            mAseatTextToHome.setVisibility(View.VISIBLE);
                                            mAseatToHome.setVisibility(View.VISIBLE);
                                            String availableSeat = String.valueOf(dataSnapshot.child(bus).child("AvailableSeat").getValue());
                                            int check = Integer.valueOf(availableSeat) ;
                                            if (check > 0) {
                                                mAseatToHome.setText(availableSeat);
                                                destination(bus,root,check);
                                            }
                                            else {
                                                mAseatToHome.setText("FULL");
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


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return root;
    }

    private void pickup(final String Pbus, final View root, final int check) {
        mPickUp.setVisibility(View.VISIBLE);
        DatabaseReference mPickupReference = FirebaseDatabase.getInstance().getReference().child("busId").child(Pbus).child("stopage");
        mPickupReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                final List<String> stopageList = new ArrayList<>();
                stopageList.add("Select PickUp Point");
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshotPick : dataSnapshot.getChildren()) {
                        String stopagename =  dataSnapshotPick.getKey();
                        stopageList.add(stopagename);
                    }
                    try{
                        ArrayAdapter<String> arrayAdapterr = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, stopageList);
                        arrayAdapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mPickUp.setAdapter(arrayAdapterr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                    confirmPickUp(Pbus, pickpoint, check);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void confirmPickUp(final String pbus, final String pickpoint, final int check) {
        mConfirmPickup.setVisibility(View.VISIBLE);
      final DatabaseReference mConfirmReference = FirebaseDatabase.getInstance().getReference().child("busId").child(pbus).child("AvailableSeat");
      final DatabaseReference mConfirmReferenceReq = FirebaseDatabase.getInstance().getReference().child("busId").child(pbus).child("stopage").child(pickpoint);
       mConfirmPickup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int dec = check -1 ;
               String decSeat = String.valueOf(dec) ;
               mConfirmReference.setValue(decSeat);
               SendReq(mConfirmReferenceReq);
               mUserReference.child("booking").setValue(true); //---------------------------------change-------------------
               DueBalance(pickpoint);
               mUserReference.child("booked").setValue(pbus);
               Intent intent = new Intent(getActivity(), MainActivity.class);
               getActivity().finish();
               startActivity(intent);
           }
       });
    }


    private void destination(final String Pbus, final View root, final int check) {
        mDestination.setVisibility(View.VISIBLE);
        final DatabaseReference mPickupReference = FirebaseDatabase.getInstance().getReference().child("busId").child(Pbus).child("stopage");
        mPickupReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> stopageList = new ArrayList<String>();
                stopageList.add("Select Destination");
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshotPick : dataSnapshot.getChildren()) {
                        String stopagename =  dataSnapshotPick.getKey();
                        stopageList.add(stopagename);
                    }
                    try {
                        ArrayAdapter<String> arrayAdapterr = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, stopageList);
                        arrayAdapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mDestination.setAdapter(arrayAdapterr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDestination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("Select Destination")){
                    //Do Nothing
                }
                else{
                    final String pickpoint = parent.getItemAtPosition(position).toString();
                    confirmToHome(Pbus, pickpoint, root, check);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void confirmToHome(final String pbus, final String pickpoint, View root, final int check) {
        mConfirmDestination.setVisibility(View.VISIBLE);
        final DatabaseReference mConfirmReference = FirebaseDatabase.getInstance().getReference().child("busId").child(pbus).child("AvailableSeat");
        final DatabaseReference mConfirmReferenceReq = FirebaseDatabase.getInstance().getReference().child("busId").child(pbus).child("stopage").child(pickpoint);
        mConfirmDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dec = check -1 ;
                String decSeat = String.valueOf(dec) ;
                mConfirmReference.setValue(decSeat);
                SendReq(mConfirmReferenceReq);
                mUserReference.child("booking").setValue(true);
                DueBalance(pickpoint);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });
    }

    private void SendReq(final DatabaseReference mpickRef) {
        mpickRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int increm = 0;
                if(dataSnapshot.exists()) {
                    String Req = (String) dataSnapshot.getValue();
                    increm = Integer.valueOf(Req) ;
                    increm = increm + 1;
                    String reqNo = String.valueOf(increm);
                    mpickRef.setValue(reqNo);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void DueBalance(String pickupPoint) {
        mFareReference = FirebaseDatabase.getInstance().getReference("fare");
        mFareReference.child(pickupPoint).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fare = dataSnapshot.getValue(String.class);
                mUserReference.child("due").setValue(fare);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}