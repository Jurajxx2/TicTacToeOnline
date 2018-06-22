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
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Comparator;

public class FindUserListAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private ArrayList<DefaultUser> mSnapshotList;
    private ArrayList<ArrayList<String>> friends;
    private FirebaseUser mUser;
    private DefaultUser defaultUser;
    private ArrayList<String> info;

    private void getUserList(DatabaseReference ref, final String findWord){
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    defaultUser = snapshot.getValue(DefaultUser.class);
                    if (!defaultUser.getName().contains(findWord) || defaultUser.getUserID().equals(mUser.getUid())){
                        continue;
                    }
                    mSnapshotList.add(defaultUser);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public FindUserListAdapter(Activity activity, String searchWord){

        info = new ArrayList<String>();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mActivity = activity;

        mSnapshotList = new ArrayList<>();

        getUserList(mDatabaseReference, searchWord);
    }

    static class ViewHolder{
        TextView name;
        TextView country;
        TextView age;
        ImageView profileImg;
    }


    @Override
    public int getCount() {
        return mSnapshotList.size();
    }

    @Override
    public DefaultUser getItem(int i) {
        DefaultUser snapshot = mSnapshotList.get(i);
        return snapshot;
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
            view = inflater.inflate(R.layout.friend_item_simple, viewGroup, false);
            final ViewHolder holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.playerName);
            holder.country = (TextView) view.findViewById(R.id.playerCountry);
            holder.age = (TextView) view.findViewById(R.id.playerAge);
            holder.profileImg = (ImageView) view.findViewById(R.id.playerImage);

            view.setTag(holder);
        }
        Log.d("test", "getView called");
        final FindUserListAdapter.ViewHolder holder = (FindUserListAdapter.ViewHolder) view.getTag();

        holder.name.setText(defaultUser.getName());
        holder.country.setText("Country: " + defaultUser.getCountry());
        holder.age.setText("Age: " + defaultUser.getAge());

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
