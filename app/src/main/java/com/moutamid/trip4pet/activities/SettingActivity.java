package com.moutamid.trip4pet.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.bottomsheets.LanguageDialog;
import com.moutamid.trip4pet.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.toolbar.title.setText("Settings");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.logout.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(this)
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> dialog.dismiss())
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        binding.terms.setOnClickListener(v -> openLink("https://google.com"));
        binding.privacy.setOnClickListener(v -> openLink("https://google.com"));

        binding.language.setOnClickListener(v -> {
            LanguageDialog languageDialog = new LanguageDialog();
            languageDialog.show(getSupportFragmentManager(), languageDialog.getTag());
        });

    }

    private void openLink(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}