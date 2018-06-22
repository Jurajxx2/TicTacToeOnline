package com.trasimus.tictactoe.online;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LocalGameObject {
    private boolean isP1start;
    private int winsP1;
    private int winsP2;
    private int draws;
    private int losesP1;
    private int losesP2;
    private boolean isP1win;
    private boolean isP2win;
    private boolean draw;
    private ArrayList<String> gameField;
    private int size;
    private int move;
    private boolean isP1move;
    private boolean isP2move;
    private boolean saved;

    public LocalGameObject(){}

    public LocalGameObject(boolean isP1start, int winsP1, int winsP2, int draws, int losesP1, int losesP2, boolean isP1win, boolean isP2win, boolean draw, int size, int move, boolean isP1move, boolean isP2move, boolean saved){
        this.isP1start = isP1start;
        this.winsP1 = winsP1;
        this.winsP2 = winsP2;
        this.draws = draws;
        this.losesP1 = losesP1;
        this.losesP2 = losesP2;
        this.isP1win = isP1win;
        this.isP2win = isP2win;
        this.draw = draw;
        this.size = size;
        this.move = move;
        this.isP1move = isP1move;
        this.isP2move = isP2move;
        this.saved = saved;
        initiateGameList();
    }

    private void initiateGameList(){
        gameField = new ArrayList<>();

        for (int a=0; a<size*size; a++){
            gameField.add("");
        }
    }

    public void clickedButton(View view, List<Button> tictacList, Boolean playerOne, TextView moveView) {

        Log.d("test", "clickedButton function");

        if (size == 3) {

            Log.d("test", "clickedButton function size 3");
            if (playerOne) {
                Log.d("test", "clickedButton function Player One");
                for (int i = 0; i < 9; i++) {
                    if (tictacList.get(i).getId() == view.getId()) {
                        gameField.set(i, "x");
                    }
                }
            } else {
                Log.d("test", "clickedButton function Player Two");
                for (int i = 0; i < 9; i++) {
                    if (tictacList.get(i).getId() == view.getId()) {
                        gameField.set(i, "o");
                    }
                }
            }
        }

        if (size == 4) {

            if (playerOne) {
                for (int i = 0; i < 16; i++) {
                    if (tictacList.get(i).getId() == view.getId()) {
                        gameField.set(i, "x");
                    }
                }
            } else {
                for (int i = 0; i < 16; i++) {
                    if (tictacList.get(i).getId() == view.getId()) {
                        gameField.set(i, "o");
                    }
                }
            }
        }


        if (size == 5) {

            if (playerOne) {
                for (int i = 0; i < 25; i++) {
                    if(tictacList.get(i).getId()==view.getId()){
                        gameField.set(i, "x");
                    }
                }
            }
            else {
                for (int i = 0; i < 25; i++) {
                    if(tictacList.get(i).getId()==view.getId()){
                        gameField.set(i, "o");
                    }
                }
            }
        }
        move(moveView);
    }

    private void move(TextView moveView){
        move++;
        isP1move =! isP1move;
        isP2move =! isP2move;

        if (isP1move){
            moveView.setText("Player 1 is on the move");
        } else if (isP2move) {
            moveView.setText("Player 2 is on the move");
        }
    }

    public void setWinner(boolean isPlayerOne){
        if (isPlayerOne) {
            setP1win(true);
        }
        else {
            setP2win(true);
        }
    }


    public boolean isP1start() {
        return isP1start;
    }

    public void setP1start(boolean p1start) {
        isP1start = p1start;
    }

    public int getWinsP1() {
        return winsP1;
    }

    public void setWinsP1(int winsP1) {
        this.winsP1 = winsP1;
    }

    public int getWinsP2() {
        return winsP2;
    }

    public void setWinsP2(int winsP2) {
        this.winsP2 = winsP2;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getLosesP1() {
        return losesP1;
    }

    public void setLosesP1(int losesP1) {
        this.losesP1 = losesP1;
    }

    public int getLosesP2() {
        return losesP2;
    }

    public void setLosesP2(int losesP2) {
        this.losesP2 = losesP2;
    }

    public boolean isP1win() {
        return isP1win;
    }

    public void setP1win(boolean p1win) {
        isP1win = p1win;
    }

    public boolean isP2win() {
        return isP2win;
    }

    public void setP2win(boolean p2win) {
        isP2win = p2win;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public ArrayList<String> getGameField() {
        return gameField;
    }

    public void setGameField(ArrayList<String> gameField) {
        this.gameField = gameField;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public boolean isP1move() {
        return isP1move;
    }

    public void setP1move(boolean p1move) {
        isP1move = p1move;
    }

    public boolean isP2move() {
        return isP2move;
    }

    public void setP2move(boolean p2move) {
        isP2move = p2move;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
