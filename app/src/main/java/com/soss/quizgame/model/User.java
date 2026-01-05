package com.soss.quizgame.model;

public class User {
  public String uid;
  public String username;
  public String email;
  public int highScore;

  public User(String uid, String username, String email, int highScore) {
    this.uid = uid;
    this.username = username;
    this.email = email;
    this.highScore = highScore;
  }

  public User() {}
}
