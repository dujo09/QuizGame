package com.soss.quizgame.view;

import android.os.Bundle;
import android.widget.*;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import com.soss.quizgame.R;
import com.soss.quizgame.viewmodel.AuthViewModelShared;

public class RegisterActivity extends ComponentActivity {
  private AuthViewModelShared authViewModelShared;
  private EditText editUsername, editEmail, editPassword;
  private Button buttonRegister;
  private TextView tvError;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    editUsername = findViewById(R.id.editTextUsername);
    editEmail = findViewById(R.id.etEmail);
    editPassword = findViewById(R.id.etPassword);
    buttonRegister = findViewById(R.id.buttonRegister);
    tvError = findViewById(R.id.tvError);
    tvError.setText("");

    authViewModelShared = new ViewModelProvider(this).get(AuthViewModelShared.class);

    buttonRegister.setOnClickListener(
        v -> {
          String username = editUsername.getText().toString().trim();
          String email = editEmail.getText().toString().trim();
          String password = editPassword.getText().toString();
          if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            tvError.setText("Register failed: Username, email and password are required");
            return;
          }

          authViewModelShared
              .signUp(username, email, password)
              .observe(
                  this,
                  user -> {
                    if (user != null) {
                      finish();
                    }
                  });

          authViewModelShared
              .getError()
              .observe(
                  this,
                  error -> {
                    if (error != null && !error.isEmpty()) {
                        tvError.setText("Register failed: " + error);
                    }
                  });
        });
  }
}
