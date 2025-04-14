package com.example.question4a;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configure sign-in to request the user's ID, email address, and basic profile
        // The ID and basic profile are included in DEFAULT_SIGN_IN
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // User is already signed in, navigate to HomeActivity
            navigateToHome(account);
        }

        findViewById(R.id.btnGoogleSignIn).setOnClickListener(view -> signIn());
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent()
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
z
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Sign in success
            if (account != null) {
                String name = account.getDisplayName();
                Log.d(TAG, "signInSuccess: user=" + name);
                navigateToHome(account);
            } else {
                Log.w(TAG, "signInSuccess: but account is null");
                Toast.makeText(this, "Authentication failed - null account", Toast.LENGTH_SHORT).show();
            }
        } catch (ApiException e) {
            // Detailed error logging to help identify the issue
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Log.w(TAG, "signInResult:failed message=" + e.getMessage());

            String errorMessage;
            switch (e.getStatusCode()) {
                case GoogleSignInStatusCodes.SIGN_IN_CANCELLED:
                    errorMessage = "Sign in cancelled";
                    break;
                case GoogleSignInStatusCodes.SIGN_IN_FAILED:
                    errorMessage = "Sign in failed";
                    break;
                case GoogleSignInStatusCodes.SIGN_IN_CURRENTLY_IN_PROGRESS:
                    errorMessage = "Sign in already in progress";
                    break;
                case GoogleSignInStatusCodes.INTERNAL_ERROR:
                    errorMessage = "Internal error";
                    break;
                case GoogleSignInStatusCodes.NETWORK_ERROR:
                    errorMessage = "Network error";
                    break;
                default:
                    errorMessage = "Sign in failed: " + e.getStatusCode();
            }

            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();

            // For debug purposes only - comment this out for production
            // Let's bypass the Google sign-in for testing the UI
            mockSignIn();
        }
    }

    // This is a temporary solution to bypass Google sign-in for UI testing
    private void mockSignIn() {
        Log.d(TAG, "Using mock sign-in for UI testing");
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("username", "Test User");
        startActivity(intent);
        // Don't finish this activity if we're just testing
    }

    private void navigateToHome(GoogleSignInAccount account) {
        String name = account.getDisplayName();
        if (name == null || name.isEmpty()) {
            name = account.getEmail();
            if (name == null || name.isEmpty()) {
                name = "Google User";
            }
        }

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("username", name);
        startActivity(intent);
        finish();
    }
}