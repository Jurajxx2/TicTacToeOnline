package com.trasimus.tictactoe.online.account;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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

import static android.provider.ContactsContract.Intents.Insert.EMAIL;

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
    private GoogleSignInClient mGoogleSignInClient;
    public static int RC_SIGN_IN = 123;

    private Button deleteAcc;
    private Button editAcc;

    private String[] countries;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mReference;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private String userUID;
    private String provider;
    private SignInButton signInButton;

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
        deleteAcc = (Button) findViewById(R.id.deleteAccount);
        editAcc = (Button) findViewById(R.id.editAccount);

        editButton = (Button) findViewById(R.id.edit);
        mEditText = (EditText) findViewById(R.id.editName);
        editing = false;

        countries = getResources().getStringArray(R.array.countries_array);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        countryEdit.setAdapter(adapter);


        user = FirebaseAuth.getInstance().getCurrentUser();

        provider = user.getProviders().get(0);

        userUID = user.getUid();
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

        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (provider.equals("facebook.com")){
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AccountActivity.this);
                    final LayoutInflater inflater = AccountActivity.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.alertdialog_facebook_content, null);
                    dialogBuilder.setView(dialogView);

                    //final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

                    dialogBuilder.setTitle("Relogin to delete account");
                    //dialogBuilder.setMessage("Enter text below");

                    AlertDialog b = dialogBuilder.create();
                    b.show();

                    Button relogin = (Button) dialogView.findViewById(R.id.FB_relogin);
                    relogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseAuth.getInstance().signOut();
                            LoginManager.getInstance().logOut();

                            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                            intent.putExtra("deletion", true);
                            intent.putExtra("userUID", userUID);
                            intent.putExtra("userID", defaultUser.getUserID());
                            intent.putExtra("gameID", defaultUser.getMyGameID());
                            startActivity(intent);
                            finish();
                        }
                    });

                } else if (provider.equals("google.com")){

                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken("456171505925-4fl47ouejun7b9l5lg12ev8tacriracn.apps.googleusercontent.com")
                            .requestEmail()
                            .build();

                    mGoogleSignInClient = GoogleSignIn.getClient(AccountActivity.this, gso);

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AccountActivity.this);
                    LayoutInflater inflater = AccountActivity.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.alertdialog_google_content, null);
                    dialogBuilder.setView(dialogView);

                    //final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

                    dialogBuilder.setTitle("Relogin to delete account");
                    //dialogBuilder.setMessage("Enter text below");

                    AlertDialog b = dialogBuilder.create();
                    b.show();

                    signInButton = (SignInButton) dialogView.findViewById(R.id.sign_in_button2);

                signInButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                    }
                });

                } else {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AccountActivity.this);
                    LayoutInflater inflater = AccountActivity.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.alertdialog_mail_content, null);
                    dialogBuilder.setView(dialogView);

                    //final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

                    dialogBuilder.setTitle("Relogin to delete account");
                    //dialogBuilder.setMessage("Enter text below");

                    AlertDialog b = dialogBuilder.create();
                    b.show();
                }
            }
        });

        editAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSomething();
            }
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    removeData();
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(AccountActivity.this, "Account deleted", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                                FirebaseAuth.getInstance().signOut();
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(AccountActivity.this, "Deleting was partially successful", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(AccountActivity.this, "Deleting was not successful", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void removeData(){
        mDatabaseReference.child("UserMap").child(userUID).removeValue();
        mDatabaseReference.child("Users").child(defaultUser.getUserID()).removeValue();
        mDatabaseReference.child("games").child(defaultUser.getMyGameID()).removeValue();
    }


    public void reSignIn(View view){
        EditText mail;
        EditText pass;

        mail = (EditText) findViewById(R.id.mailInput);
        pass = (EditText) findViewById(R.id.passwordInput);

        String usermail;
        String password;

        usermail = mail.getText().toString();
        password = pass.getText().toString();

        if (usermail.equals("") || password.equals("")){
            Toast.makeText(this, "Please fill in the email and password fields", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(usermail, password);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    removeData();
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(AccountActivity.this, "Account deleted", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                                FirebaseAuth.getInstance().signOut();
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(AccountActivity.this, "Deleting was partially successful", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(AccountActivity.this, "Deleting was not successful", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void editSomething(){
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
            editAcc.setText("Save");
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
            editAcc.setText("Edit account");

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    }
}
