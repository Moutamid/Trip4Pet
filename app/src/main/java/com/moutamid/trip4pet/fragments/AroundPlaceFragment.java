package com.moutamid.trip4pet.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.MainActivity;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.Stash;
import com.moutamid.trip4pet.adapters.CitiesAdapter;
import com.moutamid.trip4pet.api.ApiLink;
import com.moutamid.trip4pet.api.VolleySingleton;
import com.moutamid.trip4pet.databinding.FragmentAroundPlaceBinding;
import com.moutamid.trip4pet.listener.CityClick;
import com.moutamid.trip4pet.models.Cities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class AroundPlaceFragment extends Fragment {
    FragmentAroundPlaceBinding binding;
    CitiesAdapter adapter;
    private static final String TAG = "AroundPlaceFragment";
    ArrayList<Cities> list = new ArrayList<>();

    public AroundPlaceFragment() {
        // Required empty public constructor
    }

    RequestQueue requestQueue;

    @Override
    public void onResume() {
        super.onResume();
        Stash.clear(Constants.CITIES);
        Constants.initDialog(requireContext());
//        if (isAdded() && getContext() != null) {
//            Constants.setLocale(getContext(), Stash.getString(Constants.LANGUAGE, "en"));
//            Constants.initDialog(getContext());
//
////        list = Stash.getArrayList(Constants.CITIES, Cities.class);
//            if (list.isEmpty()) {
//                if (isAdded() && getActivity() != null) {
//                    Constants.showDialog();
//                    // new MyTask(getActivity(), getContext()).execute();
//                    com.moutamid.trip4pet.JsonReader.readPlacesFromAssetInBackground(requireContext(), placeList -> {
//                        // This code runs on the main thread after data is loaded
//                        requireActivity().runOnUiThread(() -> {
//                            // Use the `placeList` here
//                            Constants.dismissDialog();
//                            Log.d(TAG, "onResume: " + placeList.get(0).toString());
//                            list = new ArrayList<>(placeList);
//                            adapter = new CitiesAdapter(requireActivity(), list, cityClick);
//                            binding.cities.setAdapter(adapter);
//                        });
//                    });
//                }
//            }
////            else {
////                adapter = new CitiesAdapter(requireActivity(), list, cityClick);
////                binding.cities.setAdapter(adapter);
////            }
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAroundPlaceBinding.inflate(getLayoutInflater(), container, false);

        binding.cities.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.cities.setHasFixedSize(false);

        requestQueue = VolleySingleton.getInstance(requireContext()).getRequestQueue();

        binding.gps.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int v = isChecked ? View.VISIBLE : View.GONE;
            binding.gpsLayout.setVisibility(v);
        });

        binding.confirm.setOnClickListener(v -> {
            if (!binding.latitude.getEditText().getText().toString().isEmpty() && !binding.longitude.getEditText().getText().toString().isEmpty()) {
                Cities cities = new Cities();
                cities.setLatitude(Double.parseDouble(binding.latitude.getEditText().getText().toString()));
                cities.setLongitude(Double.parseDouble(binding.longitude.getEditText().getText().toString()));
                Stash.put(Constants.AROUND_PLACE, cities);
                startActivity(new Intent(requireContext(), MainActivity.class));
                requireActivity().finish();
            } else {
                Toast.makeText(requireContext(), getString(R.string.choose_location_first), Toast.LENGTH_SHORT).show();
            }
        });

        binding.search.getEditText().addTextChangedListener(new TextWatcher() {
            private Handler handler = new Handler();
            private Runnable runnable;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: ");
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
                            adapter = new CitiesAdapter(requireActivity(), new ArrayList<>(), cityClick);
                            binding.cities.setAdapter(adapter);
                            if (s.toString().length() >= 3) {
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

        return binding.getRoot();
    }

    @SuppressLint("StaticFieldLeak")
    public void filter(final String query) {
        Log.d(TAG, "filter: " + query);
        Log.d(TAG, "filter: " + list.size());
        Constants.showDialog();
        // Stash.getString(Constants.LANGUAGE, "en")
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
                            mainList.add(new Cities(city.getString("place_id"), city.getString("description")));
                        }
                        Log.d(TAG, "filter: size " + mainList.size());
                        adapter = new CitiesAdapter(requireActivity(), mainList, cityClick);
                        binding.cities.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
            Constants.dismissDialog();
                    Log.d(TAG, "filter: " + error.getLocalizedMessage());
                }
        );
        jsonObjectRequest.setTag(ApiLink.GET_PLACES);
        requestQueue.add(jsonObjectRequest);
    }

    CityClick cityClick = cities -> {
        View view = getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        getCityDetails(cities);
    };

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

                        Log.d(TAG, "getCityDetails: Cities " + cities.toString());

                        Stash.put(Constants.AROUND_PLACE, cities);
                        startActivity(new Intent(requireContext(), MainActivity.class));
                        requireActivity().finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Constants.dismissDialog();
                    Log.d(TAG, "filter: " + error.getLocalizedMessage());
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

}