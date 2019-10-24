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
import android.widget.Toast;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

import java.util.Objects;
import java.util.concurrent.Executor;

public class HomeFragment extends Fragment implements OnMapReadyCallback{

    private HomeViewModel homeViewModel;
    private GoogleMap mMap;
    private View root;

    private Marker MeMarker;
    private Marker busMarker;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    DatabaseReference mLocationDatabase;
    private final long MIN_TIME=1000;
    private final long MIN_DIST=5;


    private LatLng nsu;
    private LatLng latLng;

    private Double la;
    private Double lo;

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






        return root;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon= location.getLongitude();
                    nsu = new LatLng(lat, lon);
                    MeMarker =  mMap.addMarker(new MarkerOptions().position(nsu).title("Me"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nsu,16.2f));

                }
            }
        });
        mLocationDatabase = FirebaseDatabase.getInstance().getReference().child("busId");
        mLocationDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //busMarker.remove();
                mMap.clear();
                myLocation();
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String title = dataSnapshot1.getKey() ;
                        Double lat  = Double.valueOf(Objects.requireNonNull(dataSnapshot1.child("location").child("latitude").getValue(String.class)));
                        Double lon  = Double.valueOf(Objects.requireNonNull(dataSnapshot1.child("location").child("longitude").getValue(String.class)));
                        latLng = new LatLng(lat, lon);
                        busMarker =  mMap.addMarker(new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_bus_foreground)));

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void myLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon= location.getLongitude();
                    nsu = new LatLng(lat, lon);
                    MeMarker =  mMap.addMarker(new MarkerOptions().position(nsu).title("Me"));
                }
            }
        });
    }


}