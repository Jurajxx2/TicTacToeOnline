package com.trasimus.tictactoe.online.other;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
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
import com.trasimus.tictactoe.online.game.GameActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    public static int APP_REQUEST_CODE = 99;
    public static int RC_SIGN_IN = 123;
    LoginButton fbLoginButton;
    CallbackManager callbackManager = CallbackManager.Factory.create();
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

    private DatabaseReference mReference;

    private String key;

    private Bundle mBundle;
    private boolean deletion;
    private String userUID;
    private String gameID;
    private FirebaseUser user;

    private Button otherProviders;
    private AlertDialog b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.trasimus.tictactoe.online.R.layout.activity_login);
        FontHelper.setCustomTypeface(findViewById(com.trasimus.tictactoe.online.R.id.view_root));

        //TestFairy.begin(this, "2574a6227e6a3e46d0d28cf1584fb72823002c36");

        mBundle = getIntent().getExtras();
        deletion = mBundle.getBoolean("deletion");
        userUID = mBundle.getString("userUID");
        gameID = mBundle.getString("gameID");

        otherProviders = (Button) findViewById(R.id.loginProviders);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        final LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.login_posibilities, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setNegativeButton("Close", null);
        b = dialogBuilder.create();

        mail = dialogView.findViewById(R.id.mailInput);
        pass = dialogView.findViewById(R.id.passwordInput);
        fbLoginButton =  dialogView.findViewById(com.trasimus.tictactoe.online.R.id.facebook_login_button);

        Button goOffline = findViewById(R.id.offline);

        goOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MenuActivityOffline.class);
                startActivity(intent);
                finish();
            }
        });

        otherProviders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.show();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            launchMenuActivity();
        }

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("456171505925-4fl47ouejun7b9l5lg12ev8tacriracn.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


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
                Toast.makeText(LoginActivity.this, "Login cancelled, try restarting your device", Toast.LENGTH_SHORT).show();
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
                            launchMenuActivity();
                        } else if (!task.isSuccessful()) {
                            Log.w("test", "signInWithCredential", task.getException());
                            Toast.makeText(getApplicationContext(), "Firebase Facebook login failed",
                                    Toast.LENGTH_SHORT).show();

                            if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "User with Email id already exists, please login with your default login provider",
                                        Toast.LENGTH_LONG).show();
                            }
                            LoginManager.getInstance().logOut();
                        }
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
                            launchMenuActivity();
                        } else if (!task.isSuccessful()) {
                            Log.w("test", "signInWithCredential", task.getException());
                            Toast.makeText(getApplicationContext(), "Firebase Google login failed",
                                    Toast.LENGTH_SHORT).show();

                            if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "User with Email id already exists, please login with your default login provider",
                                        Toast.LENGTH_LONG).show();
                            }
                            LoginManager.getInstance().logOut();
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

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed
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
                    launchMenuActivity();
                }
            }
        }
    }

    private void launchMenuActivity() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (deletion){

            new AlertDialog.Builder(this)
                    .setTitle("Delete account")
                    .setMessage("Are you sure you want to delete your account?")
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            checkIfUserExist(user);
                        }
                    })
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            FirebaseDatabase.getInstance().getReference().child("Users").child(userUID).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("games").child(gameID).removeValue();

                            FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(LoginActivity.this, "Account deleted", Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                        LoginManager.getInstance().logOut();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Deleting was partially successful", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }).create().show();
        } else {
            checkIfUserExist(user);
        }
    }

    private void checkIfUserExist(FirebaseUser user){
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        Log.d("test", "checking for user");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("test", "onDataChange started");
                if(!dataSnapshot.exists()) {
                    friends = new ArrayList<ArrayList<String>>();
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    mReference = FirebaseDatabase.getInstance().getReference();

                    key = mReference.child("games").push().getKey();

                    //String uniqueID = UUID.randomUUID().toString();
                    DefaultUser defaultUser = new DefaultUser(firebaseUser.getUid(), mailX, "New user", "" , "", 0, friends, "", "0", key, 0, 0, 0, false, "", false, false, "", friends);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).setValue(defaultUser);
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
            launchMenuActivity();
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Email not verified")
                    .setMessage("Your email was not verified. Do you want to resend email?")
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseAuth.getInstance().signOut();
                            refreshActivity();
                        }
                    })
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // email sent

                                                Log.d("test", "Mail poslany");
                                                // after email is sent just logout the user and finish this activity
                                                Toast.makeText(LoginActivity.this, "Verification email was sent", Toast.LENGTH_LONG).show();
                                            }
                                            else
                                            {
                                                // email not sent, so display message and restart the activity or do whatever you wish to do
                                                Log.d("test", "Mail sa neposlal");
                                                Toast.makeText(LoginActivity.this, "Failed to send email. Try again later", Toast.LENGTH_LONG).show();


                                            }
                                            FirebaseAuth.getInstance().signOut();
                                            refreshActivity();
                                        }
                                    });
                        }
                    }).create().show();


            //restart this activity

        }
    }
    private void refreshActivity(){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

}
