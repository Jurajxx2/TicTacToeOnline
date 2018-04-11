package com.trasimus.tictactoe.online;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Locale;

public class AccountActivity extends AppCompatActivity {

    private TextView name;
    private TextView email;
    private TextView id;
    private TextView score;
    private ImageView profileImg;
    private Button editButton;
    private EditText mEditText;


    private String username;
    private String useremail;
    private Uri photoUrl;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mReference;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    private UserMap userMap;
    private DefaultUser defaultUser;
    private Boolean editing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.trasimus.tictactoe.online.R.layout.activity_account);
        FontHelper.setCustomTypeface(findViewById(com.trasimus.tictactoe.online.R.id.view_root));

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.mail);
        id = (TextView) findViewById(R.id.id);
        score = (TextView) findViewById(R.id.userScore);
        profileImg = (ImageView) findViewById(R.id.profile_image);
        editButton = (Button) findViewById(R.id.edit);
        mEditText = (EditText) findViewById(R.id.editName);
        editing = false;


        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        getID(user.getUid());
    }

    public void editSomething(View view){
        editing = !editing;
        if (editing){
            name.setVisibility(View.GONE);
            mEditText.setVisibility(View.VISIBLE);
            editButton.setText("Save");
        } else if (!editing){
            mDatabaseReference.child("Users").child(userMap.getUserID()).child("name").setValue(mEditText.getText().toString());

            mEditText.setVisibility(View.GONE);
            editButton.setText("Edit");
            name.setVisibility(View.VISIBLE);
            name.setText(mEditText.getText().toString());
        }
    }

    private void getID(String playerUID){
        mDatabase = mDatabaseReference.child("UserMap").child(playerUID);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
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
        mReference = mDatabaseReference.child("Users").child(userMap.getUserID());
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                defaultUser = dataSnapshot.getValue(DefaultUser.class);

                name.setText(defaultUser.getName());
                email.setText(defaultUser.getMail());
                id.setText(defaultUser.getUserID());
                score.setText(String.valueOf(defaultUser.getPoints()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
