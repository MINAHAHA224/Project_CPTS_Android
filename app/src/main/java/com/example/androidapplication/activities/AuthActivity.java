


package com.example.androidapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapplication.R;
import com.example.androidapplication.data.model.ApiResponse;
import com.example.androidapplication.data.model.user.InformationDTO;
import com.example.androidapplication.databinding.ActivityAuthBinding;
import com.example.androidapplication.utils.DialogUtils;
import com.example.androidapplication.utils.SharedPrefManager;
import com.example.androidapplication.viewmodel.AuthViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.stream.Collectors;

public class AuthActivity extends AppCompatActivity {
    private ActivityAuthBinding binding;
    private AuthViewModel authViewModel;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        configureGoogleSignIn();


        binding.signUpText.setText(Html.fromHtml("Chưa có tài khoản? <b>Đăng ký ngay</b>"));
        binding.signUpText.setOnClickListener(v -> {
            startActivity(new Intent(AuthActivity.this, RegisterActivity.class));
        });

        binding.loginButton.setOnClickListener(v -> loginUser());
        binding.googleLoginButton.setOnClickListener(v -> signInWithGoogle());
        // THÊM ĐOẠN NÀY
        binding.forgotPasswordText.setOnClickListener(v -> {
            startActivity(new Intent(AuthActivity.this, ForgotPasswordActivity.class));
        });
        // Gắn sự kiện để mở GoogleAuthActivity
//        binding.googleLoginButton.setOnClickListener(v -> {
//            startActivity(new Intent(AuthActivity.this, GoogleAuthActivity.class));
//        });
    }

    private void loginUser() {
        String email = binding.emailEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            DialogUtils.showErrorDialog(this, "Please fill all fields");
            return;
        }

        authViewModel.login(email, password).observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    setLoading(true);
                    break;
                case SUCCESS:
                    setLoading(false);
                    handleLoginSuccess(resource.data);
                    break;
                case ERROR:
                    setLoading(false);
                    String errorMessage = resource.error != null ? resource.error.getErrorDetails().stream().map(it -> it.getMessage()).collect(Collectors.joining()) : "An unknown error occurred.";
                    DialogUtils.showErrorDialog(this, errorMessage);
                    break;
            }
        });
    }
    private void configureGoogleSignIn() {
        // Cấu hình để yêu cầu serverAuthCode
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(getString(R.string.default_web_client_id)) // Yêu cầu mã 'code'
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        handleGoogleSignInResult(task);
                    } else {
                        setLoading(false);
                        DialogUtils.showErrorDialog(this, "Google Sign In cancelled.");
                    }
                });
    }

    private void signInWithGoogle() {
        setLoading(true);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // LẤY ĐƯỢC serverAuthCode THÀNH CÔNG!
            String authCode = account.getServerAuthCode();
            Log.d("GoogleSignIn", "Server Auth Code: " + authCode);

            // Gửi 'code' này lên backend của bạn
            if (authCode != null) {
                authViewModel.loginWithGoogle(authCode).observe(this, resource -> {
                    switch (resource.status) {
                        case LOADING:
                            // Đã setLoading(true) từ trước
                            break;
                        case SUCCESS:
                            setLoading(false);
                            handleLoginSuccess(resource.data);
                            break;
                        case ERROR:
                            setLoading(false);
                            String errorMessage = resource.error != null ? resource.error.getMessage() : "An unknown error occurred.";
                            DialogUtils.showErrorDialog(this, errorMessage);
                            break;
                    }
                });
            } else {
                setLoading(false);
                DialogUtils.showErrorDialog(this, "Could not get auth code from Google.");
            }
        } catch (ApiException e) {
            setLoading(false);
            Log.w("GoogleSignIn", "signInResult:failed code=" + e.getStatusCode());
            DialogUtils.showErrorDialog(this, "Google Sign In Error: " + e.getMessage());
        }
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.loginButton.setVisibility(View.INVISIBLE);
            binding.googleLoginButton.setEnabled(false);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.loginButton.setVisibility(View.VISIBLE);
            binding.googleLoginButton.setEnabled(true);
        }
    }

    private void handleLoginSuccess(ApiResponse<InformationDTO> successResponse) {
        if (successResponse != null && successResponse.getData() != null) {
            SharedPrefManager.getInstance(getApplicationContext()).saveUser(successResponse.getData());
            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            DialogUtils.showErrorDialog(this, "Login successful but no user data received.");
        }
    }
}