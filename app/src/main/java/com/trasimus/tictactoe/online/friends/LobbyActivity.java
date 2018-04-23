package com.trasimus.tictactoe.online.friends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
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
import com.trasimus.tictactoe.online.Lobby;
import com.trasimus.tictactoe.online.R;
import com.trasimus.tictactoe.online.UserMap;
import com.trasimus.tictactoe.online.game.GameActivity;
import com.trasimus.tictactoe.online.other.GameInvitationListener;
import com.trasimus.tictactoe.online.other.MenuActivity;

import java.util.ArrayList;

public class LobbyActivity extends AppCompatActivity {

    private Bundle bundle;
    private String player1;
    private String player2;
    private Lobby mLobby;
    private String lobbyID;
    private String gameID;
    private ArrayList<ArrayList<String>> messages;
    private ArrayList<String> message;
    private ArrayList<String> displayedMessages;
    private String player1Name;
    private String player2Name;
    private boolean isDeleted = false;

    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private DatabaseReference mReference;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference ref;
    private ArrayAdapter<String> mAdapter;
    private ValueEventListener mListener;
    private DefaultUser defaultUser;
    private UserMap userMap;

    private RadioGroup gameSize;
    private RadioButton size3;
    private RadioButton size4;
    private RadioButton size5;
    private TextView user1;
    private TextView user2;
    private Switch switch1;
    private Switch switch2;
    private ListView mListView;
    private EditText mEditText;
    private Button sendButton;
    private CheckBox isOnline1;
    private CheckBox isOnline2;
    private TextView lobbyState;
    private boolean isPlayerOne = false;
    private boolean isPlayerTwo = false;
    private boolean countdown = false;
    private CountDownTimer mTimer;

    private String flag;
    private int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        //pre istotu
        if (ref!=null) {
            ref.removeEventListener(mListener);
        }

        //OTESTOVAT TREBA
        Intent intent = new Intent(LobbyActivity.this, GameInvitationListener.class);
        stopService(intent);

        gameSize = (RadioGroup) findViewById(R.id.gameSize);
        size3 = (RadioButton) findViewById(R.id.size3);
        size4 = (RadioButton) findViewById(R.id.size4);
        size5 = (RadioButton) findViewById(R.id.size5);
        user1 = (TextView) findViewById(R.id.user1);
        user2 = (TextView) findViewById(R.id.user2);
        switch1 = (Switch) findViewById(R.id.switch1);
        switch2 = (Switch) findViewById(R.id.switch2);
        mListView = (ListView) findViewById(R.id.messages);
        mEditText = (EditText) findViewById(R.id.messageText);
        sendButton = (Button) findViewById(R.id.sendButton);
        isOnline1 = (CheckBox) findViewById(R.id.isOnline);
        isOnline2 = (CheckBox) findViewById(R.id.isOnline2);
        lobbyState = (TextView) findViewById(R.id.lobbyState);

        mTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                lobbyState.setText("Game will begin in: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                lobbyState.setText("Starting game...");
                ref.removeEventListener(mListener);
                if (isPlayerOne){
                    Intent intent = new Intent(LobbyActivity.this, GameActivity.class);
                    intent.putExtra("customGame", "Y");
                    intent.putExtra("IDofGame", gameID);
                    intent.putExtra("p1", player1);
                    intent.putExtra("p2", player2);
                    intent.putExtra("size", Integer.valueOf(mLobby.getSize()));
                    startActivity(intent);
                    finish();
                }

                if (isPlayerTwo){
                    Intent intent = new Intent(LobbyActivity.this, GameActivity.class);
                    intent.putExtra("gameID", gameID);
                    startActivity(intent);
                    finish();
                }

            }
        };

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(mAdapter);

        message = new ArrayList<String>();
        messages = new ArrayList<ArrayList<String>>();
        displayedMessages = new ArrayList<String>();

        bundle = getIntent().getExtras();
        player1 = bundle.getString("p1");
        player2 = bundle.getString("p2");
        lobbyID = bundle.getString("lobbyID");
        flag = getIntent().getStringExtra("X");

        if (flag==null){
            isPlayerOne = true;
            Log.d("test", "Player 1");
        } else if (flag.equals("X")){
            isPlayerTwo = true;
            Log.d("test", "Player 2");
        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        ref = mDatabaseReference.child("Lobby").child(lobbyID);

        if (isPlayerOne) {
            mDatabaseReference.child("Users").child(player1).child("lobbyID").setValue(lobbyID);

            mLobby = new Lobby(lobbyID, player1, player2, gameID, messages, true, false, false, false, "3");

            ref.setValue(mLobby);
        } else if (isPlayerTwo){
            mLobby = new Lobby();
            ref.child("connectedP2").setValue(true);
        }

        mListener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLobby = dataSnapshot.getValue(Lobby.class);

                if (mLobby == null){
                    ref.removeEventListener(mListener);
                    new AlertDialog.Builder(LobbyActivity.this)
                            .setTitle("Lobby not found")
                            .setMessage("Lobby you want to join was not found")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    finish();
                                }
                            }).create().show();
                }

                if (countdown){
                    countdown = false;
                    cancelCountdown();
                    lobbyState.setText("Waiting for players");
                }

                switch1.setChecked(mLobby.isReadyP1());
                switch2.setChecked(mLobby.isReadyP2());

                isOnline1.setChecked(mLobby.isConnectedP1());
                isOnline2.setChecked(mLobby.isConnectedP2());

                if (mLobby.getMessages() != null){
                    if (displayedMessages.size() < mLobby.getMessages().size()) {
                        messages = mLobby.getMessages();
                        String message = mLobby.getMessages().get(mLobby.getMessages().size() - 1).get(0);
                        String name = mLobby.getMessages().get(mLobby.getMessages().size() - 1).get(1);
                        message = name + ": " + message;
                        displayedMessages.add(message);
                        mAdapter.add(message);
                    }
                }

                if (mLobby.isReadyP1() && mLobby.isReadyP2() && !countdown){
                    countdown = true;
                    startCountdown();
                }

                if (i<2) {
                    for (int a=0; a<2; a++) {
                        if (i == 0) {
                            getUserInfo(mLobby.getPlayer1());
                            i++;
                        } else if (i == 1) {
                            getUserInfo(mLobby.getPlayer2());
                            i++;
                        }
                    }
                }

                if (mLobby.getSize()=="3"){
                    gameSize.check(R.id.size3);
                } else if (mLobby.getSize()=="4"){
                    gameSize.check(R.id.size4);
                } else if (mLobby.getSize()=="5"){
                    gameSize.check(R.id.size5);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText().toString().length()==0){
                    return;
                }
                message.clear();
                message.add(mEditText.getText().toString());
                if (isPlayerOne) {
                    message.add(player1Name);
                } else if (isPlayerTwo){
                    message.add(player2Name);
                }
                messages.add(message);
                mEditText.setText("");
                ref.child("messages").setValue(messages);
            }
        });

        if (isPlayerOne) {
            switch1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLobby.setReadyP1(!mLobby.isReadyP1());
                    ref.child("readyP1").setValue(mLobby.isReadyP1());
                }
            });
        } else if (isPlayerTwo) {
            switch2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLobby.setReadyP2(!mLobby.isReadyP2());
                    ref.child("readyP2").setValue(mLobby.isReadyP2());
                }
            });
        }

        gameSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == size3.getId()){
                    mLobby.setSize("3");
                    ref.child("size").setValue(mLobby.getSize());
                } else if (checkedId == size4.getId()){
                    mLobby.setSize("4");
                    ref.child("size").setValue(mLobby.getSize());
                } else if (checkedId == size5.getId()){
                    mLobby.setSize("5");
                    ref.child("size").setValue(mLobby.getSize());
                }
            }
        });
    }

    private void startCountdown(){
        mTimer.start();
    }

    private void cancelCountdown(){
        mTimer.cancel();
    }

