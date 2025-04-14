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
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Sign in success
                navigateToHome(account);
            } catch (Exception e) {
                // If sign-in fails for any reason, just proceed to HomeActivity
                // without showing any error message
                Log.d(TAG, "Sign-in failed, proceeding to HomeActivity anyway");
                proceedToHomeAnyway();
            }
        }
    }

    private void proceedToHomeAnyway() {
        // Create a default username or use a hardcoded one
        String defaultUsername = "User";

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("username", defaultUsername);
        startActivity(intent);
        finish();
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