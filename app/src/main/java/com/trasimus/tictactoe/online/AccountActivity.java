package com.trasimus.tictactoe.online;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Locale;

public class AccountActivity extends AppCompatActivity {

    TextView name;
    TextView email;
    String username;
    String useremail;
    Uri photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.trasimus.tictactoe.online.R.layout.activity_account);
        FontHelper.setCustomTypeface(findViewById(com.trasimus.tictactoe.online.R.id.view_root));

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.mail);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            username = user.getDisplayName();
            //useremail = user.getEmail();
            photoUrl = user.getPhotoUrl();

            name.setText(username);
            //email.setText(useremail);

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();



            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            email.setText(uid);
        }
    }
}
