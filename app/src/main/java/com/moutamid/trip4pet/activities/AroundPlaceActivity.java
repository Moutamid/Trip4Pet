package com.moutamid.trip4pet.activities;

import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.JsonArray;
import com.moutamid.trip4pet.Cities;
import com.moutamid.trip4pet.CitiesAdapter;
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
import java.util.List;

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

        binding.cities.setLayoutManager(new LinearLayoutManager(this));
        binding.cities.setHasFixedSize(false);

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

                    //     fetchResult(s.toString());
                }
            }
        });

    }

    public static JSONArray loadJSONArrayFromAssets(Context context, String fileName) {
        JSONArray jsonArray = null;
        try {
            // Read the JSON file from assets
            InputStream inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String jsonString = new String(buffer, StandardCharsets.UTF_8);

            // Parse the JSON string into a JSON array
            jsonArray = new JSONArray(jsonString);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    private void fetchResult(String search) {
        // Convert JSON string to JSONArray
        JSONArray jsonArray = loadJSONArrayFromAssets(this, "cities.json");
        ArrayList<Cities> matches = partialMatchSearch(jsonArray, search, new Comparator<Cities>() {
            @Override
            public int compare(Cities o1, Cities o2) {
                String name1 = o1.name;
                String country1 = o1.country;
                String name2 = o2.name;
                String country2 = o2.country;

                // Compare name or country name
                if (name1.toLowerCase().contains(search.toLowerCase()) || country1.toLowerCase().contains(search.toLowerCase())) {
                    return 0; // Match found
                } else if (name1.compareTo(name2) != 0) {
                    return name1.compareTo(name2);
                } else {
                    return country1.compareTo(country2);
                }
            }
        });

        // Display the matched JSONObjects
        if (!matches.isEmpty()) {
            CitiesAdapter adapter = new CitiesAdapter(this, matches);
            binding.cities.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No matching names found.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "fetchResult: No matching names found.");
        }

    }

    private static final String TAG = "AroundPlaceActivity";
    public static ArrayList<Cities> partialMatchSearch(JSONArray jsonArray, String searchName, Comparator<Cities> comparator) {
        ArrayList<Cities> matches = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                String name = obj.getString("name");
                String country_name = obj.getString("country_name");
                if (name.toLowerCase().contains(searchName.toLowerCase()) ||
                        country_name.toLowerCase().contains(searchName.toLowerCase())) { // Case-insensitive partial match
                    Cities cities = new Cities();
                    cities.name = name;
                    cities.country = country_name;
                    cities.state = obj.getString("state_name");
                    cities.latitude = obj.getString("latitude");
                    cities.longitude = obj.getString("longitude");
                    matches.add(cities);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return matches;
    }

}