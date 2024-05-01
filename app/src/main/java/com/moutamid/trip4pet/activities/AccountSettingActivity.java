package com.moutamid.trip4pet.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.databinding.ActivityAccountSettingBinding;

public class AccountSettingActivity extends AppCompatActivity {
    ActivityAccountSettingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAccountSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText("Account Settings");

        binding.logout.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(this)
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> dialog.dismiss())
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        binding.vehicle.setOnClickListener(v  -> startActivity(new Intent(this, VehicleActivity.class)));
        binding.name.setOnClickListener(v  -> startActivity(new Intent(this, NameActivity.class)));
//        binding.social.setOnClickListener(v  -> startActivity(new Intent(this, SocialActivity.class)));
//        binding.code.setOnClickListener(v  -> showCodeDialog());
        binding.password.setOnClickListener(v  -> startActivity(new Intent(this, PasswordActivity.class)));

    }

    private void showCodeDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.code_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.show();

    }
}