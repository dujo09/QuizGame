package com.soss.quizgame.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.soss.quizgame.model.User;
import com.soss.quizgame.repository.AuthRepository;

public class AuthViewModelShared extends AndroidViewModel {
  private final AuthRepository authRepo;
  private final LiveData<User> user;


  public AuthViewModelShared(@NonNull Application application) {
    super(application);
    authRepo = AuthRepository.getInstance(application);
    user = authRepo.getUserLive();
  }

  public LiveData<User> getUser() {
    return user;
  }

  public LiveData<String> getError() {
    return authRepo.getError();
  }

  public LiveData<FirebaseUser> signUp(String username, String email, String password) {
    return authRepo.signUp(username, email, password);
  }

  public LiveData<FirebaseUser> signIn(String email, String password) {
    return authRepo.signIn(email, password);
  }

  public void signOut() {
    authRepo.signOut();
  }
}
