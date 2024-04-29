package com.moutamid.trip4pet;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.trip4pet.activities.AddPlaceActivity;

import java.util.ArrayList;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CitiesVH> {
    Activity context;
    ArrayList<Cities> list;

    public CitiesAdapter(Activity context, ArrayList<Cities> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CitiesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CitiesVH(LayoutInflater.from(context).inflate(R.layout.cities, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesVH holder, int position) {
        Cities cities = list.get(holder.getAdapterPosition());
        String name = cities.name + ", " + cities.state + ", " + cities.country;
        holder.name.setText(name);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddPlaceActivity.class);
            intent.putExtra("GIVEN", true);
            String COORDINATES = cities.latitude + ", " + cities.longitude;
            intent.putExtra("COORDINATES", COORDINATES);
            context.startActivity(intent);
            context.finish();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CitiesVH extends RecyclerView.ViewHolder {
        TextView name;

        public CitiesVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }

}