//    private void getID(String playerUID){
//        mDatabase = mDatabaseReference.child("UserMap").child(playerUID);
//        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                userMap = dataSnapshot.getValue(UserMap.class);
//                getUserInfo();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void getUserInfo(String userID) {
        mReference = mDatabaseReference.child("Users").child(userID);
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                defaultUser = dataSnapshot.getValue(DefaultUser.class);

                if (defaultUser.getUserID().equals(mLobby.getPlayer1())){
                    user1.setText(defaultUser.getName());
                    player1Name = defaultUser.getName();
                    gameID = defaultUser.getMyGameID();
                } else if (defaultUser.getUserID().equals(mLobby.getPlayer2())){
                    user2.setText(defaultUser.getName());
                    player2Name = defaultUser.getName();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(LobbyActivity.this)
                .setTitle("Exit lobby?")
                .setMessage("Are you sure you want to leave lobby?")
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        ref.removeEventListener(mListener);
                        LobbyActivity.super.onBackPressed();
                    }
                }).create().show();
        }

    @Override
    protected void onStop() {

        if (isPlayerOne){
            if (mLobby.isReadyP1()) {
                mLobby.setReadyP1(!mLobby.isReadyP1());
                ref.child("readyP1").setValue(mLobby.isReadyP1());
            }
            mLobby.setConnectedP1(false);
            ref.child("connectedP1").setValue(mLobby.isConnectedP1());
            mDatabaseReference.child("Users").child(mLobby.getPlayer1()).child("lobbyID").setValue("");
            mDatabaseReference.child("Users").child(mLobby.getPlayer2()).child("lobbyID").setValue("");
        } else if (isPlayerTwo){
            if (mLobby.isReadyP2()) {
                mLobby.setReadyP2(!mLobby.isReadyP2());
                ref.child("readyP2").setValue(mLobby.isReadyP2());
            }
            mLobby.setConnectedP2(false);
            ref.child("connectedP2").setValue(mLobby.isConnectedP2());
            mDatabaseReference.child("Users").child(mLobby.getPlayer1()).child("lobbyID").setValue("");
            mDatabaseReference.child("Users").child(mLobby.getPlayer2()).child("lobbyID").setValue("");
        }
        if (!mLobby.isConnectedP1() && !mLobby.isConnectedP2()){
            ref.removeValue();
            isDeleted = true;
        }
        Intent intent = new Intent(LobbyActivity.this, GameInvitationListener.class);
        intent.putExtra("userID", defaultUser.getUserID());
        startService(intent);

        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //OTESTOVAT TREBA
        Intent intent = new Intent(LobbyActivity.this, GameInvitationListener.class);
        stopService(intent);


        if (isDeleted || mLobby.getLobbyID()==null){
            new AlertDialog.Builder(LobbyActivity.this)
                    .setTitle("Lobby ended")
                    .setMessage("The lobby you want to join ended")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    }).create().show();
        }
        if (isPlayerOne){
            mDatabaseReference.child("Users").child(mLobby.getPlayer1()).child("lobbyID").setValue(mLobby.getLobbyID());
        } else if (isPlayerTwo){
            mDatabaseReference.child("Users").child(mLobby.getPlayer2()).child("lobbyID").setValue(mLobby.getLobbyID());
        }
    }
}
