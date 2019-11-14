package com.nsu499.nsuts.ui.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nsu499.nsuts.R;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment {

    private ScheduleViewModel slideshowViewModel;
    DatabaseReference mDatabaseReference;
    RecyclerView mScheduleRecyclerView;
    ScheduleAdapter mScheduleListAdapter;

    ArrayList<ScheduleList> list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);

        View root = inflater.inflate(R.layout.fragment_schedule, container, false);

        final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //---Code Starts Here------------
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("schedule");

        //RecyclerView Start---------------------------------------------------------

        mScheduleRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerForSchedule);
        mScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list= new ArrayList<ScheduleList>();
                if(dataSnapshot.exists()) {
                    int i=1;
                    for (DataSnapshot resultSnapshot : dataSnapshot.getChildren()) {
                        String busTitle = resultSnapshot.getKey();
                        String departTime = resultSnapshot.child("time").getValue(String.class);
                        String dFrom = resultSnapshot.child("from").getValue(String.class);
                        String aTo = resultSnapshot.child("to").getValue(String.class);
                        ScheduleList p = new ScheduleList(departTime, dFrom, aTo, busTitle);
                        list.add(p);
                        i++;

                    }
                    Toast.makeText(getActivity(),"Schedule No "+i+" ",Toast.LENGTH_SHORT).show();
                    mScheduleListAdapter = new ScheduleAdapter(getActivity(), list);
                    mScheduleRecyclerView.setAdapter(mScheduleListAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Opps....Something Wrong",Toast.LENGTH_SHORT).show();
            }
        });

        //RecyclerView End-------------------------------------------------





        return root;
    }
}