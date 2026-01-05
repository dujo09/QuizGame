package com.soss.quizgame.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.soss.quizgame.R;
import com.soss.quizgame.viewmodel.AuthViewModelShared;
import com.soss.quizgame.viewmodel.MainViewModel;

public class MainActivity extends ComponentActivity {
  private static final String TAG = "MainActivity";
  private MainViewModel mainViewModel;
  private AuthViewModelShared authViewModelShared;
  private RecyclerView recyclerLeaderboard;
  private UserGameAdapter adapter;
  private Button buttonPlay, buttonLogout;
  private TextView tvUser, tvError;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    buttonLogout = findViewById(R.id.btnLogout);

    tvUser = findViewById(R.id.tvUser);
    tvError = findViewById(R.id.tvError);
    tvError.setText("");

    buttonPlay = findViewById(R.id.buttonPlay);
    recyclerLeaderboard = findViewById(R.id.recyclerLeaderboard);
    recyclerLeaderboard.setLayoutManager(new LinearLayoutManager(this));

    authViewModelShared = new ViewModelProvider(this).get(AuthViewModelShared.class);

    authViewModelShared
        .getUser()
        .observe(
            this,
            user -> {
              if (user == null) {
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
              } else {
                tvUser.setText(user.username);
                mainViewModel.fetchUserGames(user.uid);
              }
            });

    buttonLogout.setOnClickListener(v -> authViewModelShared.signOut());

    mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

    adapter = new UserGameAdapter();
    recyclerLeaderboard.setAdapter(adapter);

    mainViewModel
        .getUserGames()
        .observe(
            this,
            userGames -> {
              if (userGames != null) adapter.submitList(userGames);
            });
    mainViewModel
        .getError()
        .observe(
            this,
            error -> {
              if (error != null && !error.isEmpty()) tvError.setText(error);
            });

    buttonPlay.setOnClickListener(
        v -> {
          startActivity(new Intent(MainActivity.this, QuizActivity.class));
        });
  }
}
