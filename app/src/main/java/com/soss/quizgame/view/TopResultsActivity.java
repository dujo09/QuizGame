package com.soss.quizgame.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.soss.quizgame.R;
import com.soss.quizgame.viewmodel.TopResultsViewModel;

public class TopResultsActivity extends ComponentActivity {
  private TopResultsViewModel topResultsViewModel;
  private RecyclerView recyclerTopResults;
  private UserGameAdapter adapter;
  private TextView tvError;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_top_results);

    tvError = findViewById(R.id.tvError);
    tvError.setText("");

    recyclerTopResults = findViewById(R.id.recyclerTopResults);
    recyclerTopResults.setLayoutManager(new LinearLayoutManager(this));

    adapter = new UserGameAdapter();
    recyclerTopResults.setAdapter(adapter);

    topResultsViewModel = new ViewModelProvider(this).get(TopResultsViewModel.class);

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    if (firebaseUser == null) {
      startActivity(new Intent(this, LoginActivity.class));
      finish();
      return;
    }

    topResultsViewModel.fetchUserTopResults(firebaseUser.getUid());

    topResultsViewModel
        .getUserGames()
        .observe(
            this,
            userGames -> {
              if (userGames != null) adapter.submitList(userGames);
            });

    topResultsViewModel
        .getError()
        .observe(
            this,
            error -> {
              if (error != null && !error.isEmpty()) tvError.setText(error);
            });
  }
}
