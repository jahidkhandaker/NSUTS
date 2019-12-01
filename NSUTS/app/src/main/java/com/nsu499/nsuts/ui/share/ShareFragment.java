package com.nsu499.nsuts.ui.share;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nsu499.nsuts.MainActivity;
import com.nsu499.nsuts.R;

public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;
    private TextView nsu_edit;
    private TextView name_edit;
    private TextView contact_edit;
    private Button info_update;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        shareViewModel =
//                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);
//        final TextView textView = root.findViewById(R.id.text_share);
//        shareViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText("Lorem Ipsum");
//            }
//        });


        nsu_edit = root.findViewById(R.id.edit_nsuid);
        name_edit = root.findViewById(R.id.edit_name);
        contact_edit = root.findViewById(R.id.edit_contact);
        info_update = root.findViewById(R.id.info_update);

        String FuId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference().child("userId").child(FuId);

        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               try{
                   String name = dataSnapshot.child("name").getValue(String.class);
                   name_edit.setText(name);
               } catch (Exception e) {
                   e.printStackTrace();
               }
                String nsuid = dataSnapshot.child("nsuId").getValue(String.class);
                nsu_edit.setText(nsuid);
                String contact = dataSnapshot.child("contact").getValue(String.class);
                contact_edit.setText(contact);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        info_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserRef.child("name").setValue(name_edit.getText().toString());
                mUserRef.child("contact").setValue(contact_edit.getText().toString());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });


        return root;
    }
}