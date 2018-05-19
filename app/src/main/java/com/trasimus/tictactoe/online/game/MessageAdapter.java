package com.trasimus.tictactoe.online.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.trasimus.tictactoe.online.DefaultUser;
import com.trasimus.tictactoe.online.GameObject;
import com.trasimus.tictactoe.online.R;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter{

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private ArrayList<DataSnapshot> mSnapshotList;
    private boolean playerOne = false;
    private boolean playerTwo = false;
    private String player1;
    private String player2;
    private GameObject mGameObject;
    private ValueEventListener mValueEventListener;
    private DatabaseReference mReference;

    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    public MessageAdapter(Activity activity, DatabaseReference ref, final boolean playerOne, boolean playerTwo){

        mGameObject = new GameObject();

        mReference = ref;

        mActivity = activity;
        mDatabaseReference = ref.child("messages");
        mDatabaseReference.addChildEventListener(mListener);
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        mValueEventListener = mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGameObject = dataSnapshot.getValue(GameObject.class);

                player1 = mGameObject.getP1name();
                player2 = mGameObject.getP2name();

                if (!player2.equals("")){
                    mReference.removeEventListener(mValueEventListener);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSnapshotList = new ArrayList<>();
    }

    static class ViewHolder{
        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return mSnapshotList.size();
    }

    @Override
    public ArrayList<String> getItem(int i) {
        DataSnapshot snapshot = mSnapshotList.get(i);
        return (ArrayList<String>) snapshot.getValue();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chat_msg_row, viewGroup, false);
            final ViewHolder holder = new ViewHolder();
            holder.authorName = (TextView) view.findViewById(R.id.author);
            holder.body = (TextView) view.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams) holder.authorName.getLayoutParams();
            view.setTag(holder);
        }

        final ArrayList<String> message = getItem(i);
        final ViewHolder holder = (ViewHolder) view.getTag();

        boolean isMe = false;

        if (playerOne) {
            isMe = message.get(0).equals("true");
        } else if (playerTwo){
            isMe = message.get(1).equals("true");
        }
        setChatRowAppearance(isMe, holder);

        String author = "";

        if (playerOne){
            author = player1;
        } else if (playerTwo){
            author = player2;
        }

        holder.authorName.setText(author);

        String msg = message.get(2);
        holder.body.setText(msg);

        return view;
    }

    private void setChatRowAppearance(boolean isItMe, ViewHolder holder){

        if(!isItMe){
            holder.params.gravity = Gravity.END;
            holder.authorName.setTextColor(Color.GREEN);
            holder.body.setBackgroundResource(R.drawable.bubble2);
        } else {
            holder.params.gravity = Gravity.START;
            holder.authorName.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }

        holder.authorName.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);
    }

    public void cleanup(){

        mDatabaseReference.removeEventListener(mListener);
    }
}
