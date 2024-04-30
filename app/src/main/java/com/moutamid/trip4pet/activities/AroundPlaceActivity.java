package com.moutamid.trip4pet.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.moutamid.trip4pet.models.Cities;
import com.moutamid.trip4pet.adapters.CitiesAdapter;
import com.moutamid.trip4pet.databinding.ActivityAroundPlaceBinding;
import com.moutamid.trip4pet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;

public class AroundPlaceActivity extends AppCompatActivity {
    ActivityAroundPlaceBinding binding;
    ArrayList<Cities> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAroundPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        list.add(new Cities("Sialkot", "Pakistan", "Punjab", 33.70411, 73.08887));
        list.add(new Cities("Lahore", "Pakistan", "Punjab", 33.70411, 73.08887));
        list.add(new Cities("Jehlum", "Pakistan", "Punjab", 33.70411, 73.08887));
        list.add(new Cities("Karachi", "Pakistan", "Sindh", 33.70411, 73.08887));
        list.add(new Cities("Quetta", "Pakistan", "Balochistan", 33.70411, 73.08887));

        binding.cities.setLayoutManager(new LinearLayoutManager(this));
        binding.cities.setHasFixedSize(false);
        CitiesAdapter adapter = new CitiesAdapter(this, list);
        binding.cities.setAdapter(adapter);

        binding.toolbar.title.setText("Add a place");

        binding.toolbar.back.setOnClickListener(v -> {
            startActivity(new Intent(this, AddPlaceActivity.class));
            finish();
        });

        binding.confirm.setOnClickListener(v -> {
            if (binding.gps.isChecked()){
                Intent intent = new Intent(this, AddPlaceActivity.class);
                intent.putExtra("GIVEN", true);
                String COORDINATES = binding.latitude.getEditText().getText().toString() + ", " + binding.longitude.getEditText().getText().toString();
                intent.putExtra("COORDINATES", COORDINATES);
                startActivity(intent);
                finish();
            }
        });

        binding.gps.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int v = isChecked ? View.VISIBLE : View.GONE;
            binding.gpsLayout.setVisibility(v);
        });

        binding.search.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()){
                    binding.gps.setVisibility(View.VISIBLE);
                    binding.cities.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().isEmpty()){
                    binding.gps.setVisibility(View.VISIBLE);
                    binding.cities.setVisibility(View.GONE);
                } else {
                    binding.gps.setChecked(false);
                    binding.gps.setVisibility(View.GONE);
                    binding.cities.setVisibility(View.VISIBLE);
                }
            }
        });

    }

}