package com.trasimus.tictactoe.online.Other;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trasimus.tictactoe.online.DefaultUser;
import com.trasimus.tictactoe.online.FontHelper;
import com.trasimus.tictactoe.online.R;
import com.trasimus.tictactoe.online.UserMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

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
        setContentView(com.trasimus.tictactoe.online.R.layout.activity_login);
        FontHelper.setCustomTypeface(findViewById(com.trasimus.tictactoe.online.R.id.view_root));

        mail = (EditText) findViewById(R.id.mailInput);
        pass = (EditText) findViewById(R.id.passwordInput);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            launchAccountActivity();
        }

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
//        if (account != null) {
//            launchAccountActivity();
//        }

        logger = AppEventsLogger.newLogger(this);

        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        com.facebook.AccessToken token = com.facebook.AccessToken.getCurrentAccessToken();

//        if (accessToken != null || token != null) {
//            //Handle Returning User
//            launchAccountActivity();
//        }

        fbLoginButton = (LoginButton) findViewById(com.trasimus.tictactoe.online.R.id.facebook_login_button);
        fbLoginButton.setReadPermissions(Arrays.asList(EMAIL));

        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                com.facebook.AccessToken token1 = loginResult.getAccessToken();
                handleFacebookAccessToken(token1);

            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Login error", Toast.LENGTH_SHORT).show();
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
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            launchAccountActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed " + task.getResult().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            launchAccountActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Error while signing in", Toast.LENGTH_SHORT).show();
                        }

                        // ...
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


        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
            } else {
                if (loginResult.getAccessToken() != null) {
                    launchAccountActivity();
                }
            }
        }
    }

//    private void onLogin(final LoginType loginType) {
//        final Intent intent = new Intent(this, AccountKitActivity.class);
//        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
//                new AccountKitConfiguration.AccountKitConfigurationBuilder(
//                        loginType,
//                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
//        // ... perform additional configuration ...
//        intent.putExtra(
//                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
//                configurationBuilder.build());
//        startActivityForResult(intent, APP_REQUEST_CODE);
//    }

//    public void onPhoneLogin(View view) {
//        onLoginSMS(view);
//        onLogin(LoginType.PHONE);
//    }
//
//    public void onEmailLogin(View view) {
//        onLoginEmail(view);
//        onLogin(LoginType.EMAIL);
//    }

    private void launchAccountActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        checkIfUserExist(user);
    }

    private void checkIfUserExist(FirebaseUser user){
        mailX = user.getEmail();
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("UserMap").child(user.getUid());
        Log.d("test", "checking for user");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("test", "onDataChange started");
                if(!dataSnapshot.exists()) {
                    friends = new ArrayList<ArrayList<String>>();
                    //create new user
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    String uniqueID = UUID.randomUUID().toString();
                    UserMap map = new UserMap(mailX, uniqueID);

                    DefaultUser defaultUser = new DefaultUser(uniqueID, mailX, firebaseUser.getDisplayName(), "" , "", 0, friends, null, null, null, "");
                    Log.d("test", "default user initiated");


                    FirebaseDatabase.getInstance().getReference().child("UserMap").child(firebaseUser.getUid()).setValue(map);
                    Log.d("test", "user map zapisany v databaze");

                    FirebaseDatabase.getInstance().getReference().child("Users").child(uniqueID).setValue(defaultUser);
                    Log.d("test", "toto tu este je?");
                }
                startActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void startActivity(){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void signIn(View view){

        email = mail.getText().toString();
        password = pass.getText().toString();

        if (email.equals("") || password.equals("")){
            Toast.makeText(this, "Please fill in the email and password fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login error: wrong username or password", Toast.LENGTH_SHORT).show();
                        } else {
                            checkIfEmailVerified();
                        }
                    }
                });


    }

    public void signUp(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
            launchAccountActivity();
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(LoginActivity.this, "Please verify your email", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();

            //restart this activity

        }
    }

}
