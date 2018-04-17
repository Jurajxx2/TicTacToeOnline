package com.trasimus.tictactoe.online;

import java.util.ArrayList;

public class Lobby {

    private String player1;
    private String player2;
    private String gameID;
    private String lobbyID;
    private ArrayList<ArrayList<String>> messages;
    private boolean connectedP1;
    private boolean connectedP2;
    private boolean readyP1;
    private boolean readyP2;
    private String size;

    public Lobby(){}

    public Lobby(String lobbyID, String player1, String player2, String gameID, ArrayList<ArrayList<String>> messages, boolean connectedP1, boolean connectedP2, boolean readyP1, boolean readyP2, String size){
        this.player1 = player1;
        this.player2 = player2;
        this.gameID = gameID;
        this.messages = messages;
        this.connectedP1 = connectedP1;
        this.connectedP2 = connectedP2;
        this.readyP1 = readyP1;
        this.readyP2 = readyP2;
        this.lobbyID = lobbyID;
        this.size = size;
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

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(String lobbyID) {
        this.lobbyID = lobbyID;
    }

    public ArrayList<ArrayList<String>> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<ArrayList<String>> messages) {
        this.messages = messages;
    }

    public boolean isConnectedP1() {
        return connectedP1;
    }

    public void setConnectedP1(boolean connectedP1) {
        this.connectedP1 = connectedP1;
    }

    public boolean isConnectedP2() {
        return connectedP2;
    }

    public void setConnectedP2(boolean connectedP2) {
        this.connectedP2 = connectedP2;
    }

    public boolean isReadyP1() {
        return readyP1;
    }

    public void setReadyP1(boolean readyP1) {
        this.readyP1 = readyP1;
    }

    public boolean isReadyP2() {
        return readyP2;
    }

    public void setReadyP2(boolean readyP2) {
        this.readyP2 = readyP2;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
