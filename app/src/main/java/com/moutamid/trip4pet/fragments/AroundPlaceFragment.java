package com.moutamid.trip4pet.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.MainActivity;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.activities.AroundPlaceActivity;
import com.moutamid.trip4pet.adapters.CitiesAdapter;
import com.moutamid.trip4pet.databinding.FragmentAroundPlaceBinding;
import com.moutamid.trip4pet.listener.CityClick;
import com.moutamid.trip4pet.models.Cities;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class AroundPlaceFragment extends Fragment {
    FragmentAroundPlaceBinding binding;
    CitiesAdapter adapter;
    private static final String TAG = "AroundPlaceFragment";
    ArrayList<Cities> list = new ArrayList<>();
    public AroundPlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        Constants.setLocale(requireContext(), Stash.getString(Constants.LANGUAGE, "en"));
        Constants.initDialog(requireContext());
        Constants.showDialog();
        list.clear();
        MyTask task = new MyTask();
        task.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAroundPlaceBinding.inflate(getLayoutInflater(), container, false);

        binding.cities.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.cities.setHasFixedSize(false);

        binding.gps.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int v = isChecked ? View.VISIBLE : View.GONE;
            binding.gpsLayout.setVisibility(v);
        });

        binding.confirm.setOnClickListener(v -> {
            Cities cities = new Cities();
            cities.latitude = Double.parseDouble(binding.latitude.getEditText().getText().toString());
            cities.longitude = Double.parseDouble(binding.longitude.getEditText().getText().toString());
            Stash.put(Constants.AROUND_PLACE, cities);
            startActivity(new Intent(requireContext(), MainActivity.class));
            requireActivity().finish();
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

        return binding.getRoot();
    }

    public class MyTask extends AsyncTask<Void, Void, ArrayList<Cities>> {

        @Override
        protected ArrayList<Cities> doInBackground(Void... voids) {
            try {
                Log.d(TAG, "doInBackground: Reading json");
                AssetManager assetManager = requireContext().getAssets();
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
            adapter = new CitiesAdapter(requireActivity(), list, cityClick);
            binding.cities.setAdapter(adapter);
        }
    }

    CityClick cityClick = cities -> {
        View view = getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        Stash.put(Constants.AROUND_PLACE, cities);
        startActivity(new Intent(requireContext(), MainActivity.class));
        requireActivity().finish();
    };

}