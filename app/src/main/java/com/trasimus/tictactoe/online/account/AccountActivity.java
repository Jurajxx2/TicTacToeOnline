package com.trasimus.tictactoe.online.account;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trasimus.tictactoe.online.DefaultUser;
import com.trasimus.tictactoe.online.FontHelper;
import com.trasimus.tictactoe.online.friends.LobbyActivity;
import com.trasimus.tictactoe.online.other.GameInvitationListener;
import com.trasimus.tictactoe.online.other.LoginActivity;
import com.trasimus.tictactoe.online.other.MenuActivity;
import com.trasimus.tictactoe.online.R;
import com.trasimus.tictactoe.online.UserMap;

import java.util.Arrays;

public class AccountActivity extends AppCompatActivity {

    private TextView name;
    private TextView email;
    private TextView id;
    private TextView score;
    private ImageView profileImg;
    private ImageView opica;
    private ImageView horse;
    private ImageView grumpy;
    private ImageView default_profile;
    private TextView profileInfo;
    private Button editButton;
    private EditText mEditText;
    private TextView countryView;
    private TextView ageView;
    private EditText ageEdit;
    private AutoCompleteTextView countryEdit;
    private String photo;

    private String username;
    private String useremail;
    private String country;
    private int age;
    private String[] countries;

    private Uri photoUrl;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mReference;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    private UserMap userMap;
    private DefaultUser defaultUser;
    private Boolean editing;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.accountmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (editing){
            Toast.makeText(this, "Please, finish editing your account", Toast.LENGTH_SHORT).show();
            return true;
        }
        int id = item.getItemId();

