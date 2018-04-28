package com.trasimus.tictactoe.online.other;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    private ArrayList<String> mStrings;
    private ListView mListView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        mListView = findViewById(R.id.rankingsList);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        usersReference = mDatabaseReference.child("Users");

        mList = new ArrayList<DefaultUser>();
        mStrings = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mDefaultUser = new DefaultUser();

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
    }

    public void sortList(){
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

        mListView.setAdapter(adapter);

        for (int i=0; i<mStrings.size(); i++){
            adapter.add(mStrings.get(i));
        }
    }


}
