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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trasimus.tictactoe.online.GameObject;
import com.trasimus.tictactoe.online.R;

import java.util.List;

public class GameFinderActivity extends AppCompatActivity {

    private ListView mListView;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> fruits_list;

    private GameObject mGameObject;
    private GameListAdapter mAdapter;

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.gamemenu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
////        if (id == R.id.invitedGames){
////            Intent intent = new Intent(GameFinderActivity.this, LobbyViewer.class);
////            startActivity(intent);
////        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
