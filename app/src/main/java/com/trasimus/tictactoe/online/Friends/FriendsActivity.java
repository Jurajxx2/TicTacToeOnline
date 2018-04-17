package com.trasimus.tictactoe.online.Friends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trasimus.tictactoe.online.DefaultUser;
import com.trasimus.tictactoe.online.R;
import com.trasimus.tictactoe.online.UserMap;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private DatabaseReference ref;
    private DatabaseReference mDatabase;
    private DatabaseReference mReference;
    private DefaultUser mDefaultUser;
    private DefaultUser defaultUser;
    private UserMap mMap;

    private FirebaseUser mUser;

    private ArrayAdapter adapter;
    private ListView mListView;
    private Bundle bundle;

    private String userID;
    private ArrayList<ArrayList<String>> friends;

    private String positiveBTN;
    private String negativeBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        bundle = getIntent().getExtras();
        userID = bundle.getString("ID");

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        ref = mDatabaseReference.child("Users").child(userID).child("Friends");

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mListView = (ListView) findViewById(R.id.friendList);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        mListView.setAdapter(adapter);

        getUserInfo(userID, false, "", "", false, 0);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showUser(position);
            }
        });
    }

    private void showUser(int position) {
        if (friends.get(position).get(0).equals(userID)){
            getUserInfo(friends.get(position).get(1), false, friends.get(position).get(2), "SENT", true, position);
        } else if (friends.get(position).get(1).equals(userID)){
            getUserInfo(friends.get(position).get(0), false, friends.get(position).get(2), "RECIEVED", true, position);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.menuBtn){
            Intent intent = new Intent(this, FindFriends.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getID(String playerUID){
        mDatabase = mDatabaseReference.child("UserMap").child(playerUID);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMap = dataSnapshot.getValue(UserMap.class);
                getUserInfo(mMap.getUserID(), false, "", "", false, 0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUserInfo(final String userIDX, final boolean isListFunction, final String isAccepted, final String state, final boolean isShowUserF, final int position) {
        mReference = mDatabaseReference.child("Users").child(userIDX);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isListFunction){
                    defaultUser = dataSnapshot.getValue(DefaultUser.class);
                    String content = defaultUser.getName();
                    if (isAccepted.equals("0") && state.equals("RECIEVED")){
                        content = content + " [PENDING - ACCEPT OR DENIE]";
                    } else if (isAccepted.equals("0") && state.equals("SENT")){
                        content = content + " [PENDING - SENT]";
                    }
                    adapter.add(content);
                } else if (isShowUserF){
                    defaultUser = dataSnapshot.getValue(DefaultUser.class);
                    String title = "User " + defaultUser.getUserID();
                    String message = "Name: " + defaultUser.getName() + "\n";
                    if (defaultUser.getAge()!=null) {
                        message = message + " aged " + defaultUser.getAge() + "\n";
                    }
                    if (defaultUser.getCountry()!=null) {
                        message = message + " from " + defaultUser.getCountry() + "\n";
                    }
                    if (isAccepted.equals("0") && state.equals("RECIEVED")){
                        positiveBTN = "Accept";
                        negativeBTN = "Delete request";
                    } else if (isAccepted.equals("0") && state.equals("SENT")){
                        positiveBTN = "";
                        negativeBTN = "Delete request";
                    } else if (isAccepted.equals("1")){
                        positiveBTN = "Invite to game";
                        negativeBTN = "Delete friend";
                    }

                    new AlertDialog.Builder(FriendsActivity.this)
                            .setTitle(title)
                            .setIcon(R.drawable.icon_profile_empty)
                            .setMessage(message)
                            .setNegativeButton(negativeBTN, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (isAccepted.equals("0") && state.equals("RECIEVED")){
                                        //DELETE REQUEST
                                        mDatabaseReference.child("Users").child(userIDX).child("Friends").child(String.valueOf(position)).removeValue();
                                        mDatabaseReference.child("Users").child(userID).child("Friends").child(String.valueOf(position)).removeValue();
                                        refreshActivity();
                                    } else if (isAccepted.equals("0") && state.equals("SENT")){
                                        //DELETE REQUEST
                                        mDatabaseReference.child("Users").child(userIDX).child("Friends").child(String.valueOf(position)).removeValue();
                                        mDatabaseReference.child("Users").child(userID).child("Friends").child(String.valueOf(position)).removeValue();
                                        refreshActivity();
                                    } else if (isAccepted.equals("1")){
                                        //DELETE FRIEND
                                        mDatabaseReference.child("Users").child(userIDX).child("Friends").child(String.valueOf(position)).removeValue();
                                        mDatabaseReference.child("Users").child(userID).child("Friends").child(String.valueOf(position)).removeValue();
                                        refreshActivity();
                                    }
                                }
                            })
                            .setPositiveButton(positiveBTN, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    if (isAccepted.equals("0") && state.equals("RECIEVED")){
                                        //ACCEPT FRIEND REQUEST
                                        mDatabaseReference.child("Users").child(userIDX).child("Friends").child(String.valueOf(position)).child("2").setValue("1");
                                        mDatabaseReference.child("Users").child(userID).child("Friends").child(String.valueOf(position)).child("2").setValue("1");
                                        refreshActivity();
                                    }  else if (isAccepted.equals("1")){
                                        //INVITE TO GAME
                                        String lobbyID = mDatabaseReference.child("Lobby").push().getKey();
                                        mDatabaseReference.child("Users").child(userIDX).child("lobbyID").setValue(lobbyID);
                                        mDatabaseReference.child("Users").child(userID).child("lobbyID").setValue(lobbyID);
                                        Toast.makeText(FriendsActivity.this, "Game Invitation Sent", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(FriendsActivity.this, LobbyActivity.class);
                                        intent.putExtra("p1", userID);
                                        intent.putExtra("p2", defaultUser.getUserID());
                                        intent.putExtra("lobbyID", lobbyID);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }).create().show();

                } else {
                    mDefaultUser = dataSnapshot.getValue(DefaultUser.class);
                    getFriendList();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getFriendList(){
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friends = (ArrayList<ArrayList<String>>) dataSnapshot.getValue();

                if (friends == null){
                    return;
                }

                for (int i=0; i<friends.size(); i++){
                    if (friends.get(i).get(0).equals(userID)){
                        getUserInfo(friends.get(i).get(1), true, friends.get(i).get(2), "SENT", false, 0);
                    } else if (friends.get(i).get(1).equals(userID)){
                        getUserInfo(friends.get(i).get(0), true, friends.get(i).get(2), "RECIEVED", false, 0);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void refreshActivity(){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}
