package com.trasimus.tictactoe.online.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trasimus.tictactoe.online.R;

public class GameSizeChoose extends AppCompatActivity {

    private Bundle mBundle;
    private String p2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_size_choose);

        mBundle = getIntent().getExtras();
        p2 = mBundle.getString("p2");
    }

    public void launchGameActivity3(View view){
        Intent intent = new Intent(GameSizeChoose.this, GameActivity.class);
        if (p2!=null){
            intent.putExtra("p2", p2);
        }
        intent.putExtra("size", 3);
        startActivity(intent);
        finish();
    }

    public void launchGameActivity4(View view){
        Intent intent = new Intent(GameSizeChoose.this, GameActivity.class);
        if (p2!=null){
            intent.putExtra("p2", p2);
        }
        intent.putExtra("size", 4);
        startActivity(intent);
        finish();
    }

    public void launchGameActivity5(View view){
        Intent intent = new Intent(GameSizeChoose.this, GameActivity.class);
        if (p2!=null){
            intent.putExtra("p2", p2);
        }
        intent.putExtra("size", 5);
        startActivity(intent);
        finish();
    }
}
