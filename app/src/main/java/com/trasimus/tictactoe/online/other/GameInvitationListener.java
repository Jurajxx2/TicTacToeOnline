package com.trasimus.tictactoe.online.other;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trasimus.tictactoe.online.DefaultUser;
import com.trasimus.tictactoe.online.Lobby;
import com.trasimus.tictactoe.online.UserMap;
import com.trasimus.tictactoe.online.R;
import com.trasimus.tictactoe.online.game.GameActivity;

public class GameInvitationListener extends Service {

    private FirebaseUser mUser;
    private DefaultUser defaultUser;
    private DefaultUser otherUser;
    private UserMap userMap;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference ref;
    private DatabaseReference mDatabase;
    private DatabaseReference mReference;
    private String userID;
    private String lobbyID;
    private NotificationManager notificationManager;
    private Lobby mLobby;
    private ValueEventListener mListener;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("test", "SERVICE vnutri sa zacala");

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("my_channel_id", name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager = (NotificationManager) getSystemService(
                    NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        } else {
            notificationManager = (NotificationManager) getSystemService(
                    NOTIFICATION_SERVICE);
        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        if (mUser==null){
            Toast.makeText(GameInvitationListener.this, "Loading notification listener failed: you won't recieve any game invitations.", Toast.LENGTH_SHORT).show();
            stopSelf();
        }

        if (mUser.getUid()==null){
            Toast.makeText(GameInvitationListener.this, "Loading notification listener failed: you won't recieve any game invitations.", Toast.LENGTH_SHORT).show();
            stopSelf();
        }

        getID(mUser.getUid());
    }

    private void getID(String playerUID){
        mDatabase = mDatabaseReference.child("UserMap").child(playerUID);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userMap = dataSnapshot.getValue(UserMap.class);
                getUserInfo(userMap.getUserID(), false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUserInfo(String playerID, final boolean isAnotherPlayer) {
        mReference = mDatabaseReference.child("Users").child(playerID);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    defaultUser = dataSnapshot.getValue(DefaultUser.class);
                    firebaseListenerWithNotification(defaultUser.getUserID());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void firebaseListenerWithNotification(String user){
        userID = user;
        ref = mDatabaseReference.child("Users").child(userID).child("lobbyID");

        mListener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()==null){
                    return;
                }
                lobbyID = dataSnapshot.getValue().toString();

                if (lobbyID != null && !lobbyID.equals("")){

                    Intent intent = new Intent(GameInvitationListener.this, GameActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("X", "X");
                    intent.putExtra("lobbyID", lobbyID);
                    PendingIntent pendingIntent = PendingIntent.getActivity(GameInvitationListener.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    Notification notification = new Notification.Builder(GameInvitationListener.this)
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setTicker("New invitation")  // the status text
                            .setWhen(System.currentTimeMillis())  // the time stamp
                            .setContentTitle("Game Invitation")  // the label of the entry
                            .setContentText("You have been invited to play Tic Tac Toe")  // the contents of the entry
                            .setContentIntent(pendingIntent)  // The intent to send when the entry is clicked
                            .build();

                    notificationManager.notify(NotificationManager.IMPORTANCE_DEFAULT, notification);
                    mReference.child("lobbyID").setValue("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        ref.removeEventListener(mListener);
        super.onDestroy();
    }
}
