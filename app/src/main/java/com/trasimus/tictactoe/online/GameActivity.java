package com.trasimus.tictactoe.online;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {
    private LinearLayout mLinearLayout;
    private GridLayout mGridLayout;

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button button10;
    private Button button11;
    private Button button12;
    private Button button13;
    private Button button14;
    private Button button15;
    private Button button16;
    private Button button17;
    private Button button18;
    private Button button19;
    private Button button20;
    private Button button21;
    private Button button22;
    private Button button23;
    private Button button24;
    private Button button25;

    private Button[] tictac;
    private String[] game;
    private Button buttons;
    private int childCount;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.trasimus.tictactoe.online.R.layout.activity_game);

        bundle = getIntent().getExtras();
        Button surrender = (Button) findViewById(com.trasimus.tictactoe.online.R.id.surrender_btn);
        mLinearLayout = (LinearLayout) findViewById(R.id.linear);



        if (bundle.getInt("size") == 3) {
            getLayoutInflater().inflate(R.layout.threexthree, mLinearLayout);
            mGridLayout = (GridLayout) findViewById(R.id.gridLayout);

            tictac = new Button[9];
            game = new String[9];
            childCount = mGridLayout.getChildCount();

            for (int i = 0; i < childCount; i++) {
                View v = mGridLayout.getChildAt(i);
                buttons = (Button) findViewById(v.getId());
                //buttons.setBackgroundColor(2);
                tictac[i] = buttons;
            }

        } else if (bundle.getInt("size") == 4) {
            getLayoutInflater().inflate(R.layout.fourxfour, mLinearLayout);
            mGridLayout = (GridLayout) findViewById(R.id.gridLayout);

            tictac = new Button[16];
            game = new String[16];
            childCount = mGridLayout.getChildCount();

            for (int i = 0; i < childCount; i++) {
                View v = mGridLayout.getChildAt(i);
                buttons = (Button) findViewById(v.getId());
                //buttons.setBackgroundColor(2);
                tictac[i] = buttons;
            }
        } else if (bundle.getInt("size") == 5) {
            getLayoutInflater().inflate(R.layout.fivexfive, mLinearLayout);
            mGridLayout = (GridLayout) findViewById(R.id.gridLayout);

            tictac = new Button[25];
            game = new String[25];
            childCount = mGridLayout.getChildCount();

            for (int i = 0; i < childCount; i++) {
                View v = mGridLayout.getChildAt(i);
                buttons = (Button) findViewById(v.getId());
                //buttons.setBackgroundColor(2);
                tictac[i] = buttons;
            }
        }


        surrender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void doSomething(View view) {
        Toast.makeText(this, "Button clicked with id " + view.getId(), Toast.LENGTH_SHORT).show();
        Button test = (Button) findViewById(view.getId());
        //test.setTextSize(20);
        //test.setText("X");
        test.setBackgroundResource(R.drawable.x);

        if (bundle.getInt("size") == 3) {

            for (int i = 0; i < 9; i++) {
                if (tictac[i].getText().toString().equals("X")) {
                    game[i] = "x";
                }
            }

            if (game[0]=="x" && game[1]=="x" && game[2]=="x") {
                tictac[0].setBackgroundColor(0xFF00FF00);
                tictac[1].setBackgroundColor(0xFF00FF00);
                tictac[2].setBackgroundColor(0xFF00FF00);
            }
            if (game[3]=="x" && game[4]=="x" && game[5]=="x") {
                tictac[3].setBackgroundColor(0xFF00FF00);
                tictac[4].setBackgroundColor(0xFF00FF00);
                tictac[5].setBackgroundColor(0xFF00FF00);
            }
            if (game[6]=="x" && game[7]=="x" && game[8]=="x") {
                tictac[6].setBackgroundColor(0xFF00FF00);
                tictac[7].setBackgroundColor(0xFF00FF00);
                tictac[8].setBackgroundColor(0xFF00FF00);
            }
            if (game[0]=="x" && game[3]=="x" && game[6]=="x") {
                tictac[0].setBackgroundColor(0xFF00FF00);
                tictac[3].setBackgroundColor(0xFF00FF00);
                tictac[6].setBackgroundColor(0xFF00FF00);
            }
            if (game[1]=="x" && game[4]=="x" && game[7]=="x") {
                tictac[1].setBackgroundColor(0xFF00FF00);
                tictac[4].setBackgroundColor(0xFF00FF00);
                tictac[7].setBackgroundColor(0xFF00FF00);
            }
            if (game[2]=="x" && game[5]=="x" && game[8]=="x") {
                tictac[2].setBackgroundColor(0xFF00FF00);
                tictac[5].setBackgroundColor(0xFF00FF00);
                tictac[8].setBackgroundColor(0xFF00FF00);
            }
            if (game[0]=="x" && game[4]=="x" && game[8]=="x") {
                tictac[0].setBackgroundColor(0xFF00FF00);
                tictac[4].setBackgroundColor(0xFF00FF00);
                tictac[8].setBackgroundColor(0xFF00FF00);
            }
            if (game[2]=="x" && game[4]=="x" && game[6]=="x") {
                tictac[2].setBackgroundColor(0xFF00FF00);
                tictac[4].setBackgroundColor(0xFF00FF00);
                tictac[6].setBackgroundColor(0xFF00FF00);
            }
        }


        if (bundle.getInt("size") == 4) {

            for (int i = 0; i < 16; i++) {
                if (tictac[i].getText().toString().equals("X")) {
                    game[i] = "x";
                }
            }

            if (game[0]=="x" && game[1]=="x" && game[2]=="x" && game[3]=="x") {
                tictac[0].setBackgroundColor(0xFF00FF00);
                tictac[1].setBackgroundColor(0xFF00FF00);
                tictac[2].setBackgroundColor(0xFF00FF00);
                tictac[3].setBackgroundColor(0xFF00FF00);
            }
            if (game[4]=="x" && game[5]=="x" && game[6]=="x" && game[7]=="x") {
                tictac[4].setBackgroundColor(0xFF00FF00);
                tictac[5].setBackgroundColor(0xFF00FF00);
                tictac[6].setBackgroundColor(0xFF00FF00);
                tictac[7].setBackgroundColor(0xFF00FF00);
            }
            if (game[8]=="x" && game[9]=="x" && game[10]=="x" && game[11]=="x") {
                tictac[8].setBackgroundColor(0xFF00FF00);
                tictac[9].setBackgroundColor(0xFF00FF00);
                tictac[10].setBackgroundColor(0xFF00FF00);
                tictac[11].setBackgroundColor(0xFF00FF00);
            }
            if (game[12]=="x" && game[13]=="x" && game[14]=="x" && game[15]=="x") {
                tictac[12].setBackgroundColor(0xFF00FF00);
                tictac[13].setBackgroundColor(0xFF00FF00);
                tictac[14].setBackgroundColor(0xFF00FF00);
                tictac[15].setBackgroundColor(0xFF00FF00);
            }
            if (game[0]=="x" && game[4]=="x" && game[8]=="x" && game[12]=="x") {
                tictac[0].setBackgroundColor(0xFF00FF00);
                tictac[4].setBackgroundColor(0xFF00FF00);
                tictac[8].setBackgroundColor(0xFF00FF00);
                tictac[12].setBackgroundColor(0xFF00FF00);
            }
            if (game[1]=="x" && game[5]=="x" && game[9]=="x" && game[13]=="x") {
                tictac[1].setBackgroundColor(0xFF00FF00);
                tictac[5].setBackgroundColor(0xFF00FF00);
                tictac[9].setBackgroundColor(0xFF00FF00);
                tictac[13].setBackgroundColor(0xFF00FF00);
            }
            if (game[2]=="x" && game[6]=="x" && game[10]=="x" && game[14]=="x") {
                tictac[2].setBackgroundColor(0xFF00FF00);
                tictac[6].setBackgroundColor(0xFF00FF00);
                tictac[10].setBackgroundColor(0xFF00FF00);
                tictac[14].setBackgroundColor(0xFF00FF00);
            }
            if (game[3]=="x" && game[7]=="x" && game[11]=="x" && game[15]=="x") {
                tictac[3].setBackgroundColor(0xFF00FF00);
                tictac[7].setBackgroundColor(0xFF00FF00);
                tictac[11].setBackgroundColor(0xFF00FF00);
                tictac[15].setBackgroundColor(0xFF00FF00);
            }
            if (game[0]=="x" && game[5]=="x" && game[10]=="x" && game[15]=="x") {
                tictac[0].setBackgroundColor(0xFF00FF00);
                tictac[5].setBackgroundColor(0xFF00FF00);
                tictac[10].setBackgroundColor(0xFF00FF00);
                tictac[15].setBackgroundColor(0xFF00FF00);
            }
            if (game[3]=="x" && game[6]=="x" && game[9]=="x" && game[12]=="x") {
                tictac[3].setBackgroundColor(0xFF00FF00);
                tictac[6].setBackgroundColor(0xFF00FF00);
                tictac[9].setBackgroundColor(0xFF00FF00);
                tictac[12].setBackgroundColor(0xFF00FF00);
            }
        }



        if (bundle.getInt("size") == 5) {

            for (int i = 0; i < 25; i++) {
                if (tictac[i].getText().toString().equals("X")) {
                    game[i] = "x";
                }
            }

            if (game[0]=="x" && game[1]=="x" && game[2]=="x" && game[3]=="x"  && game[4]=="x") {
                tictac[0].setBackgroundColor(0xFF00FF00);
                tictac[1].setBackgroundColor(0xFF00FF00);
                tictac[2].setBackgroundColor(0xFF00FF00);
                tictac[3].setBackgroundColor(0xFF00FF00);
                tictac[4].setBackgroundColor(0xFF00FF00);
            }
            if (game[5]=="x" && game[6]=="x" && game[7]=="x" && game[8]=="x" && game[9]=="x") {
                tictac[5].setBackgroundColor(0xFF00FF00);
                tictac[6].setBackgroundColor(0xFF00FF00);
                tictac[7].setBackgroundColor(0xFF00FF00);
                tictac[8].setBackgroundColor(0xFF00FF00);
                tictac[9].setBackgroundColor(0xFF00FF00);
            }
            if (game[10]=="x" && game[11]=="x" && game[12]=="x" && game[13]=="x" && game[14]=="x") {
                tictac[10].setBackgroundColor(0xFF00FF00);
                tictac[11].setBackgroundColor(0xFF00FF00);
                tictac[12].setBackgroundColor(0xFF00FF00);
                tictac[13].setBackgroundColor(0xFF00FF00);
                tictac[14].setBackgroundColor(0xFF00FF00);
            }
            if (game[15]=="x" && game[16]=="x" && game[17]=="x" && game[18]=="x" && game[19]=="x") {
                tictac[15].setBackgroundColor(0xFF00FF00);
                tictac[16].setBackgroundColor(0xFF00FF00);
                tictac[17].setBackgroundColor(0xFF00FF00);
                tictac[18].setBackgroundColor(0xFF00FF00);
                tictac[19].setBackgroundColor(0xFF00FF00);
            }
            if (game[20]=="x" && game[21]=="x" && game[22]=="x" && game[23]=="x" && game[24]=="x") {
                tictac[20].setBackgroundColor(0xFF00FF00);
                tictac[21].setBackgroundColor(0xFF00FF00);
                tictac[22].setBackgroundColor(0xFF00FF00);
                tictac[23].setBackgroundColor(0xFF00FF00);
                tictac[24].setBackgroundColor(0xFF00FF00);
            }
            if (game[0]=="x" && game[5]=="x" && game[10]=="x" && game[15]=="x" && game[20]=="x") {
                tictac[0].setBackgroundColor(0xFF00FF00);
                tictac[5].setBackgroundColor(0xFF00FF00);
                tictac[10].setBackgroundColor(0xFF00FF00);
                tictac[15].setBackgroundColor(0xFF00FF00);
                tictac[20].setBackgroundColor(0xFF00FF00);
            }
            if (game[1]=="x" && game[6]=="x" && game[11]=="x" && game[16]=="x" && game[21]=="x") {
                tictac[1].setBackgroundColor(0xFF00FF00);
                tictac[6].setBackgroundColor(0xFF00FF00);
                tictac[11].setBackgroundColor(0xFF00FF00);
                tictac[16].setBackgroundColor(0xFF00FF00);
                tictac[21].setBackgroundColor(0xFF00FF00);
            }
            if (game[2]=="x" && game[7]=="x" && game[12]=="x" && game[17]=="x" && game[22]=="x") {
                tictac[2].setBackgroundColor(0xFF00FF00);
                tictac[7].setBackgroundColor(0xFF00FF00);
                tictac[12].setBackgroundColor(0xFF00FF00);
                tictac[17].setBackgroundColor(0xFF00FF00);
                tictac[22].setBackgroundColor(0xFF00FF00);
            }
            if (game[3]=="x" && game[8]=="x" && game[13]=="x" && game[18]=="x" && game[23]=="x") {
                tictac[3].setBackgroundColor(0xFF00FF00);
                tictac[8].setBackgroundColor(0xFF00FF00);
                tictac[13].setBackgroundColor(0xFF00FF00);
                tictac[18].setBackgroundColor(0xFF00FF00);
                tictac[23].setBackgroundColor(0xFF00FF00);
            }
            if (game[4]=="x" && game[9]=="x" && game[14]=="x" && game[19]=="x" && game[24]=="x") {
                tictac[4].setBackgroundColor(0xFF00FF00);
                tictac[9].setBackgroundColor(0xFF00FF00);
                tictac[14].setBackgroundColor(0xFF00FF00);
                tictac[19].setBackgroundColor(0xFF00FF00);
                tictac[24].setBackgroundColor(0xFF00FF00);
            }
            if (game[0]=="x" && game[6]=="x" && game[12]=="x" && game[18]=="x" && game[24]=="x") {
                tictac[0].setBackgroundColor(0xFF00FF00);
                tictac[6].setBackgroundColor(0xFF00FF00);
                tictac[12].setBackgroundColor(0xFF00FF00);
                tictac[18].setBackgroundColor(0xFF00FF00);
                tictac[24].setBackgroundColor(0xFF00FF00);
            }
            if (game[4]=="x" && game[8]=="x" && game[12]=="x" && game[16]=="x" && game[20]=="x") {
                tictac[4].setBackgroundColor(0xFF00FF00);
                tictac[8].setBackgroundColor(0xFF00FF00);
                tictac[12].setBackgroundColor(0xFF00FF00);
                tictac[16].setBackgroundColor(0xFF00FF00);
                tictac[20].setBackgroundColor(0xFF00FF00);
            }
        }
    }
}
