package com.moutamid.trip4pet.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.listener.ImageListener;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    Context context;
    ArrayList<Uri> list;
    ImageListener imageListener;
    private final int limit = 6;

    public ImageAdapter(Context context, ArrayList<Uri> list, ImageListener imageListener) {
        this.context = context;
        this.list = list;
        this.imageListener = imageListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.image_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Glide.with(context).load(list.get(holder.getAdapterPosition())).into(holder.imageView);
        holder.itemView.setOnClickListener(v -> imageListener.onClick(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return Math.min(list.size(), limit);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }

    }

}
