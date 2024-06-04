package com.moutamid.trip4pet.activities;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.databinding.ActivityForgotBinding;

public class ForgotActivity extends AppCompatActivity {
    ActivityForgotBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityForgotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText("Reset Password");

        Constants.initDialog(this);

        binding.reset.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                Constants.auth().sendPasswordResetEmail(binding.email.getEditText().getText().toString().trim())
                        .addOnSuccessListener(unused -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, "A password reset link is sent to your email", Toast.LENGTH_LONG).show();
                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

    }

    private boolean valid() {
        if (binding.email.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.email_is_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getEditText().getText().toString()).matches()) {
            Toast.makeText(this, R.string.email_is_not_valid, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}