        if (id == R.id.deleteAcc){
            new AlertDialog.Builder(AccountActivity.this)
                    .setTitle("Delete Account?")
                    .setMessage("Are you sure you want to delete your account?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(AccountActivity.this, GameInvitationListener.class);
                            stopService(intent);
                            mDatabaseReference.child("UserMap").child(user.getUid()).removeValue();
                            mDatabaseReference.child("Users").child(defaultUser.getUserID()).removeValue();
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(AccountActivity.this, "Your account was removed", Toast.LENGTH_LONG).show();
                                                FirebaseAuth.getInstance().signOut();
                                                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(AccountActivity.this, "Error, account was not removed:" + task.getException() , Toast.LENGTH_LONG).show();
                                                FirebaseAuth.getInstance().signOut();
                                                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                        }
                    }).create().show();
        }
        if (id == R.id.connectAcc){
            Intent intent = new Intent(AccountActivity.this, ConnectAccountsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.edit){
            editSomething(item);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.trasimus.tictactoe.online.R.layout.activity_account);
        FontHelper.setCustomTypeface(findViewById(com.trasimus.tictactoe.online.R.id.view_root));

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.mail);
        id = (TextView) findViewById(R.id.id);
        score = (TextView) findViewById(R.id.userScore);
        countryView = (TextView)findViewById(R.id.country);
        ageView = (TextView)findViewById(R.id.age);
        ageEdit = (EditText)findViewById(R.id.editAge);
        countryEdit = (AutoCompleteTextView)findViewById(R.id.editCountry);
        profileImg = (ImageView) findViewById(R.id.profile_image);
        grumpy = (ImageView) findViewById(R.id.grumpy);
        horse = (ImageView) findViewById(R.id.horse);
        opica = (ImageView) findViewById(R.id.opica);
        default_profile = (ImageView) findViewById(R.id.default_profile);
        profileInfo = (TextView) findViewById(R.id.profileInfo);

        editButton = (Button) findViewById(R.id.edit);
        mEditText = (EditText) findViewById(R.id.editName);
        editing = false;

        countries = getResources().getStringArray(R.array.countries_array);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        countryEdit.setAdapter(adapter);


        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        getID(user.getUid());

        grumpy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileImg.setImageResource(R.drawable.grumpy);
                mDatabaseReference.child("Users").child(userMap.getUserID()).child("photoID").setValue("grumpy");
            }
        });

        horse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileImg.setImageResource(R.drawable.kon);
                mDatabaseReference.child("Users").child(userMap.getUserID()).child("photoID").setValue("kon");
            }
        });

        opica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileImg.setImageResource(R.drawable.opica);
                mDatabaseReference.child("Users").child(userMap.getUserID()).child("photoID").setValue("opica");
            }
        });

        default_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileImg.setImageResource(R.drawable.icon_profile_empty);
                mDatabaseReference.child("Users").child(userMap.getUserID()).child("photoID").setValue("0");
            }
        });
    }

    public void editSomething(MenuItem item){
        if (mEditText.getText().toString().length()<3 && editing){
            Toast.makeText(this, "Name has to have at least 3 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Arrays.asList(countries).contains(countryEdit.getText().toString()) && editing){
            Toast.makeText(this, "Please define your country", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ageEdit.getText().toString().isEmpty() && editing){
            Toast.makeText(this, "Please define your age", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ageEdit.getText().toString().length()>3 && editing){
            Toast.makeText(this, "Sorry, you are too old for this game", Toast.LENGTH_SHORT).show();
            return;
        }

        editing = !editing;
        if (editing){
            name.setVisibility(View.GONE);
            mEditText.setVisibility(View.VISIBLE);
            mEditText.setText(name.getText().toString());

            countryView.setVisibility(View.GONE);
            countryEdit.setVisibility(View.VISIBLE);
            countryEdit.setText(countryView.getText().toString());

            ageView.setVisibility(View.GONE);
            ageEdit.setVisibility(View.VISIBLE);
            ageEdit.setText(ageView.getText().toString());

            opica.setVisibility(View.VISIBLE);
            horse.setVisibility(View.VISIBLE);
            grumpy.setVisibility(View.VISIBLE);
            default_profile.setVisibility(View.VISIBLE);
            profileInfo.setVisibility(View.VISIBLE);
            //editButton.setText("Save");
            item.setTitle("Save");
        } else if (!editing){
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            if (getCurrentFocus()!=null) {
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }

            mDatabaseReference.child("Users").child(userMap.getUserID()).child("name").setValue(mEditText.getText().toString());
            mDatabaseReference.child("Users").child(userMap.getUserID()).child("country").setValue(countryEdit.getText().toString());
            mDatabaseReference.child("Users").child(userMap.getUserID()).child("age").setValue(ageEdit.getText().toString());

            //editButton.setText("Edit");
            item.setTitle("Edit");

            mEditText.setVisibility(View.GONE);
            name.setVisibility(View.VISIBLE);
            name.setText(mEditText.getText().toString());

            countryEdit.setVisibility(View.GONE);
            countryView.setVisibility(View.VISIBLE);
            countryView.setText(countryEdit.getText().toString());

            ageEdit.setVisibility(View.GONE);
            ageView.setVisibility(View.VISIBLE);
            ageView.setText(ageEdit.getText().toString());

            opica.setVisibility(View.GONE);
            horse.setVisibility(View.GONE);
            grumpy.setVisibility(View.GONE);
            default_profile.setVisibility(View.GONE);
            profileInfo.setVisibility(View.GONE);
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
                id.setText(defaultUser.getUserID());
                score.setText(String.valueOf(defaultUser.getPoints()));
                ageView.setText(defaultUser.getAge());
                countryView.setText(defaultUser.getCountry());
                if (defaultUser.getPhotoID()!=null){
                    if (defaultUser.getPhotoID().equals("grumpy")){
                        profileImg.setImageResource(R.drawable.grumpy);
                    }if (defaultUser.getPhotoID().equals("kon")){
                        profileImg.setImageResource(R.drawable.kon);
                    }if (defaultUser.getPhotoID().equals("opica")){
                        profileImg.setImageResource(R.drawable.opica);
                    }if (defaultUser.getPhotoID().equals("0")){
                        profileImg.setImageResource(R.drawable.icon_profile_empty);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (editing){
            Toast.makeText(this, "Please, finish editing your account", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(AccountActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
}
