package com.trasimus.tictactoe.online.Other;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameInvitationListener extends Service {

    private DatabaseReference mDatabaseReference;
    private DatabaseReference ref;
    private String userID;
    private String lobbyID;

    @Override
    public void onCreate() {
        super.onCreate();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userID = intent.getExtras().getString("userID");

        ref = mDatabaseReference.child("Users").child(userID).child("lobbyID");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lobbyID = dataSnapshot.getValue().toString();

                if (lobbyID != null && lobbyID !=""){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return super.onStartCommand(intent, flags, startId);
    }
}
