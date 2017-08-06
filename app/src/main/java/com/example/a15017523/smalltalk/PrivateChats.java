package com.example.a15017523.smalltalk;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrivateChats extends Fragment {

    ListView listView;
    DatabaseReference mdatabaseReference, databaseReference;
    FirebaseListAdapter<Users> firebaseListAdapter;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    String uid;
    String chatUser;

    public PrivateChats() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_private_chats, container, false);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        uid = mFirebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mdatabaseReference = databaseReference.child("Users");
        firebaseListAdapter = new FirebaseListAdapter<Users>(getActivity(),
                Users.class,
                R.layout.row,
                mdatabaseReference) {
            @Override
            protected void populateView(View view, Users u, final int position) {
                TextView textViewUser = (TextView)view.findViewById(R.id.tvUser);
                ImageView imageViewUser = (ImageView)view.findViewById(R.id.iVUser);
                String username = u.getName().toString().trim();
                String photoUrl = u.getPhotoUrl().toString().trim();
                textViewUser.setText(username);
                Picasso.with(getActivity().getBaseContext()).load(photoUrl).into(imageViewUser);

                chatUser = firebaseListAdapter.getRef(position).getKey();

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mdatabaseReference.child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.hasChild(chatUser)) {
                                    Map map = new HashMap();
                                    map.put(uid + "/Chat/" + uid + "/" + chatUser, "created");
                                    map.put(chatUser + "/Chat/" + chatUser + "/" + uid, "created");

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        Intent intent = new Intent(getActivity(), Chat.class);
                        String itemKey = String.valueOf(firebaseListAdapter.getRef(position).getKey());
                        intent.putExtra("key", itemKey);
                        startActivity(intent);
                    }
                });
            }
        };
        listView = (ListView) rootView.findViewById(R.id.idLVPrivateChats);
        listView.setAdapter(firebaseListAdapter);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return rootView;
    }

}
