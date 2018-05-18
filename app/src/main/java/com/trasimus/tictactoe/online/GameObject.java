package com.trasimus.tictactoe.online;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private ArrayList<String> gameList;
    private String player1;
    private String player2;
    private Boolean moveP1;
    private Boolean moveP2;
    private Boolean startPlayer;
    private int move;
    private int viewID;
    private int size;
    private String gameID;
    private Boolean winnerP1;
    private Boolean winnerP2;
    private Boolean connectedP1;
    private Boolean connectedP2;
    private Boolean draw;
    private String p1name;
    private String p2name;
    private boolean isDeleted;
    private boolean p1paused;
    private boolean p2paused;
    private ArrayList<ArrayList<String>> messages;


    public GameObject(){

    }

    public GameObject(String gameID, String player1, String player2, int size, Boolean moveP1, Boolean moveP2, Boolean startPlayer, int move, int viewID, Boolean winnerP1, Boolean winnerP2, Boolean connectedP1, Boolean connectedP2, Boolean draw, String p1name, String p2name, boolean isDeleted, boolean p1paused, boolean p2paused, ArrayList<ArrayList<String>> messages){
      this.player1 = player1;
      this.player2 = player2;
      this.moveP1 = moveP1;
      this.moveP2 = moveP2;
      this.startPlayer = startPlayer;
      this.move = move;
      this.viewID = viewID;
      this.size = size;
      this.gameID = gameID;
      this.winnerP1 = winnerP1;
      this.winnerP2 = winnerP2;
      this.connectedP1 = connectedP1;
      this.connectedP2 = connectedP2;
      this.draw = draw;
      this.p1name = p1name;
      this.p2name = p2name;
      this.isDeleted = isDeleted;
      this.p1paused = p1paused;
      this.p2paused = p2paused;
      this.messages = messages;
      //this.clickedButton = clickedButton;
      initiateGameList();
    }

    private void initiateGameList(){
        gameList = new ArrayList<>();

        for (int a=0; a<size*size; a++){
            gameList.add("");
        }
    }

    public boolean movePlayer(String player){
        Boolean isOnMove = false;

        if (player == player1) {
            if (moveP1) {
                isOnMove = true;
            }
            else {
                isOnMove = false;
            }
        }
        else if (player == player2){
            if (moveP2) {
                isOnMove = true;
            }
            else {
                isOnMove = false;
            }
        }
        return isOnMove;
    }

    public void move(){
        this.move++;
        moveP1 = !moveP1;
        moveP2 = !moveP2;
    }

    public void enableIfIsOnMove(Boolean isOnMove, List<Button> tictacList){
        for (int i = 0; i < size*size; i++) {
            tictacList.get(i).setEnabled(isOnMove);
        }
    }

    public void setWinner(boolean isPlayerOne){
        if (isPlayerOne) {
            setWinnerP1(true);
        }
        else {
            setWinnerP2(true);
        }
    }

    public void clickedButton(View view, List<Button> tictacList, Boolean playerOne) {

        Log.d("test", "clickedButton function");
        this.setViewID(view.getId());

        if (size == 3) {

            Log.d("test", "clickedButton function size 3");
            if (playerOne) {
                Log.d("test", "clickedButton function Player One");
                for (int i = 0; i < 9; i++) {
                    if(tictacList.get(i).getId()==view.getId()){
                        gameList.set(i, "x");
                    }
                }
            }
            else {
                Log.d("test", "clickedButton function Player Two");
                for (int i = 0; i < 9; i++) {
                    if(tictacList.get(i).getId()==view.getId()){
                        gameList.set(i, "o");
                    }
                }
            }
        }

        if (size == 4) {

            if (playerOne) {
                for (int i = 0; i < 16; i++) {
                    if(tictacList.get(i).getId()==view.getId()){
                        gameList.set(i, "x");
                    }
                }
            }
            else {
                for (int i = 0; i < 16; i++) {
                    if(tictacList.get(i).getId()==view.getId()){
                        gameList.set(i, "o");
                    }
                }
            }
        }


        if (size == 5) {

            if (playerOne) {
                for (int i = 0; i < 25; i++) {
                    if(tictacList.get(i).getId()==view.getId()){
                        gameList.set(i, "x");
                    }
                }
            }
            else {
                for (int i = 0; i < 25; i++) {
                    if(tictacList.get(i).getId()==view.getId()){
                        gameList.set(i, "o");
                    }
                }
            }
        }


//        if (size == 3) {
//
//            Log.d("test", "clickedButton function size 3");
//            if (playerOne) {
//                Log.d("test", "clickedButton function Player One");
//                for (int i = 0; i < 9; i++) {
//                    if(tictacList.get(i).getId()==view.getId()){
//                        gameList.set(i, "x");
//                    }
//                }
//            }
//            else {
//                Log.d("test", "clickedButton function Player Two");
//                for (int i = 0; i < 9; i++) {
//                    if(tictacList.get(i).getId()==view.getId()){
//                        gameList.set(i, "o");
//                    }
//                }
//            }
//
//
//            if (gameList.get(0)=="x" && gameList.get(1)=="x" && gameList.get(2)=="x") {
//                tictacList.get(0).setBackgroundColor(0xFF00FF00);
//                tictacList.get(1).setBackgroundColor(0xFF00FF00);
//                tictacList.get(2).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//                Log.d("test", "clickedButton function Winner SET");
//            }
//            if (gameList.get(3)=="x" && gameList.get(4)=="x" && gameList.get(5)=="x") {
//                tictacList.get(3).setBackgroundColor(0xFF00FF00);
//                tictacList.get(4).setBackgroundColor(0xFF00FF00);
//                tictacList.get(5).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(6)=="x" && gameList.get(7)=="x" && gameList.get(8)=="x") {
//                tictacList.get(6).setBackgroundColor(0xFF00FF00);
//                tictacList.get(7).setBackgroundColor(0xFF00FF00);
//                tictacList.get(8).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(0)=="x" && gameList.get(3)=="x" && gameList.get(6)=="x") {
//                tictacList.get(0).setBackgroundColor(0xFF00FF00);
//                tictacList.get(3).setBackgroundColor(0xFF00FF00);
//                tictacList.get(6).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(1)=="x" && gameList.get(4)=="x" && gameList.get(7)=="x") {
//                tictacList.get(1).setBackgroundColor(0xFF00FF00);
//                tictacList.get(4).setBackgroundColor(0xFF00FF00);
//                tictacList.get(7).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(2)=="x" && gameList.get(5)=="x" && gameList.get(8)=="x") {
//                tictacList.get(2).setBackgroundColor(0xFF00FF00);
//                tictacList.get(5).setBackgroundColor(0xFF00FF00);
//                tictacList.get(8).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(0)=="x" && gameList.get(4)=="x" && gameList.get(8)=="x") {
//                tictacList.get(0).setBackgroundColor(0xFF00FF00);
//                tictacList.get(4).setBackgroundColor(0xFF00FF00);
//                tictacList.get(8).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(2)=="x" && gameList.get(4)=="x" && gameList.get(6)=="x") {
//                tictacList.get(2).setBackgroundColor(0xFF00FF00);
//                tictacList.get(4).setBackgroundColor(0xFF00FF00);
//                tictacList.get(6).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//
//
//            if (gameList.get(0)=="o" && gameList.get(1)=="o" && gameList.get(2)=="o") {
//                tictacList.get(0).setBackgroundColor(0xFF00FF00);
//                tictacList.get(1).setBackgroundColor(0xFF00FF00);
//                tictacList.get(2).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(3)=="o" && gameList.get(4)=="o" && gameList.get(5)=="o") {
//                tictacList.get(3).setBackgroundColor(0xFF00FF00);
//                tictacList.get(4).setBackgroundColor(0xFF00FF00);
//                tictacList.get(5).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(6)=="o" && gameList.get(7)=="o" && gameList.get(8)=="o") {
//                tictacList.get(6).setBackgroundColor(0xFF00FF00);
//                tictacList.get(7).setBackgroundColor(0xFF00FF00);
//                tictacList.get(8).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(0)=="o" && gameList.get(3)=="o" && gameList.get(6)=="o") {
//                tictacList.get(0).setBackgroundColor(0xFF00FF00);
//                tictacList.get(3).setBackgroundColor(0xFF00FF00);
//                tictacList.get(6).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(1)=="o" && gameList.get(4)=="o" && gameList.get(7)=="o") {
//                tictacList.get(1).setBackgroundColor(0xFF00FF00);
//                tictacList.get(4).setBackgroundColor(0xFF00FF00);
//                tictacList.get(7).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(2)=="o" && gameList.get(5)=="o" && gameList.get(8)=="o") {
//                tictacList.get(2).setBackgroundColor(0xFF00FF00);
//                tictacList.get(5).setBackgroundColor(0xFF00FF00);
//                tictacList.get(8).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(0)=="o" && gameList.get(4)=="o" && gameList.get(8)=="o") {
//                tictacList.get(0).setBackgroundColor(0xFF00FF00);
//                tictacList.get(4).setBackgroundColor(0xFF00FF00);
//                tictacList.get(8).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(2)=="o" && gameList.get(4)=="o" && gameList.get(6)=="o") {
//                tictacList.get(2).setBackgroundColor(0xFF00FF00);
//                tictacList.get(4).setBackgroundColor(0xFF00FF00);
//                tictacList.get(6).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//        }
//
//
//        if (size == 4) {
//
//            if (playerOne) {
//                for (int i = 0; i < 16; i++) {
//                    if(tictacList.get(i).getId()==view.getId()){
//                        gameList.set(i, "x");
//                    }
//                }
//            }
//            else {
//                for (int i = 0; i < 16; i++) {
//                    if(tictacList.get(i).getId()==view.getId()){
//                        gameList.set(i, "o");
//                    }
//                }
//            }
//
//            if (gameList.get(0)=="x" && gameList.get(1)=="x" && gameList.get(2)=="x" && gameList.get(3)=="x") {
//                tictacList.get(0).setBackgroundColor(0xFF00FF00);
//                tictacList.get(1).setBackgroundColor(0xFF00FF00);
//                tictacList.get(2).setBackgroundColor(0xFF00FF00);
//                tictacList.get(3).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(4)=="x" && gameList.get(5)=="x" && gameList.get(6)=="x" && gameList.get(7)=="x") {
//                tictacList.get(4).setBackgroundColor(0xFF00FF00);
//                tictacList.get(5).setBackgroundColor(0xFF00FF00);
//                tictacList.get(6).setBackgroundColor(0xFF00FF00);
//                tictacList.get(7).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(8)=="x" && gameList.get(9)=="x" && gameList.get(10)=="x" && gameList.get(11)=="x") {
//                tictacList.get(8).setBackgroundColor(0xFF00FF00);
//                tictacList.get(9).setBackgroundColor(0xFF00FF00);
//                tictacList.get(10).setBackgroundColor(0xFF00FF00);
//                tictacList.get(11).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(12)=="x" && gameList.get(13)=="x" && gameList.get(14)=="x" && gameList.get(15)=="x") {
//                tictacList.get(12).setBackgroundColor(0xFF00FF00);
//                tictacList.get(13).setBackgroundColor(0xFF00FF00);
//                tictacList.get(14).setBackgroundColor(0xFF00FF00);
//                tictacList.get(15).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(0)=="x" && gameList.get(4)=="x" && gameList.get(8)=="x" && gameList.get(12)=="x") {
//                tictacList.get(0).setBackgroundColor(0xFF00FF00);
//                tictacList.get(4).setBackgroundColor(0xFF00FF00);
//                tictacList.get(8).setBackgroundColor(0xFF00FF00);
//                tictacList.get(12).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(1)=="x" && gameList.get(5)=="x" && gameList.get(9)=="x" && gameList.get(13)=="x") {
//                tictacList.get(1).setBackgroundColor(0xFF00FF00);
//                tictacList.get(5).setBackgroundColor(0xFF00FF00);
//                tictacList.get(9).setBackgroundColor(0xFF00FF00);
//                tictacList.get(13).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(2)=="x" && gameList.get(6)=="x" && gameList.get(10)=="x" && gameList.get(14)=="x") {
//                tictacList.get(2).setBackgroundColor(0xFF00FF00);
//                tictacList.get(6).setBackgroundColor(0xFF00FF00);
//                tictacList.get(10).setBackgroundColor(0xFF00FF00);
//                tictacList.get(14).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(3)=="x" && gameList.get(7)=="x" && gameList.get(11)=="x" && gameList.get(15)=="x") {
//                tictacList.get(3).setBackgroundColor(0xFF00FF00);
//                tictacList.get(7).setBackgroundColor(0xFF00FF00);
//                tictacList.get(11).setBackgroundColor(0xFF00FF00);
//                tictacList.get(15).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(0)=="x" && gameList.get(5)=="x" && gameList.get(10)=="x" && gameList.get(15)=="x") {
//                tictacList.get(0).setBackgroundColor(0xFF00FF00);
//                tictacList.get(5).setBackgroundColor(0xFF00FF00);
//                tictacList.get(10).setBackgroundColor(0xFF00FF00);
//                tictacList.get(15).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(3)=="x" && gameList.get(6)=="x" && gameList.get(9)=="x" && gameList.get(12)=="x") {
//                tictacList.get(3).setBackgroundColor(0xFF00FF00);
//                tictacList.get(6).setBackgroundColor(0xFF00FF00);
//                tictacList.get(9).setBackgroundColor(0xFF00FF00);
//                tictacList.get(12).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//
//
//            if (gameList.get(0)=="o" && gameList.get(1)=="o" && gameList.get(2)=="o" && gameList.get(3)=="o") {
//                tictacList.get(0).setBackgroundColor(0xFF00FF00);
//                tictacList.get(1).setBackgroundColor(0xFF00FF00);
//                tictacList.get(2).setBackgroundColor(0xFF00FF00);
//                tictacList.get(3).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(4)=="o" && gameList.get(5)=="o" && gameList.get(6)=="o" && gameList.get(7)=="o") {
//                tictacList.get(4).setBackgroundColor(0xFF00FF00);
//                tictacList.get(5).setBackgroundColor(0xFF00FF00);
//                tictacList.get(6).setBackgroundColor(0xFF00FF00);
//                tictacList.get(7).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(8)=="o" && gameList.get(9)=="o" && gameList.get(10)=="o" && gameList.get(11)=="o") {
//                tictacList.get(8).setBackgroundColor(0xFF00FF00);
//                tictacList.get(9).setBackgroundColor(0xFF00FF00);
//                tictacList.get(10).setBackgroundColor(0xFF00FF00);
//                tictacList.get(11).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(12)=="o" && gameList.get(13)=="o" && gameList.get(14)=="o" && gameList.get(15)=="o") {
//                tictacList.get(12).setBackgroundColor(0xFF00FF00);
//                tictacList.get(13).setBackgroundColor(0xFF00FF00);
//                tictacList.get(14).setBackgroundColor(0xFF00FF00);
//                tictacList.get(15).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(0)=="o" && gameList.get(4)=="o" && gameList.get(8)=="o" && gameList.get(12)=="o") {
//                tictacList.get(0).setBackgroundColor(0xFF00FF00);
//                tictacList.get(4).setBackgroundColor(0xFF00FF00);
//                tictacList.get(8).setBackgroundColor(0xFF00FF00);
//                tictacList.get(12).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(1)=="o" && gameList.get(5)=="o" && gameList.get(9)=="o" && gameList.get(13)=="o") {
//                tictacList.get(1).setBackgroundColor(0xFF00FF00);
//                tictacList.get(5).setBackgroundColor(0xFF00FF00);
//                tictacList.get(9).setBackgroundColor(0xFF00FF00);
//                tictacList.get(13).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(2)=="o" && gameList.get(6)=="o" && gameList.get(10)=="o" && gameList.get(14)=="o") {
//                tictacList.get(2).setBackgroundColor(0xFF00FF00);
//                tictacList.get(6).setBackgroundColor(0xFF00FF00);
//                tictacList.get(10).setBackgroundColor(0xFF00FF00);
//                tictacList.get(14).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(3)=="o" && gameList.get(7)=="o" && gameList.get(11)=="o" && gameList.get(15)=="o") {
//                tictacList.get(3).setBackgroundColor(0xFF00FF00);
//                tictacList.get(7).setBackgroundColor(0xFF00FF00);
//                tictacList.get(11).setBackgroundColor(0xFF00FF00);
//                tictacList.get(15).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(0)=="o" && gameList.get(5)=="o" && gameList.get(10)=="o" && gameList.get(15)=="o") {
//                tictacList.get(0).setBackgroundColor(0xFF00FF00);
//                tictacList.get(5).setBackgroundColor(0xFF00FF00);
//                tictacList.get(10).setBackgroundColor(0xFF00FF00);
//                tictacList.get(15).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(3)=="o" && gameList.get(6)=="o" && gameList.get(9)=="o" && gameList.get(12)=="o") {
//                tictacList.get(3).setBackgroundColor(0xFF00FF00);
//                tictacList.get(6).setBackgroundColor(0xFF00FF00);
//                tictacList.get(9).setBackgroundColor(0xFF00FF00);
//                tictacList.get(12).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//
//        }
//
//
//
//        if (size == 5) {
//
//            if (playerOne) {
//                for (int i = 0; i < 25; i++) {
//                    if(tictacList.get(i).getId()==view.getId()){
//                        gameList.set(i, "x");
//                    }
//                }
//            }
//            else {
//                for (int i = 0; i < 25; i++) {
//                    if(tictacList.get(i).getId()==view.getId()){
//                        gameList.set(i, "o");
//                    }
//                }
//            }
//
//            if (gameList.get(0)=="x" && gameList.get(1)=="x" && gameList.get(2)=="x" && gameList.get(3)=="x"  && gameList.get(4)=="x") {
//                tictacList.get(0).setBackgroundColor(0xFF00FF00);
//                tictacList.get(1).setBackgroundColor(0xFF00FF00);
//                tictacList.get(2).setBackgroundColor(0xFF00FF00);
//                tictacList.get(3).setBackgroundColor(0xFF00FF00);
//                tictacList.get(4).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(5)=="x" && gameList.get(6)=="x" && gameList.get(7)=="x" && gameList.get(8)=="x" && gameList.get(9)=="x") {
//                tictacList.get(5).setBackgroundColor(0xFF00FF00);
//                tictacList.get(6).setBackgroundColor(0xFF00FF00);
//                tictacList.get(7).setBackgroundColor(0xFF00FF00);
//                tictacList.get(8).setBackgroundColor(0xFF00FF00);
//                tictacList.get(9).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(10)=="x" && gameList.get(11)=="x" && gameList.get(12)=="x" && gameList.get(13)=="x" && gameList.get(14)=="x") {
//                tictacList.get(10).setBackgroundColor(0xFF00FF00);
//                tictacList.get(11).setBackgroundColor(0xFF00FF00);
//                tictacList.get(12).setBackgroundColor(0xFF00FF00);
//                tictacList.get(13).setBackgroundColor(0xFF00FF00);
//                tictacList.get(14).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(15)=="x" && gameList.get(16)=="x" && gameList.get(17)=="x" && gameList.get(18)=="x" && gameList.get(19)=="x") {
//                tictacList.get(15).setBackgroundColor(0xFF00FF00);
//                tictacList.get(16).setBackgroundColor(0xFF00FF00);
//                tictacList.get(17).setBackgroundColor(0xFF00FF00);
//                tictacList.get(18).setBackgroundColor(0xFF00FF00);
//                tictacList.get(19).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(20)=="x" && gameList.get(21)=="x" && gameList.get(22)=="x" && gameList.get(23)=="x" && gameList.get(24)=="x") {
//                tictacList.get(20).setBackgroundColor(0xFF00FF00);
//                tictacList.get(21).setBackgroundColor(0xFF00FF00);
//                tictacList.get(22).setBackgroundColor(0xFF00FF00);
//                tictacList.get(23).setBackgroundColor(0xFF00FF00);
//                tictacList.get(24).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(0)=="x" && gameList.get(5)=="x" && gameList.get(10)=="x" && gameList.get(15)=="x" && gameList.get(20)=="x") {
//                tictacList.get(0).setBackgroundColor(0xFF00FF00);
//                tictacList.get(5).setBackgroundColor(0xFF00FF00);
//                tictacList.get(10).setBackgroundColor(0xFF00FF00);
//                tictacList.get(15).setBackgroundColor(0xFF00FF00);
//                tictacList.get(20).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(1)=="x" && gameList.get(6)=="x" && gameList.get(11)=="x" && gameList.get(16)=="x" && gameList.get(21)=="x") {
//                tictacList.get(1).setBackgroundColor(0xFF00FF00);
//                tictacList.get(6).setBackgroundColor(0xFF00FF00);
//                tictacList.get(11).setBackgroundColor(0xFF00FF00);
//                tictacList.get(16).setBackgroundColor(0xFF00FF00);
//                tictacList.get(21).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(2)=="x" && gameList.get(7)=="x" && gameList.get(12)=="x" && gameList.get(17)=="x" && gameList.get(22)=="x") {
//                tictacList.get(2).setBackgroundColor(0xFF00FF00);
//                tictacList.get(7).setBackgroundColor(0xFF00FF00);
//                tictacList.get(12).setBackgroundColor(0xFF00FF00);
//                tictacList.get(17).setBackgroundColor(0xFF00FF00);
//                tictacList.get(22).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(3)=="x" && gameList.get(8)=="x" && gameList.get(13)=="x" && gameList.get(18)=="x" && gameList.get(23)=="x") {
//                tictacList.get(3).setBackgroundColor(0xFF00FF00);
//                tictacList.get(8).setBackgroundColor(0xFF00FF00);
//                tictacList.get(13).setBackgroundColor(0xFF00FF00);
//                tictacList.get(18).setBackgroundColor(0xFF00FF00);
//                tictacList.get(23).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(4)=="x" && gameList.get(9)=="x" && gameList.get(14)=="x" && gameList.get(19)=="x" && gameList.get(24)=="x") {
//                tictacList.get(4).setBackgroundColor(0xFF00FF00);
//                tictacList.get(9).setBackgroundColor(0xFF00FF00);
//                tictacList.get(14).setBackgroundColor(0xFF00FF00);
//                tictacList.get(19).setBackgroundColor(0xFF00FF00);
//                tictacList.get(24).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(0)=="x" && gameList.get(6)=="x" && gameList.get(12)=="x" && gameList.get(18)=="x" && gameList.get(24)=="x") {
//                tictacList.get(0).setBackgroundColor(0xFF00FF00);
//                tictacList.get(6).setBackgroundColor(0xFF00FF00);
//                tictacList.get(12).setBackgroundColor(0xFF00FF00);
//                tictacList.get(18).setBackgroundColor(0xFF00FF00);
//                tictacList.get(24).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(4)=="x" && gameList.get(8)=="x" && gameList.get(12)=="x" && gameList.get(16)=="x" && gameList.get(20)=="x") {
//                tictacList.get(4).setBackgroundColor(0xFF00FF00);
//                tictacList.get(8).setBackgroundColor(0xFF00FF00);
//                tictacList.get(12).setBackgroundColor(0xFF00FF00);
//                tictacList.get(16).setBackgroundColor(0xFF00FF00);
//                tictacList.get(20).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//
//
//
//            if (gameList.get(0)=="o" && gameList.get(1)=="o" && gameList.get(2)=="o" && gameList.get(3)=="o"  && gameList.get(4)=="o") {
//                tictacList.get(0).setBackgroundColor(0xFF00FF00);
//                tictacList.get(1).setBackgroundColor(0xFF00FF00);
//                tictacList.get(2).setBackgroundColor(0xFF00FF00);
//                tictacList.get(3).setBackgroundColor(0xFF00FF00);
//                tictacList.get(4).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(5)=="o" && gameList.get(6)=="o" && gameList.get(7)=="o" && gameList.get(8)=="o" && gameList.get(9)=="o") {
//                tictacList.get(5).setBackgroundColor(0xFF00FF00);
//                tictacList.get(6).setBackgroundColor(0xFF00FF00);
//                tictacList.get(7).setBackgroundColor(0xFF00FF00);
//                tictacList.get(8).setBackgroundColor(0xFF00FF00);
//                tictacList.get(9).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(10)=="o" && gameList.get(11)=="o" && gameList.get(12)=="o" && gameList.get(13)=="o" && gameList.get(14)=="o") {
//                tictacList.get(10).setBackgroundColor(0xFF00FF00);
//                tictacList.get(11).setBackgroundColor(0xFF00FF00);
//                tictacList.get(12).setBackgroundColor(0xFF00FF00);
//                tictacList.get(13).setBackgroundColor(0xFF00FF00);
//                tictacList.get(14).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(15)=="o" && gameList.get(16)=="o" && gameList.get(17)=="o" && gameList.get(18)=="o" && gameList.get(19)=="o") {
//                tictacList.get(15).setBackgroundColor(0xFF00FF00);
//                tictacList.get(16).setBackgroundColor(0xFF00FF00);
//                tictacList.get(17).setBackgroundColor(0xFF00FF00);
//                tictacList.get(18).setBackgroundColor(0xFF00FF00);
//                tictacList.get(19).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(20)=="o" && gameList.get(21)=="o" && gameList.get(22)=="o" && gameList.get(23)=="o" && gameList.get(24)=="o") {
//                tictacList.get(20).setBackgroundColor(0xFF00FF00);
//                tictacList.get(21).setBackgroundColor(0xFF00FF00);
//                tictacList.get(22).setBackgroundColor(0xFF00FF00);
//                tictacList.get(23).setBackgroundColor(0xFF00FF00);
//                tictacList.get(24).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(0)=="o" && gameList.get(5)=="o" && gameList.get(10)=="o" && gameList.get(15)=="o" && gameList.get(20)=="o") {
//                tictacList.get(0).setBackgroundColor(0xFF00FF00);
//                tictacList.get(5).setBackgroundColor(0xFF00FF00);
//                tictacList.get(10).setBackgroundColor(0xFF00FF00);
//                tictacList.get(15).setBackgroundColor(0xFF00FF00);
//                tictacList.get(20).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(1)=="o" && gameList.get(6)=="o" && gameList.get(11)=="o" && gameList.get(16)=="o" && gameList.get(21)=="o") {
//                tictacList.get(1).setBackgroundColor(0xFF00FF00);
//                tictacList.get(6).setBackgroundColor(0xFF00FF00);
//                tictacList.get(11).setBackgroundColor(0xFF00FF00);
//                tictacList.get(16).setBackgroundColor(0xFF00FF00);
//                tictacList.get(21).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(2)=="o" && gameList.get(7)=="o" && gameList.get(12)=="o" && gameList.get(17)=="o" && gameList.get(22)=="o") {
//                tictacList.get(2).setBackgroundColor(0xFF00FF00);
//                tictacList.get(7).setBackgroundColor(0xFF00FF00);
//                tictacList.get(12).setBackgroundColor(0xFF00FF00);
//                tictacList.get(17).setBackgroundColor(0xFF00FF00);
//                tictacList.get(22).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(3)=="o" && gameList.get(8)=="o" && gameList.get(13)=="o" && gameList.get(18)=="o" && gameList.get(23)=="o") {
//                tictacList.get(3).setBackgroundColor(0xFF00FF00);
//                tictacList.get(8).setBackgroundColor(0xFF00FF00);
//                tictacList.get(13).setBackgroundColor(0xFF00FF00);
//                tictacList.get(18).setBackgroundColor(0xFF00FF00);
//                tictacList.get(23).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(4)=="o" && gameList.get(9)=="o" && gameList.get(14)=="o" && gameList.get(19)=="o" && gameList.get(24)=="o") {
//                tictacList.get(4).setBackgroundColor(0xFF00FF00);
//                tictacList.get(9).setBackgroundColor(0xFF00FF00);
//                tictacList.get(14).setBackgroundColor(0xFF00FF00);
//                tictacList.get(19).setBackgroundColor(0xFF00FF00);
//                tictacList.get(24).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(0)=="o" && gameList.get(6)=="o" && gameList.get(12)=="o" && gameList.get(18)=="o" && gameList.get(24)=="o") {
//                tictacList.get(0).setBackgroundColor(0xFF00FF00);
//                tictacList.get(6).setBackgroundColor(0xFF00FF00);
//                tictacList.get(12).setBackgroundColor(0xFF00FF00);
//                tictacList.get(18).setBackgroundColor(0xFF00FF00);
//                tictacList.get(24).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//            if (gameList.get(4)=="o" && gameList.get(8)=="o" && gameList.get(12)=="o" && gameList.get(16)=="o" && gameList.get(20)=="o") {
//                tictacList.get(4).setBackgroundColor(0xFF00FF00);
//                tictacList.get(8).setBackgroundColor(0xFF00FF00);
//                tictacList.get(12).setBackgroundColor(0xFF00FF00);
//                tictacList.get(16).setBackgroundColor(0xFF00FF00);
//                tictacList.get(20).setBackgroundColor(0xFF00FF00);
//                setWinner(playerOne);
//            }
//        }


    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<String> getGameList() {
        return gameList;
    }

    public void setGameList(ArrayList<String> gameList) {
        this.gameList = gameList;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public Boolean getMoveP1() {
        return moveP1;
    }

    public void setMoveP1(Boolean moveP1) {
        this.moveP1 = moveP1;
    }

    public Boolean getMoveP2() {
        return moveP2;
    }

    public void setMoveP2(Boolean moveP2) {
        this.moveP2 = moveP2;
    }

    public Boolean getStartPlayer() {
        return startPlayer;
    }

    public void setStartPlayer(Boolean startPlayer) {
        this.startPlayer = startPlayer;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public int getViewID() {
        return viewID;
    }

    public void setViewID(int viewID) {
        this.viewID = viewID;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public Boolean getWinnerP1() {
        return winnerP1;
    }

    public void setWinnerP1(Boolean winnerP1) {
        this.winnerP1 = winnerP1;
    }

    public Boolean getWinnerP2() {
        return winnerP2;
    }

    public void setWinnerP2(Boolean winnerP2) {
        this.winnerP2 = winnerP2;
    }

    public Boolean getConnectedP1() {
        return connectedP1;
    }

    public void setConnectedP1(Boolean connectedP1) {
        this.connectedP1 = connectedP1;
    }

    public Boolean getConnectedP2() {
        return connectedP2;
    }

    public void setConnectedP2(Boolean connectedP2) {
        this.connectedP2 = connectedP2;
    }

    public Boolean getDraw() {
        return draw;
    }

    public void setDraw(Boolean draw) {
        this.draw = draw;
    }

    public String getP1name() {
        return p1name;
    }

    public void setP1name(String p1name) {
        this.p1name = p1name;
    }

    public String getP2name() {
        return p2name;
    }

    public void setP2name(String p2name) {
        this.p2name = p2name;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isP1paused() {
        return p1paused;
    }

    public void setP1paused(boolean p1paused) {
        this.p1paused = p1paused;
    }

    public boolean isP2paused() {
        return p2paused;
    }

    public void setP2paused(boolean p2paused) {
        this.p2paused = p2paused;
    }

    public ArrayList<ArrayList<String>> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<ArrayList<String>> messages) {
        this.messages = messages;
    }
}
