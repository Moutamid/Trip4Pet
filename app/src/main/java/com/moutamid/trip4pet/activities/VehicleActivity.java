package com.moutamid.trip4pet.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.Stash;
import com.moutamid.trip4pet.databinding.ActivityVehicleBinding;

public class VehicleActivity extends AppCompatActivity {
    ActivityVehicleBinding binding;
    MaterialCardView selected;
    String selectedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityVehicleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.setLocale(getBaseContext(), Stash.getString(Constants.LANGUAGE, "en"));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText(getString(R.string.my_vehicle));

        selectedText = Stash.getString(Constants.Vehicle, getString(R.string.not_defined));

        selected = binding.notDefined;

        binding.notDefined.setOnClickListener(v -> changeUI(v, getString(R.string.not_defined)));
        binding.smallDog.setOnClickListener(v -> changeUI(v, getString(R.string.small_dog)));
        binding.mediumDog.setOnClickListener(v -> changeUI(v, getString(R.string.medium_dog)));
        binding.bigDog.setOnClickListener(v -> changeUI(v, getString(R.string.big_dog)));
        binding.cat.setOnClickListener(v -> changeUI(v, getString(R.string.cat)));
        binding.hamster.setOnClickListener(v -> changeUI(v, getString(R.string.hamster)));
        binding.birds.setOnClickListener(v -> changeUI(v, getString(R.string.birds)));
        binding.others.setOnClickListener(v -> changeUI(v, getString(R.string.others)));

        if (selectedText.equals(getString(R.string.not_defined))) {
            changeUI(binding.notDefined, getString(R.string.not_defined));
        } else if (selectedText.equals(getString(R.string.small_dog))) {
            changeUI(binding.smallDog, getString(R.string.small_dog));
        } else if (selectedText.equals(getString(R.string.medium_dog))) {
            changeUI(binding.mediumDog, getString(R.string.medium_dog));
        } else if (selectedText.equals(getString(R.string.big_dog))) {
            changeUI(binding.bigDog, getString(R.string.big_dog));
        } else if (selectedText.equals(getString(R.string.cat))) {
            changeUI(binding.cat, getString(R.string.cat));
        } else if (selectedText.equals(getString(R.string.hamster))) {
            changeUI(binding.hamster, getString(R.string.hamster));
        } else if (selectedText.equals(getString(R.string.birds))) {
            changeUI(binding.birds, getString(R.string.birds));
        } else if (selectedText.equals(getString(R.string.others))) {
            changeUI(binding.others, getString(R.string.others));
        }

        binding.apply.setOnClickListener(v -> {
            Stash.put(Constants.Vehicle, selectedText);
            onBackPressed();
        });

    }

    private void changeUI(View v, String vehicle) {
        selectedText = vehicle;
        selected.setCardBackgroundColor(getResources().getColor(R.color.grey));
        selected.setStrokeColor(getResources().getColor(R.color.grey));
        selected = (MaterialCardView) v;
        selected.setCardBackgroundColor(getResources().getColor(R.color.blue_light));
        selected.setStrokeColor(getResources().getColor(R.color.blue));
    }
}