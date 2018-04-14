package com.trasimus.tictactoe.online;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FindFriends extends AppCompatActivity {

    private String[] countries;
    private EditText nameFind;
    private AutoCompleteTextView countryFind;
    private EditText ageFind;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter2;

    private FirebaseUser mUser;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mReference;
    private DatabaseReference mDatabase;
    private DatabaseReference reference;
    private ValueEventListener mListener;
    private ArrayList<String> friendRequest;

    private DefaultUser mDefaultUser;
    private ArrayList<DefaultUser> mList;
    private ArrayList<String> mUsers;
    private ArrayList<Integer> listedUsers;
    private String userID;
    private DefaultUser defaultUser;
    private UserMap userMap;
    private DefaultUser currentUser;
    private ArrayList<ArrayList<String>> friendListArray1;
    private ArrayList<ArrayList<String>> friendListArray2;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        nameFind = (EditText)findViewById(R.id.nameFind);
        countryFind = (AutoCompleteTextView)findViewById(R.id.countryFind);
        ageFind = (EditText)findViewById(R.id.ageFind);

        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        countries = getResources().getStringArray(R.array.countries_array);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        countryFind.setAdapter(adapter);

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
                //userID = listedUsers.get(position);
                Log.d("test", "test " + listedUsers.get(position));
                getUser(mList.get(listedUsers.get(position)));
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

    private void getUser(DefaultUser defaultUser){
        showUser(defaultUser);
    }

    private void showUser(final DefaultUser defaultUser){
        String title = "User " + defaultUser.getUserID();
        String message = "Name: " + defaultUser.getName() + "\n";
        if (defaultUser.getAge()!=null) {
            message = message + " aged " + defaultUser.getAge() + "\n";
        }
        if (defaultUser.getCountry()!=null) {
            message = message + " from " + defaultUser.getCountry() + "\n";
        }

        new AlertDialog.Builder(FindFriends.this)
                .setTitle(title)
                .setIcon(R.drawable.icon_profile_empty)
                .setMessage(message)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton("Add friend", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        friendRequest = new ArrayList<String>();
                        friendRequest.add(currentUser.getUserID());
                        friendRequest.add(currentUser.getUserID());
                        friendRequest.add("0");

                        friendListArray1 = new ArrayList<ArrayList<String>>();
                        friendListArray2 = new ArrayList<ArrayList<String>>();

                        if (defaultUser.getFriends() == null){
                            friendListArray1.add(friendRequest);
                        } else {
                            friendListArray1 = defaultUser.getFriends();
                            friendListArray1.add(friendRequest);
                        }

                        if (currentUser.getFriends() == null){
                            friendListArray2.add(friendRequest);
                        } else {
                            friendListArray2 = currentUser.getFriends();
                            friendListArray2.add(friendRequest);
                        }

                        mDatabaseReference.child("Users").child(defaultUser.getUserID()).child("Friends").setValue(friendListArray1);
                        mDatabaseReference.child("Users").child(currentUser.getUserID()).child("Friends").setValue(friendListArray2);
                        Toast.makeText(FindFriends.this, "Friend request was sent", Toast.LENGTH_SHORT);

                    }
                }).create().show();
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
