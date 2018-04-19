package com.trasimus.tictactoe.online.game;

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
import com.trasimus.tictactoe.online.GameObject;
import com.trasimus.tictactoe.online.R;

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
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final GameObject gameObject = getItem(position);

        if (!gameObject.getPlayer2().equals("")){
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.empty_item, parent, false);
                final ViewHolder holder = new ViewHolder();
                convertView.setTag(holder);
            }

            Log.d("test", "getView2 called");
            final ViewHolder holder = (ViewHolder) convertView.getTag();

            return convertView;
        }

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.game_item, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.game = (TextView) convertView.findViewById(R.id.gameItem);
            holder.params = (LinearLayout.LayoutParams) holder.game.getLayoutParams();
            convertView.setTag(holder);
        }

        Log.d("test", "getView called");
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        String size = gameObject.getSize() + "x" + gameObject.getSize() + " game";
        holder.game.setText(size);

        return convertView;
    }


    public void cleanup(){

        mDatabaseReference.removeEventListener(mListener);
    }
}
