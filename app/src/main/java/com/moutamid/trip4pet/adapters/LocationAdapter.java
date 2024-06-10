package com.moutamid.trip4pet.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.activities.DetailActivity;
import com.moutamid.trip4pet.models.CommentModel;
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
        return new LocationVH(LayoutInflater.from(context).inflate(R.layout.list_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LocationVH holder, int position) {
        LocationsModel model = list.get(holder.getAdapterPosition());
        holder.name.setText(model.name);
        holder.desc.setText(model.description);
        if (model.comments!=null){
            float rating = 0;
            for (CommentModel commentModel : model.comments){
                rating += commentModel.rating;
            }
            float total = rating/5;
            holder.rating.setText(String.valueOf(total));
        } else {
            holder.rating.setText("0.0");
        }
        if (model.images != null) {
            holder.imagesCount.setText(model.images.size() + "");
            holder.typeOfPlace.setText(model.typeOfPlace);
            Glide.with(context).load(model.images.get(0)).into(holder.image);
        } else {
            holder.imagesCount.setText("0");
            Glide.with(context).load(R.drawable.trip4pet_logo).into(holder.image);
        }
        holder.itemView.setOnClickListener(v -> {
            Stash.put(Constants.MODEL, model);
            context.startActivity(new Intent(context, DetailActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LocationVH extends RecyclerView.ViewHolder{
        ImageView image;
        TextView imagesCount, rating, typeOfPlace, name, desc;
        public LocationVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imagesCount = itemView.findViewById(R.id.imagesCount);
            rating = itemView.findViewById(R.id.rating);
            typeOfPlace = itemView.findViewById(R.id.typeOfPlace);
            name = itemView.findViewById(R.id.name);
            desc = itemView.findViewById(R.id.desc);
        }
    }

}
