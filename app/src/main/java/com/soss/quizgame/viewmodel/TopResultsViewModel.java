package com.soss.quizgame.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.soss.quizgame.model.UserGame;
import com.soss.quizgame.repository.UserGameRepository;
import java.util.List;

public class TopResultsViewModel extends ViewModel {
  private final LiveData<List<UserGame>> userGamesLiveData;
  private final UserGameRepository userGameRepo;

  public TopResultsViewModel() {
    userGameRepo = new UserGameRepository();
    userGamesLiveData = userGameRepo.getUserGameLiveData();
  }

  public void fetchUserTopResults(String userId) {
    userGameRepo.fetchUserTopResults(userId);
  }

  public LiveData<String> getError() {
    return userGameRepo.getError();
  }

  public LiveData<List<UserGame>> getUserGames() {
    return userGamesLiveData;
  }
}
