package com.nsu499.nsuts.ui.support;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
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
import com.nsu499.nsuts.R;

public class SupportFragment extends Fragment {

    private SupportViewModel toolsViewModel;
    private AutoCompleteTextView mComment;
    private Button CommentSubmit;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mUserReference;
    private String uEmail;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        toolsViewModel =
//                ViewModelProviders.of(this).get(SupportViewModel.class);
        View root = inflater.inflate(R.layout.fragment_support, container, false);
//        final TextView textView = root.findViewById(R.id.text_tools);
//        toolsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Complain");
        mUserReference = FirebaseDatabase.getInstance().getReference().child("userId").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uEmail = dataSnapshot.child("email").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mComment = root.findViewById(R.id.support_comment);

        CommentSubmit = root.findViewById(R.id.comment_submit);
        CommentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((mComment.getText().toString()).length()) > 0) {
                    String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    mDatabaseReference.child(uId).child("email").setValue(uEmail);
                    mDatabaseReference.child(uId).child("text").setValue(mComment.getText().toString());
                }
            }
        });



        return root;
    }
}