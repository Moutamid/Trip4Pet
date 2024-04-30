package com.moutamid.trip4pet.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.trip4pet.models.LocationsModel;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationVH> {
    Context context;
    ArrayList<LocationsModel> list;

    public LocationAdapter(Context context, ArrayList<LocationsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LocationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LocationVH extends RecyclerView.ViewHolder{

        public LocationVH(@NonNull View itemView) {
            super(itemView);
        }
    }

}
