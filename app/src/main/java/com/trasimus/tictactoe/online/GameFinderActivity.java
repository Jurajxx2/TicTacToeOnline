package com.trasimus.tictactoe.online;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finder);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("games");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value = dataSnapshot.getValue(String.class);

                fruits_list.add(value);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mListView = (ListView) findViewById(R.id.gameList);

            fruits = new String[]{
                    "Cape Gooseberry",
                    "Capuli cherry",
            };

        fruits_list = new ArrayList<String>(Arrays.asList(fruits));

        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, fruits_list);

        mListView.setAdapter(arrayAdapter);

            fruits_list.add("testxxx");
            arrayAdapter.notifyDataSetChanged();


    }

    public void launchGameActivity3(View view){
        myRef.setValue("3x3 game");
        Intent intent = new Intent(GameFinderActivity.this, GameActivity.class);
        intent.putExtra("size", 3);
        startActivity(intent);
        finish();
    }

    public void launchGameActivity4(View view){
        myRef.setValue("4x4 game");
        Intent intent = new Intent(GameFinderActivity.this, GameActivity.class);
        intent.putExtra("size", 4);
        startActivity(intent);
        finish();
    }

    public void launchGameActivity5(View view){
        myRef.setValue("5x5 game");
        Intent intent = new Intent(GameFinderActivity.this, GameActivity.class);
        intent.putExtra("size", 5);
        startActivity(intent);
        finish();
    }
}
