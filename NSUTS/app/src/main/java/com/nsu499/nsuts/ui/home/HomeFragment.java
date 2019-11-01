package com.nsu499.nsuts.ui.home;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nsu499.nsuts.R;

import java.util.Objects;

public class HomeFragment extends Fragment implements OnMapReadyCallback{

    private HomeViewModel homeViewModel;
    private GoogleMap mMap;
    private View root;

    private Marker MeMarker;
    private Marker busMarker;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private Location location;

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


        final String[] FixedBus = new String[1];
        String FuId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference().child("userId").child(FuId);
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FixedBus[0] = dataSnapshot.child("booked").getValue(String.class);
                //Toast.makeText(getActivity(),FixedBus[0], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mLocationDatabase = FirebaseDatabase.getInstance().getReference().child("busId");
        mLocationDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                //busMarker.remove();
                mMap.clear();
                double [] myLoc;
                myLoc =  myLocation();
                if(dataSnapshot.exists()) {
                    float bLat, bLon;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String title = dataSnapshot1.getKey() ;
                        Double lat  = Double.valueOf(Objects.requireNonNull(dataSnapshot1.child("location").child("latitude").getValue(String.class)));
                        Double lon  = Double.valueOf(Objects.requireNonNull(dataSnapshot1.child("location").child("longitude").getValue(String.class)));
                        latLng = new LatLng(lat, lon);

                        bLat = convertToFloat(lat);
                        bLon = convertToFloat(lon);

                        busMarker =  mMap.addMarker(new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_bus_foreground)));
                        if (title.equals(FixedBus[0])){
                            float[] dist = new float[1];
                            //location.distanceBetween(23.815455, 90.425264, 23.892020, 90.401956, dist);
                            location.distanceBetween(myLoc[0], myLoc[1], bLat, bLon, dist);
                            String dur = String.valueOf(dist[0] / 1000.00);
                            Toast.makeText(getActivity(), dur, Toast.LENGTH_SHORT).show();
//                            if ((dist[0])/1000 < 3){
//                                Toast.makeText(getActivity(), "comming", Toast.LENGTH_LONG).show();
//                            }

                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private double[] myLocation() {
        final double [] myL = new double[2];
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    myL[0] = location.getLatitude();
                    myL[1]= location.getLongitude();
                    nsu = new LatLng(myL[0], myL[1]);
                    MeMarker =  mMap.addMarker(new MarkerOptions().position(nsu).title("Me"));
                }
            }
        });
        return myL;
    }

    public static Float convertToFloat(double doubleValue) {
        return (float) doubleValue;
    }

}