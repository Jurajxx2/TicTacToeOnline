package com.trasimus.tictactoe.online.Other;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.trasimus.tictactoe.online.R;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText nameView;
    private EditText emailView;
    private EditText password1View;
    private EditText password2View;

    private String name;
    private String email;
    private String password1;
    private String password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        nameView = (EditText) findViewById(R.id.name);
        emailView = (EditText) findViewById(R.id.mail);
        password1View = (EditText) findViewById(R.id.password1);
        password2View = (EditText) findViewById(R.id.password2);

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("test", "Zmena Auth stavu");
                    // User is signed in
                    // NOTE: this Activity should get onpen only when the user is not signed in, otherwise
                    // the user will receive another verification email.
                    sendVerificationEmail();
                } else {
                    // User is signed out
                    Log.d("test", "Ziadna zmena auth stavu");

                }
            }
        });
    }

    private void sendVerificationEmail()
    {
        Log.d("test", "Zacina sa posielat mail");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent

                            Log.d("test", "Mail poslany");
                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            Log.d("test", "Uzivatel odhlaseny");
                            finish();
                            Log.d("test", "Aktivita ukoncena");
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do
                            Log.d("test", "Mail sa neposlal");
                            //restart this activity
//                            overridePendingTransition(0, 0);
//                            finish();
//                            overridePendingTransition(0, 0);
//                            startActivity(getIntent());

                        }
                    }
                });
    }

    public void registerUser(View view){

        Log.d("test", "Starting registering user");
        name = nameView.getText().toString();
        email = emailView.getText().toString();
        password1 = password1View.getText().toString();
        password2 = password2View.getText().toString();

        if (!(name.length()<20 && name.length()>4)){
            Toast.makeText(this, "Your name must contain more than 4 and less than 20 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.contains("@")){
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!(password1.length()<10 && password1.length()>5)){
            Toast.makeText(this, "Your password must contain more than 6 characters and less than 15 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!(password1.equals(password2))){
            Toast.makeText(this, "Your passwords does not match" + password1 + " " + password2, Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("test", "All fields are ok");

        mAuth.createUserWithEmailAndPassword(email, password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(RegisterActivity.this, "User succsesfully created", Toast.LENGTH_SHORT).show();
                        Log.d("test", "User was created");
                    }
                });
    }
}
