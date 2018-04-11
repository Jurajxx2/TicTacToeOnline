package com.trasimus.tictactoe.online;

public class UserMap {

    private String mail;
    private String userID;

    public UserMap(){

    }

    public UserMap(String mail, String userID){
        this.mail = mail;
        this.userID = userID;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
