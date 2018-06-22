package com.trasimus.tictactoe.online.other;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
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
import com.trasimus.tictactoe.online.R;
import com.trasimus.tictactoe.online.account.AccountActivity;
import com.trasimus.tictactoe.online.DefaultUser;
import com.trasimus.tictactoe.online.friends.FriendsActivity;
import com.trasimus.tictactoe.online.game.GameSizeChoose;
import com.trasimus.tictactoe.online.game.GameSizeChooseLocal;

public class MenuActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference userRef2;
    private DefaultUser defaultUser;
    private boolean loading = true;
    private String YOUR_ADMOB_APP_ID;
    private InterstitialAd mInterstitialAd;

    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.trasimus.tictactoe.online.R.layout.activity_menu);

        YOUR_ADMOB_APP_ID = "ca-app-pub-4083999126685128~4669704807";

        MobileAds.initialize(this, YOUR_ADMOB_APP_ID);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //getID(mFirebaseUser.getUid());
        getUserInfo();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAdapter = new ArrayAdapter<String>(this, R.layout.menu_item, R.id.menuItem);
        ListView menu = (ListView) findViewById(com.trasimus.tictactoe.online.R.id.menu);
        menu.setAdapter(mAdapter);
        mAdapter.addAll(this.getResources().getStringArray(R.array.menu));
        mAdapter.add("Logout");

        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        if (loading){
                            Toast.makeText(MenuActivity.this, "Please, check your connection", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if (defaultUser.getName()==null || defaultUser.getName().equals("")){
                            Toast.makeText(MenuActivity.this, "Please, set your name in Account to play a game", Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            Intent intent = new Intent(MenuActivity.this, GameSizeChoose.class);
                            startActivity(intent);
                            break;
                        }
                    case 1:
                        Intent intent5 = new Intent(MenuActivity.this, GameSizeChooseLocal.class);
                        startActivity(intent5);
                        break;
                    case 2:
                        if (loading){
                            Toast.makeText(MenuActivity.this, "Please, check your connection", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        Intent intent2 = new Intent(MenuActivity.this, RankingsActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        if (loading){
                            Toast.makeText(MenuActivity.this, "Please, check your connection", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        launchAccountActivity();
                        break;
                    case 4:
                        if (loading){
                            Toast.makeText(MenuActivity.this, "Please, check your connection", Toast.LENGTH_SHORT).show();
                            break;
                        } if (defaultUser.getName()==null || defaultUser.getName().equals("")){
                        Toast.makeText(MenuActivity.this, "Please, set your name, country of origin and age in Account to play a game", Toast.LENGTH_SHORT).show();
                        break;
                            }else {
                            Intent intent3 = new Intent(MenuActivity.this, FriendsActivity.class);
                            intent3.putExtra("ID", defaultUser.getUserID());
                            startActivity(intent3);
                            break;
                        }
                    case 5:
                        if (loading){
                            Toast.makeText(MenuActivity.this, "Please, check your connection", Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            Intent intent4 = new Intent(MenuActivity.this, OptionsActivity.class);
                            startActivity(intent4);
                        }
                        break;
                    case 6:
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                        launchLoginActivity();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    public void launchAccountActivity() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void getUserInfo(){
        userRef2 = mDatabaseReference.child("Users").child(mFirebaseUser.getUid());
        userRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                defaultUser = dataSnapshot.getValue(DefaultUser.class);
                userRef2.child("lobbyID").setValue("");

                Log.d("test", "Tu zacina service");
                Intent intent = new Intent(MenuActivity.this, GameInvitationListener.class);
                intent.putExtra("userID", defaultUser.getUserID());
                startService(intent);
                Log.d("test", "Service zacala");

                loading = false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
