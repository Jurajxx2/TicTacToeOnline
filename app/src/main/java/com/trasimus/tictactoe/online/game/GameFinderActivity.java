package com.trasimus.tictactoe.online.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.trasimus.tictactoe.online.GameObject;
import com.trasimus.tictactoe.online.R;
import com.trasimus.tictactoe.online.UserMap;

import java.util.List;

public class GameFinderActivity extends AppCompatActivity {

    private ListView mListView;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference mReference;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> fruits_list;

    private GameObject mGameObject;
    private GameListAdapter mAdapter;

    private String key;
    private DefaultUser mDefaultUser;
    private UserMap mUserMap;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReference2;

    private boolean done = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finder);

        mGameObject = new GameObject();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        getID(mUser.getUid());

        mListView = (ListView) findViewById(R.id.gameList);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GameObject test = mAdapter.getItem(position);
                getGameInfo(test.getGameID());
            }
        });
    }

    private void getID(String playerUID){
        mDatabaseReference = myRef.child("UserMap").child(playerUID);
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUserMap = dataSnapshot.getValue(UserMap.class);
                getUserInfo();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUserInfo() {
        mDatabaseReference2 = myRef.child("Users").child(mUserMap.getUserID());
        mDatabaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDefaultUser = dataSnapshot.getValue(DefaultUser.class);
                done = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getGameInfo(String gameID){
        mReference = myRef.child("games").child(gameID);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGameObject = dataSnapshot.getValue(GameObject.class);

                Intent intent = new Intent(GameFinderActivity.this, GameActivity.class);

                intent.putExtra("gameID", mGameObject.getGameID());
                intent.putExtra("size", mGameObject.getSize());

                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart(){
        super.onStart();
        mAdapter = new GameListAdapter(this, myRef);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.cleanup();
    }

    public void launchGameActivity3(View view){
        if (!done){
            Toast.makeText(this, "Please check your internet connection or try again later", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(GameFinderActivity.this, GameActivity.class);
        intent.putExtra("size", 3);
        intent.putExtra("key", mDefaultUser.getMyGameID());
        startActivity(intent);
        finish();
    }

    public void launchGameActivity4(View view){
        if (!done){
            Toast.makeText(this, "Please check your internet connection or try again later", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(GameFinderActivity.this, GameActivity.class);
        intent.putExtra("size", 4);
        intent.putExtra("key", mDefaultUser.getMyGameID());
        startActivity(intent);
        finish();
    }

    public void launchGameActivity5(View view){
        if (!done){
            Toast.makeText(this, "Please check your internet connection or try again later", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(GameFinderActivity.this, GameActivity.class);
        intent.putExtra("size", 5);
        intent.putExtra("key", mDefaultUser.getMyGameID());
        startActivity(intent);
        finish();
    }
}
