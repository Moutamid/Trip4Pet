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

import com.moutamid.trip4pet.Stash;
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
        this.currentItems = new ArrayList<>(list.subList(0, Math.min(20, list.size())));
    }

    @NonNull
    @Override
    public CitiesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CitiesVH(LayoutInflater.from(context).inflate(R.layout.cities, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesVH holder, int position) {
        Cities cities = currentItems.get(holder.getAdapterPosition());
        holder.name.setText(cities.getName());
        holder.itemView.setOnClickListener(v -> cityClick.onClick(cities));
    }

    @Override
    public int getItemCount() {
        return currentItems.size();
    }

    public class CitiesVH extends RecyclerView.ViewHolder {
        TextView name;

        public CitiesVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }

}
