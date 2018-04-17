package com.trasimus.tictactoe.online.Friends;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trasimus.tictactoe.online.Lobby;
import com.trasimus.tictactoe.online.R;

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

    private DatabaseReference mDatabaseReference;
    private DatabaseReference ref;
    private ArrayAdapter<String> mAdapter;
    private ValueEventListener mListener;

    private TextView user1;
    private TextView user2;
    private Switch switch1;
    private Switch switch2;
    private ListView mListView;
    private EditText mEditText;
    private Button sendButton;
    private CheckBox isOnline1;
    private CheckBox isOnline2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        user1 = (TextView) findViewById(R.id.user1);
        user2 = (TextView) findViewById(R.id.user2);
        switch1 = (Switch) findViewById(R.id.switch1);
        switch2 = (Switch) findViewById(R.id.switch2);
        mListView = (ListView) findViewById(R.id.messages);
        mEditText = (EditText) findViewById(R.id.messageText);
        sendButton = (Button) findViewById(R.id.sendButton);
        isOnline1 = (CheckBox) findViewById(R.id.isOnline);
        isOnline2 = (CheckBox) findViewById(R.id.isOnline2);

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mListView.setAdapter(mAdapter);

        message = new ArrayList<String>();
        messages = new ArrayList<ArrayList<String>>();

        bundle = getIntent().getExtras();
        player1 = bundle.getString("p1");
        player2 = bundle.getString("p2");
        lobbyID = bundle.getString("lobbyID");

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        ref = mDatabaseReference.child("Lobby").child(lobbyID);

        gameID = mDatabaseReference.child("Games").getKey();

        mLobby = new Lobby(lobbyID, player1, player2, gameID, messages, true, false, false, false, "");

        ref.setValue(mLobby);

        mListener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLobby = dataSnapshot.getValue(Lobby.class);

                switch1.setChecked(mLobby.isReadyP1());
                switch2.setChecked(mLobby.isReadyP2());

                isOnline1.setChecked(mLobby.isConnectedP1());
                isOnline2.setChecked(mLobby.isConnectedP2());

                if (mLobby.getMessages() != null){
                    messages = mLobby.getMessages();
                    String message = mLobby.getMessages().get(mLobby.getMessages().size()-1).get(0);
                    String name = mLobby.getMessages().get(mLobby.getMessages().size()-1).get(1);
                    message = name + ": " + message;
                    mAdapter.add(message);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText.getText().toString() == ""){
                    return;
                }
                message.clear();
                message.add(mEditText.getText().toString());
                message.add(player1);
                messages.add(message);
                mEditText.setText("");
                ref.child("messages").setValue(messages);
            }
        });
    }
}
