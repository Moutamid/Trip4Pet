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

import com.fxn.stash.Stash;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.SplashActivity;
import com.moutamid.trip4pet.databinding.ActivityAccountSettingBinding;
import com.moutamid.trip4pet.models.UserModel;

public class AccountSettingActivity extends AppCompatActivity {
    ActivityAccountSettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAccountSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.setLocale(getBaseContext(), Stash.getString(Constants.LANGUAGE, "en"));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText(R.string.account_settings);

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

        binding.delete.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.warning))
                    .setMessage(getString(R.string.deleting_data))
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        dialog.dismiss();
                        deleteData();
                    })
                    .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss())
                    .show();
        });

        binding.vehicle.setOnClickListener(v -> startActivity(new Intent(this, VehicleActivity.class)));
        binding.name.setOnClickListener(v -> startActivity(new Intent(this, NameActivity.class)));
//        binding.social.setOnClickListener(v  -> startActivity(new Intent(this, SocialActivity.class)));
//        binding.code.setOnClickListener(v  -> showCodeDialog());
        binding.password.setOnClickListener(v -> startActivity(new Intent(this, PasswordActivity.class)));

    }

    private void deleteData() {
        // TODO
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
       if (userModel != null){
           binding.email.setText(userModel.email);
           binding.name.setText(userModel.name);

           String vehicle = Stash.getString(Constants.Vehicle, getString(R.string.not_defined));
           binding.myVehicle.setText(vehicle);
       }
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