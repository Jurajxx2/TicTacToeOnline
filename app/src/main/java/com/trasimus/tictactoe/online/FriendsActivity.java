package com.trasimus.tictactoe.online;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.Map;

public class FriendsActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private DatabaseReference ref;
    private DatabaseReference mDatabase;
    private DatabaseReference mReference;
    private DefaultUser mDefaultUser;
    private FirebaseUser mUser;
    private UserMap mMap;

    private ArrayAdapter adapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mListView = (ListView) findViewById(R.id.friendList);
        mListView.setAdapter(adapter);

        getID(mUser.getUid());
    }

    private void fillFriendList(){
        for (int i=0; i<mDefaultUser.getFriends().size(); i++){
            getUserInfo(mDefaultUser.getFriends().get(i).get(0));
        }
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
                getUserInfo(mMap.getUserID());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUserInfo(String userID) {
        mReference = mDatabaseReference.child("Users").child(userID);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDefaultUser = dataSnapshot.getValue(DefaultUser.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
