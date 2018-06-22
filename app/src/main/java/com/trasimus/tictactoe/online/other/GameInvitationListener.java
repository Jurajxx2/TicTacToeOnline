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
import com.trasimus.tictactoe.online.CountDownTimer2;
import com.trasimus.tictactoe.online.DefaultUser;
import com.trasimus.tictactoe.online.R;
import com.trasimus.tictactoe.online.game.GameActivity;

import java.text.SimpleDateFormat;

public class GameInvitationListener extends Service {

    private FirebaseUser mUser;
    private DefaultUser defaultUser;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference ref;
    private DatabaseReference mReference;
    private String userID;
    private String lobbyID;
    private NotificationManager notificationManager;
    private ValueEventListener mListener;

    private int i=0;

    private CountDownTimer2 mTimer; //Timer for setting updated time to show if player is online

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


        //Initiate CountDownTimer
//        mTimer = new CountDownTimer2(1000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//            }
//
//            @Override
//            public void onFinish() {
//                //mDatabaseReference.child("Users").child(mUser.getUid()).child("lastOnline").setValue(i);
//                //mTimer.start();
//                //i++;
//            }
//        };
        //mTimer.start();

        getUserInfo(mUser.getUid());
    }

    private void getUserInfo(String playerID) {
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
        mDatabaseReference.child("Users").child(userID).child("isOnline").setValue(true);


        mListener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()==null){
                    return;
                }
                lobbyID = dataSnapshot.getValue().toString();

                if (lobbyID != null && !lobbyID.equals("")){

                    mDatabaseReference.child("Users").child(userID).child("response").setValue(lobbyID);

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
                            .setContentText("You have been invited to play Tic Tac Toe Online")  // the contents of the entry
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
        mDatabaseReference.child("Users").child(userID).child("isOnline").setValue(false);
        ref.removeEventListener(mListener);
        super.onDestroy();
    }
}
