package com.soss.quizgame.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.soss.quizgame.model.Question;
import com.soss.quizgame.model.UserGame;
import com.soss.quizgame.repository.QuizRepository;
import com.soss.quizgame.repository.UserGameRepository;
import java.util.List;

public class QuizViewModel extends ViewModel {
  private static final String TAG = "QuizViewModel";
  private final QuizRepository quizRepo;
  private final UserGameRepository userGameRepository;
  private final MutableLiveData<List<Question>> questionsLiveData;
  private final MediatorLiveData<Question> currentQuestion = new MediatorLiveData<>();
  private final MutableLiveData<Integer> currentIndex = new MutableLiveData<>(0);
  private boolean areAllQuestionsCorrect = true;
  private boolean isQuizFinished = false;
  private int score = 0;

  public QuizViewModel() {
    userGameRepository = new UserGameRepository();
    quizRepo = new QuizRepository();
    questionsLiveData = quizRepo.getQuestionsLiveData();

    currentQuestion.addSource(
        questionsLiveData,
        list -> {
          if (list == null || list.isEmpty()) {
            currentIndex.setValue(-1);
          } else {
            currentIndex.setValue(0);
          }
        });

    currentQuestion.addSource(
        currentIndex,
        idx -> {
          List<Question> list = questionsLiveData.getValue();
          if (list == null || list.isEmpty() || idx == null || idx < 0 || idx >= list.size()) {
            currentQuestion.setValue(null);
          } else {
            currentQuestion.setValue(list.get(idx));
          }
        });

    fetchQuestions();
  }

  public LiveData<Question> getCurrentQuestion() {
    return currentQuestion;
  }

  public LiveData<String> getError() {
    return quizRepo.getError();
  }

  public boolean answerCurrentQuestion(int selectedOption) {
    Question question = currentQuestion.getValue();
    if (question == null) {
      return false;
    }
    List<Question> questions = questionsLiveData.getValue();
    Integer idx = currentIndex.getValue();
    if (questions != null && idx != null && idx >= questions.size() - 1) {
      isQuizFinished = true;
    }
    if (selectedOption == question.correctOptionIndex) {
      score += 100;
      return true;
    } else {
      areAllQuestionsCorrect = false;
      return false;
    }
  }

  public void nextQuestion() {
    int idx = currentIndex.getValue() == null ? 0 : currentIndex.getValue();
    currentIndex.setValue(idx + 1);
  }

  public boolean areAllQuestionsCorrect() {
    return areAllQuestionsCorrect;
  }

  public boolean isQuizFinished() {
    return isQuizFinished;
  }

  public int getScore() {
    return score;
  }

  public void fetchQuestions() {
    quizRepo.fetchQuestions();
  }

  public void updateHighScore(String uid, int score) {
    quizRepo.updateHighScore(uid, score);
  }

  public void addUserGame(UserGame userGame) {
    userGameRepository.addUserGame(userGame);
  }
}
