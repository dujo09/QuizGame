package com.soss.quizgame.model;

import java.util.List;

public class Question {
  public String questionText;
  public List<String> options;
  public int correctOptionIndex;

  public Question(
      String questionText,
      List<String> options,
      int correctOptionIndex) {
    this.questionText = questionText;
    this.options = options;
    this.correctOptionIndex = correctOptionIndex;
  }

  public Question() {}
}
