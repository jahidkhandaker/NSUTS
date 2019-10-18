package com.nsu499.nsuts.ui.home;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nsu499.nsuts.R;

import java.util.concurrent.Executor;

public class HomeFragment extends Fragment implements OnMapReadyCallback{

    private HomeViewModel homeViewModel;
    private GoogleMap mMap;
    private View root;

    private Marker busMarker;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    DatabaseReference mLocationDatabase;
    private final long MIN_TIME=1000;
    private final long MIN_DIST=5;

    private Location currentLocation;
    private LatLng latLng;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
         root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        return root;
    }


    @Override
    public void onMapReady( final GoogleMap googleMap) {

        final Double la = 23.815121;
        final Double lo = 90.425460;
        ll(mMap, googleMap, la, lo,"NSU");
        mLocationDatabase = FirebaseDatabase.getInstance().getReference().child("busId");
        mLocationDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                busMarker.remove();
                ll(mMap, googleMap, la, lo,"NSU");
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String title = dataSnapshot1.getKey() ;
                        Double lat  = Double.valueOf(dataSnapshot1.child("location").child("latitude").getValue(String.class));
                        Double lon  = Double.valueOf(dataSnapshot1.child("location").child("longitude").getValue(String.class));


                        ll(mMap, googleMap, lat, lon, title);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




//        mFusedLocationProviderClient.getLastLocation()
//                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            // Logic to handle location object
//                            currentLocation = location;
//                            ll(mMap, googleMap, currentLocation.getLatitude(), currentLocation.getLongitude(),"df");
//
//                        }
//                    }
//                });


    }

    private void ll(GoogleMap map, GoogleMap googleMap, Double la, Double lo, String title) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng nsu = new LatLng(la, lo);
        //LatLng sydney = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        busMarker =  mMap.addMarker(new MarkerOptions().position(nsu).title(title));
        if (title == "NSU")
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nsu,12.2f));
    }


}