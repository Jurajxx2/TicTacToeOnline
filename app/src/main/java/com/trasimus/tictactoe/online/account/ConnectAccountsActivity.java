 package com.trasimus.tictactoe.online.account;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.trasimus.tictactoe.online.R;
import com.trasimus.tictactoe.online.other.RegisterActivity;

import java.util.ArrayList;
import java.util.Arrays;

 public class ConnectAccountsActivity extends AppCompatActivity {

     public static int APP_REQUEST_CODE = 99;
     public static int RC_SIGN_IN = 123;
     LoginButton fbLoginButton;
     CallbackManager callbackManager = CallbackManager.Factory.create();
     AppEventsLogger logger;
     private static final String EMAIL = "email";
     private GoogleSignInClient mGoogleSignInClient;
     private FirebaseAuth mAuth;
     private FirebaseUser currentUser;

     private EditText mail;
     private EditText pass;

     private String email;
     private String password;

     private String mailX;

     private ArrayList<ArrayList<String>> friends;


     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_connect_accounts);

         mail = (EditText) findViewById(R.id.mailInput);
         pass = (EditText) findViewById(R.id.passwordInput);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

         SignInButton signInButton = findViewById(R.id.sign_in_button);
         signInButton.setSize(SignInButton.SIZE_STANDARD);

         // Configure sign-in to request the user's ID, email address, and basic
         // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
         GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestIdToken(getString(R.string.default_web_client_id))
                 .requestEmail()
                 .build();

         // Build a GoogleSignInClient with the options specified by gso.
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

         GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

         logger = AppEventsLogger.newLogger(this);

         AccessToken accessToken = AccountKit.getCurrentAccessToken();
         com.facebook.AccessToken token = com.facebook.AccessToken.getCurrentAccessToken();

         fbLoginButton = (LoginButton) findViewById(com.trasimus.tictactoe.online.R.id.facebook_login_button);
         fbLoginButton.setReadPermissions(Arrays.asList(EMAIL));

         fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
             @Override
             public void onSuccess(LoginResult loginResult) {
                 Toast.makeText(ConnectAccountsActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                 com.facebook.AccessToken token1 = loginResult.getAccessToken();
                 handleFacebookAccessToken(token1);

             }

             @Override
             public void onCancel() {
                 Toast.makeText(ConnectAccountsActivity.this, "Login cancelled", Toast.LENGTH_SHORT).show();
             }

             @Override
             public void onError(FacebookException error) {
                 Toast.makeText(ConnectAccountsActivity.this, "Login error", Toast.LENGTH_SHORT).show();
             }
         });

         signInButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                 startActivityForResult(signInIntent, RC_SIGN_IN);
             }
         });
     }

     private void handleFacebookAccessToken(com.facebook.AccessToken token) {

         AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

         linkCredentials(credential);
     }

     private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

         AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

         linkCredentials(credential);
     }

     public void signInConnect(View view){

         email = mail.getText().toString();
         password = pass.getText().toString();

         if (email.equals("") || password.equals("")){
             Toast.makeText(this, "Please fill in the email and password fields", Toast.LENGTH_SHORT).show();
             return;
         }

         AuthCredential credential = EmailAuthProvider.getCredential(email, password);
         linkCredentials(credential);
     }

     private void linkCredentials(final AuthCredential credential){
         mAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if (task.isSuccessful()) {
                     Toast.makeText(ConnectAccountsActivity.this, "Linking to another account succesful", Toast.LENGTH_SHORT).show();
                 } else {
                     //Zatial tu nic nie je
                 }
             }
         });
     }

     @Override
     protected void onActivityResult(
             final int requestCode,
             final int resultCode,
             final Intent data) {
         callbackManager.onActivityResult(requestCode, resultCode, data);

         super.onActivityResult(requestCode, resultCode, data);

         // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
         if (requestCode == RC_SIGN_IN) {
             // The Task returned from this call is always completed, no need to attach
             // a listener.
             Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
             try {
                 // Google Sign In was successful, authenticate with Firebase
                 GoogleSignInAccount account = task.getResult(ApiException.class);
                 firebaseAuthWithGoogle(account);
             } catch (ApiException e) {
                 // Google Sign In failed, update UI appropriately
                 // ...
                 Toast.makeText(this, "Error while signing in", Toast.LENGTH_SHORT).show();
             }
         }

     }

     private void checkIfEmailVerified()
     {
         FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

         if (user.isEmailVerified())
         {
             // user is verified, so you can finish this activity or send user to activity which you want.
             Toast.makeText(ConnectAccountsActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
         }
         else
         {
             // email is not verified, so just prompt the message to the user and restart this activity.
             // NOTE: don't forget to log out the user.
             Toast.makeText(ConnectAccountsActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();
             FirebaseAuth.getInstance().signOut();

             //restart this activity

         }
     }

 }
