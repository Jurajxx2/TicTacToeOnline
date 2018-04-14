package com.trasimus.tictactoe.online;

import android.net.Uri;

import java.util.ArrayList;

public class DefaultUser {

    private String userID;
    private String mail;
    private String name;
    private int points;
    private Uri userIMG;
    private ArrayList<ArrayList<String>> friends;
    private String fbAccount;
    private String googleAccount;
    private String mailAccount;
    private String age;
    private String country;

    public DefaultUser(){

    }

    public DefaultUser(String userID, String mail, String name, String age, String country, int points, ArrayList<ArrayList<String>> friends, String fbAccount, String googleAccount, String mailAccount){

        this.userID = userID;
        this.mail = mail;
        this.name = name;
        this.points = points;
        this.userIMG = userIMG;
        this.friends = friends;
        this.fbAccount = fbAccount;
        this.googleAccount = googleAccount;
        this.mailAccount = mailAccount;
        this.age = age;
        this.country = country;
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

    public Uri getUserIMG() {
        return userIMG;
    }

    public void setUserIMG(Uri userIMG) {
        this.userIMG = userIMG;
    }

    public ArrayList<ArrayList<String>> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<ArrayList<String>> friends) {
        this.friends = friends;
    }

    public String getFbAccount() {
        return fbAccount;
    }

    public void setFbAccount(String fbAccount) {
        this.fbAccount = fbAccount;
    }

    public String getGoogleAccount() {
        return googleAccount;
    }

    public void setGoogleAccount(String googleAccount) {
        this.googleAccount = googleAccount;
    }

    public String getMailAccount() {
        return mailAccount;
    }

    public void setMailAccount(String mailAccount) {
        this.mailAccount = mailAccount;
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
}
