package com.soss.quizgame.repository;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.*;
import com.soss.quizgame.model.UserGame;
import java.util.*;

public class UserGameRepository {
  private static String TAG = "UserGameRepository";
  private final MutableLiveData<List<UserGame>> userGameLiveData = new MutableLiveData<>();
  private final MutableLiveData<String> error = new MutableLiveData<>();
  private FirebaseFirestore db;

  public UserGameRepository() {
    db = FirebaseFirestore.getInstance();
  }

  public void fetchUserGames(String userId) {
    Log.i(TAG, "fetchUserGames: " + userId);
    db.collection("user_games")
        .orderBy("timestamp")
        .whereEqualTo("uid", userId)
        .limitToLast(10)
        .addSnapshotListener(
            (snapshots, e) -> {
              if (e != null) {
                error.postValue(e.getMessage());
                Log.i(TAG, "fetchUserGames: " + e);
                return;
              }
              List<UserGame> userGames = new ArrayList<>();
              for (QueryDocumentSnapshot doc : snapshots) {
                UserGame userGame = doc.toObject(UserGame.class);
                userGames.add(userGame);
              }
              userGameLiveData.postValue(userGames);
            });
  }

  public void addUserGame(UserGame userGame) {
    db.collection("user_games").add(userGame).addOnFailureListener(e -> {
        error.postValue(e.getMessage());
    });
  }

  public LiveData<List<UserGame>> getUserGameLiveData() {
    return userGameLiveData;
  }

  public MutableLiveData<String> getError() {
    return error;
  }
}
