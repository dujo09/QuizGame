package com.soss.quizgame.model;

public class UserGame {
    public String uid;
    public int score;
    public String timestamp;

    public UserGame(String uid, int score, String timestamp) {
        this.uid = uid;
        this.score = score;
        this.timestamp = timestamp;
    }

    public UserGame() {}
}
