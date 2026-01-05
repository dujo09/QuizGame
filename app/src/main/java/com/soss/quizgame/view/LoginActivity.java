package com.soss.quizgame.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;
import com.soss.quizgame.R;
import com.soss.quizgame.viewmodel.AuthViewModelShared;

public class LoginActivity extends ComponentActivity {
  private AuthViewModelShared authViewModelShared;
  private EditText etEmail, etPassword;
  private Button btnLogin;
  private TextView tvRegister, tvError;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    authViewModelShared = new ViewModelProvider(this).get(AuthViewModelShared.class);

    etEmail = findViewById(R.id.etEmail);
    etPassword = findViewById(R.id.etPassword);
    btnLogin = findViewById(R.id.btnLogin);
    tvRegister = findViewById(R.id.tvRegister);
    tvError = findViewById(R.id.tvError);
    tvError.setText("");

    btnLogin.setOnClickListener(
        v -> {
          String email = etEmail.getText().toString().trim();
          String password = etPassword.getText().toString();
          if (email.isEmpty() || password.isEmpty()) {
            tvError.setText("Login failed: Email and password are required");
            return;
          }

          authViewModelShared
              .signIn(email, password)
              .observe(
                  this,
                  user -> {
                    if (user != null) {
                      finish();
                    }
                  });
        });
    authViewModelShared
        .getError()
        .observe(
            this,
            error -> {
              if (error != null && !error.isEmpty()) {
                tvError.setText("Login failed: " + error);
              }
            });
    authViewModelShared
        .getUser()
        .observe(
            this,
            user -> {
              if (user != null) {
                finish();
              }
            });
    tvRegister.setOnClickListener(
        v -> {
          startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
  }
}
