package com.trasimus.tictactoe.online.friends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trasimus.tictactoe.online.DefaultUser;
import com.trasimus.tictactoe.online.R;
import com.trasimus.tictactoe.online.game.GameSizeChoose;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private DatabaseReference ref;
    private DatabaseReference mDatabase;
    private DatabaseReference mReference;
    private DatabaseReference friendReference;
    private DefaultUser mDefaultUser;
    private DefaultUser defaultUser;
    private Button findFriends;
    private FriendListAdapter mAdapter;

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
        findFriends = (Button) findViewById(R.id.find_friends);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        //mListView.setAdapter(adapter);

        getUserInfo(userID, false, "", "", false, 0);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showUser(position);
            }
        });


        findFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendsActivity.this, FindFriends.class);
                startActivity(intent);
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
    protected void onStart() {
        super.onStart();
        mAdapter = new FriendListAdapter(this, ref);
        mListView.setAdapter(mAdapter);
    }


    private void getUserInfo(final String userIDX, final boolean isListFunction, final String isAccepted, final String state, final boolean isShowUserF, final int position) {
        mReference = mDatabaseReference.child("Users").child(userIDX);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isListFunction){
                    defaultUser = dataSnapshot.getValue(DefaultUser.class);
                    String content;
                    if (defaultUser==null){
                      content = "User was deleted";
                    } else {
                        content = defaultUser.getName();
                        if (isAccepted.equals("0") && state.equals("RECIEVED")) {
                            content = content + " [PENDING - ACCEPT OR DENIE]";
                        } else if (isAccepted.equals("0") && state.equals("SENT")) {
                            content = content + " [PENDING - SENT]";
                        }
                    }
                    adapter.add(content);
                } else if (isShowUserF){
                    defaultUser = dataSnapshot.getValue(DefaultUser.class);

                    if (defaultUser==null){
                        new AlertDialog.Builder(FriendsActivity.this)
                                .setTitle("Deleted user")
                                .setIcon(R.drawable.icon_profile_empty)
                                .setMessage("User was deleted")
                                .setNegativeButton("Delete from friends", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mDatabaseReference.child("Users").child(userID).child("Friends").child(String.valueOf(position)).removeValue();
                                        refreshActivity();
                                    }
                                })
                                .setPositiveButton(positiveBTN, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                }).create().show();
                    } else {
                        String message = "Name: " + defaultUser.getName() + "\n";
                        if (defaultUser.getAge() != null) {
                            message = message + " aged " + defaultUser.getAge() + "\n";
                        }
                        if (defaultUser.getCountry() != null) {
                            message = message + " from " + defaultUser.getCountry() + "\n";
                        }
                        if (isAccepted.equals("0") && state.equals("RECIEVED")) {
                            positiveBTN = "Accept";
                            negativeBTN = "Delete request";
                        } else if (isAccepted.equals("0") && state.equals("SENT")) {
                            positiveBTN = "";
                            negativeBTN = "Delete request";
                        } else if (isAccepted.equals("1")) {
                            positiveBTN = "Invite to game";
                            negativeBTN = "Delete friend";
                        }

                        int profileImage = R.drawable.icon_profile_empty;

                        if (defaultUser.getPhotoID() != null) {
                            if (defaultUser.getPhotoID().equals("grumpy")) {
                                profileImage = R.drawable.grumpy;
                            }
                            if (defaultUser.getPhotoID().equals("kon")) {
                                profileImage = R.drawable.kon;
                            }
                            if (defaultUser.getPhotoID().equals("opica")) {
                                profileImage = R.drawable.opica;
                            }
                            if (defaultUser.getPhotoID().equals("0")) {
                                profileImage = R.drawable.icon_profile_empty;
                            }
                        }

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FriendsActivity.this);
                        final LayoutInflater inflater = FriendsActivity.this.getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.user_profile, null);
                        dialogBuilder.setView(dialogView);
                        dialogBuilder.setNegativeButton(negativeBTN, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                friends.remove(position);
                                mDatabaseReference.child("Users").child(userID).child("Friends").setValue(friends);
                                deleteRequest(userIDX);
                            }
                        });
                        dialogBuilder.setPositiveButton(positiveBTN, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                if (isAccepted.equals("0") && state.equals("RECIEVED")) {
                                    //ACCEPT FRIEND REQUEST
                                    acceptRequest(userIDX);
                                    mDatabaseReference.child("Users").child(userID).child("Friends").child(String.valueOf(position)).child("2").setValue("1");
                                } else if (isAccepted.equals("1")) {
                                    //INVITE TO GAME
                                    Intent intent = new Intent(FriendsActivity.this, GameSizeChoose .class);
                                    intent.putExtra("p2", defaultUser.getUserID());
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                        AlertDialog b = dialogBuilder.create();
                        ImageView profileIMG = dialogView.findViewById(R.id.profileIMG);
                        TextView nick = dialogView.findViewById(R.id.nick);
                        TextView age = dialogView.findViewById(R.id.age);
                        TextView from = dialogView.findViewById(R.id.from);
                        TextView wins = dialogView.findViewById(R.id.winsnum);
                        TextView loses = dialogView.findViewById(R.id.losesnum);
                        TextView points = dialogView.findViewById(R.id.pointssum);
                        TextView draws = dialogView.findViewById(R.id.drawsnum);
                        TextView ranking = dialogView.findViewById(R.id.ranking);

                        nick.setText(defaultUser.getName());

                        if (defaultUser.getAge().equals("")){
                            age.setText("Undefined");
                        } else {
                            age.setText(defaultUser.getAge());
                        }

                        if (defaultUser.getCountry().equals("")){
                            from.setText("Undefined");
                        } else {
                            from.setText(defaultUser.getCountry());
                        }

                        String won = String.valueOf(defaultUser.getGamesWon());
                        wins.setText(won);
                        String lost = String.valueOf(defaultUser.getGamesLost());
                        loses.setText(lost);
                        String point = String.valueOf(defaultUser.getPoints());
                        points.setText(point);
                        ranking.setText("1.");

                        if (defaultUser.getPhotoID() != null) {
                            if (defaultUser.getPhotoID().equals("grumpy")) {
                                profileIMG.setImageResource(R.drawable.grumpy);
                            }
                            if (defaultUser.getPhotoID().equals("kon")) {
                                profileIMG.setImageResource(R.drawable.kon);
                            }
                            if (defaultUser.getPhotoID().equals("opica")) {
                                profileIMG.setImageResource(R.drawable.opica);
                            }
                            if (defaultUser.getPhotoID().equals("0")) {
                                profileIMG.setImageResource(R.drawable.icon_profile_empty);
                            }
                        }

                        b.show();
                    }
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

    private void deleteRequest(String friendID){
        friendReference = mDatabaseReference.child("Users").child(friendID).child("Friends");
        friendReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ArrayList<String>> friendsOfFriendToDelete;

                friendsOfFriendToDelete = (ArrayList<ArrayList<String>>) dataSnapshot.getValue();

                for (int i=0; i<friendsOfFriendToDelete.size(); i++){
                    if (friendsOfFriendToDelete.get(i).get(0).equals(userID) || friendsOfFriendToDelete.get(i).get(1).equals(userID)){
                        friendsOfFriendToDelete.remove(i);
                        friendReference.setValue(friendsOfFriendToDelete);
                        break;
                    }
                }

                refreshActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void acceptRequest(String friendID){
        friendReference = mDatabaseReference.child("Users").child(friendID).child("Friends");
        friendReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ArrayList<String>> friendsOfFriendToAccept;

                friendsOfFriendToAccept = (ArrayList<ArrayList<String>>) dataSnapshot.getValue();

                for (int i=0; i<friendsOfFriendToAccept.size(); i++){
                    if (friendsOfFriendToAccept.get(i).get(0).equals(userID) || friendsOfFriendToAccept.get(i).get(1).equals(userID)){
                        friendsOfFriendToAccept.get(i).set(2, "1");
                        friendReference.setValue(friendsOfFriendToAccept);
                        break;
                    }
                }

                refreshActivity();
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
