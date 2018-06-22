package com.trasimus.tictactoe.online.other;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trasimus.tictactoe.online.DefaultUser;
import com.trasimus.tictactoe.online.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RankingsActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private DatabaseReference usersReference;
    private DefaultUser mDefaultUser;
    private ArrayList<DefaultUser> mList;
    private ArrayList<DefaultUser> friendList;
    private ArrayList<String> mStrings;
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private int page = 0;
    private boolean showFriends = false;
    private FirebaseUser mUser;
    private DefaultUser currentUser;
    private ArrayList<ArrayList<String>> friends;

    private TextView pageCount;
    private Button switchDisplay;
    private Button oneLeft;
    private Button moreLeft;
    private Button moreRight;
    private Button oneRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        mListView = findViewById(R.id.rankingsList);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        usersReference = mDatabaseReference.child("Users");
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        pageCount = (TextView) findViewById(R.id.pageCount);
        switchDisplay = (Button) findViewById(R.id.showFriends);
        oneLeft = (Button) findViewById(R.id.btny);
        moreLeft = (Button) findViewById(R.id.btnyyy);
        oneRight = (Button) findViewById(R.id.btnx);
        moreRight = (Button) findViewById(R.id.btnxxx);

        mList = new ArrayList<DefaultUser>();
        friendList = new ArrayList<DefaultUser>();
        mStrings = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mDefaultUser = new DefaultUser();
        currentUser = new DefaultUser();
        friends = new ArrayList<ArrayList<String>>();

        usersReference.child(mUser.getUid()).child("Friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friends = (ArrayList<ArrayList<String>>)dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    mDefaultUser = snapshot.getValue(DefaultUser.class);
                    mList.add(mDefaultUser);
                }
                sortList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    mDefaultUser = snapshot.getValue(DefaultUser.class);
                    if (friends!=null) {

                        for (int i = 0; i < friends.size(); i++) {

                            if ((mDefaultUser.getUserID().equals(friends.get(i).get(0)) || mDefaultUser.getUserID().equals(friends.get(i).get(1))) && friends.get(i).get(2).equals("1")) {
                                friendList.add(mDefaultUser);

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        switchDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFriends = !showFriends;
                page = 0;

                adapter.clear();
                mListView.setAdapter(null);

                if (showFriends){
                    switchDisplay.setText("Show all");
                    sortFriendList();

                } else {
                    switchDisplay.setText("Show only friends");
                    sortList();
                }
            }
        });

        oneLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page>0) {
                    page--;
                    switchPage();
                }
            }
        });

        oneRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showFriends && ((friendList.size()-1)/10 > page)) {
                    page++;
                    switchPage();
                } else if (!showFriends && ((mList.size()-1)/10 > page)){
                    page++;
                    switchPage();
                }
            }
        });

        moreLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page>9) {
                    page = page-10;
                    switchPage();
                } else {
                    page = 0;
                    switchPage();
                }
            }
        });

        moreRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showFriends && ((friendList.size()-1)/10 > 9 + page)) {
                    page = page+10;
                    switchPage();
                } else if (!showFriends && ((mList.size()-1)/10 > 9 + page)){
                    page = page+10;
                    switchPage();
                }
            }
        });
    }

    private void switchPage(){
        adapter.clear();
        String displayText;

        if (mStrings.size()<10) {
            displayText = "1. - " + mStrings.size() + "./" + mStrings.size();
            pageCount.setText(displayText);
        } else {
            if (page*10+10>mStrings.size()){
                displayText = page*10 + ". - " + mStrings.size() + "./" + mStrings.size();
                pageCount.setText(displayText);
            } else {
                displayText = page*10 + ". - " + page*10+9 + "./" + mStrings.size();
                pageCount.setText(displayText);
            }
        }
        //Last page
        if (mStrings.size()<(page*10)+10){
            for (int i = page * 10; i < mStrings.size(); i++) {
                adapter.add(mStrings.get(i));
            }
        } else {
            for (int i = page * 10; i < (page * 10) + 10; i++) {
                adapter.add(mStrings.get(i));
            }
        }

    }

    public void sortList(){
        mStrings.clear();

        Collections.sort(mList, new Comparator<DefaultUser>() {
            @Override
            public int compare(DefaultUser o1, DefaultUser o2) {
                return Integer.valueOf(o2.getPoints()).compareTo(o1.getPoints());
            }
        });

        int position = 0;
        for (int x=0; x<mList.size(); x++){
            if (mList.get(x).getName()!=null) {
                position++;
                mStrings.add(position + ". " + mList.get(x).getName() + " " + mList.get(x).getPoints());
            }
        }
        if (mStrings.size()<10) {
            String displayText = "1. - " + mStrings.size() + "./" + mStrings.size();
            pageCount.setText(displayText);
        }

        mListView.setAdapter(adapter);

        if (mStrings.size()<10) {
            for (int i = 0; i < mStrings.size(); i++) {
                adapter.add(mStrings.get(i));
            }
        } else {
            for (int i = 0; i < 10; i++) {
                adapter.add(mStrings.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void sortFriendList(){
        mStrings.clear();

        Collections.sort(friendList, new Comparator<DefaultUser>() {
            @Override
            public int compare(DefaultUser o1, DefaultUser o2) {
                return Integer.valueOf(o2.getPoints()).compareTo(o1.getPoints());
            }
        });

        int position = 0;
        for (int x=0; x<friendList.size(); x++){
            if (friendList.get(x).getName()!=null) {
                position++;
                mStrings.add(position + ". " + friendList.get(x).getName() + " " + friendList.get(x).getPoints());
            }
        }
        if (mStrings.size()<10) {
            String displayText = "1. - " + mStrings.size() + "./" + mStrings.size();
            pageCount.setText(displayText);
        }

        mListView.setAdapter(adapter);

        if (mStrings.size()<10) {
            for (int i = 0; i < mStrings.size(); i++) {
                adapter.add(mStrings.get(i));
            }
        } else {
            for (int i = 0; i < 10; i++) {
                adapter.add(mStrings.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }


}
