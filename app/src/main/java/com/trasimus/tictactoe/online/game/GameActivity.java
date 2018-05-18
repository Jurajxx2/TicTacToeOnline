package com.trasimus.tictactoe.online.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.trasimus.tictactoe.online.GameObject;
import com.trasimus.tictactoe.online.R;
import com.trasimus.tictactoe.online.UserMap;
import com.trasimus.tictactoe.online.account.AccountActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    //TODO Remove lobby, add chat support to games, like in 8ball pool, where cloud with message text appears
    //TODO Work with player 1 and player 2, edit this in top
    private LinearLayout mLinearLayout;
    private GridLayout mGridLayout;
    private boolean isDeleted;
    private boolean otherPlayer = false;
    private boolean paused = false;

    private Button[] tictac;
    private String[] game;
    private Button buttons;
    private int childCount;
    private Bundle bundle;
    private GameObject newGame;

    private Random mRandom;
    private Boolean startPlayer;
    private Boolean theOtherPlayer;

    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference firstPlayerEventListener;
    private DatabaseReference myRef;
    private DatabaseReference userRef1;
    private DatabaseReference userRef2;
    private DatabaseReference userRef3;

    private String key;

    private List<Button> tictacList;
    private List<String> gameList;

    private TextView players;
    private TextView gameState;

    private ProgressBar loadProgress;
    private ProgressBar gameProgress;
    private Button surrender;
    private ImageView surrenderIMG;
    private ImageView pauseIMG;
    private ImageView chatIMG;

    private ImageView profile1;
    private ImageView profile2;

    private UserMap userMap;
    private DefaultUser defaultUser;
    private DefaultUser defaultUser2;

    private String playerX;
    private Boolean playerOne;
    private Boolean playerTwo;

    private ArrayList gameContent;

    private ValueEventListener listener1;
    private ValueEventListener gameListener;

    private CountDownTimer mTimer;
    private TextView moveView;

    private ArrayList<ArrayList<String>> messages;
    private TextView count;

    private EditText editTxt;
    private Button sendBtn;
    private ListView showMsg;
    private ArrayList<String> singleMessage;
    private DatabaseReference adapterDatabaseReference;
    private MessageAdapter mAdapter;

    private int r=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.trasimus.tictactoe.online.R.layout.activity_game);

        //Initiate arraylist of arraylist messages and single message
        messages = new ArrayList<ArrayList<String>>();
        singleMessage = new ArrayList<String>();

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
        profile1 = (ImageView) findViewById(R.id.profile);
        profile2 = (ImageView) findViewById(R.id.profile2);

        surrenderIMG = (ImageView) findViewById(R.id.exitIMG);
        chatIMG = (ImageView) findViewById(R.id.chatIMG);
        pauseIMG = (ImageView) findViewById(R.id.pauseIMG);
        count = (TextView) findViewById(R.id.counter);

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
                int proegress = (int) millisUntilFinished / 1000;
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

        surrenderIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                } else if (!newGame.getWinnerP1() && !newGame.getWinnerP2()) {
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
                } else if (newGame.getWinnerP1() || newGame.getWinnerP2() || newGame.getDraw()) {
                    finish();
                }
            }
        });

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
                } else if (!newGame.getWinnerP1() && !newGame.getWinnerP2()) {
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
                } else if (newGame.getWinnerP1() || newGame.getWinnerP2() || newGame.getDraw()) {
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
            key = bundle.getString("key");
            mRandom = new Random();
            startPlayer = mRandom.nextBoolean();
            theOtherPlayer = !startPlayer;

            newGame = new GameObject(key, mFirebaseUser.getUid(), "", bundle.getInt("size"), startPlayer, theOtherPlayer, startPlayer, 0, 0, false, false, true, false, false, "", "", false, false, false, messages);
            mDatabaseReference.child("games").child(key).setValue(newGame);

            getID(playerX);

            firstPlayerEventListener = mDatabaseReference.child("games").child(key);

            listener1 = firstPlayerEventListener.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newGame = dataSnapshot.getValue(GameObject.class);

                    if (newGame.getGameList() == null) {
                        firstPlayerEventListener.removeEventListener(listener1);
                        new AlertDialog.Builder(GameActivity.this)
                                .setTitle("Game not found")
                                .setMessage("Game you want to join was not found")
                                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        finish();
                                    }
                                })
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0, int arg1) {
                                        finish();
                                    }
                                }).create().show();
                    }

                    if (newGame.isP1paused() && newGame.isP2paused()){
                        count.setText("2/2");
                    } else if ((newGame.isP1paused() && !newGame.isP2paused()) || (!newGame.isP1paused() && newGame.isP2paused())){
                        count.setText("1/2");
                    } else if (!newGame.isP1paused() && !newGame.isP2paused()){
                        count.setText("0/2");
                    }


                    if ((!newGame.getPlayer2().equals("")) && (newGame.getMove() == 0) && newGame.getConnectedP2()) {
                        Log.d("test", "Player 2 set");
                        players.setText(newGame.getP1name() + " vs " + newGame.getP2name());
                        gameTimer();
                        surrender.setText("Surrender");
                        newGame.setConnectedP2(true);
                        mDatabaseReference.child("games").child(key).child("connectedP2").setValue(newGame.getConnectedP2());
                        loadProgress.setVisibility(View.GONE);
                        otherPlayer(newGame.getPlayer2());
                    }

                    if ((!newGame.getPlayer2().equals(""))) {
                        if (newGame.getMoveP1()) {
                            moveView.setText("Player 1 is on the move");
                        } else if (newGame.getMoveP2()) {
                            moveView.setText("Player 2 is on the move");
                        }
                    }

                    if (newGame.getWinnerP1()) {
                        gameState.setText("Player 1 wins");
                    } else if (newGame.getWinnerP2()) {
                        gameState.setText("Player 2 wins");
                    }

                    if (newGame.getDraw()) {
                        gameState.setText("No winner");
                    }

                    if (newGame.getWinnerP1() || newGame.getWinnerP2() || newGame.getDraw()) {
                        myRef.removeEventListener(gameListener);
                        stopTimer();
                        surrender.setText("Leave");
                        moveView.setText("Game ended");
                        firstPlayerEventListener.removeEventListener(listener1);
                    }


                    if (newGame.getWinnerP1() || newGame.getWinnerP2() || newGame.getDraw()) {
                        givePoints();
                        mDatabaseReference.child("games").child(key).child("player1").setValue("");
                        mDatabaseReference.child("games").child(key).child("player2").setValue("");
                        mDatabaseReference.child("games").child(key).child("isDeleted").setValue(true);
                        String message = null;
                        if (newGame.getWinnerP1()) {
                            message = "Player " + newGame.getP1name() + " won";
                        } else if (newGame.getWinnerP2()) {
                            message = "Player " + newGame.getP2name() + " won. Better luck next time";
                        } else if (newGame.getDraw()) {
                            message = "No player won";
                        }
                        new AlertDialog.Builder(GameActivity.this)
                                .setTitle("Game ended")
                                .setMessage(message)
                                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        finish();
                                    }
                                })
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0, int arg1) {
                                        finish();
                                    }
                                }).create().show();
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
            getID(playerX);

            firstPlayerEventListener = mDatabaseReference.child("games").child(key);

            listener1 = firstPlayerEventListener.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newGame = dataSnapshot.getValue(GameObject.class);

                    if ((newGame.getPlayer2().equals("") && r==1) || (!newGame.getPlayer2().equals(defaultUser.getUserID()) && r==1)){
                        new AlertDialog.Builder(GameActivity.this)
                                .setTitle("Game session ended")
                                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        finish();
                                    }
                                })
                                .setMessage("Game you want to connect is no longer available")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0, int arg1) {
                                        finish();
                                    }
                                }).create().show();
                    }

                    if (newGame.getMove() == 0 && !newGame.getWinnerP1() && !newGame.getWinnerP2() && r==0) {
                        r++;
                        mDatabaseReference.child("games").child(key).child("player2").setValue(playerX);
                        mDatabaseReference.child("games").child(key).child("connectedP2").setValue(true);
                        surrender.setText("Surrender");
                        gameTimer();
                        loadProgress.setVisibility(View.GONE);
                        otherPlayer(newGame.getPlayer1());
                        players.setText(newGame.getP2name() + " vs " + newGame.getP1name());
                    }


                    if (newGame.isP1paused() && newGame.isP2paused()){
                        count.setText("2/2");
                    } else if ((newGame.isP1paused() && !newGame.isP2paused()) || (!newGame.isP1paused() && newGame.isP2paused())){
                        count.setText("1/2");
                    } else if (!newGame.isP1paused() && !newGame.isP2paused()){
                        count.setText("0/2");
                    }

                    if ((!newGame.getPlayer2().equals(""))) {
                        if (newGame.getMoveP1()) {
                            moveView.setText("Player 1 is on the move");
                        } else if (newGame.getMoveP2()) {
                            moveView.setText("Player 2 is on the move");
                        }
                    }

                    if (newGame.getWinnerP1()) {
                        gameState.setText("Player 1 wins");
                    } else if (newGame.getWinnerP2()) {
                        gameState.setText("Player 2 wins");
                    }

                    if (newGame.getDraw()) {
                        gameState.setText("No winner");
                    }

                    if (newGame.getWinnerP1() || newGame.getWinnerP2() || newGame.getDraw()) {
                        myRef.removeEventListener(gameListener);
                        stopTimer();
                        surrender.setText("Leave");
                        moveView.setText("Game ended");
                        firstPlayerEventListener.removeEventListener(listener1);
                    }


                    if (newGame.getWinnerP1() || newGame.getWinnerP2() || newGame.getDraw()) {
                        givePoints();
                        mDatabaseReference.child("games").child(key).child("player1").setValue("");
                        mDatabaseReference.child("games").child(key).child("player2").setValue("");
                        mDatabaseReference.child("games").child(key).child("isDeleted").setValue(true);
                        String message = null;
                        if (newGame.getWinnerP1()) {
                            message = "Player " + newGame.getP1name() + " won. Better luck next time";
                        } else if (newGame.getWinnerP2()) {
                            message = "You won!";
                        } else if (newGame.getDraw()) {
                            message = "No player won";
                        }
                        new AlertDialog.Builder(GameActivity.this)
                                .setTitle("Game ended")
                                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        finish();
                                    }
                                })
                                .setMessage(message)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0, int arg1) {
                                        finish();
                                    }
                                }).create().show();
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

                if (newGame.getPlayer2().equals("")) {
                    return;
                }
                newGame.setGameList(gameContent);

                if (newGame.getMove() != 0 && (newGame.getConnectedP1() && newGame.getConnectedP2())) {
                    Log.d("test", "Timer restarted, move: " + newGame.getMove());
                    restartTimer();
                }

                for (int i = 0; i < gameContent.size(); i++) {
                    //Log.d("test", "testAlpha" + gameContent.get(i));
                    //Log.d("test", "testBeta" + newGame.getGameList().get(i));

                    if (gameContent.get(i).equals("x")) {
                        //Log.d("test", "ALPHA + " + gameContent.get(i));
                        tictacList.get(i).setBackgroundResource(R.drawable.x);
                    }
                    if (gameContent.get(i).equals("o")) {
                        //Log.d("test", "ALPHA + " + gameContent.get(i));
                        tictacList.get(i).setBackgroundResource(R.drawable.o);
                    }
                }

                checkWinner(gameContent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Chat listener
        chatIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GameActivity.this);
                final LayoutInflater inflater = GameActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.chat_window, null);
                dialogBuilder.setView(dialogView);

                //final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

                dialogBuilder.setTitle("Chat");
                //dialogBuilder.setMessage("Enter text below");

                AlertDialog b = dialogBuilder.create();
                b.show();

                editTxt = dialogView.findViewById(R.id.editTxt);
                sendBtn = dialogView.findViewById(R.id.sendBtn);
                showMsg = dialogView.findViewById(R.id.showMessage);


                sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editTxt.getText().toString().equals("")) {
                            return;
                        }
                        if (editTxt.getText().toString().length() > 128) {
                            Toast.makeText(GameActivity.this, "Too long message(MAX 128 characters)", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        singleMessage.add(playerOne.toString());
                        singleMessage.add(playerTwo.toString());
                        singleMessage.add(editTxt.getText().toString());
                        if (newGame.getMessages() != null) {
                            messages = newGame.getMessages();
                        }
                        messages.add(singleMessage);
                        newGame.setMessages(messages);

                        mDatabaseReference.child("games").child(key).child("messages").setValue(newGame.getMessages());

                        editTxt.setText("");

                        singleMessage.clear();
                        messages.clear();
                    }
                });
            }
        });

        //Pause listener
        pauseIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!paused) {
                    paused = true;
                    pauseIMG.setImageResource(R.drawable.play);
                    if (playerOne){
                        mDatabaseReference.child("games").child(key).child("p1paused").setValue(true);
                    } else if (playerTwo){
                        mDatabaseReference.child("games").child(key).child("p2paused").setValue(true);
                    }
                } else if (paused){
                    paused = false;
                    pauseIMG.setImageResource(R.drawable.pause);
                    if (playerOne){
                        mDatabaseReference.child("games").child(key).child("p1paused").setValue(false);
                    } else if (playerTwo){
                        mDatabaseReference.child("games").child(key).child("p2paused").setValue(false);
                    }
                }
            }
        });
    }


    //Work with timer
    private void gameTimer() {
        mTimer.start();
    }

    private void restartTimer() {
        if (mTimer == null) { return; }
        mTimer.cancel();
        mTimer.start();
    }

    private void stopTimer() {
        if (mTimer == null) { return; }
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

        if (otherPlayer){
            return;
        }

        //If game is paused
        if (newGame.isP1paused() && newGame.isP2paused()){
            return;
        }

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
        if ((newGame.getMove() == newGame.getGameList().size()) && (!newGame.getWinnerP1() && !newGame.getWinnerP2())) {
            newGame.setDraw(true);
            mDatabaseReference.child("games").child(key).child("draw").setValue(newGame.getDraw());
        }
    }


    @Override
    public void onBackPressed() {
        if (otherPlayer){
            GameActivity.super.onBackPressed();
        } else if (newGame.getPlayer2().equals("")) {
            new AlertDialog.Builder(GameActivity.this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit game finder?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            GameActivity.super.onBackPressed();
                        }
                    }).create().show();
        } else if (!newGame.getWinnerP1() && !newGame.getWinnerP2()) {
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
        } else if (newGame.getWinnerP1() || newGame.getWinnerP2() || newGame.getDraw()) {
            finish();
        }
    }

    @Override
    protected void onStop() {
        if (otherPlayer){
            super.onStop();
        } else if (playerOne) {
            mDatabaseReference.child("games").child(key).child("connectedP1").setValue(false);
            if (newGame.getPlayer2().equals("")  && !newGame.getWinnerP1() && !newGame.getWinnerP2() && !newGame.getDraw()) {
                firstPlayerEventListener.removeEventListener(listener1);
                mDatabaseReference.child("games").child(key).child("connectedP1").setValue(false);
                mDatabaseReference.child("games").child(key).child("player1").setValue("");
                mDatabaseReference.child("games").child(key).child("player2").setValue("");
                mDatabaseReference.child("games").child(key).child("isDeleted").setValue(true);
                newGame.setDeleted(true);
                isDeleted = true;
                super.onStop();
            }
            if (!newGame.getPlayer2().equals("") && !newGame.getConnectedP2()  && !newGame.getWinnerP1() && !newGame.getWinnerP2() && !newGame.getDraw()) {
                firstPlayerEventListener.removeEventListener(listener1);
                mDatabaseReference.child("games").child(key).child("connectedP1").setValue(false);
                mDatabaseReference.child("games").child(key).child("player1").setValue("");
                mDatabaseReference.child("games").child(key).child("player2").setValue("");
                super.onStop();
            }
            if (newGame.getConnectedP2()  && !newGame.getWinnerP1() && !newGame.getWinnerP2() && !newGame.getDraw()) {
                Log.d("test", "Player disconnected");
                mDatabaseReference.child("games").child(key).child("connectedP1").setValue(false);
            }
        } else if (playerTwo && !newGame.getWinnerP1() && !newGame.getWinnerP2() && !newGame.getDraw()) {
            mDatabaseReference.child("games").child(key).child("connectedP2").setValue(false);
            if (!newGame.getConnectedP1()) {
                firstPlayerEventListener.removeEventListener(listener1);
                mDatabaseReference.child("games").child(key).child("player1").setValue("");
                mDatabaseReference.child("games").child(key).child("player2").setValue("");
                mDatabaseReference.child("games").child(key).child("connectedP2").setValue(false);
                super.onStop();
            } else if (!newGame.getWinnerP1() && !newGame.getWinnerP2() && !newGame.getDraw()){
                Log.d("test", "Player disconnected");
                mDatabaseReference.child("games").child(key).child("connectedP2").setValue(false);
            }
        }
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (otherPlayer){
            finish();
        } else if (playerOne) {
            Log.d("test", "Player connected");
            if (!newGame.getWinnerP1() && !newGame.getWinnerP2() && !newGame.getDraw()) {
                mDatabaseReference.child("games").child(key).child("connectedP1").setValue(true);
            }
        } else if (playerTwo) {
            Log.d("test", "Player connected");
            if (!newGame.getWinnerP1() && !newGame.getWinnerP2() && !newGame.getDraw()) {
                mDatabaseReference.child("games").child(key).child("connectedP2").setValue(true);
            }
        }
    }

    private void givePoints() {
        if (playerOne) {
            mDatabaseReference.child("Users").child(userMap.getUserID()).child("gamesPlayed").setValue(defaultUser.getGamesPlayed() + 1);
            if (newGame.getWinnerP1()) {
                mDatabaseReference.child("Users").child(userMap.getUserID()).child("points").setValue(defaultUser.getPoints() + 3);
                mDatabaseReference.child("Users").child(userMap.getUserID()).child("gamesWon").setValue(defaultUser.getGamesWon() + 1);
            } else if (newGame.getDraw()) {
                mDatabaseReference.child("Users").child(userMap.getUserID()).child("points").setValue(defaultUser.getPoints() + 1);
            } else if (newGame.getWinnerP2()){
                mDatabaseReference.child("Users").child(userMap.getUserID()).child("gamesLost").setValue(defaultUser.getGamesLost() + 1);
            }
        } else if (playerTwo) {
            mDatabaseReference.child("Users").child(userMap.getUserID()).child("gamesPlayed").setValue(defaultUser.getGamesPlayed() + 1);
            if (newGame.getWinnerP2()) {
                mDatabaseReference.child("Users").child(userMap.getUserID()).child("points").setValue(defaultUser.getPoints() + 3);
            } else if (newGame.getDraw()) {
                mDatabaseReference.child("Users").child(userMap.getUserID()).child("points").setValue(defaultUser.getPoints() + 1);
            } else if (newGame.getWinnerP1()){
                mDatabaseReference.child("Users").child(userMap.getUserID()).child("gamesLost").setValue(defaultUser.getGamesLost() + 1);
            }
        }
    }

    private void getID(String playerUID) {
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

    private void getUserInfo() {
        userRef2 = mDatabaseReference.child("Users").child(userMap.getUserID());
        userRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                defaultUser = dataSnapshot.getValue(DefaultUser.class);
                if (playerOne) {
                    mDatabaseReference.child("games").child(key).child("p1name").setValue(defaultUser.getName());
                } else if (playerTwo) {
                    mDatabaseReference.child("games").child(key).child("p2name").setValue(defaultUser.getName());
                }
                if (defaultUser.getPhotoID() != null) {
                    if (defaultUser.getPhotoID().equals("grumpy")) {
                        profile1.setImageResource(R.drawable.grumpy);
                    }
                    if (defaultUser.getPhotoID().equals("kon")) {
                        profile1.setImageResource(R.drawable.kon);
                    }
                    if (defaultUser.getPhotoID().equals("opica")) {
                        profile1.setImageResource(R.drawable.opica);
                    }
                    if (defaultUser.getPhotoID().equals("0")) {
                        profile1.setImageResource(R.drawable.icon_profile_empty);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void otherPlayer(String userid) {
        userRef3 = mDatabaseReference.child("Users").child(userid);
        userRef3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                defaultUser2 = dataSnapshot.getValue(DefaultUser.class);
                if (defaultUser2.getPhotoID() != null) {
                    if (defaultUser2.getPhotoID().equals("grumpy")) {
                        profile2.setImageResource(R.drawable.grumpy);
                    }
                    if (defaultUser2.getPhotoID().equals("kon")) {
                        profile2.setImageResource(R.drawable.kon);
                    }
                    if (defaultUser2.getPhotoID().equals("opica")) {
                        profile2.setImageResource(R.drawable.opica);
                    }
                    if (defaultUser2.getPhotoID().equals("0")) {
                        profile2.setImageResource(R.drawable.icon_profile_empty);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void checkWinner(ArrayList<String> gameList) {

        if (newGame.getSize() == 3) {

            if (gameList.get(0).equals("x") && gameList.get(1).equals("x") && gameList.get(2).equals("x")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(3).equals("x") && gameList.get(4).equals("x") && gameList.get(5).equals("x")) {
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(6).equals("x") && gameList.get(7).equals("x") && gameList.get(8).equals("x")) {
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(0).equals("x") && gameList.get(3).equals("x") && gameList.get(6).equals("x")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(1).equals("x") && gameList.get(4).equals("x") && gameList.get(7).equals("x")) {
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(2).equals("x") && gameList.get(5).equals("x") && gameList.get(8).equals("x")) {
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(0).equals("x") && gameList.get(4).equals("x") && gameList.get(8).equals("x")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(2).equals("x") && gameList.get(4).equals("x") && gameList.get(6).equals("x")) {
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }


            if (gameList.get(0).equals("o") && gameList.get(1).equals("o") && gameList.get(2).equals("o")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(3).equals("o") && gameList.get(4).equals("o") && gameList.get(5).equals("o")) {
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(6).equals("o") && gameList.get(7).equals("o") && gameList.get(8).equals("o")) {
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(0).equals("o") && gameList.get(3).equals("o") && gameList.get(6).equals("o")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(1).equals("o") && gameList.get(4).equals("o") && gameList.get(7).equals("o")) {
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(2).equals("o") && gameList.get(5).equals("o") && gameList.get(8).equals("o")) {
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(0).equals("o") && gameList.get(4).equals("o") && gameList.get(8).equals("o")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(2).equals("o") && gameList.get(4).equals("o") && gameList.get(6).equals("o")) {
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
        }


        if (newGame.getSize() == 4) {

            if (gameList.get(0).equals("x") && gameList.get(1).equals("x") && gameList.get(2).equals("x") && gameList.get(3).equals("x")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(4).equals("x") && gameList.get(5).equals("x") && gameList.get(6).equals("x") && gameList.get(7).equals("x")) {
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(8).equals("x") && gameList.get(9).equals("x") && gameList.get(10).equals("x") && gameList.get(11).equals("x")) {
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(11).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(12).equals("x") && gameList.get(13).equals("x") && gameList.get(14).equals("x") && gameList.get(15).equals("x")) {
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(13).setBackgroundColor(0xFF00FF00);
                tictacList.get(14).setBackgroundColor(0xFF00FF00);
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(0).equals("x") && gameList.get(4).equals("x") && gameList.get(8).equals("x") && gameList.get(12).equals("x")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(1).equals("x") && gameList.get(5).equals("x") && gameList.get(9).equals("x") && gameList.get(13).equals("x")) {
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                tictacList.get(13).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(2).equals("x") && gameList.get(6).equals("x") && gameList.get(10).equals("x") && gameList.get(14).equals("x")) {
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(14).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(3).equals("x") && gameList.get(7).equals("x") && gameList.get(11).equals("x") && gameList.get(15).equals("x")) {
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                tictacList.get(11).setBackgroundColor(0xFF00FF00);
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(0).equals("x") && gameList.get(5).equals("x") && gameList.get(10).equals("x") && gameList.get(15).equals("x")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(3).equals("x") && gameList.get(6).equals("x") && gameList.get(9).equals("x") && gameList.get(12).equals("x")) {
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }


            if (gameList.get(0).equals("o") && gameList.get(1).equals("o") && gameList.get(2).equals("o") && gameList.get(3).equals("o")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(4).equals("o") && gameList.get(5).equals("o") && gameList.get(6).equals("o") && gameList.get(7).equals("o")) {
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(8).equals("o") && gameList.get(9).equals("o") && gameList.get(10).equals("o") && gameList.get(11).equals("o")) {
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(11).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(12).equals("o") && gameList.get(13).equals("o") && gameList.get(14).equals("o") && gameList.get(15).equals("o")) {
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(13).setBackgroundColor(0xFF00FF00);
                tictacList.get(14).setBackgroundColor(0xFF00FF00);
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(0).equals("o") && gameList.get(4).equals("o") && gameList.get(8).equals("o") && gameList.get(12).equals("o")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(1).equals("o") && gameList.get(5).equals("o") && gameList.get(9).equals("o") && gameList.get(13).equals("o")) {
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                tictacList.get(13).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(2).equals("o") && gameList.get(6).equals("o") && gameList.get(10).equals("o") && gameList.get(14).equals("o")) {
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(14).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(3).equals("o") && gameList.get(7).equals("o") && gameList.get(11).equals("o") && gameList.get(15).equals("o")) {
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                tictacList.get(11).setBackgroundColor(0xFF00FF00);
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(0).equals("o") && gameList.get(5).equals("o") && gameList.get(10).equals("o") && gameList.get(15).equals("o")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(3).equals("o") && gameList.get(6).equals("o") && gameList.get(9).equals("o") && gameList.get(12).equals("o")) {
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }

        }


        if (newGame.getSize() == 5) {

            if (gameList.get(0).equals("x") && gameList.get(1).equals("x") && gameList.get(2).equals("x") && gameList.get(3).equals("x") && gameList.get(4).equals("x")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(5).equals("x") && gameList.get(6).equals("x") && gameList.get(7).equals("x") && gameList.get(8).equals("x") && gameList.get(9).equals("x")) {
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(10).equals("x") && gameList.get(11).equals("x") && gameList.get(12).equals("x") && gameList.get(13).equals("x") && gameList.get(14).equals("x")) {
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(11).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(13).setBackgroundColor(0xFF00FF00);
                tictacList.get(14).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(15).equals("x") && gameList.get(16).equals("x") && gameList.get(17).equals("x") && gameList.get(18).equals("x") && gameList.get(19).equals("x")) {
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                tictacList.get(16).setBackgroundColor(0xFF00FF00);
                tictacList.get(17).setBackgroundColor(0xFF00FF00);
                tictacList.get(18).setBackgroundColor(0xFF00FF00);
                tictacList.get(19).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(20).equals("x") && gameList.get(21).equals("x") && gameList.get(22).equals("x") && gameList.get(23).equals("x") && gameList.get(24).equals("x")) {
                tictacList.get(20).setBackgroundColor(0xFF00FF00);
                tictacList.get(21).setBackgroundColor(0xFF00FF00);
                tictacList.get(22).setBackgroundColor(0xFF00FF00);
                tictacList.get(23).setBackgroundColor(0xFF00FF00);
                tictacList.get(24).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(0).equals("x") && gameList.get(5).equals("x") && gameList.get(10).equals("x") && gameList.get(15).equals("x") && gameList.get(20).equals("x")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                tictacList.get(20).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(1).equals("x") && gameList.get(6).equals("x") && gameList.get(11).equals("x") && gameList.get(16).equals("x") && gameList.get(21).equals("x")) {
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(11).setBackgroundColor(0xFF00FF00);
                tictacList.get(16).setBackgroundColor(0xFF00FF00);
                tictacList.get(21).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(2).equals("x") && gameList.get(7).equals("x") && gameList.get(12).equals("x") && gameList.get(17).equals("x") && gameList.get(22).equals("x")) {
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(17).setBackgroundColor(0xFF00FF00);
                tictacList.get(22).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(3).equals("x") && gameList.get(8).equals("x") && gameList.get(13).equals("x") && gameList.get(18).equals("x") && gameList.get(23).equals("x")) {
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(13).setBackgroundColor(0xFF00FF00);
                tictacList.get(18).setBackgroundColor(0xFF00FF00);
                tictacList.get(23).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(4).equals("x") && gameList.get(9).equals("x") && gameList.get(14).equals("x") && gameList.get(19).equals("x") && gameList.get(24).equals("x")) {
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                tictacList.get(14).setBackgroundColor(0xFF00FF00);
                tictacList.get(19).setBackgroundColor(0xFF00FF00);
                tictacList.get(24).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(0).equals("x") && gameList.get(6).equals("x") && gameList.get(12).equals("x") && gameList.get(18).equals("x") && gameList.get(24).equals("x")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(18).setBackgroundColor(0xFF00FF00);
                tictacList.get(24).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(4).equals("x") && gameList.get(8).equals("x") && gameList.get(12).equals("x") && gameList.get(16).equals("x") && gameList.get(20).equals("x")) {
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(16).setBackgroundColor(0xFF00FF00);
                tictacList.get(20).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }


            if (gameList.get(0).equals("o") && gameList.get(1).equals("o") && gameList.get(2).equals("o") && gameList.get(3).equals("o") && gameList.get(4).equals("o")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(5).equals("o") && gameList.get(6).equals("o") && gameList.get(7).equals("o") && gameList.get(8).equals("o") && gameList.get(9).equals("o")) {
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(10).equals("o") && gameList.get(11).equals("o") && gameList.get(12).equals("o") && gameList.get(13).equals("o") && gameList.get(14).equals("o")) {
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(11).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(13).setBackgroundColor(0xFF00FF00);
                tictacList.get(14).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(15).equals("o") && gameList.get(16).equals("o") && gameList.get(17).equals("o") && gameList.get(18).equals("o") && gameList.get(19).equals("o")) {
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                tictacList.get(16).setBackgroundColor(0xFF00FF00);
                tictacList.get(17).setBackgroundColor(0xFF00FF00);
                tictacList.get(18).setBackgroundColor(0xFF00FF00);
                tictacList.get(19).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(20).equals("o") && gameList.get(21).equals("o") && gameList.get(22).equals("o") && gameList.get(23).equals("o") && gameList.get(24).equals("o")) {
                tictacList.get(20).setBackgroundColor(0xFF00FF00);
                tictacList.get(21).setBackgroundColor(0xFF00FF00);
                tictacList.get(22).setBackgroundColor(0xFF00FF00);
                tictacList.get(23).setBackgroundColor(0xFF00FF00);
                tictacList.get(24).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(0).equals("o") && gameList.get(5).equals("o") && gameList.get(10).equals("o") && gameList.get(15).equals("o") && gameList.get(20).equals("o")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                tictacList.get(20).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(1).equals("o") && gameList.get(6).equals("o") && gameList.get(11).equals("o") && gameList.get(16).equals("o") && gameList.get(21).equals("o")) {
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(11).setBackgroundColor(0xFF00FF00);
                tictacList.get(16).setBackgroundColor(0xFF00FF00);
                tictacList.get(21).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(2).equals("o") && gameList.get(7).equals("o") && gameList.get(12).equals("o") && gameList.get(17).equals("o") && gameList.get(22).equals("o")) {
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(17).setBackgroundColor(0xFF00FF00);
                tictacList.get(22).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(3).equals("o") && gameList.get(8).equals("o") && gameList.get(13).equals("o") && gameList.get(18).equals("o") && gameList.get(23).equals("o")) {
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(13).setBackgroundColor(0xFF00FF00);
                tictacList.get(18).setBackgroundColor(0xFF00FF00);
                tictacList.get(23).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(4).equals("o") && gameList.get(9).equals("o") && gameList.get(14).equals("o") && gameList.get(19).equals("o") && gameList.get(24).equals("o")) {
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                tictacList.get(14).setBackgroundColor(0xFF00FF00);
                tictacList.get(19).setBackgroundColor(0xFF00FF00);
                tictacList.get(24).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(0).equals("o") && gameList.get(6).equals("o") && gameList.get(12).equals("o") && gameList.get(18).equals("o") && gameList.get(24).equals("o")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(18).setBackgroundColor(0xFF00FF00);
                tictacList.get(24).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(4).equals("o") && gameList.get(8).equals("o") && gameList.get(12).equals("o") && gameList.get(16).equals("o") && gameList.get(20).equals("o")) {
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(16).setBackgroundColor(0xFF00FF00);
                tictacList.get(20).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
        }
        mDatabaseReference.child("games").child(key).child("winnerP1").setValue(newGame.getWinnerP1());
        mDatabaseReference.child("games").child(key).child("winnerP2").setValue(newGame.getWinnerP2());
    }
}
