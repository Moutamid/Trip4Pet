package com.moutamid.trip4pet.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fxn.stash.Stash;
import com.google.android.material.card.MaterialCardView;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
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
        binding.van.setOnClickListener(v -> changeUI(v, getString(R.string.van)));
        binding.largeVan.setOnClickListener(v -> changeUI(v, getString(R.string.large_van)));
        binding.miniVan.setOnClickListener(v -> changeUI(v, getString(R.string.mini_van)));
        binding.x4.setOnClickListener(v -> changeUI(v, getString(R.string._4x4)));
        binding.overcab.setOnClickListener(v -> changeUI(v, getString(R.string.overcab_motorhome)));
        binding.lowProfile.setOnClickListener(v -> changeUI(v, getString(R.string.low_profile_motorhome)));
        binding.aClass.setOnClickListener(v -> changeUI(v, getString(R.string.a_class_motorhome)));
        binding.heavy.setOnClickListener(v -> changeUI(v, getString(R.string.motorhome_heavyweight)));

        if (selectedText.equals(getString(R.string.not_defined))){
            changeUI(binding.notDefined, getString(R.string.not_defined));
        } else if (selectedText.equals(getString(R.string.van))){
            changeUI(binding.van, getString(R.string.van));
        } else if (selectedText.equals(getString(R.string.large_van))){
            changeUI(binding.largeVan, getString(R.string.large_van));
        } else if (selectedText.equals(getString(R.string.mini_van))){
            changeUI(binding.miniVan, getString(R.string.mini_van));
        } else if (selectedText.equals(getString(R.string._4x4))){
            changeUI(binding.x4, getString(R.string._4x4));
        } else if (selectedText.equals(getString(R.string.overcab_motorhome))){
            changeUI(binding.overcab, getString(R.string.overcab_motorhome));
        } else if (selectedText.equals(getString(R.string.low_profile_motorhome))){
            changeUI(binding.lowProfile, getString(R.string.low_profile_motorhome));
        } else if (selectedText.equals(getString(R.string.a_class_motorhome))){
            changeUI(binding.aClass, getString(R.string.a_class_motorhome));
        } else if (selectedText.equals(getString(R.string.motorhome_heavyweight))){
            changeUI(binding.heavy, getString(R.string.motorhome_heavyweight));
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