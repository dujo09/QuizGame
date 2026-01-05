package com.soss.quizgame.model;

public class UserGame {
    public String uid;
    public int score;
    public String timestamp;
    public boolean isAllCorrect;

    public UserGame(String uid, int score, String timestamp, boolean isAllCorrect) {
        this.uid = uid;
        this.score = score;
        this.timestamp = timestamp;
        this.isAllCorrect = isAllCorrect;
    }

    public UserGame() {}
}
