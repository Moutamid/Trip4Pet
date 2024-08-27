package com.moutamid.trip4pet.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import com.moutamid.trip4pet.Stash;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;
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
import java.util.stream.Collectors;

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
            com.moutamid.trip4pet.JsonReader.readPlacesFromAssetInBackground(this, placeList -> {
                // This code runs on the main thread after data is loaded
                runOnUiThread(() -> {
                    // Use the `placeList` here
                    Constants.dismissDialog();
                    Log.d(TAG, "onResume: " + placeList.get(0).toString());
                    list = new ArrayList<>(placeList);
                    adapter = new CitiesAdapter(AroundPlaceActivity.this, list, cityClick);
                    binding.cities.setAdapter(adapter);
                });
            });
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
            private Handler handler = new Handler();
            private Runnable runnable;
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
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }

                runnable = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "afterTextChanged: ");
                        if (s.toString().isEmpty()) {
                            binding.gps.setVisibility(View.VISIBLE);
                            binding.cities.setVisibility(View.GONE);
                        } else {
                            if (s.toString().length() > 3) {
                             filter(s.toString());
                            }
                            new Handler().postDelayed(() -> {
                                binding.gps.setChecked(false);
                                binding.gps.setVisibility(View.GONE);
                                binding.cities.setVisibility(View.VISIBLE);
                            }, 2000);
                        }
                    }
                };

                // Post the runnable with a delay (e.g., 500 milliseconds)
                handler.postDelayed(runnable, 500);
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    public void filter(final String query) {
        Log.d(TAG, "filter: " + query);
        Log.d(TAG, "filter: " + list.size());
        Constants.showDialog();
        // Stash.getString(Constants.LANGUAGE, "en")
        TranslateAPI type = new TranslateAPI(Stash.getString(Constants.LANGUAGE, "en"), Language.ENGLISH, query);
        type.setTranslateListener(new TranslateAPI.TranslateListener() {
            @Override
            public void onSuccess(String translatedText) {
                Constants.dismissDialog();
                Log.d(TAG, "onSuccess: " + translatedText);
                String[] split = translatedText.replace(",", "").split(" ");
                new AsyncTask<Void, Void, ArrayList<Cities>>() {
                    @Override
                    protected ArrayList<Cities> doInBackground(Void... voids) {
                        ArrayList<Cities> mainList = new ArrayList<>(list);
                        ArrayList<Cities> filterList;
                        if (split.length > 1) {
                            Log.d(TAG, "doInBackground: length 2 " + split[0] + " " + split[1]);
                            filterList = (ArrayList<Cities>) mainList.stream()
                                    .filter(item -> item.getCountry_name().toLowerCase().equals(split[1].toString().toLowerCase()))
                                    .collect(Collectors.toList());

                            Log.d(TAG, "doInBackground: Country " + filterList.size());

                            filterList = (ArrayList<Cities>) filterList.stream()
                                    .filter(item -> item.getName().toLowerCase().equals(split[0].toString().toLowerCase()))
                                    .collect(Collectors.toList());
                        } else {
                            Log.d(TAG, "doInBackground: length 1 " + split[0] + " " + split[0]);
                            filterList = (ArrayList<Cities>) mainList.stream()
                                    .filter(item -> item.getName().toLowerCase().equals(split[0].toString().toLowerCase()) ||
                                            item.getCountry_name().toLowerCase().equals(split[0].toString().toLowerCase())
                                    )
                                    .collect(Collectors.toList());
                        }
                        Log.d(TAG, "doInBackground: " + filterList.size());
                        return filterList;
                    }

                    @Override
                    protected void onPostExecute(ArrayList<Cities> filterList) {
                        Constants.dismissDialog();
                        if (filterList.isEmpty()) {
                            adapter = new CitiesAdapter(AroundPlaceActivity.this, filterList, cityClick);
                            binding.cities.setAdapter(adapter);
                        }
                        ArrayList<Cities> temp = new ArrayList<>();
                        ArrayList<Cities> filter = new ArrayList<>(filterList.subList(0, Math.min(20, filterList.size())));
                        for (Cities city : filter) {
                            Log.d(TAG, "onPostExecute: " + city.getName());
                            TranslateAPI type = new TranslateAPI(Language.ENGLISH, Stash.getString(Constants.LANGUAGE, "en"), city.getName());
                            type.setTranslateListener(new TranslateAPI.TranslateListener() {
                                @Override
                                public void onSuccess(String translatedText) {
                                    city.setName(translatedText);
                                    temp.add(city);
                                    adapter = new CitiesAdapter(AroundPlaceActivity.this, temp, cityClick);
                                    binding.cities.setAdapter(adapter);
                                }

                                @Override
                                public void onFailure(String ErrorText) {
                                    Constants.dismissDialog();
                                    Log.d(TAG, "onFailure: " + ErrorText);
                                }
                            });
                        }
                    }
                }.execute();
            }

            @Override
            public void onFailure(String ErrorText) {
                Constants.dismissDialog();
                Log.d(TAG, "onFailure: " + ErrorText);
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
        String COORDINATES = cities.getLatitude() + ", " + cities.getLongitude();
        String PLACE = cities.getName() + ", " + cities.getCountry_name();
        intent.putExtra(Constants.COORDINATES, COORDINATES);
        intent.putExtra(Constants.PLACE, PLACE);
        startActivity(intent);
        finish();
    };

}