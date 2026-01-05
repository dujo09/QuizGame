package com.soss.quizgame.view;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.widget.*;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.soss.quizgame.R;
import com.soss.quizgame.model.Question;
import com.soss.quizgame.model.UserGame;
import com.soss.quizgame.repository.QuizRepository;
import com.soss.quizgame.repository.UserGameRepository;
import com.soss.quizgame.viewmodel.QuizViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class QuizActivity extends ComponentActivity {
  private QuizViewModel quizViewModel;
  private TextView textQuestion;
  private Button[] optionButtons = new Button[4];
  private Button finishButton, nextButton;
  private TextView tvCorrect, tvIncorrect, tvScore, tvError;
  private int[] optionIds = {R.id.buttonA, R.id.buttonB, R.id.buttonC, R.id.buttonD};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quiz);

    tvScore = findViewById(R.id.tvScore);
    tvError = findViewById(R.id.tvError);
    tvError.setText("");

    finishButton = findViewById(R.id.buttonFinish);
    finishButton.setOnClickListener(v -> endQuiz());
    finishButton.setVisibility(GONE);

    nextButton = findViewById(R.id.buttonNext);
    nextButton.setOnClickListener(v -> quizViewModel.nextQuestion());
    nextButton.setEnabled(false);

    tvCorrect = findViewById(R.id.tvCorrect);
    tvCorrect.setVisibility(GONE);

    tvIncorrect = findViewById(R.id.tvIncorrect);
    tvIncorrect.setVisibility(GONE);

    textQuestion = findViewById(R.id.textViewQuestion);
    for (int i = 0; i < 4; i++) {
      optionButtons[i] = findViewById(optionIds[i]);
      final int idx = i;
      optionButtons[i].setOnClickListener(v -> onAnswerSelected(idx));
    }

    quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

    quizViewModel
        .getCurrentQuestion()
        .observe(
            this,
            currentQuestion -> {
              if (currentQuestion != null) {
                showQuestion(currentQuestion);
              }
            });
    quizViewModel.getError().observe(this, error -> {
      if (error != null && !error.isEmpty()) tvError.setText(error);
    });
  }

  private void showQuestion(Question question) {
    nextButton.setEnabled(false);
    tvIncorrect.setVisibility(GONE);
    tvCorrect.setVisibility(GONE);

    if (question != null) {
      optionButtons[0].setEnabled(true);
      optionButtons[1].setEnabled(true);
      optionButtons[2].setEnabled(true);
      optionButtons[3].setEnabled(true);

      textQuestion.setText(question.questionText);
      optionButtons[0].setText(question.options.get(0));
      optionButtons[1].setText(question.options.get(1));
      optionButtons[2].setText(question.options.get(2));
      optionButtons[3].setText(question.options.get(3));
    }
  }

  private void onAnswerSelected(int selectedIdx) {
    optionButtons[0].setEnabled(false);
    optionButtons[1].setEnabled(false);
    optionButtons[2].setEnabled(false);
    optionButtons[3].setEnabled(false);

    boolean isCorrect = quizViewModel.answerCurrentQuestion(selectedIdx);

    if (isCorrect) {
      tvCorrect.setVisibility(VISIBLE);
      tvScore.setText(quizViewModel.getScore() + " \uD83C\uDF1F");
    } else {
      tvIncorrect.setVisibility(VISIBLE);
    }

    if (quizViewModel.isQuizFinished()) {
      finishButton.setVisibility(VISIBLE);
      nextButton.setVisibility(GONE);
    } else {
      nextButton.setEnabled(true);
    }
  }

  private void endQuiz() {
    int finalScore = quizViewModel.getScore();
    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    quizViewModel.updateHighScore(uid, finalScore);

    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    UserGame userGame = new UserGame(uid, finalScore, now.format(formatter));
    quizViewModel.addUserGame(userGame);

    Toast.makeText(this, "Quiz over! Score: " + finalScore, Toast.LENGTH_LONG).show();
    finish();
  }
}
