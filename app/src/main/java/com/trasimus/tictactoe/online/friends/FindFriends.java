package com.trasimus.tictactoe.online.friends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import com.trasimus.tictactoe.online.game.GameSizeChoose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FindFriends extends AppCompatActivity {

    private String[] countries;
    private EditText nameFind;
    private ArrayAdapter<String> adapter2;

    private FirebaseUser mUser;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mReference;
    private DatabaseReference mDatabase;
    private DatabaseReference reference;
    private DatabaseReference ref;
    private DatabaseReference ref2;
    private ArrayList<String> friendRequest;
    private DefaultUser showUser;
    private boolean isFriend;
    private int x=0;

    private DefaultUser mDefaultUser;
    private ArrayList<DefaultUser> mList;
    private ArrayList<String> mUsers;
    private ArrayList<Integer> listedUsers;
    private String userID;
    private DefaultUser currentUser;
    private ArrayList<ArrayList<String>> friendListArray1;
    private ArrayList<ArrayList<String>> friendListArray2;
    private ArrayList<ArrayList<String>> friends;
    private ArrayList<String> friendsInSearchResult;

    private FindUserListAdapter mAdapter;

    private ArrayList<String> friendIDs;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        nameFind = (EditText)findViewById(R.id.nameFind);

        friendIDs = new ArrayList<String>();
        friendsInSearchResult = new ArrayList<String>();

        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        countries = getResources().getStringArray(R.array.countries_array);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        reference = mDatabaseReference.child("Users");
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        getUserInfo();

        mListView = (ListView)findViewById(R.id.searchResult);

        mList = new ArrayList<DefaultUser>();
        mList.clear();
        mUsers = new ArrayList<String>();
        mUsers.clear();
        listedUsers = new ArrayList<Integer>();
        listedUsers.clear();

        sortArray();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showUser = mList.get(listedUsers.get(position));
                showUser(position);
            }
        });
    }

    private void sortArray(){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    mDefaultUser = snapshot.getValue(DefaultUser.class);
                    mList.add(mDefaultUser);
                }

                Collections.sort(mList, new Comparator<DefaultUser>() {
                    @Override
                    public int compare(DefaultUser o1, DefaultUser o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void doAnything(View view){

        if (nameFind.getText().toString().isEmpty()){
            Toast.makeText(this, "Please, define user name", Toast.LENGTH_LONG).show();
            return;
        }

        //mListView.setAdapter(adapter2);
        //adapter2.clear();
        //mUsers.clear();
        //listedUsers.clear();
        //friendsInSearchResult.clear();

        nextStep();
        mAdapter = new FindUserListAdapter(this, nameFind.getText().toString());
        mListView.setAdapter(mAdapter);
    }

    private void nextStep(){
        int position = 1;

        String content;

        for (int x=0; x<mList.size(); x++){
            if (!mList.get(x).getName().contains(nameFind.getText().toString()) || mList.get(x).getUserID().equals(currentUser.getUserID())){
                continue;
            }

            listedUsers.add(x);

            content = position + ". " + mList.get(x).getName();

            for (int all=0; all<friendIDs.size(); all++) {
                if (mList.get(x).getUserID().equals(friendIDs.get(all))){
                    //friendsInSearchResult.add("X");
                    friendsInSearchResult.add(position-1, "X");
                    break;
                } else {
                    friendsInSearchResult.add(position-1, "O");
                }
            }
            //Toast.makeText(this, "ArraySize: " + friendsInSearchResult.size(), Toast.LENGTH_SHORT).show();
            if (mList.get(x).getAge()!=null) {
                content = content + " aged " + mList.get(x).getAge();
            }
            if (mList.get(x).getCountry()!=null) {
                content = content + " from " + mList.get(x).getCountry();
            }
            position++;
            mUsers.add(content);
        }

        for (int i=0; i<mUsers.size(); i++){
            adapter2.add(mUsers.get(i));
        }
    }

    private void showUser(int positionInResult){

        if (friendsInSearchResult.size()>0) {
            if (friendsInSearchResult.get(positionInResult).equals("X")) {
                isFriend = true;
            } else {
                isFriend = false;
            }
        } else {
            isFriend = false;
        }


        if (isFriend){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FindFriends.this);
            final LayoutInflater inflater = FindFriends.this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.user_profile, null);
            dialogBuilder.setView(dialogView);

            for (x=0; x<friends.size(); x++){
                if (friends.get(x).get(0).equals(showUser.getUserID()) || friends.get(x).get(1).equals(showUser.getUserID())){
                    final int omega = x;
                    if (friends.get(x).get(0).equals(showUser.getUserID()) && friends.get(x).get(2).equals("0")){
                        dialogBuilder.setNegativeButton("Delete request", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                friends.remove(omega);
                                mDatabaseReference.child("Users").child(userID).child("Friends").setValue(friends);
                                deleteRequest(showUser.getUserID());
                            }
                        });
                        dialogBuilder.setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                acceptRequest(showUser.getUserID());
                                mDatabaseReference.child("Users").child(userID).child("Friends").child(String.valueOf(omega)).child("2").setValue("1");
                            }
                        });
                    } else if (friends.get(x).get(1).equals(showUser.getUserID()) && friends.get(x).get(2).equals("0")){
                        dialogBuilder.setNegativeButton("Delete request", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                friends.remove(omega);
                                mDatabaseReference.child("Users").child(userID).child("Friends").setValue(friends);
                                deleteRequest(showUser.getUserID());
                            }
                        });
                        dialogBuilder.setPositiveButton("", null);
                    } else if (friends.get(x).get(2).equals("1")) {

                        dialogBuilder.setNegativeButton("Delete friend", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                friends.remove(omega);
                                mDatabaseReference.child("Users").child(userID).child("Friends").setValue(friends);
                                deleteRequest(showUser.getUserID());
                            }
                        });
                        dialogBuilder.setPositiveButton("Invite to game", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(FindFriends.this, GameSizeChoose.class);
                                intent.putExtra("p2", showUser.getUserID());
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
            }


            AlertDialog b = dialogBuilder.create();
            ImageView profileIMG = dialogView.findViewById(R.id.profileIMG);
            TextView nick = dialogView.findViewById(R.id.nick);
            TextView age = dialogView.findViewById(R.id.age);
            TextView from = dialogView.findViewById(R.id.from);
            TextView wins = dialogView.findViewById(R.id.wins);
            TextView loses = dialogView.findViewById(R.id.loses);
            TextView points = dialogView.findViewById(R.id.points);
            TextView ranking = dialogView.findViewById(R.id.ranking);

            nick.setText(showUser.getName());
            age.setText(showUser.getAge());
            from.setText(showUser.getCountry());
            //wins.setText(showUser.getGamesWon());
            //loses.setText(showUser.getGamesLost());
            //points.setText(showUser.getPoints());

            if (showUser.getPhotoID() != null) {
                if (showUser.getPhotoID().equals("grumpy")) {
                    profileIMG.setImageResource(R.drawable.grumpy);
                }
                if (showUser.getPhotoID().equals("kon")) {
                    profileIMG.setImageResource(R.drawable.kon);
                }
                if (showUser.getPhotoID().equals("opica")) {
                    profileIMG.setImageResource(R.drawable.opica);
                }
                if (showUser.getPhotoID().equals("0")) {
                    profileIMG.setImageResource(R.drawable.icon_profile_empty);
                }
            }

            b.show();        } else {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FindFriends.this);
            final LayoutInflater inflater = FindFriends.this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.user_profile, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setNegativeButton(android.R.string.no, null);
            dialogBuilder.setPositiveButton("Add friend", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    if (friends!=null) {
                        if (friends.size() > 99) {
                            Toast.makeText(FindFriends.this, "Maximum number of friends in friend list is 100. Please delete some friends to add new", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    if (showUser.getFriends() != null) {
                        if (showUser.getFriends().size() > 99) {
                            Toast.makeText(FindFriends.this, "Maximum number of friends in friend list is 100. Player " + showUser.getName() + " already have maximum number of friends", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    friendRequest = new ArrayList<String>();
                    friendRequest.add(currentUser.getUserID());//WHO SENT REQUEST
                    friendRequest.add(showUser.getUserID());//WHO RECIEVE REQUEST
                    friendRequest.add("0");//IF WAS ACCEPTED

                    friendListArray1 = new ArrayList<ArrayList<String>>();
                    friendListArray2 = new ArrayList<ArrayList<String>>();

                    getFriendsFriendList(showUser.getUserID());

                    if (friends == null) {
                        Log.d("test", "Get friends is null Current user");
                        friendListArray2.add(friendRequest);
                    } else {
                        friendListArray2.addAll(friends);
                        friendListArray2.add(friendRequest);
                    }

                    mDatabaseReference.child("Users").child(currentUser.getUserID()).child("Friends").setValue(friendListArray2);
                    Toast.makeText(FindFriends.this, "Friend request was sent", Toast.LENGTH_SHORT);
                    finish();

                }
            });
            AlertDialog b = dialogBuilder.create();
            ImageView profileIMG = dialogView.findViewById(R.id.profileIMG);
            TextView nick = dialogView.findViewById(R.id.nick);
            TextView age = dialogView.findViewById(R.id.age);
            TextView from = dialogView.findViewById(R.id.from);
            TextView wins = dialogView.findViewById(R.id.wins);
            TextView loses = dialogView.findViewById(R.id.loses);
            TextView points = dialogView.findViewById(R.id.pointssum);
            TextView ranking = dialogView.findViewById(R.id.ranking);

            nick.setText(showUser.getName());
            if (showUser.getAge().equals("")){
                age.setText("Undefined");
            } else {
                age.setText(showUser.getAge());
            }

            if (showUser.getCountry().equals("")){
                from.setText("Undefined");
            } else {
                from.setText(showUser.getCountry());
            }

            String won = String.valueOf(showUser.getGamesWon());
            wins.setText(won);
            String lost = String.valueOf(showUser.getGamesLost());
            loses.setText(lost);
            String point = String.valueOf(showUser.getPoints());
            points.setText(point);

            if (showUser.getPhotoID() != null) {
                if (showUser.getPhotoID().equals("grumpy")) {
                    profileIMG.setImageResource(R.drawable.grumpy);
                }
                if (showUser.getPhotoID().equals("kon")) {
                    profileIMG.setImageResource(R.drawable.kon);
                }
                if (showUser.getPhotoID().equals("opica")) {
                    profileIMG.setImageResource(R.drawable.opica);
                }
                if (showUser.getPhotoID().equals("0")) {
                    profileIMG.setImageResource(R.drawable.icon_profile_empty);
                }
            }

            b.show();
        }
    }

    private void getFriendList(){
        ref = mDatabaseReference.child("Users").child(currentUser.getUserID()).child("Friends");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friends = (ArrayList<ArrayList<String>>) dataSnapshot.getValue();

                if (friends == null){
                    return;
                }

                for (int i=0; i<friends.size(); i++){
                    if (friends.get(i).get(0).equals(userID)){
                        friendIDs.add(friends.get(i).get(1));
                        //getUserInfo(friends.get(i).get(1), true, friends.get(i).get(2), "SENT", false, 0);
                    } else if (friends.get(i).get(1).equals(userID)){
                        //getUserInfo(friends.get(i).get(0), true, friends.get(i).get(2), "RECIEVED", false, 0);
                        friendIDs.add(friends.get(i).get(0));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getFriendsFriendList(final String friendID){
        ref2 = mDatabaseReference.child("Users").child(friendID).child("Friends");
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ArrayList<String>> friendsOfFriend = new ArrayList<ArrayList<String>>();

                friendsOfFriend = (ArrayList<ArrayList<String>>) dataSnapshot.getValue();

                if (friendsOfFriend == null) {
                    Log.d("test", "Get friends is null Another user");
                    friendListArray1.add(friendRequest);
                } else {
                    friendListArray1.addAll(friendsOfFriend);
                    friendListArray1.add(friendRequest);
                }
                mDatabaseReference.child("Users").child(friendID).child("Friends").setValue(friendListArray1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getUserInfo() {
        mReference = mDatabaseReference.child("Users").child(mUser.getUid());
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(DefaultUser.class);
                userID = currentUser.getUserID();

                getFriendList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void deleteRequest(final String friendID){
        reference.child(friendID).child("Friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ArrayList<String>> friendsOfFriendToDelete;

                friendsOfFriendToDelete = (ArrayList<ArrayList<String>>) dataSnapshot.getValue();

                for (int i=0; i<friendsOfFriendToDelete.size(); i++){
                    if (friendsOfFriendToDelete.get(i).get(0).equals(userID) || friendsOfFriendToDelete.get(i).get(1).equals(userID)){
                        friendsOfFriendToDelete.remove(i);
                        reference.child(friendID).child("Friends").setValue(friendsOfFriendToDelete);
                        break;
                    }
                }
                Toast.makeText(FindFriends.this, "User was successfully deleted from your friend list", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void acceptRequest(final String friendID){
        reference.child(friendID).child("Friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ArrayList<String>> friendsOfFriendToAccept;

                friendsOfFriendToAccept = (ArrayList<ArrayList<String>>) dataSnapshot.getValue();

                for (int i=0; i<friendsOfFriendToAccept.size(); i++){
                    if (friendsOfFriendToAccept.get(i).get(0).equals(userID) || friendsOfFriendToAccept.get(i).get(1).equals(userID)){
                        friendsOfFriendToAccept.get(i).set(2, "1");
                        reference.child(friendID).child("Friends").setValue(friendsOfFriendToAccept);
                        break;
                    }
                }
                Toast.makeText(FindFriends.this, "Request was accepted", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
