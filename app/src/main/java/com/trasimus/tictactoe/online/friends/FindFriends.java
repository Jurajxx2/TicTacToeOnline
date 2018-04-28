package com.trasimus.tictactoe.online.friends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
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
import com.trasimus.tictactoe.online.other.LoginActivity;

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
    private int alpha;

    private DefaultUser mDefaultUser;
    private ArrayList<DefaultUser> mList;
    private ArrayList<String> mUsers;
    private ArrayList<Integer> listedUsers;
    private String userID;
    private UserMap userMap;
    private DefaultUser currentUser;
    private ArrayList<ArrayList<String>> friendListArray1;
    private ArrayList<ArrayList<String>> friendListArray2;
    private ArrayList<ArrayList<String>> friends;
    private ArrayList<String> friendsInSearchResult;

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
        getID(mUser.getUid());

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

        mListView.setAdapter(adapter2);
        adapter2.clear();
        mUsers.clear();
        listedUsers.clear();
        friendsInSearchResult.clear();

        nextStep();
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
            Toast.makeText(this, showUser.getName() + " is your friend or friend request was sent", Toast.LENGTH_LONG).show();
        } else {
            String title = "User " + showUser.getUserID();
            String message = "Name: " + showUser.getName() + "\n";
            if (showUser.getAge() != null) {
                message = message + " aged " + showUser.getAge() + "\n";
            }
            if (showUser.getCountry() != null) {
                message = message + " from " + showUser.getCountry() + "\n";
            }

            int profileImage = R.drawable.icon_profile_empty;

            if (showUser.getPhotoID()!=null){
                if (showUser.getPhotoID().equals("grumpy")){
                    profileImage = R.drawable.grumpy;
                }if (showUser.getPhotoID().equals("kon")){
                    profileImage = R.drawable.kon;
                }if (showUser.getPhotoID().equals("opica")){
                    profileImage = R.drawable.opica;
                }if (showUser.getPhotoID().equals("0")){
                    profileImage = R.drawable.icon_profile_empty;
                }
            }

            new AlertDialog.Builder(FindFriends.this)
                    .setTitle(title)
                    .setIcon(profileImage)
                    .setMessage(message)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton("Add friend", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
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
                    }).create().show();
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



    private void getID(String playerUID){
        mDatabase = mDatabaseReference.child("UserMap").child(playerUID);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userMap = dataSnapshot.getValue(UserMap.class);
                getUserInfo();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUserInfo() {
        mReference = mDatabaseReference.child("Users").child(userMap.getUserID());
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



}
