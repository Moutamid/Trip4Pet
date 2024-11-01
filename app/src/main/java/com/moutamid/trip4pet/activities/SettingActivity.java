package com.moutamid.trip4pet.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moutamid.trip4pet.Stash;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.MainActivity;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.SplashActivity;
import com.moutamid.trip4pet.bottomsheets.LanguageDialog;
import com.moutamid.trip4pet.databinding.ActivitySettingBinding;
import com.moutamid.trip4pet.listener.BottomSheetDismissListener;

import java.util.List;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.setLocale(getBaseContext(), Stash.getString(Constants.LANGUAGE, "en"));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String measure = Stash.getString(Constants.MEASURE, Constants.Metric);

        if (measure.equals(Constants.Metric)){
            binding.metricChip.setChecked(true);
        } else {
            binding.imperialChip.setChecked(true);
        }

        binding.measureChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup chipGroup, @NonNull List<Integer> list) {
                String meas = binding.metricChip.isChecked() ? Constants.Metric : Constants.Imperial;
                Stash.put(Constants.MEASURE, meas);
            }
        });

        binding.toolbar.title.setText(R.string.settings);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.logout.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(this)
                    .setMessage(getString(R.string.are_you_sure_you_want_to_logout))
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        dialog.dismiss();
                        Constants.auth().signOut();
                        startActivity(new Intent(this, SplashActivity.class));
                        finish();
                    })
                    .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss())
                    .show();
        });

        binding.terms.setOnClickListener(v -> startActivity(new Intent(this, ContractActivity.class)));
        binding.privacy.setOnClickListener(v -> openLink("https://google.com"));

        binding.language.setOnClickListener(v -> {
            LanguageDialog languageDialog = new LanguageDialog();
            languageDialog.setListener(() -> {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            });
            languageDialog.show(getSupportFragmentManager(), languageDialog.getTag());
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void openLink(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}