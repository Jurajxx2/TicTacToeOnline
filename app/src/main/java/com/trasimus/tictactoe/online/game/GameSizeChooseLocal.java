package com.trasimus.tictactoe.online.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.trasimus.tictactoe.online.LocalGameObject;
import com.trasimus.tictactoe.online.R;

public class GameSizeChooseLocal extends AppCompatActivity {

    private LocalGameObject newGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_size_choose_local);

        SharedPreferences mPrefs = getSharedPreferences("MyObject", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        newGame = gson.fromJson( json, LocalGameObject.class);

        if (newGame!=null) {
            if (newGame.isSaved()) {
                AlertDialog a;
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GameSizeChooseLocal.this);
                dialogBuilder.setTitle("Saved game");
                dialogBuilder.setMessage("You have a saved game. Do you want to continue playing?");
                dialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(GameSizeChooseLocal.this, GameActivityLocal.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialogBuilder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        newGame.setSaved(false);
                        SharedPreferences mPrefs = getSharedPreferences("MyObject", MODE_PRIVATE);

                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(newGame); // myObject - instance of MyObject
                        prefsEditor.putString("MyObject", json);
                        prefsEditor.apply();
                    }
                });
                a = dialogBuilder.create();
                a.show();
            }
        }
    }

    public void launchGameActivity3local(View view){
        Intent intent = new Intent(GameSizeChooseLocal.this, GameActivityLocal.class);

        intent.putExtra("size", 3);
        startActivity(intent);
        finish();
    }

    public void launchGameActivity4local(View view){
        Intent intent = new Intent(GameSizeChooseLocal.this, GameActivityLocal.class);

        intent.putExtra("size", 4);
        startActivity(intent);
        finish();
    }

    public void launchGameActivity5local(View view){
        Intent intent = new Intent(GameSizeChooseLocal.this, GameActivityLocal.class);

        intent.putExtra("size", 5);
        startActivity(intent);
        finish();
    }
}
