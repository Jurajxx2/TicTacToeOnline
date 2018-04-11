package com.trasimus.tictactoe.online;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameFinderActivity extends AppCompatActivity {

    private ListView mListView;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String value;
    private String[] fruits;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> fruits_list;

    private GameObject mGameObject;
    private GameListAdapter mAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finder);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mListView = (ListView) findViewById(R.id.gameList);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GameObject test = mAdapter.getItem(position);
                test.getGameID();
                Toast.makeText(GameFinderActivity.this, "test" + test.getGameID(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GameFinderActivity.this, GameActivity.class);

                intent.putExtra("gameID", test.getGameID());
                intent.putExtra("size", 3);

                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onStart(){
        super.onStart();
        Log.d("test", "test2");

        mAdapter = new GameListAdapter(this, myRef);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.cleanup();
    }

    public void launchGameActivity3(View view){
        Intent intent = new Intent(GameFinderActivity.this, GameActivity.class);
        intent.putExtra("size", 3);
        startActivity(intent);
        finish();
    }

    public void launchGameActivity4(View view){
        Intent intent = new Intent(GameFinderActivity.this, GameActivity.class);
        intent.putExtra("size", 4);
        startActivity(intent);
        finish();
    }

    public void launchGameActivity5(View view){
        Intent intent = new Intent(GameFinderActivity.this, GameActivity.class);
        intent.putExtra("size", 5);
        startActivity(intent);
        finish();
    }
}
