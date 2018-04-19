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
import android.support.v4.app.NotificationCompat;
import android.util.Log;

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
import com.trasimus.tictactoe.online.friends.LobbyActivity;
import com.trasimus.tictactoe.online.R;

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
            NotificationChannel channel = new NotificationChannel(NotificationChannel.DEFAULT_CHANNEL_ID, name, importance);
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
                if (!isAnotherPlayer) {
                    defaultUser = dataSnapshot.getValue(DefaultUser.class);
                    firebaseListenerWithNotification(defaultUser.getUserID());
                } else if (isAnotherPlayer){
                    otherUser = dataSnapshot.getValue(DefaultUser.class);
                    showNotification(otherUser.getName());
                }
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
                lobbyID = dataSnapshot.getValue().toString();

                if (lobbyID != null && !lobbyID.equals("")){

                    getPlayerId(lobbyID);
//                    Intent intent = new Intent(GameInvitationListener.this, LobbyActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.putExtra("X", "X");
//                    intent.putExtra("lobbyID", lobbyID);
//                    PendingIntent pendingIntent = PendingIntent.getActivity(GameInvitationListener.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                    Notification notification = new Notification.Builder(GameInvitationListener.this)
//                            .setSmallIcon(R.drawable.icon_profile_empty)
//                            .setTicker("New invitation")  // the status text
//                            .setWhen(System.currentTimeMillis())  // the time stamp
//                            .setContentTitle("Game Invitation")  // the label of the entry
//                            .setContentText("You have been invited to play Tic Tac Toe with ")  // the contents of the entry
//                            .setContentIntent(pendingIntent)  // The intent to send when the entry is clicked
//                            .build();
//
//                    notificationManager.notify(NotificationManager.IMPORTANCE_DEFAULT, notification);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPlayerId(String lobbby){
        mDatabaseReference.child("Lobby").child(lobbby).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLobby = dataSnapshot.getValue(Lobby.class);
                if (mLobby == null){
                    return;
                }
                getUserInfo(mLobby.getPlayer1(), true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showNotification(String userName){
        Intent intent = new Intent(GameInvitationListener.this, LobbyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("X", "X");
        intent.putExtra("lobbyID", lobbyID);
        PendingIntent pendingIntent = PendingIntent.getActivity(GameInvitationListener.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(GameInvitationListener.this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setTicker("New invitation")  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("Game Invitation")  // the label of the entry
                .setContentText("You have been invited to play Tic Tac Toe with ")  // the contents of the entry
                .setContentIntent(pendingIntent)  // The intent to send when the entry is clicked
                .build();

        notificationManager.notify(NotificationManager.IMPORTANCE_DEFAULT, notification);
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
