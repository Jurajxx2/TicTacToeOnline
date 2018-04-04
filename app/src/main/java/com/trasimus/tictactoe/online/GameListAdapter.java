package com.trasimus.tictactoe.online;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class GameListAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private ArrayList<DataSnapshot> mSnapshotList;


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

    public GameListAdapter(Activity activity, DatabaseReference ref){

        mActivity = activity;
        mDatabaseReference = ref.child("games");
        mDatabaseReference.addChildEventListener(mListener);

        mSnapshotList = new ArrayList<>();

        Log.d("test", "Game list adapter called");

    }

    static class ViewHolder{
        TextView game;
        LinearLayout.LayoutParams params;
    }


    @Override
    public int getCount() {
        return mSnapshotList.size();
    }

    @Override
    public GameObject getItem(int position) {
        DataSnapshot snapshot = mSnapshotList.get(position);
        return snapshot.getValue(GameObject.class);
    }

    @Override
    public long getItemId(int position) {
        Log.d("test", "getItemId called");
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("test", "getView beginning, why not working???");

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.game_item, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.game = (TextView) convertView.findViewById(R.id.gameItem);
            holder.params = (LinearLayout.LayoutParams) holder.game.getLayoutParams();
            convertView.setTag(holder);
        }

        Log.d("test", "getView called");
        final GameObject gameObject = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        String size = gameObject.getSize() + "x" + gameObject.getSize() + " game";
        holder.game.setText(size);

        return convertView;
    }


    public void cleanup(){

        mDatabaseReference.removeEventListener(mListener);
    }
}
