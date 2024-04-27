package com.moutamid.trip4pet.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.databinding.ActivityVehicleBinding;

public class VehicleActivity extends AppCompatActivity {
    ActivityVehicleBinding binding;
    MaterialCardView selected;
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
        binding.toolbar.title.setText("My Vehicle");

        selected = binding.notDefined;

        binding.notDefined.setOnClickListener(this::changeUI);
        binding.van.setOnClickListener(this::changeUI);
        binding.largeVan.setOnClickListener(this::changeUI);
        binding.miniVan.setOnClickListener(this::changeUI);
        binding.x4.setOnClickListener(this::changeUI);
        binding.overcab.setOnClickListener(this::changeUI);
        binding.lowProfile.setOnClickListener(this::changeUI);
        binding.aClass.setOnClickListener(this::changeUI);
        binding.heavy.setOnClickListener(this::changeUI);

    }

    private void changeUI(View v) {
        selected.setCardBackgroundColor(getResources().getColor(R.color.grey));
        selected.setStrokeColor(getResources().getColor(R.color.grey));
        selected = (MaterialCardView) v;
        selected.setCardBackgroundColor(getResources().getColor(R.color.blue_light));
        selected.setStrokeColor(getResources().getColor(R.color.blue));
    }
}