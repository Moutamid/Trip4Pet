package com.moutamid.trip4pet;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moutamid.trip4pet.models.Cities;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JsonReader {
    private static final String TAG = "JsonReader";

    public interface OnDataLoadedListener {
        void onDataLoaded(ArrayList<Cities> placeList);
    }

    public static void readPlacesFromAssetInBackground(Context context, OnDataLoadedListener listener) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            ArrayList<Cities> placeList = new ArrayList<>();
            try {
                InputStream is = context.getAssets().open("cities.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String json = new String(buffer, "UTF-8");
//                Log.d(TAG, "readPlacesFromAssetInBackground: " + json);
//                JSONArray jsonArray = new JSONArray(json);
//                Log.d(TAG, "readPlacesFromAssetInBackground: " + jsonArray.length());
//                JSONObject jsonObject = jsonArray.getJSONObject(0);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    ModelClass model = new ModelClass();
//                    placeList.add(model);
//                }
//                Log.d(TAG, "readPlacesFromAssetInBackground: " + jsonObject.toString());

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Cities>>() {
                }.getType();
                placeList = gson.fromJson(json, listType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            listener.onDataLoaded(placeList);
        });
    }
}
