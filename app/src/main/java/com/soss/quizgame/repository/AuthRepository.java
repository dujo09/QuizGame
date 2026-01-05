package com.soss.quizgame.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.soss.quizgame.model.User;

public class AuthRepository {
  private static final String TAG = "UserRepository";
  private static final String PREFS = "userPrefs";
  private static final String KEY_USER = "cachedUser";
  private static AuthRepository instance;
  private final Gson gson = new Gson();
  private final MutableLiveData<User> userLive = new MutableLiveData<>();
  private final MutableLiveData<String> error = new MutableLiveData<>();
  private final SharedPreferences prefs;
  private FirebaseAuth mAuth;
  private FirebaseFirestore db;

  public AuthRepository(Context context) {
    mAuth = FirebaseAuth.getInstance();
    db = FirebaseFirestore.getInstance();
    prefs = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    String json = prefs.getString(KEY_USER, null);
    if (json != null) {
      try {
        User cached = gson.fromJson(json, User.class);
        userLive.postValue(cached);
      } catch (Exception e) {
        Log.w(TAG, "failed to parse cached user", e);
      }
    } else {
      userLive.postValue(null);
    }
  }

  public static synchronized AuthRepository getInstance(Context ctx) {
    if (instance == null) instance = new AuthRepository(ctx);
    return instance;
  }

  public MutableLiveData<FirebaseUser> signUp(String username, String email, String password) {
    MutableLiveData<FirebaseUser> result = new MutableLiveData<>();
    mAuth
        .createUserWithEmailAndPassword(email, password)
        .addOnSuccessListener(
            authResult -> {
              FirebaseUser fbUser = mAuth.getCurrentUser();
              if (fbUser != null) {
                String uid = fbUser.getUid();
                User user = new User(uid, username, email, 0);
                db.collection("users").document(uid).set(user);
                result.postValue(fbUser);
              }
              cacheUser(fbUser);
            })
        .addOnFailureListener(
            e -> {
              error.postValue(e.getMessage());
              result.postValue(null);
              clearCache();
            });
    return result;
  }

  public MutableLiveData<FirebaseUser> signIn(String email, String password) {
    MutableLiveData<FirebaseUser> result = new MutableLiveData<>();
    mAuth
        .signInWithEmailAndPassword(email, password)
        .addOnSuccessListener(
            authResult -> {
              FirebaseUser firebaseUser = mAuth.getCurrentUser();
              result.postValue(firebaseUser);
              cacheUser(firebaseUser);
            })
        .addOnFailureListener(
            e -> {
              error.postValue(e.getMessage());
              result.postValue(null);
              clearCache();
            });
    return result;
  }

  public void signOut() {
    mAuth.signOut();
    clearCache();
  }

  public void cacheUser(FirebaseUser user) {
    if (user == null) {
      userLive.postValue(null);
      return;
    }
    String uid = user.getUid();
    db.collection("users")
        .document(uid)
        .get()
        .addOnSuccessListener(
            doc -> {
              if (!doc.exists()) {
                clearCache();
                return;
              }

              User u = doc.toObject(User.class);
              if (u == null) {
                clearCache();
                return;
              }

              userLive.postValue(u);
              saveToPrefs(u);
            })
        .addOnFailureListener(e -> Log.w(TAG, "refresh failed", e));
  }

  public void saveToPrefs(User u) {
    try {
      String json = gson.toJson(u);
      prefs.edit().putString(KEY_USER, json).apply();
    } catch (Exception e) {
      Log.w(TAG, "save prefs failed", e);
    }
  }

  public void clearCache() {
    prefs.edit().remove(KEY_USER).apply();
    userLive.postValue(null);
  }

  public LiveData<User> getUserLive() {
    return userLive;
  }

  public LiveData<String> getError() {
    return error;
  }
}
