package com.soss.quizgame.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.soss.quizgame.model.UserGame;
import com.soss.quizgame.repository.UserGameRepository;
import java.util.List;

public class HistoryViewModel extends ViewModel {
  private final LiveData<List<UserGame>> userGamesLiveData;
  private UserGameRepository userGameRepo;

  public HistoryViewModel() {
    userGameRepo = new UserGameRepository();
    userGamesLiveData = userGameRepo.getUserGameLiveData();
  }

  public void fetchUserGames(String userId) {
    userGameRepo.fetchUserGames(userId);
  }

  public LiveData<String> getError() {
    return userGameRepo.getError();
  }

  public LiveData<List<UserGame>> getUserGames() {
    return userGamesLiveData;
  }
}
