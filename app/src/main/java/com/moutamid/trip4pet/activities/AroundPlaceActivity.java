package com.moutamid.trip4pet.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.MainActivity;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.Stash;
import com.moutamid.trip4pet.adapters.CitiesAdapter;
import com.moutamid.trip4pet.api.ApiLink;
import com.moutamid.trip4pet.api.VolleySingleton;
import com.moutamid.trip4pet.databinding.ActivityAroundPlaceBinding;
import com.moutamid.trip4pet.listener.CityClick;
import com.moutamid.trip4pet.models.Cities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class AroundPlaceActivity extends AppCompatActivity {
    ActivityAroundPlaceBinding binding;
    ArrayList<Cities> list = new ArrayList<>();
    private static final String TAG = "AroundPlaceActivity";
    CitiesAdapter adapter;

    RequestQueue requestQueue;

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
//        if (list.isEmpty()) {
//            Constants.showDialog();
//            com.moutamid.trip4pet.JsonReader.readPlacesFromAssetInBackground(this, placeList -> {
//                // This code runs on the main thread after data is loaded
//                runOnUiThread(() -> {
//                    // Use the `placeList` here
//                    Constants.dismissDialog();
//                    Log.d(TAG, "onResume: " + placeList.get(0).toString());
//                    list = new ArrayList<>(placeList);
//                    adapter = new CitiesAdapter(AroundPlaceActivity.this, list, cityClick);
//                    binding.cities.setAdapter(adapter);
//                });
//            });
//        }
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

        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

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
                            adapter = new CitiesAdapter(AroundPlaceActivity.this, new ArrayList<>(), cityClick);
                            binding.cities.setAdapter(adapter);
                            if (s.toString().length() > 3) {
                                String name = s.toString().substring(0, 1).toUpperCase(Locale.ROOT) + s.toString().substring(1);
                                Log.d(TAG, "run: " + name);
                                requestQueue.cancelAll(ApiLink.GET_PLACES);
                                filter(name);
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
        String url = ApiLink.searchCities(query);
        Log.d(TAG, "filter: " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Constants.dismissDialog();
                    try {
                        Log.d(TAG, "API RESPONSE");
                        ArrayList<Cities> mainList = new ArrayList<>();
                        JSONArray predictions = response.getJSONArray("predictions");
                        for (int i = 0; i < predictions.length(); i++) {
                            JSONObject city = predictions.getJSONObject(i);
                            Cities cities = new Cities();
                            cities.setId(city.getString("place_id"));
                            cities.setName(city.getString("description"));
                            mainList.add(cities);
                        }
                        Log.d(TAG, "filter: size " + mainList.size());
                        adapter = new CitiesAdapter(AroundPlaceActivity.this, mainList, cityClick);
                        binding.cities.setAdapter(adapter);
                    } catch (JSONException e) {
                        Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "filter: " + error.getLocalizedMessage());
                }
        );
        jsonObjectRequest.setTag(ApiLink.GET_PLACES);
        requestQueue.add(jsonObjectRequest);
    }

    CityClick cityClick = this::getCityDetails;

    private void getCityDetails(Cities cities) {
        Constants.showDialog();
        String url = ApiLink.getPlace(cities.getId());
        Log.d(TAG, "getCityDetails: " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Constants.dismissDialog();
                    try {
                        Log.d(TAG, "API RESPONSE");
                        JSONObject result = response.getJSONObject("result");
                        JSONObject geometry = result.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        String[] add = cities.getName().split(", ");
                        cities.setName(add[0]);
                        if (add.length == 3) {
                            cities.setState_name(add[1]);
                            cities.setCountry_name(add[2]);
                        } else if (add.length == 2) {
                            cities.setCountry_name(add[1]);
                        }
                        cities.setLatitude(location.getDouble("lat"));
                        cities.setLongitude(location.getDouble("lng"));

                        Log.d(TAG, "getCityDetails: Cities " + cities);

                        Intent intent = new Intent(AroundPlaceActivity.this, AddPlaceActivity.class);
                        intent.putExtra("GIVEN", true);
                        String COORDINATES = cities.getLatitude() + ", " + cities.getLongitude();
                        String PLACE = cities.getName() + ", " + cities.getCountry_name();
                        intent.putExtra(Constants.COORDINATES, COORDINATES);
                        intent.putExtra(Constants.PLACE, PLACE);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "filter: " + error.getLocalizedMessage());
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

}