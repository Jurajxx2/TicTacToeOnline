package com.trasimus.tictactoe.online.friends;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trasimus.tictactoe.online.DefaultUser;
import com.trasimus.tictactoe.online.R;

import java.util.ArrayList;

public class FriendListAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private ArrayList<DataSnapshot> mSnapshotList;
    private ArrayList<ArrayList<String>> friends;
    private FirebaseUser mUser;
    private DefaultUser defaultUser;
    private ArrayList<String> info;

    private void getFriendList(DatabaseReference ref){
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friends = (ArrayList<ArrayList<String>>) dataSnapshot.getValue();

                if (friends == null){
                    return;
                }

                for (int i=0; i<friends.size(); i++){
                    if (friends.get(i).get(0).equals(mUser.getUid())){
                        getUserInfo(friends.get(i).get(1), true, friends.get(i).get(2), "SENT");
                    } else if (friends.get(i).get(1).equals(mUser.getUid())){
                        getUserInfo(friends.get(i).get(0), true, friends.get(i).get(2), "RECEIVED");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUserInfo(final String userIDX, final boolean findUsers, final String isAccepted, final String state) {
        mDatabaseReference.child(userIDX).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isAccepted.equals("0")){
                    info.add(state);
                } else if (isAccepted.equals("1")){
                    info.add("ACCEPTED");
                }
                mSnapshotList.add(dataSnapshot);
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public FriendListAdapter(Activity activity, DatabaseReference ref){
        info = new ArrayList<String>();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mActivity = activity;

        mSnapshotList = new ArrayList<>();

        getFriendList(ref);
    }

    static class ViewHolder{
        TextView name;
        TextView country;
        TextView age;
        TextView requestState;
        ImageView profileImg;
        ImageView isOnline;
        LinearLayout.LayoutParams params;
    }


    @Override
    public int getCount() {
        return mSnapshotList.size();
    }

    @Override
    public DefaultUser getItem(int i) {
        DataSnapshot snapshot = mSnapshotList.get(i);
        return snapshot.getValue(DefaultUser.class);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final DefaultUser defaultUser = getItem(i);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.friend_item, viewGroup, false);
            final ViewHolder holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.playerName);
            holder.country = (TextView) view.findViewById(R.id.playerCountry);
            holder.age = (TextView) view.findViewById(R.id.playerAge);
            holder.isOnline = (ImageView) view.findViewById(R.id.isOnline);
            holder.profileImg = (ImageView) view.findViewById(R.id.playerImage);
            holder.requestState = (TextView) view.findViewById(R.id.requestState);

            //holder.params = (LinearLayout.LayoutParams) holder.game.getLayoutParams();
            view.setTag(holder);
        }
        Log.d("test", "getView called");
        final FriendListAdapter.ViewHolder holder = (FriendListAdapter.ViewHolder) view.getTag();

        holder.name.setText(defaultUser.getName());
        holder.country.setText("Country: " + defaultUser.getCountry());
        holder.age.setText("Age: " + defaultUser.getAge());
        if (defaultUser.isOnline()) {
            holder.isOnline.setImageResource(R.drawable.isonline);
        } else {
            holder.isOnline.setImageResource(R.drawable.isoffline);
        }
        if (info.get(i).equals("SENT")){
            holder.requestState.setText("PENDING");
        } else if (info.get(i).equals("RECEIVED")) {
            holder.requestState.setText("ACCEPT OR DENY");
        } else if (info.get(i).equals("ACCEPTED")){
            holder.requestState.setText("");
        }

        if (defaultUser.getPhotoID() != null) {
            if (defaultUser.getPhotoID().equals("grumpy")) {
                holder.profileImg.setImageResource(R.drawable.grumpy);
            }
            if (defaultUser.getPhotoID().equals("kon")) {
                holder.profileImg.setImageResource(R.drawable.kon);
            }
            if (defaultUser.getPhotoID().equals("opica")) {
                holder.profileImg.setImageResource(R.drawable.opica);
            }
            if (defaultUser.getPhotoID().equals("0")) {
                holder.profileImg.setImageResource(R.drawable.icon_profile_empty);
            }
        }

        return view;    }
}
