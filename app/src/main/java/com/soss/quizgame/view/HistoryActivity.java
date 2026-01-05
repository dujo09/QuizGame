package com.soss.quizgame.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.soss.quizgame.R;
import com.soss.quizgame.viewmodel.AuthViewModelShared;
import com.soss.quizgame.viewmodel.HistoryViewModel;

public class HistoryActivity extends ComponentActivity {
  private static final String TAG = "HistoryActivity";
  private HistoryViewModel historyViewModel;
  private AuthViewModelShared authViewModelShared;
  private RecyclerView rvHistory;
  private HistoryAdapter adapter;
  private Button buttonPlay;
  private TextView tvError;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_history);

    tvError = findViewById(R.id.tvError);
    tvError.setText("");

    buttonPlay = findViewById(R.id.buttonPlay);
    rvHistory = findViewById(R.id.rvHistory);
    rvHistory.setLayoutManager(new LinearLayoutManager(this));

    historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
    adapter = new HistoryAdapter();
    rvHistory.setAdapter(adapter);

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
                historyViewModel.fetchUserGames(user.uid);
              }
            });

    historyViewModel
        .getUserGames()
        .observe(
            this,
            userGames -> {
              if (userGames != null) adapter.submitList(userGames);
            });
    historyViewModel
        .getError()
        .observe(
            this,
            error -> {
              if (error != null && !error.isEmpty()) tvError.setText(error);
            });
    buttonPlay.setOnClickListener(
        v -> {
          startActivity(new Intent(this, QuizActivity.class));
        });
  }
}
