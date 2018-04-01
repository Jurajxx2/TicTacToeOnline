package com.trasimus.tictactoe.online;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.accountkit.AccountKit;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {

    Button cancel;
    Button play3;
    Button play4;
    Button play5;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.trasimus.tictactoe.online.R.layout.activity_menu);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        cancel = (Button) findViewById(com.trasimus.tictactoe.online.R.id.button14);
        play3 = (Button) findViewById(com.trasimus.tictactoe.online.R.id.button11);
        play4 = (Button) findViewById(com.trasimus.tictactoe.online.R.id.button12);
        play5 = (Button) findViewById(com.trasimus.tictactoe.online.R.id.button13);
        ListView menu = (ListView) findViewById(com.trasimus.tictactoe.online.R.id.menu);

        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(MenuActivity.this, GameFinderActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        launchAccountActivity();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void hideSelection(View view){
        cancel.setVisibility(View.GONE);
        play3.setVisibility(View.GONE);
        play4.setVisibility(View.GONE);
        play5.setVisibility(View.GONE);

    }

    public void launchGameActivity3(View view){
        Intent intent = new Intent(MenuActivity.this, GameActivity.class);
        intent.putExtra("size", 3);
        startActivity(intent);
    }

    public void launchGameActivity4(View view){
        Intent intent = new Intent(MenuActivity.this, GameActivity.class);
        intent.putExtra("size", 4);
        startActivity(intent);
    }

    public void launchGameActivity5(View view){
        Intent intent = new Intent(MenuActivity.this, GameActivity.class);
        intent.putExtra("size", 5);
        startActivity(intent);
    }

    public void launchAccountActivity() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    public void mLogout(View view){
        mGoogleSignInClient.signOut();

        FirebaseAuth.getInstance().signOut();

        AccountKit.logOut();
        LoginManager.getInstance().logOut();
        launchLoginActivity();
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
