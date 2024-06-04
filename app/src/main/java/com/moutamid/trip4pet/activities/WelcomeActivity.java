package com.moutamid.trip4pet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fxn.stash.Stash;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.MainActivity;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.databinding.ActivityWelcomeBinding;
import com.moutamid.trip4pet.models.UserModel;

public class WelcomeActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    ActivityWelcomeBinding binding;

    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.create.setOnClickListener(v -> {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        });

        binding.google.setOnClickListener(v -> {
            Constants.showDialog();
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("firebaseAuthWithGoogle", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, getString(R.string.google_sign_in_failed) + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.w("firebaseAuthWithGoogle", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        Constants.auth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = Constants.auth().getCurrentUser();
                            updateUI(user);
                        } else {
                            Constants.dismissDialog();
                            Toast.makeText(WelcomeActivity.this, getString(R.string.google_sign_in_failed_error_code) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Constants.databaseReference().child(Constants.USER).child(user.getUid()).get()
                .addOnSuccessListener(snapshot -> {
                    Constants.dismissDialog();
                    if (!snapshot.exists()) {
                        UserModel userDetails = new UserModel();
                        userDetails.id = user.getUid();
                        userDetails.email = user.getEmail();
                        userDetails.name = user.getDisplayName();
                        userDetails.password = "";
                        userDetails.vip = false;
                        Stash.put(Constants.STASH_USER, userDetails);
                        Constants.databaseReference().child(Constants.USER).child(user.getUid())
                                .setValue(userDetails).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(WelcomeActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(WelcomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}