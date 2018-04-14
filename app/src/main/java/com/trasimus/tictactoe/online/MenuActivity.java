package com.trasimus.tictactoe.online;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.AccountKit;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference userRef1;
    private DatabaseReference userRef2;
    private UserMap userMap;
    private DefaultUser defaultUser;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.trasimus.tictactoe.online.R.layout.activity_menu);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getID(mFirebaseUser.getUid());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        ListView menu = (ListView) findViewById(com.trasimus.tictactoe.online.R.id.menu);

        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        if (loading){
                            Toast.makeText(MenuActivity.this, "Please, check your connection", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if (defaultUser.getName()==null){
                            Toast.makeText(MenuActivity.this, "Please, set your name in Account to play a game", Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            Intent intent = new Intent(MenuActivity.this, GameFinderActivity.class);
                            startActivity(intent);
                            break;
                        }
                    case 1:
                        Intent intent2 = new Intent(MenuActivity.this, RankingsActivity.class);
                        startActivity(intent2);
                        break;
                    case 2:
                        launchAccountActivity();
                        break;
                    case 3:
                        Intent intent3 = new Intent(MenuActivity.this, FriendsActivity.class);
                        startActivity(intent3);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    private void checkDatabaseForCurrentUser() {
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseUser.getEmail();

    }

    public void launchAccountActivity() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }

    public void mLogout(View view){
        //mGoogleSignInClient.signOut();

        FirebaseAuth.getInstance().signOut();

        //AccountKit.logOut();
        //LoginManager.getInstance().logOut();
        launchLoginActivity();
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void getID(String playerUID){
        userRef1 = mDatabaseReference.child("UserMap").child(playerUID);
        userRef1.addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void getUserInfo(){
        userRef2 = mDatabaseReference.child("Users").child(userMap.getUserID());
        userRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                defaultUser = dataSnapshot.getValue(DefaultUser.class);
                loading = false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
