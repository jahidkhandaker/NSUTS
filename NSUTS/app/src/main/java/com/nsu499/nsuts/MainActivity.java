package com.nsu499.nsuts;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private DatabaseReference mUserReference;
    private String FuId;

    private Button mLogout;
    private TextView mUserEmailView;
    private TextView mUserIdView;
    private View hView;

    private final String Channel_ID = "personal_Notification";
    private final int Notification_Id = 001;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        hView =  navigationView.getHeaderView(0);
        FuId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mUserReference = FirebaseDatabase.getInstance().getReference().child("userId").child(FuId);


        //-------currentuser start---------------
        mUserIdView = hView.findViewById(R.id.userNsuId);

        mUserEmailView = hView.findViewById(R.id.userEmailView);

        mUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue(String.class);
                mUserIdView.setText(email);
                String balance = dataSnapshot.child("balance").getValue(String.class);
                mUserEmailView.setText("Balance: "+ balance);
                int baln = Integer.valueOf(balance);
                if (baln < 30) {
                     balanceNotification(baln);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_booking,
                R.id.nav_schedule,
                R.id.nav_support,
                R.id.nav_info )
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //////---Hard Code Start Here------------------------------------------------------

            mLogout= findViewById(R.id.nav_Logout);
            mLogout.setOnClickListener(new NavigationView.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });


        //////---Hard Code Ends Here------------------------------------------------------
    }
    public void balanceNotification(int baln) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Channel_ID, "ntfy", importance);
            channel.setDescription("ntfy");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,Channel_ID);
        builder.setSmallIcon(R.drawable.ic_menu_feedback);
        builder.setContentTitle("Low Balance");
        builder.setContentText("Current Balance: "+ baln);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(Notification_Id,builder.build());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

//    @Override
//    public void onBackPressed() {
//       finish();
//    }

}
