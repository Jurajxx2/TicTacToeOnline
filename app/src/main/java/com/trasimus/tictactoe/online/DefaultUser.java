package com.trasimus.tictactoe.online;

import android.net.Uri;

import java.util.ArrayList;

public class DefaultUser {

    private String userID;
    private String mail;
    private String name;
    private int points;
    private ArrayList<ArrayList<String>> friends;
    private String age;
    private String country;
    private String lobbyID;
    private String photoID;
    private String myGameID;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;
    private boolean isOnline;

    public DefaultUser(){

    }

    public DefaultUser(String userID, String mail, String name, String age, String country, int points, ArrayList<ArrayList<String>> friends, String lobbyID, String photoID, String myGameID, int gamesPlayed, int gamesWon, int gamesLost, boolean isOnline){

        this.userID = userID;
        this.mail = mail;
        this.name = name;
        this.points = points;
        this.friends = friends;
        this.age = age;
        this.country = country;
        this.lobbyID = lobbyID;
        this.photoID = photoID;
        this.myGameID = myGameID;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.isOnline = isOnline;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public ArrayList<ArrayList<String>> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<ArrayList<String>> friends) {
        this.friends = friends;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(String lobbyID) {
        this.lobbyID = lobbyID;
    }

    public String getPhotoID() {
        return photoID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public String getMyGameID() {
        return myGameID;
    }

    public void setMyGameID(String myGameID) {
        this.myGameID = myGameID;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
