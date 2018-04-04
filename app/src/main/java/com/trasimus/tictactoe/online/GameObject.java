package com.trasimus.tictactoe.online;

import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private ArrayList<String> gameList;
    private int player1;
    private int player2;
    private Boolean moveP1;
    private Boolean moveP2;
    private Boolean startPlayer;
    private int move;
    private int viewID;
    private int size;
    private String gameID;

    public GameObject(){

    }

    public GameObject(String gameID, int player1, int player2, int size, Boolean moveP1, Boolean moveP2, Boolean startPlayer, int move, int viewID){
      this.player1 = player1;
      this.player2 = player2;
      this.moveP1 = moveP1;
      this.moveP2 = moveP2;
      this.startPlayer = startPlayer;
      this.move = move;
      this.viewID = viewID;
      this.size = size;
      this.gameID = gameID;
      //this.clickedButton = clickedButton;
      initiateGameList();
    }

    private void initiateGameList(){
        gameList = new ArrayList<>();

        for (int a=0; a<size*size; a++){
            gameList.add("");
        }
    }

    public Boolean move(int player){
        Boolean isOnMove = false;
        move++;

        if (player == player1) {
            if (move % 2 == 0) {
                isOnMove = true;
            }
            else {
                isOnMove = false;
            }
        }
        else if (player == player2){
            if (move % 2 == 0) {
                isOnMove = false;
            }
            else {
                isOnMove = true;
            }
        }
        return isOnMove;
    }

    public void enableIfIsOnMove(Boolean isOnMove, List<Button> tictacList){
        for (int i = 0; i < size*size; i++) {
            tictacList.get(i).setEnabled(isOnMove);
        }
    }

    public void clickedButton(View view, List<Button> tictacList) {


        this.setViewID(view.getId());

        view.setBackgroundResource(R.drawable.x);

        if (size == 3) {


            for (int i = 0; i < 9; i++) {
                if(tictacList.get(i).getId()==view.getId()){
                    gameList.set(i, "x");
                }
            }

            if (gameList.get(0)=="x" && gameList.get(1)=="x" && gameList.get(2)=="x") {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
            }
            if (gameList.get(3)=="x" && gameList.get(4)=="x" && gameList.get(5)=="x") {
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
            }
            if (gameList.get(6)=="x" && gameList.get(7)=="x" && gameList.get(8)=="x") {
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
            }
            if (gameList.get(0)=="x" && gameList.get(3)=="x" && gameList.get(6)=="x") {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(3).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
            }
            if (gameList.get(1)=="x" && gameList.get(4)=="x" && gameList.get(7)=="x") {
                tictacList.get(1).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(7).setBackgroundColor(0xFF00FF00);
            }
            if (gameList.get(2)=="x" && gameList.get(5)=="x" && gameList.get(8)=="x") {
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(5).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
            }
            if (gameList.get(0)=="x" && gameList.get(4)=="x" && gameList.get(8)=="x") {
                tictacList.get(0).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(8).setBackgroundColor(0xFF00FF00);
            }
            if (gameList.get(2)=="x" && gameList.get(4)=="x" && gameList.get(6)=="x") {
                tictacList.get(2).setBackgroundColor(0xFF00FF00);
                tictacList.get(4).setBackgroundColor(0xFF00FF00);
                tictacList.get(6).setBackgroundColor(0xFF00FF00);
            }
        }


//        if (bundle.getInt("size") == 4) {
//
//            for (int i = 0; i < 16; i++) {
//                if(tictac[i].getId()==view.getId()){
//                    game[i] = "x";
//                }
//            }
//
//            if (game[0]=="x" && game[1]=="x" && game[2]=="x" && game[3]=="x") {
//                tictac[0].setBackgroundColor(0xFF00FF00);
//                tictac[1].setBackgroundColor(0xFF00FF00);
//                tictac[2].setBackgroundColor(0xFF00FF00);
//                tictac[3].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[4]=="x" && game[5]=="x" && game[6]=="x" && game[7]=="x") {
//                tictac[4].setBackgroundColor(0xFF00FF00);
//                tictac[5].setBackgroundColor(0xFF00FF00);
//                tictac[6].setBackgroundColor(0xFF00FF00);
//                tictac[7].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[8]=="x" && game[9]=="x" && game[10]=="x" && game[11]=="x") {
//                tictac[8].setBackgroundColor(0xFF00FF00);
//                tictac[9].setBackgroundColor(0xFF00FF00);
//                tictac[10].setBackgroundColor(0xFF00FF00);
//                tictac[11].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[12]=="x" && game[13]=="x" && game[14]=="x" && game[15]=="x") {
//                tictac[12].setBackgroundColor(0xFF00FF00);
//                tictac[13].setBackgroundColor(0xFF00FF00);
//                tictac[14].setBackgroundColor(0xFF00FF00);
//                tictac[15].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[0]=="x" && game[4]=="x" && game[8]=="x" && game[12]=="x") {
//                tictac[0].setBackgroundColor(0xFF00FF00);
//                tictac[4].setBackgroundColor(0xFF00FF00);
//                tictac[8].setBackgroundColor(0xFF00FF00);
//                tictac[12].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[1]=="x" && game[5]=="x" && game[9]=="x" && game[13]=="x") {
//                tictac[1].setBackgroundColor(0xFF00FF00);
//                tictac[5].setBackgroundColor(0xFF00FF00);
//                tictac[9].setBackgroundColor(0xFF00FF00);
//                tictac[13].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[2]=="x" && game[6]=="x" && game[10]=="x" && game[14]=="x") {
//                tictac[2].setBackgroundColor(0xFF00FF00);
//                tictac[6].setBackgroundColor(0xFF00FF00);
//                tictac[10].setBackgroundColor(0xFF00FF00);
//                tictac[14].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[3]=="x" && game[7]=="x" && game[11]=="x" && game[15]=="x") {
//                tictac[3].setBackgroundColor(0xFF00FF00);
//                tictac[7].setBackgroundColor(0xFF00FF00);
//                tictac[11].setBackgroundColor(0xFF00FF00);
//                tictac[15].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[0]=="x" && game[5]=="x" && game[10]=="x" && game[15]=="x") {
//                tictac[0].setBackgroundColor(0xFF00FF00);
//                tictac[5].setBackgroundColor(0xFF00FF00);
//                tictac[10].setBackgroundColor(0xFF00FF00);
//                tictac[15].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[3]=="x" && game[6]=="x" && game[9]=="x" && game[12]=="x") {
//                tictac[3].setBackgroundColor(0xFF00FF00);
//                tictac[6].setBackgroundColor(0xFF00FF00);
//                tictac[9].setBackgroundColor(0xFF00FF00);
//                tictac[12].setBackgroundColor(0xFF00FF00);
//            }
//        }
//
//
//
//        if (bundle.getInt("size") == 5) {
//
//            for (int i = 0; i < 25; i++) {
//                if(tictac[i].getId()==view.getId()){
//                    game[i] = "x";
//                }
//            }
//
//            if (game[0]=="x" && game[1]=="x" && game[2]=="x" && game[3]=="x"  && game[4]=="x") {
//                tictac[0].setBackgroundColor(0xFF00FF00);
//                tictac[1].setBackgroundColor(0xFF00FF00);
//                tictac[2].setBackgroundColor(0xFF00FF00);
//                tictac[3].setBackgroundColor(0xFF00FF00);
//                tictac[4].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[5]=="x" && game[6]=="x" && game[7]=="x" && game[8]=="x" && game[9]=="x") {
//                tictac[5].setBackgroundColor(0xFF00FF00);
//                tictac[6].setBackgroundColor(0xFF00FF00);
//                tictac[7].setBackgroundColor(0xFF00FF00);
//                tictac[8].setBackgroundColor(0xFF00FF00);
//                tictac[9].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[10]=="x" && game[11]=="x" && game[12]=="x" && game[13]=="x" && game[14]=="x") {
//                tictac[10].setBackgroundColor(0xFF00FF00);
//                tictac[11].setBackgroundColor(0xFF00FF00);
//                tictac[12].setBackgroundColor(0xFF00FF00);
//                tictac[13].setBackgroundColor(0xFF00FF00);
//                tictac[14].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[15]=="x" && game[16]=="x" && game[17]=="x" && game[18]=="x" && game[19]=="x") {
//                tictac[15].setBackgroundColor(0xFF00FF00);
//                tictac[16].setBackgroundColor(0xFF00FF00);
//                tictac[17].setBackgroundColor(0xFF00FF00);
//                tictac[18].setBackgroundColor(0xFF00FF00);
//                tictac[19].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[20]=="x" && game[21]=="x" && game[22]=="x" && game[23]=="x" && game[24]=="x") {
//                tictac[20].setBackgroundColor(0xFF00FF00);
//                tictac[21].setBackgroundColor(0xFF00FF00);
//                tictac[22].setBackgroundColor(0xFF00FF00);
//                tictac[23].setBackgroundColor(0xFF00FF00);
//                tictac[24].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[0]=="x" && game[5]=="x" && game[10]=="x" && game[15]=="x" && game[20]=="x") {
//                tictac[0].setBackgroundColor(0xFF00FF00);
//                tictac[5].setBackgroundColor(0xFF00FF00);
//                tictac[10].setBackgroundColor(0xFF00FF00);
//                tictac[15].setBackgroundColor(0xFF00FF00);
//                tictac[20].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[1]=="x" && game[6]=="x" && game[11]=="x" && game[16]=="x" && game[21]=="x") {
//                tictac[1].setBackgroundColor(0xFF00FF00);
//                tictac[6].setBackgroundColor(0xFF00FF00);
//                tictac[11].setBackgroundColor(0xFF00FF00);
//                tictac[16].setBackgroundColor(0xFF00FF00);
//                tictac[21].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[2]=="x" && game[7]=="x" && game[12]=="x" && game[17]=="x" && game[22]=="x") {
//                tictac[2].setBackgroundColor(0xFF00FF00);
//                tictac[7].setBackgroundColor(0xFF00FF00);
//                tictac[12].setBackgroundColor(0xFF00FF00);
//                tictac[17].setBackgroundColor(0xFF00FF00);
//                tictac[22].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[3]=="x" && game[8]=="x" && game[13]=="x" && game[18]=="x" && game[23]=="x") {
//                tictac[3].setBackgroundColor(0xFF00FF00);
//                tictac[8].setBackgroundColor(0xFF00FF00);
//                tictac[13].setBackgroundColor(0xFF00FF00);
//                tictac[18].setBackgroundColor(0xFF00FF00);
//                tictac[23].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[4]=="x" && game[9]=="x" && game[14]=="x" && game[19]=="x" && game[24]=="x") {
//                tictac[4].setBackgroundColor(0xFF00FF00);
//                tictac[9].setBackgroundColor(0xFF00FF00);
//                tictac[14].setBackgroundColor(0xFF00FF00);
//                tictac[19].setBackgroundColor(0xFF00FF00);
//                tictac[24].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[0]=="x" && game[6]=="x" && game[12]=="x" && game[18]=="x" && game[24]=="x") {
//                tictac[0].setBackgroundColor(0xFF00FF00);
//                tictac[6].setBackgroundColor(0xFF00FF00);
//                tictac[12].setBackgroundColor(0xFF00FF00);
//                tictac[18].setBackgroundColor(0xFF00FF00);
//                tictac[24].setBackgroundColor(0xFF00FF00);
//            }
//            if (game[4]=="x" && game[8]=="x" && game[12]=="x" && game[16]=="x" && game[20]=="x") {
//                tictac[4].setBackgroundColor(0xFF00FF00);
//                tictac[8].setBackgroundColor(0xFF00FF00);
//                tictac[12].setBackgroundColor(0xFF00FF00);
//                tictac[16].setBackgroundColor(0xFF00FF00);
//                tictac[20].setBackgroundColor(0xFF00FF00);
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

    public int getPlayer1() {
        return player1;
    }

    public void setPlayer1(int player1) {
        this.player1 = player1;
    }

    public int getPlayer2() {
        return player2;
    }

    public void setPlayer2(int player2) {
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
}
