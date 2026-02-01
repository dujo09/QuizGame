package com.soss.quizgame.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.*;
import com.soss.quizgame.model.Question;
import java.util.*;

public class QuizRepository {
  private static String TAG = "QuizRepository";
  private final MutableLiveData<List<Question>> questionsLiveData = new MutableLiveData<>();
  private final MutableLiveData<String> error = new MutableLiveData<>();
  private FirebaseFirestore db;

  public QuizRepository() {
    db = FirebaseFirestore.getInstance();
  }

  public void fetchQuestions() {
    db.collection("questions")
        .get()
        .addOnSuccessListener(
            snapshots -> {
              List<Question> list = new ArrayList<>();
              for (QueryDocumentSnapshot doc : snapshots) {
                Question q = doc.toObject(Question.class);
                list.add(q);
              }
              Collections.shuffle(list);
              questionsLiveData.postValue(list);
            })
        .addOnFailureListener(
            e -> {
              error.postValue(e.getMessage());
            });
  }

  public MutableLiveData<List<Question>> getQuestionsLiveData() {
    return questionsLiveData;
  }

  public MutableLiveData<String> getError() {
    return error;
  }

  public void updateHighScore(String uid, int newScore) {
    DocumentReference userRef = db.collection("users").document(uid);
    userRef
        .get()
        .addOnSuccessListener(
            documentSnapshot -> {
              if (documentSnapshot.exists()) {
                int oldScore = documentSnapshot.getLong("highScore").intValue();
                if (newScore > oldScore) {
                  userRef.update("highScore", newScore);
                }
              }
            });
  }
}
