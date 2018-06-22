package com.trasimus.tictactoe.online.game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.trasimus.tictactoe.online.GameObject;
import com.trasimus.tictactoe.online.R;
import com.trasimus.tictactoe.online.LocalGameObject;
import com.trasimus.tictactoe.online.other.OptionsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameActivityLocal extends AppCompatActivity {

    private LinearLayout mLinearLayout;
    private GridLayout mGridLayout;

    private Button[] tictac;
    private String[] game;
    private Button buttons;
    private int childCount;
    private Bundle bundle;

    private List<Button> tictacList;

    private ImageView backgroundImage;

    private ConstraintLayout gameBackground;
    private TextView moveView;

    private SharedPreferences prefs; //Settings for char/gamecolor
    public static final String CHAT_PREFS = "ChatPrefs"; //Settings for char/gamecolor

    private LocalGameObject newGame;

    private String p1char;
    private String p2char;

    private String gameColor;

    private AlertDialog b;
    private AlertDialog a;

    private ImageView showStats;
    private ImageView save;
    private ImageView exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_local);

        //Get preferences
        prefs = getSharedPreferences(CHAT_PREFS, 0 );

        //Assign views
        mLinearLayout = (LinearLayout) findViewById(R.id.linear);
        moveView = (TextView) findViewById(R.id.move);
        gameBackground = (ConstraintLayout) findViewById(R.id.gameBackground);
        exit = (ImageView) findViewById(R.id.exitIMG);
        showStats = (ImageView)findViewById(R.id.showStats);
        save = (ImageView)findViewById(R.id.save);

        //Get extra info
        bundle = getIntent().getExtras();
        int gameSize = bundle.getInt("size");
        p1char = prefs.getString("primaryChar", null);
        p2char = prefs.getString("secondaryChar", null);
        if (p1char==null){
            p1char = "x";
        }
        if (p2char==null){
            p2char = "o";
        }

        //Get user preferences
        SharedPreferences prefs = getSharedPreferences(OptionsActivity.CHAT_PREFS, MODE_PRIVATE);
        gameColor = prefs.getString("gameColor", null);
        if (gameColor == null){
            gameBackground.setBackgroundResource(R.drawable.gradient3);
        } else {
            switch (gameColor) {
                case "red":
                    gameBackground.setBackgroundResource(R.drawable.gradient3);
                    break;
                case "blue":
                    gameBackground.setBackgroundResource(R.drawable.gradient4);
                    break;
                case "green":
                    gameBackground.setBackgroundResource(R.drawable.gradient5);
                    break;
                default:
                    gameBackground.setBackgroundResource(R.drawable.gradient3);
                    break;
            }
        }

        SharedPreferences mPrefs = getSharedPreferences("MyObject", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        LocalGameObject obj = gson.fromJson(json, LocalGameObject.class);

        if (obj!=null) {
            if (obj.isSaved()) {
                gameSize = obj.getSize();
            }
        }


        //Initiate game layout
        if (gameSize == 3) {
            getLayoutInflater().inflate(R.layout.threexthree, mLinearLayout);
            mGridLayout = (GridLayout) findViewById(R.id.gridLayout);
            backgroundImage = (ImageView) findViewById(R.id.backgroundImage);

            if (gameColor == null){
                backgroundImage.setImageResource(R.drawable.grid3a);
            } else {
                switch (gameColor) {
                    case "red":
                        backgroundImage.setImageResource(R.drawable.grid3a);
                        break;
                    case "blue":
                        backgroundImage.setImageResource(R.drawable.grid3b);
                        break;
                    case "green":
                        backgroundImage.setImageResource(R.drawable.grid3c);
                        break;
                    default:
                        backgroundImage.setImageResource(R.drawable.grid3a);
                        break;
                }
            }

            tictac = new Button[9];
            game = new String[9];
            childCount = mGridLayout.getChildCount();

            for (int i = 0; i < childCount; i++) {
                View v = mGridLayout.getChildAt(i);
                buttons = (Button) findViewById(v.getId());
                tictac[i] = buttons;
            }

        } else if (gameSize == 4) {
            getLayoutInflater().inflate(R.layout.fourxfour, mLinearLayout);
            mGridLayout = (GridLayout) findViewById(R.id.gridLayout);
            backgroundImage = (ImageView) findViewById(R.id.backgroundImage);

            if (gameColor == null){
                backgroundImage.setImageResource(R.drawable.grid4a);
            } else {
                switch (gameColor) {
                    case "red":
                        backgroundImage.setImageResource(R.drawable.grid4a);
                        break;
                    case "blue":
                        backgroundImage.setImageResource(R.drawable.grid4b);
                        break;
                    case "green":
                        backgroundImage.setImageResource(R.drawable.grid4c);
                        break;
                    default:
                        backgroundImage.setImageResource(R.drawable.grid4a);
                        break;
                }
            }

            tictac = new Button[16];
            game = new String[16];
            childCount = mGridLayout.getChildCount();

            for (int i = 0; i < childCount; i++) {
                View v = mGridLayout.getChildAt(i);
                buttons = (Button) findViewById(v.getId());
                tictac[i] = buttons;
            }
        } else if (gameSize == 5) {
            getLayoutInflater().inflate(R.layout.fivexfive, mLinearLayout);
            mGridLayout = (GridLayout) findViewById(R.id.gridLayout);
            backgroundImage = (ImageView) findViewById(R.id.backgroundImage);

            if (gameColor == null){
                backgroundImage.setImageResource(R.drawable.grid5a);
            } else {
                switch (gameColor) {
                    case "red":
                        backgroundImage.setImageResource(R.drawable.grid5a);
                        break;
                    case "blue":
                        backgroundImage.setImageResource(R.drawable.grid5b);
                        break;
                    case "green":
                        backgroundImage.setImageResource(R.drawable.grid5c);
                        break;
                    default:
                        backgroundImage.setImageResource(R.drawable.grid5a);
                        break;
                }
            }

            tictac = new Button[25];
            game = new String[25];
            childCount = mGridLayout.getChildCount();

            for (int i = 0; i < childCount; i++) {
                View v = mGridLayout.getChildAt(i);
                buttons = (Button) findViewById(v.getId());
                tictac[i] = buttons;
            }
        }

        tictacList = Arrays.asList(tictac);

        if (obj!=null) {
            if (obj.isSaved()) {
                fillGameField(obj);
                if (obj.isP1move()) {
                    moveView.setText("Player 1 is on the move");
                } else {
                    moveView.setText("Player 2 is on the move");
                }
            } else {
                Random mRandom = new Random();
                boolean startPlayer = mRandom.nextBoolean();
                newGame = new LocalGameObject(startPlayer, 0, 0, 0, 0, 0, false, false, false, gameSize, 0, startPlayer, !startPlayer, false);
                if (startPlayer) {
                    moveView.setText("Player 1 is on the move");
                } else {
                    moveView.setText("Player 2 is on the move");
                }
            }
        } else {
            Random mRandom = new Random();
            boolean startPlayer = mRandom.nextBoolean();
            newGame = new LocalGameObject(startPlayer, 0, 0, 0, 0, 0, false, false, false, gameSize, 0, startPlayer, !startPlayer, false);
            if (startPlayer) {
                moveView.setText("Player 1 is on the move");
            } else {
                moveView.setText("Player 2 is on the move");
            }
        }

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitAlert();
            }
        });

        showStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStats();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog x;
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GameActivityLocal.this);

                dialogBuilder.setTitle("Save and exit?");
                dialogBuilder.setMessage("This will save this game and you will be able to continue playing next time");
                dialogBuilder.setPositiveButton("Save and Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        newGame.setSaved(true);
                        SharedPreferences mPrefs = getSharedPreferences("MyObject", MODE_PRIVATE);

                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(newGame); // myObject - instance of MyObject
                        prefsEditor.putString("MyObject", json);
                        prefsEditor.apply();
                        finish();
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", null);
                dialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {

                    }
                });
                x = dialogBuilder.create();
                x.show();
            }
        });
    }

    private void fillGameField(LocalGameObject obj){
        newGame = obj;
        for (int i = 0; i < newGame.getGameField().size(); i++) {

            if (newGame.getGameField().get(i).equals("x")) {
                tictacList.get(i).setEnabled(false);
                switch (p1char) {
                    case "x":
                        tictacList.get(i).setBackgroundResource(R.drawable.x);
                        break;
                    case "a":
                        break;
                    case "b":
                        break;
                    case "c":
                        break;
                    case "d":
                        break;
                    default:
                        tictacList.get(i).setBackgroundResource(R.drawable.x);
                        break;
                }
            } else if (newGame.getGameField().get(i).equals("o")) {
                tictacList.get(i).setEnabled(false);
                switch (p2char) {
                    case "x":
                        tictacList.get(i).setBackgroundResource(R.drawable.o);
                        break;
                    case "a":
                        break;
                    case "b":
                        break;
                    case "c":
                        break;
                    case "d":
                        break;
                    default:
                        tictacList.get(i).setBackgroundResource(R.drawable.o);
                        break;
                }

            }
        }

    }

    private void exitAlert(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GameActivityLocal.this);
        dialogBuilder.setTitle("Exit?");
        dialogBuilder.setMessage("Are you sure you want to exit?");
        dialogBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                newGame.setSaved(false);
                SharedPreferences mPrefs = getSharedPreferences("MyObject", MODE_PRIVATE);

                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(newGame); // myObject - instance of MyObject
                prefsEditor.putString("MyObject", json);
                prefsEditor.apply();
                finish();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);
        a = dialogBuilder.create();
        a.show();
    }

    @Override
    public void onBackPressed() {
        exitAlert();
    }

    public void doSomething(View view) {

        view.setClickable(false);
//        if (newGame.isP1win()||newGame.isP2win()||newGame.isDraw()){
//            b.show();
//            return;
//        }
//
//        for (int i = 0; i < newGame.getGameField().size(); i++) {
//            if (tictacList.get(i).getId() == view.getId()) {
//                if (!newGame.getGameField().get(i).equals("")) {
//                    return;
//                }
//            }
//        }
        if (newGame.isP1move()){
            switch (p1char) {
                case "x":
                    findViewById(view.getId()).setBackgroundResource(R.drawable.x);
                    break;
                case "a":
                    break;
                case "b":
                    break;
                case "c":
                    break;
                case "d":
                    break;
                default:
                    findViewById(view.getId()).setBackgroundResource(R.drawable.x);
                    break;
            }
        }

        if (newGame.isP2move()){
            switch (p2char) {
                case "x":
                    findViewById(view.getId()).setBackgroundResource(R.drawable.o);
                    break;
                case "a":
                    break;
                case "b":
                    break;
                case "c":
                    break;
                case "d":
                    break;
                default:
                    findViewById(view.getId()).setBackgroundResource(R.drawable.o);
                    break;
            }
        }

        newGame.clickedButton(view, tictacList, newGame.isP1move(), moveView);
        checkWinner(newGame.getGameField());
    }

    private void showStats(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GameActivityLocal.this);
        final LayoutInflater inflater = GameActivityLocal.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.restart_game_local, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Hide", null);
        b = dialogBuilder.create();

        TextView winsP1 = dialogView.findViewById(R.id.p1wins);
        TextView losesP1 = dialogView.findViewById(R.id.p1loses);
        TextView drawsP1 = dialogView.findViewById(R.id.p1draws);
        TextView winsP2 = dialogView.findViewById(R.id.p2wins);
        TextView losesP2 = dialogView.findViewById(R.id.p2loses);
        TextView drawsP2 = dialogView.findViewById(R.id.p2draws);
        Button playAgain3 = dialogView.findViewById(R.id.button31);
        Button playAgain4 = dialogView.findViewById(R.id.button32);
        Button playAgain5 = dialogView.findViewById(R.id.button33);

        playAgain3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.hide();
                reinitiateGame(3);
            }
        });

        playAgain4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.hide();
                reinitiateGame(4);
            }
        });

        playAgain5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.hide();
                reinitiateGame(5);
            }
        });

        String helper = "Wins: " + newGame.getWinsP1();
        winsP1.setText(helper);
        helper = "Wins: " + newGame.getWinsP2();
        winsP2.setText(helper);
        helper = "Loses: " + newGame.getLosesP1();
        losesP1.setText(helper);
        helper = "Loses: " + newGame.getLosesP2();
        losesP2.setText(helper);
        helper = "Draws: " + newGame.getDraws();
        drawsP1.setText(helper);
        helper = "Draws: " + newGame.getDraws();
        drawsP2.setText(helper);

        b.show();
    }

    private void reinitiateGame(int gameSize){
        mLinearLayout.removeAllViews();
        //Initiate game layout
        if (gameSize == 3) {
            getLayoutInflater().inflate(R.layout.threexthree, mLinearLayout);
            mGridLayout = (GridLayout) findViewById(R.id.gridLayout);
            backgroundImage = (ImageView) findViewById(R.id.backgroundImage);

            if (gameColor == null){
                backgroundImage.setImageResource(R.drawable.grid3a);
            } else {
                switch (gameColor) {
                    case "red":
                        backgroundImage.setImageResource(R.drawable.grid3a);
                        break;
                    case "blue":
                        backgroundImage.setImageResource(R.drawable.grid3b);
                        break;
                    case "green":
                        backgroundImage.setImageResource(R.drawable.grid3c);
                        break;
                    default:
                        backgroundImage.setImageResource(R.drawable.grid3a);
                        break;
                }
            }

            tictac = new Button[9];
            game = new String[9];
            childCount = mGridLayout.getChildCount();

            for (int i = 0; i < childCount; i++) {
                View v = mGridLayout.getChildAt(i);
                buttons = (Button) findViewById(v.getId());
                tictac[i] = buttons;
            }

        } else if (gameSize == 4) {
            getLayoutInflater().inflate(R.layout.fourxfour, mLinearLayout);
            mGridLayout = (GridLayout) findViewById(R.id.gridLayout);
            backgroundImage = (ImageView) findViewById(R.id.backgroundImage);

            if (gameColor == null){
                backgroundImage.setImageResource(R.drawable.grid4a);
            } else {
                switch (gameColor) {
                    case "red":
                        backgroundImage.setImageResource(R.drawable.grid4a);
                        break;
                    case "blue":
                        backgroundImage.setImageResource(R.drawable.grid4b);
                        break;
                    case "green":
                        backgroundImage.setImageResource(R.drawable.grid4c);
                        break;
                    default:
                        backgroundImage.setImageResource(R.drawable.grid4a);
                        break;
                }
            }

            tictac = new Button[16];
            game = new String[16];
            childCount = mGridLayout.getChildCount();

            for (int i = 0; i < childCount; i++) {
                View v = mGridLayout.getChildAt(i);
                buttons = (Button) findViewById(v.getId());
                tictac[i] = buttons;
            }
        } else if (gameSize == 5) {
            getLayoutInflater().inflate(R.layout.fivexfive, mLinearLayout);
            mGridLayout = (GridLayout) findViewById(R.id.gridLayout);
            backgroundImage = (ImageView) findViewById(R.id.backgroundImage);

            if (gameColor == null){
                backgroundImage.setImageResource(R.drawable.grid5a);
            } else {
                switch (gameColor) {
                    case "red":
                        backgroundImage.setImageResource(R.drawable.grid5a);
                        break;
                    case "blue":
                        backgroundImage.setImageResource(R.drawable.grid5b);
                        break;
                    case "green":
                        backgroundImage.setImageResource(R.drawable.grid5c);
                        break;
                    default:
                        backgroundImage.setImageResource(R.drawable.grid5a);
                        break;
                }
            }

            tictac = new Button[25];
            game = new String[25];
            childCount = mGridLayout.getChildCount();

            for (int i = 0; i < childCount; i++) {
                View v = mGridLayout.getChildAt(i);
                buttons = (Button) findViewById(v.getId());
                tictac[i] = buttons;
            }
        }

        tictacList = Arrays.asList(tictac);
        Random mRandom = new Random();
        boolean startPlayer = mRandom.nextBoolean();
        LocalGameObject newGameObject = new LocalGameObject(startPlayer, newGame.getWinsP1(), newGame.getWinsP2(), newGame.getDraws(), newGame.getLosesP1(), newGame.getLosesP2(), false, false, false, gameSize, 0, startPlayer, !startPlayer, false);
        newGame = newGameObject;
        if (startPlayer){
            moveView.setText("Player 1 is on the move");
        } else {
            moveView.setText("Player 2 is on the move");
        }
    }

    private class CheckWinner extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            checkWinner(newGame.getGameField());
            return null;
        }
    }


    private void checkWinner(ArrayList<String> gameList) {

        if (newGame.getSize() == 3) {

            if (gameList.get(0).equals("x") && gameList.get(1).equals("x") && gameList.get(2).equals("x")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(3).equals("x") && gameList.get(4).equals("x") && gameList.get(5).equals("x")) {
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(6).equals("x") && gameList.get(7).equals("x") && gameList.get(8).equals("x")) {
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(0).equals("x") && gameList.get(3).equals("x") && gameList.get(6).equals("x")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(1).equals("x") && gameList.get(4).equals("x") && gameList.get(7).equals("x")) {
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(2).equals("x") && gameList.get(5).equals("x") && gameList.get(8).equals("x")) {
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(0).equals("x") && gameList.get(4).equals("x") && gameList.get(8).equals("x")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(2).equals("x") && gameList.get(4).equals("x") && gameList.get(6).equals("x")) {
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }


            if (gameList.get(0).equals("o") && gameList.get(1).equals("o") && gameList.get(2).equals("o")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(3).equals("o") && gameList.get(4).equals("o") && gameList.get(5).equals("o")) {
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(6).equals("o") && gameList.get(7).equals("o") && gameList.get(8).equals("o")) {
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(0).equals("o") && gameList.get(3).equals("o") && gameList.get(6).equals("o")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(1).equals("o") && gameList.get(4).equals("o") && gameList.get(7).equals("o")) {
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(2).equals("o") && gameList.get(5).equals("o") && gameList.get(8).equals("o")) {
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(0).equals("o") && gameList.get(4).equals("o") && gameList.get(8).equals("o")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(2).equals("o") && gameList.get(4).equals("o") && gameList.get(6).equals("o")) {
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
        }


        if (newGame.getSize() == 4) {

            if (gameList.get(0).equals("x") && gameList.get(1).equals("x") && gameList.get(2).equals("x") && gameList.get(3).equals("x")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(4).equals("x") && gameList.get(5).equals("x") && gameList.get(6).equals("x") && gameList.get(7).equals("x")) {
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(8).equals("x") && gameList.get(9).equals("x") && gameList.get(10).equals("x") && gameList.get(11).equals("x")) {
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(11).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(12).equals("x") && gameList.get(13).equals("x") && gameList.get(14).equals("x") && gameList.get(15).equals("x")) {
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(13).setBackgroundColor(0xFF00FF00);
                tictacList.get(14).setBackgroundColor(0xFF00FF00);
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(0).equals("x") && gameList.get(4).equals("x") && gameList.get(8).equals("x") && gameList.get(12).equals("x")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(1).equals("x") && gameList.get(5).equals("x") && gameList.get(9).equals("x") && gameList.get(13).equals("x")) {
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                tictacList.get(13).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(2).equals("x") && gameList.get(6).equals("x") && gameList.get(10).equals("x") && gameList.get(14).equals("x")) {
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(14).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(3).equals("x") && gameList.get(7).equals("x") && gameList.get(11).equals("x") && gameList.get(15).equals("x")) {
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                tictacList.get(11).setBackgroundColor(0xFF00FF00);
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(0).equals("x") && gameList.get(5).equals("x") && gameList.get(10).equals("x") && gameList.get(15).equals("x")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(3).equals("x") && gameList.get(6).equals("x") && gameList.get(9).equals("x") && gameList.get(12).equals("x")) {
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }


            if (gameList.get(0).equals("o") && gameList.get(1).equals("o") && gameList.get(2).equals("o") && gameList.get(3).equals("o")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(4).equals("o") && gameList.get(5).equals("o") && gameList.get(6).equals("o") && gameList.get(7).equals("o")) {
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(8).equals("o") && gameList.get(9).equals("o") && gameList.get(10).equals("o") && gameList.get(11).equals("o")) {
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(11).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(12).equals("o") && gameList.get(13).equals("o") && gameList.get(14).equals("o") && gameList.get(15).equals("o")) {
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(13).setBackgroundColor(0xFF00FF00);
                tictacList.get(14).setBackgroundColor(0xFF00FF00);
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(0).equals("o") && gameList.get(4).equals("o") && gameList.get(8).equals("o") && gameList.get(12).equals("o")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(1).equals("o") && gameList.get(5).equals("o") && gameList.get(9).equals("o") && gameList.get(13).equals("o")) {
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                tictacList.get(13).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(2).equals("o") && gameList.get(6).equals("o") && gameList.get(10).equals("o") && gameList.get(14).equals("o")) {
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(14).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(3).equals("o") && gameList.get(7).equals("o") && gameList.get(11).equals("o") && gameList.get(15).equals("o")) {
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                tictacList.get(11).setBackgroundColor(0xFF00FF00);
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(0).equals("o") && gameList.get(5).equals("o") && gameList.get(10).equals("o") && gameList.get(15).equals("o")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(3).equals("o") && gameList.get(6).equals("o") && gameList.get(9).equals("o") && gameList.get(12).equals("o")) {
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }

        }


        if (newGame.getSize() == 5) {

            if (gameList.get(0).equals("x") && gameList.get(1).equals("x") && gameList.get(2).equals("x") && gameList.get(3).equals("x") && gameList.get(4).equals("x")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(5).equals("x") && gameList.get(6).equals("x") && gameList.get(7).equals("x") && gameList.get(8).equals("x") && gameList.get(9).equals("x")) {
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(10).equals("x") && gameList.get(11).equals("x") && gameList.get(12).equals("x") && gameList.get(13).equals("x") && gameList.get(14).equals("x")) {
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(11).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(13).setBackgroundColor(0xFF00FF00);
                tictacList.get(14).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(15).equals("x") && gameList.get(16).equals("x") && gameList.get(17).equals("x") && gameList.get(18).equals("x") && gameList.get(19).equals("x")) {
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                tictacList.get(16).setBackgroundColor(0xFF00FF00);
                tictacList.get(17).setBackgroundColor(0xFF00FF00);
                tictacList.get(18).setBackgroundColor(0xFF00FF00);
                tictacList.get(19).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(20).equals("x") && gameList.get(21).equals("x") && gameList.get(22).equals("x") && gameList.get(23).equals("x") && gameList.get(24).equals("x")) {
                tictacList.get(20).setBackgroundColor(0xFF00FF00);
                tictacList.get(21).setBackgroundColor(0xFF00FF00);
                tictacList.get(22).setBackgroundColor(0xFF00FF00);
                tictacList.get(23).setBackgroundColor(0xFF00FF00);
                tictacList.get(24).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(0).equals("x") && gameList.get(5).equals("x") && gameList.get(10).equals("x") && gameList.get(15).equals("x") && gameList.get(20).equals("x")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                tictacList.get(20).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(1).equals("x") && gameList.get(6).equals("x") && gameList.get(11).equals("x") && gameList.get(16).equals("x") && gameList.get(21).equals("x")) {
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(11).setBackgroundColor(0xFF00FF00);
                tictacList.get(16).setBackgroundColor(0xFF00FF00);
                tictacList.get(21).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(2).equals("x") && gameList.get(7).equals("x") && gameList.get(12).equals("x") && gameList.get(17).equals("x") && gameList.get(22).equals("x")) {
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(17).setBackgroundColor(0xFF00FF00);
                tictacList.get(22).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(3).equals("x") && gameList.get(8).equals("x") && gameList.get(13).equals("x") && gameList.get(18).equals("x") && gameList.get(23).equals("x")) {
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(13).setBackgroundColor(0xFF00FF00);
                tictacList.get(18).setBackgroundColor(0xFF00FF00);
                tictacList.get(23).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(4).equals("x") && gameList.get(9).equals("x") && gameList.get(14).equals("x") && gameList.get(19).equals("x") && gameList.get(24).equals("x")) {
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                tictacList.get(14).setBackgroundColor(0xFF00FF00);
                tictacList.get(19).setBackgroundColor(0xFF00FF00);
                tictacList.get(24).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(0).equals("x") && gameList.get(6).equals("x") && gameList.get(12).equals("x") && gameList.get(18).equals("x") && gameList.get(24).equals("x")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(18).setBackgroundColor(0xFF00FF00);
                tictacList.get(24).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }
            if (gameList.get(4).equals("x") && gameList.get(8).equals("x") && gameList.get(12).equals("x") && gameList.get(16).equals("x") && gameList.get(20).equals("x")) {
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(16).setBackgroundColor(0xFF00FF00);
                tictacList.get(20).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(true);
            }


            if (gameList.get(0).equals("o") && gameList.get(1).equals("o") && gameList.get(2).equals("o") && gameList.get(3).equals("o") && gameList.get(4).equals("o")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(5).equals("o") && gameList.get(6).equals("o") && gameList.get(7).equals("o") && gameList.get(8).equals("o") && gameList.get(9).equals("o")) {
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(10).equals("o") && gameList.get(11).equals("o") && gameList.get(12).equals("o") && gameList.get(13).equals("o") && gameList.get(14).equals("o")) {
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(11).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(13).setBackgroundColor(0xFF00FF00);
                tictacList.get(14).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(15).equals("o") && gameList.get(16).equals("o") && gameList.get(17).equals("o") && gameList.get(18).equals("o") && gameList.get(19).equals("o")) {
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                tictacList.get(16).setBackgroundColor(0xFF00FF00);
                tictacList.get(17).setBackgroundColor(0xFF00FF00);
                tictacList.get(18).setBackgroundColor(0xFF00FF00);
                tictacList.get(19).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(20).equals("o") && gameList.get(21).equals("o") && gameList.get(22).equals("o") && gameList.get(23).equals("o") && gameList.get(24).equals("o")) {
                tictacList.get(20).setBackgroundColor(0xFF00FF00);
                tictacList.get(21).setBackgroundColor(0xFF00FF00);
                tictacList.get(22).setBackgroundColor(0xFF00FF00);
                tictacList.get(23).setBackgroundColor(0xFF00FF00);
                tictacList.get(24).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(0).equals("o") && gameList.get(5).equals("o") && gameList.get(10).equals("o") && gameList.get(15).equals("o") && gameList.get(20).equals("o")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(10).setBackgroundColor(0xFF00FF00);
                tictacList.get(15).setBackgroundColor(0xFF00FF00);
                tictacList.get(20).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(1).equals("o") && gameList.get(6).equals("o") && gameList.get(11).equals("o") && gameList.get(16).equals("o") && gameList.get(21).equals("o")) {
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(11).setBackgroundColor(0xFF00FF00);
                tictacList.get(16).setBackgroundColor(0xFF00FF00);
                tictacList.get(21).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(2).equals("o") && gameList.get(7).equals("o") && gameList.get(12).equals("o") && gameList.get(17).equals("o") && gameList.get(22).equals("o")) {
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(17).setBackgroundColor(0xFF00FF00);
                tictacList.get(22).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(3).equals("o") && gameList.get(8).equals("o") && gameList.get(13).equals("o") && gameList.get(18).equals("o") && gameList.get(23).equals("o")) {
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(13).setBackgroundColor(0xFF00FF00);
                tictacList.get(18).setBackgroundColor(0xFF00FF00);
                tictacList.get(23).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(4).equals("o") && gameList.get(9).equals("o") && gameList.get(14).equals("o") && gameList.get(19).equals("o") && gameList.get(24).equals("o")) {
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(9).setBackgroundColor(0xFF00FF00);
                tictacList.get(14).setBackgroundColor(0xFF00FF00);
                tictacList.get(19).setBackgroundColor(0xFF00FF00);
                tictacList.get(24).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(0).equals("o") && gameList.get(6).equals("o") && gameList.get(12).equals("o") && gameList.get(18).equals("o") && gameList.get(24).equals("o")) {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(18).setBackgroundColor(0xFF00FF00);
                tictacList.get(24).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
            if (gameList.get(4).equals("o") && gameList.get(8).equals("o") && gameList.get(12).equals("o") && gameList.get(16).equals("o") && gameList.get(20).equals("o")) {
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
                tictacList.get(12).setBackgroundColor(0xFF00FF00);
                tictacList.get(16).setBackgroundColor(0xFF00FF00);
                tictacList.get(20).setBackgroundColor(0xFF00FF00);
                newGame.setWinner(false);
            }
        }
        if ((newGame.getMove() == newGame.getGameField().size()) && (!newGame.isP1win() && !newGame.isP2win())) {
            newGame.setDraw(true);
        }
        if (newGame.isP1win() || newGame.isP2win() || newGame.isDraw()){
            if (newGame.isP1win()){
                newGame.setWinsP1(newGame.getWinsP1() + 1);
            } else if (newGame.isP2win()){
                newGame.setWinsP2(newGame.getWinsP2() + 1);
            } else if (newGame.isDraw()){
                newGame.setDraws(newGame.getDraws() + 1);
            }
            for (int i = 0; i < newGame.getGameField().size(); i++) {
                tictacList.get(i).setEnabled(false);
            }
            showStats();
        }
    }
}
