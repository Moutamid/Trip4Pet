package com.moutamid.trip4pet.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.listener.CityClick;
import com.moutamid.trip4pet.models.Cities;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.activities.AddPlaceActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CitiesVH> {
    Activity context;
    ArrayList<Cities> list;
    ArrayList<Cities> currentItems;
    CityClick cityClick;
    private static final String TAG = "CitiesAdapter";

    public CitiesAdapter(Activity context, ArrayList<Cities> list, CityClick cityClick) {
        this.context = context;
        this.list = list;
        this.cityClick = cityClick;
        this.currentItems = new ArrayList<>(list.subList(0, 20));
    }

    @NonNull
    @Override
    public CitiesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CitiesVH(LayoutInflater.from(context).inflate(R.layout.cities, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesVH holder, int position) {
        Cities cities = currentItems.get(holder.getAdapterPosition());
        final String[] name = {cities.name + ", " + cities.state_name + ", " + cities.country_name};
//        TranslateAPI type = new TranslateAPI(Language.ENGLISH, Stash.getString(Constants.LANGUAGE, "en"), name[0]);
//        type.setTranslateListener(new TranslateAPI.TranslateListener() {
//            @Override
//            public void onSuccess(String translatedText) {
//                Log.d(TAG, "onSuccess: " + translatedText);
//                name[0] = translatedText;
//                holder.name.setText(translatedText);
//            }
//
//            @Override
//            public void onFailure(String ErrorText) {
//                Log.d(TAG, "onFailure: " + ErrorText);
//            }
//        });
        holder.name.setText(name[0]);
        holder.itemView.setOnClickListener(v -> cityClick.onClick(cities));
      //  holder.itemView.setOnClickListener(v -> Log.d(TAG, "onBindViewHolder: " + cities.name + ", " + cities.state_name + ", " + cities.country_name));
    }

    @Override
    public int getItemCount() {
        return currentItems.size();
    }

    @SuppressLint("StaticFieldLeak")
    public void filter(final String query) {
        Log.d(TAG, "filter: " + query);
        Log.d(TAG, "filter: " + list.size());
         Constants.showDialog();
        // Stash.getString(Constants.LANGUAGE, "en")
        TranslateAPI type = new TranslateAPI(Language.AUTO_DETECT, Language.ENGLISH, query);
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
                                    .filter(item -> item.country_name.toLowerCase().equals(split[1].toString().toLowerCase()))
                                    .collect(Collectors.toList());

                            Log.d(TAG, "doInBackground: Country " + filterList.size());

                            filterList = (ArrayList<Cities>) filterList.stream()
                                    .filter(item -> item.name.toLowerCase().equals(split[0].toString().toLowerCase()))
                                    .collect(Collectors.toList());
                        } else {
                            Log.d(TAG, "doInBackground: length 1 " + split[0] + " " + split[0]);
                            filterList = (ArrayList<Cities>) mainList.stream()
                                    .filter(item -> item.name.toLowerCase().equals(split[0].toString().toLowerCase()) ||
                                            item.country_name.toLowerCase().equals(split[0].toString().toLowerCase())
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
                            currentItems.clear();
                            notifyDataSetChanged();
                        }
                        ArrayList<Cities> temp = new ArrayList<>();
                        for (Cities city : filterList) {
                            TranslateAPI type = new TranslateAPI(Language.ENGLISH, Stash.getString(Constants.LANGUAGE, "en"), city.name);
                            type.setTranslateListener(new TranslateAPI.TranslateListener() {
                                @Override
                                public void onSuccess(String translatedText) {
                                    city.name = translatedText;
                                    temp.add(city);
                                    currentItems = new ArrayList<>(temp.subList(0, Math.min(20, temp.size())));
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(String ErrorText) {
                                    Constants.dismissDialog();
                                    Log.d(TAG, "onFailure: " + ErrorText);
                                }
                            });
                        }
//                        new Handler().postDelayed(() -> {
//                            currentItems = new ArrayList<>(filterList.subList(0, Math.min(20, filterList.size())));
//                            notifyDataSetChanged();
//                        }, 1500);
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

    public class CitiesVH extends RecyclerView.ViewHolder {
        TextView name;

        public CitiesVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }

}
