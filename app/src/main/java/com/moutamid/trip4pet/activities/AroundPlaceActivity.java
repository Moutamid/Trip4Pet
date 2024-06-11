package com.moutamid.trip4pet.activities;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fxn.stash.Stash;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.adapters.CitiesAdapter;
import com.moutamid.trip4pet.databinding.ActivityAroundPlaceBinding;
import com.moutamid.trip4pet.fragments.AroundPlaceFragment;
import com.moutamid.trip4pet.listener.CityClick;
import com.moutamid.trip4pet.models.Cities;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class AroundPlaceActivity extends AppCompatActivity {
    ActivityAroundPlaceBinding binding;
    ArrayList<Cities> list = new ArrayList<>();
    private static final String TAG = "AroundPlaceActivity";
    CitiesAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAroundPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.setLocale(getBaseContext(), Stash.getString(Constants.LANGUAGE, "en"));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.cities.setLayoutManager(new LinearLayoutManager(this));
        binding.cities.setHasFixedSize(false);

        Constants.initDialog(this);

//        list = Stash.getArrayList(Constants.CITIES, Cities.class);
        if (list.isEmpty()) {
            Constants.showDialog();
            MyTask task = new MyTask();
            task.execute();
        }
//        else {
//            adapter = new CitiesAdapter(requireActivity(), list, cityClick);
//            binding.cities.setAdapter(adapter);
//        }

//        list = Stash.getArrayList(Constants.CITIES, Cities.class);
//        if (list.isEmpty()) {
//            Constants.showDialog();
//            MyTask task = new MyTask();
//            task.execute();
//        } else {
//            adapter = new CitiesAdapter(AroundPlaceActivity.this, list);
//            binding.cities.setAdapter(adapter);
//        }

        binding.toolbar.title.setText(getString(R.string.add_a_place));

        binding.toolbar.back.setOnClickListener(v -> {
            startActivity(new Intent(this, AddPlaceActivity.class));
            finish();
        });

        binding.confirm.setOnClickListener(v -> {
            if (binding.gps.isChecked()) {
                Intent intent = new Intent(this, AddPlaceActivity.class);
                intent.putExtra("GIVEN", true);
                String COORDINATES = binding.latitude.getEditText().getText().toString() + ", " + binding.longitude.getEditText().getText().toString();
                String PLACE = binding.city.getEditText().getText().toString() + ", " + binding.country.getEditText().getText().toString();
                intent.putExtra(Constants.COORDINATES, COORDINATES);
                intent.putExtra(Constants.PLACE, PLACE);
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
                if (s.toString().isEmpty()) {
                    binding.gps.setVisibility(View.VISIBLE);
                    binding.cities.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    binding.gps.setVisibility(View.VISIBLE);
                    binding.cities.setVisibility(View.GONE);
                } else {
                    binding.gps.setChecked(false);
                    binding.gps.setVisibility(View.GONE);
                    binding.cities.setVisibility(View.VISIBLE);
                    if (s.toString().length() > 3) {
                        adapter.filter(s.toString());
                    }
                }
            }
        });

    }

    public class MyTask extends AsyncTask<Void, Void, ArrayList<Cities>> {

        @Override
        protected ArrayList<Cities> doInBackground(Void... voids) {
            try {
                Log.d(TAG, "doInBackground: Reading json");
                AssetManager assetManager = AroundPlaceActivity.this.getAssets();
                InputStream inputStream = assetManager.open("cities.json");
                Reader reader = new InputStreamReader(inputStream, "UTF-8"); // Replace "UTF-8" with appropriate encoding if known

                JsonReader jsonReader = new JsonReader(reader);
                jsonReader.setLenient(true); // Handle potential parsing issues

                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Cities>>() {
                }.getType(); // Replace MyDataModel with your actual class
                ArrayList<Cities> dataArray = new ArrayList<>();
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    Cities dataItem = gson.fromJson(jsonReader, Cities.class);
                    dataArray.add(dataItem);
                    Log.d(TAG, "doInBackground: Sizeee   " + dataArray.size());
                }
                jsonReader.endArray();
                reader.close();

                return dataArray;
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Error: " + e.getLocalizedMessage());
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Cities> dataArray) {
            super.onPostExecute(dataArray);
            list = new ArrayList<>(dataArray);
            Constants.dismissDialog();
           // Stash.put(Constants.CITIES, list);
            Log.d(TAG, "onPostExecute: LIST SIZE  " + list.size());
            adapter = new CitiesAdapter(AroundPlaceActivity.this, list, cityClick);
            binding.cities.setAdapter(adapter);
        }
    }

    CityClick cityClick = cities -> {
        Intent intent = new Intent(AroundPlaceActivity.this, AddPlaceActivity.class);
        intent.putExtra("GIVEN", true);
        String COORDINATES = cities.latitude + ", " + cities.longitude;
        String PLACE = cities.name + ", " + cities.country_name;
        intent.putExtra(Constants.COORDINATES, COORDINATES);
        intent.putExtra(Constants.PLACE, PLACE);
        startActivity(intent);
        finish();
    };

}