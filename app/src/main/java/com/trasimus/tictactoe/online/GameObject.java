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
    private boolean p1paused;
    private boolean p2paused;
    private ArrayList<ArrayList<String>> messages;
    private String request;
    private int winsP1;
    private int winsP2;
    private int draws;
    private int losesP1;
    private int losesP2;
    private boolean playAgainP1;
    private boolean playAgainP2;
    private String p1char;
    private String p2char;


    public GameObject(){

    }

    public GameObject(String gameID, String player1, String player2, int size, Boolean moveP1, Boolean moveP2, Boolean startPlayer, int move, int viewID, Boolean winnerP1, Boolean winnerP2, Boolean connectedP1, Boolean connectedP2, Boolean draw, String p1name, String p2name, boolean p1paused, boolean p2paused, ArrayList<ArrayList<String>> messages, String request, int winsP1, int winsP2, int draws, int losesP1, int losesP2, boolean playAgainP1, boolean playAgainP2, String p1char, String p2char){
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
      this.p1paused = p1paused;
      this.p2paused = p2paused;
      this.messages = messages;
      this.request = request;
      this.winsP1 = winsP1;
      this.winsP2 = winsP2;
      this.draws = draws;
      this.losesP1 = losesP1;
      this.losesP2 = losesP2;
      this.playAgainP1 = playAgainP1;
      this.playAgainP2 = playAgainP2;
      this.p1char = p1char;
      this.p2char = p2char;
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

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
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

    public boolean isPlayAgainP1() {
        return playAgainP1;
    }

    public void setPlayAgainP1(boolean playAgainP1) {
        this.playAgainP1 = playAgainP1;
    }

    public boolean isPlayAgainP2() {
        return playAgainP2;
    }

    public void setPlayAgainP2(boolean playAgainP2) {
        this.playAgainP2 = playAgainP2;
    }

    public String getP1char() {
        return p1char;
    }

    public void setP1char(String p1char) {
        this.p1char = p1char;
    }

    public String getP2char() {
        return p2char;
    }

    public void setP2char(String p2char) {
        this.p2char = p2char;
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
}
