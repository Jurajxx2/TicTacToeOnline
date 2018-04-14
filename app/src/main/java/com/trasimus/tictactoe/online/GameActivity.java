package com.trasimus.tictactoe.online;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private LinearLayout mLinearLayout;
    private GridLayout mGridLayout;

    private Button[] tictac;
    private String[] game;
    private Button buttons;
    private int childCount;
    private Bundle bundle;
    private GameObject newGame;

    private Random mRandom;
    private Boolean startPlayer;
    private Boolean theOtherPlayer;

    private String player1;
    private String player2;
    private Boolean moveP1;
    private Boolean moveP2;

    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference firstPlayerEventListener;
    private DatabaseReference myRef;
    private DatabaseReference userRef1;
    private DatabaseReference userRef2;


    private String key;

    private List<Button> tictacList;
    private List<String> gameList;

    private GameObject value;

    private TextView players;
    private TextView gameState;

    private ProgressBar loadProgress;
    private ProgressBar gameProgress;
    private Button surrender;

    private UserMap userMap;
    private DefaultUser defaultUser;

    private String playerX;
    private Boolean playerOne;
    private Boolean playerTwo;

    private ArrayList gameContent;

    private ValueEventListener listener1;
    private ValueEventListener gameListener;
    private ValueEventListener userListener1;
    private ValueEventListener userListener2;

    private CountDownTimer mTimer;
    private TextView moveView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.trasimus.tictactoe.online.R.layout.activity_game);


        if (!(firstPlayerEventListener==null || myRef==null)) {
            firstPlayerEventListener.removeEventListener(listener1);
            myRef.removeEventListener(gameListener);
        }


        //Get extra info
        bundle = getIntent().getExtras();
        int gameSize = bundle.getInt("size");

        //Assign views
        surrender = (Button) findViewById(com.trasimus.tictactoe.online.R.id.surrender_btn);
        mLinearLayout = (LinearLayout) findViewById(R.id.linear);
        players = (TextView) findViewById(R.id.players);
        gameState = (TextView) findViewById(R.id.gameState);
        moveView = (TextView) findViewById(R.id.move);
        gameProgress = (ProgressBar) findViewById(R.id.progressBar);
        loadProgress = (ProgressBar) findViewById(R.id.progressBar2);

        //Set properties for game progress bar at the bottom
        gameProgress.setMax(30);

        //Set texts
        players.setText("Waiting for second player");
        gameState.setText("Waiting...");

        //Actual user and database
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        playerX = mFirebaseUser.getUid();


        //Initiate CountDownTimer
        mTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                gameState.setText("Time left: " + millisUntilFinished / 1000);
                int proegress = (int)millisUntilFinished/1000;
                gameProgress.setProgress(30 - proegress);
            }

            @Override
            public void onFinish() {
                if (newGame.getMoveP1()) {
                    newGame.setWinnerP2(true);
                } else if (newGame.getMoveP2()) {
                    newGame.setWinnerP1(true);
                }
                mDatabaseReference.child("games").child(key).child("winnerP1").setValue(newGame.getWinnerP1());
                mDatabaseReference.child("games").child(key).child("winnerP2").setValue(newGame.getWinnerP2());
                surrender.setText("Leave");
                moveView.setText("Game ended");
                gameProgress.setProgress(0);
            }
        };

        //Initiate game layout
        if (gameSize == 3) {
            getLayoutInflater().inflate(R.layout.threexthree, mLinearLayout);
            mGridLayout = (GridLayout) findViewById(R.id.gridLayout);

            tictac = new Button[9];
            game = new String[9];
            childCount = mGridLayout.getChildCount();

            for (int i = 0; i < childCount; i++) {
                View v = mGridLayout.getChildAt(i);
                buttons = (Button) findViewById(v.getId());
                tictac[i] = buttons;
            }

        } else if (gameSize == 4) {
            getLayoutInflater().inflate(R.layout.fourxfour, mLinearLayout);
            mGridLayout = (GridLayout) findViewById(R.id.gridLayout);

            tictac = new Button[16];
            game = new String[16];
            childCount = mGridLayout.getChildCount();

            for (int i = 0; i < childCount; i++) {
                View v = mGridLayout.getChildAt(i);
                buttons = (Button) findViewById(v.getId());
                tictac[i] = buttons;
            }
        } else if (gameSize == 5) {
            getLayoutInflater().inflate(R.layout.fivexfive, mLinearLayout);
            mGridLayout = (GridLayout) findViewById(R.id.gridLayout);

            tictac = new Button[25];
            game = new String[25];
            childCount = mGridLayout.getChildCount();

            for (int i = 0; i < childCount; i++) {
                View v = mGridLayout.getChildAt(i);
                buttons = (Button) findViewById(v.getId());
                tictac[i] = buttons;
            }
        }


        surrender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newGame.getPlayer2().equals("")) {
                    new AlertDialog.Builder(GameActivity.this)
                            .setTitle("Really Exit?")
                            .setMessage("Are you sure you want to exit game finder?")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    GameActivity.super.onBackPressed();
                                }
                            }).create().show();
                } else if(!newGame.getWinnerP1() && !newGame.getWinnerP2()){
                    new AlertDialog.Builder(GameActivity.this)
                            .setTitle("Really Exit?")
                            .setMessage("Are you sure you want to surrender?")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {

                                    if (playerOne) {
                                        newGame.setWinnerP2(true);
                                        mDatabaseReference.child("games").child(key).child("winnerP2").setValue(true);
                                    } else if (playerTwo) {
                                        newGame.setWinnerP1(true);
                                        mDatabaseReference.child("games").child(key).child("winnerP1").setValue(true);
                                    }
                                    GameActivity.super.onBackPressed();
                                }
                            }).create().show();
                } else if(newGame.getWinnerP1() || newGame.getWinnerP2()){
                    finish();
                }
            }
        });

        tictacList = Arrays.asList(tictac);
        gameList = Arrays.asList(game);

        //If player creates the game
        if (bundle.getString("gameID") == null) {

            playerOne = true;
            playerTwo = false;

            key = mDatabaseReference.child("games").push().getKey();

            mRandom = new Random();
            startPlayer = mRandom.nextBoolean();

            theOtherPlayer = !startPlayer;

            newGame = new GameObject(key, mFirebaseUser.getUid(), "", bundle.getInt("size"), startPlayer, theOtherPlayer, startPlayer, 0, 0, false, false, true, false, false, "", "");

            mDatabaseReference.child("games").child(key).setValue(newGame);

            getID(playerX);

            firstPlayerEventListener = mDatabaseReference.child("games").child(key);

            listener1 = firstPlayerEventListener.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newGame = dataSnapshot.getValue(GameObject.class);

                    getPoints();

                    if (newGame.getWinnerP1() || newGame.getWinnerP2()){
                        myRef.removeEventListener(gameListener);
                        surrender.setText("Leave");
                        moveView.setText("Game ended");
                    }

                    if ((!newGame.getPlayer2().equals("")) && (newGame.getMove() == 0)) {
                        Log.d("test", "Player 2 set");
                        players.setText(newGame.getP1name() + " vs " + newGame.getP2name());
                        gameTimer();
                        surrender.setText("Surrender");
                        newGame.setConnectedP2(true);
                        mDatabaseReference.child("games").child(key).child("connectedP2").setValue(newGame.getConnectedP2());
                        loadProgress.setVisibility(View.GONE);
                    }

                    if ((!newGame.getPlayer2().equals(""))) {
                        if (newGame.getMoveP1()) {
                            moveView.setText("Player 1 is on the move");
                        } else if (newGame.getMoveP2()){
                            moveView.setText("Player 2 is on the move");
                        }
                    }

                    if(newGame.getWinnerP1()){stopTimer(); gameState.setText("Player 1 wins");}
                    else if(newGame.getWinnerP2()){stopTimer(); gameState.setText("Player 2 wins");}

                    if (newGame.getDraw()){
                        stopTimer();
                        gameState.setText("No winner");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        } else
        //If player connects to the game
            {

            playerOne = false;
            playerTwo = true;

            newGame = new GameObject();
            key = bundle.getString("gameID");

            mDatabaseReference.child("games").child(key).child("player2").setValue(playerX);

            getID(playerX);

            firstPlayerEventListener = mDatabaseReference.child("games").child(key);

            listener1 = firstPlayerEventListener.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newGame = dataSnapshot.getValue(GameObject.class);

                    getPoints();

                    players.setText(newGame.getP1name() + " vs " + newGame.getP2name());

                    if (newGame.getMove()==0){
                        surrender.setText("Surrender");
                    }

                    if (newGame.getWinnerP1() || newGame.getWinnerP2()){
                        myRef.removeEventListener(gameListener);
                        surrender.setText("Leave");
                        moveView.setText("Game ended");
                    }

                    if ((!newGame.getPlayer2().equals(""))) {
                        if (newGame.getMoveP1()) {
                            moveView.setText("Player 1 is on the move");
                        } else if (newGame.getMoveP2()){
                            moveView.setText("Player 2 is on the move");
                        }
                    }

                    if(newGame.getWinnerP1()){stopTimer(); gameState.setText("Player 1 wins");}
                    else if(newGame.getWinnerP2()){stopTimer(); gameState.setText("Player 2 wins");}

                    if (newGame.getDraw()){
                        stopTimer();
                        gameState.setText("No winner");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        //Listener for GameList child
        myRef = mDatabaseReference.child("games").child(key).child("gameList");

        gameListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gameContent = (ArrayList<Object>) dataSnapshot.getValue();

                newGame.setGameList(gameContent);

                if(newGame.getMove() != 0) {
                    restartTimer();
                }

                for (int i = 0; i < gameContent.size(); i++) {
                    Log.d("test", "testAlpha" + gameContent.get(i));
                    Log.d("test", "testBeta" + newGame.getGameList().get(i));

                    if (gameContent.get(i).equals("x")) {
                        Log.d("test", "ALPHA + " + gameContent.get(i));
                        tictacList.get(i).setBackgroundResource(R.drawable.x);
                    }
                    if (gameContent.get(i).equals("o")) {
                        Log.d("test", "ALPHA + " + gameContent.get(i));
                        tictacList.get(i).setBackgroundResource(R.drawable.o);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //Work with timer
    private void gameTimer() {
        mTimer.start();
    }

    private void restartTimer() {
        if (mTimer == null) {
            return;
        }
        mTimer.cancel();
        mTimer.start();
    }

    private void stopTimer() {
        if (mTimer == null) {
            return;
        }
        Log.d("test", "Stop timer");
        mTimer.cancel();
    }

    private void startTimer() {
        if (mTimer == null) {
            return;
        }
        mTimer.start();
    }




    //On click method, when user clicks on btn in the game
    public void doSomething(View view) {

        //If any player won, game ended
        if (newGame.getWinnerP1() || newGame.getWinnerP2()) {
            return;
        }

        //if player 2 is not present, game does not begin
        if (newGame.getPlayer2().equals("")) {
            return;
        }


        //If player one is not on the move, he cannot click
        if (playerOne) {
            if (!newGame.getMoveP1()) {
                return;
            }
        }
        //If player two is not on the move, he cannot click
        if (playerTwo) {
            if (!newGame.getMoveP2()) {
                return;
            }
        }

        //If game field has already been clicked before, it cannot be changed
        for (int i = 0; i < newGame.getGameList().size(); i++) {
            if (tictacList.get(i).getId() == view.getId()) {
                if (!newGame.getGameList().get(i).equals("")) {
                    return;
                }
            }
        }

        //Clicked button function, move function, then update database
        newGame.clickedButton(view, tictacList, playerOne);
        newGame.move();

        mDatabaseReference.child("games").child(key).child("move").setValue(newGame.getMove());

        mDatabaseReference.child("games").child(key).child("viewID").setValue(view.getId());
        mDatabaseReference.child("games").child(key).child("gameList").setValue(newGame.getGameList());


        mDatabaseReference.child("games").child(key).child("moveP1").setValue(newGame.getMoveP1());
        mDatabaseReference.child("games").child(key).child("moveP2").setValue(newGame.getMoveP2());
        mDatabaseReference.child("games").child(key).child("winnerP1").setValue(newGame.getWinnerP1());
        mDatabaseReference.child("games").child(key).child("winnerP2").setValue(newGame.getWinnerP2());

        //If any player won, listener for GameField will be disabled
        if (newGame.getWinnerP1() || newGame.getWinnerP2()) {
            myRef.removeEventListener(gameListener);
        }

        //If all fields are full, no player wins
        if((newGame.getMove() == newGame.getGameList().size()) && (!newGame.getWinnerP1() && !newGame.getWinnerP2())){
            newGame.setDraw(true);
            mDatabaseReference.child("games").child(key).child("draw").setValue(newGame.getDraw());
        }


        Toast.makeText(this, "Button clicked with id " + view.getId(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        if (newGame.getPlayer2().equals("")) {
            new AlertDialog.Builder(GameActivity.this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit game finder?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            GameActivity.super.onBackPressed();
                        }
                    }).create().show();
        } else if(!newGame.getWinnerP1() && !newGame.getWinnerP2()){
            new AlertDialog.Builder(GameActivity.this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to surrender?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {

                            if (playerOne) {
                                newGame.setWinnerP2(true);
                                mDatabaseReference.child("games").child(key).child("winnerP2").setValue(true);
                            } else if (playerTwo) {
                                newGame.setWinnerP1(true);
                                mDatabaseReference.child("games").child(key).child("winnerP1").setValue(true);
                            }
                            GameActivity.super.onBackPressed();
                        }
                    }).create().show();
        } else if(newGame.getWinnerP1() || newGame.getWinnerP2()){
            finish();
        }
    }

    @Override
    protected void onStop() {

        if (playerOne && newGame.getPlayer2().equals("")){
            mDatabaseReference.child("games").child(key).removeValue();
        }
        else if ((!newGame.getPlayer2().equals("")) && ((newGame.getConnectedP1() && !newGame.getConnectedP2())|| (!newGame.getConnectedP1() && newGame.getConnectedP2())) ){
            mDatabaseReference.child("games").child(key).removeValue();
        } else if(playerOne){
            mDatabaseReference.child("games").child(key).child("connectedP1").setValue(false);
        } else if(playerTwo){
            mDatabaseReference.child("games").child(key).child("connectedP2").setValue(false);
        }
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if(playerOne){
            mDatabaseReference.child("games").child(key).child("connectedP1").setValue(true);
        } else if(playerTwo){
            mDatabaseReference.child("games").child(key).child("connectedP2").setValue(true);
        }

//        if (playerOne) {
//            listener1 = firstPlayerEventListener.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    newGame = dataSnapshot.getValue(GameObject.class);
//
//                    if (newGame.getWinnerP1() || newGame.getWinnerP2()) {
//                        myRef.removeEventListener(gameListener);
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                }
//            });
//        } else if(playerTwo){
//            listener1 = firstPlayerEventListener.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    newGame = dataSnapshot.getValue(GameObject.class);
//
//                    players.setText(newGame.getPlayer1() + " vs " + newGame.getPlayer2());
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }
    }

    private void getPoints(){
        if (playerOne) {
            if (newGame.getWinnerP1()) {
                mDatabaseReference.child("Users").child(userMap.getUserID()).child("points").setValue(defaultUser.getPoints() + 3);
            }
            else if (newGame.getDraw()){
                mDatabaseReference.child("Users").child(userMap.getUserID()).child("points").setValue(defaultUser.getPoints() + 1);
            }
        }
        else if (playerTwo){
            if (newGame.getWinnerP2()) {
                mDatabaseReference.child("Users").child(userMap.getUserID()).child("points").setValue(defaultUser.getPoints() + 3);
            }
            else if (newGame.getDraw()){
                mDatabaseReference.child("Users").child(userMap.getUserID()).child("points").setValue(defaultUser.getPoints() + 1);
            }
        }
    }

    private void getID(String playerUID){
        userRef1 = mDatabaseReference.child("UserMap").child(playerUID);
        userRef1.addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void getUserInfo(){
        userRef2 = mDatabaseReference.child("Users").child(userMap.getUserID());
        userRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                defaultUser = dataSnapshot.getValue(DefaultUser.class);
                if (playerOne) {
                    mDatabaseReference.child("games").child(key).child("p1name").setValue(defaultUser.getName());
                } else if(playerTwo){
                    mDatabaseReference.child("games").child(key).child("p2name").setValue(defaultUser.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
