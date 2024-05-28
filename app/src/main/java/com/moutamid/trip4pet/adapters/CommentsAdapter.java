package com.moutamid.trip4pet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.models.CommentModel;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentVH> {
    Context context;
    ArrayList<CommentModel> list;

    public CommentsAdapter(Context context, ArrayList<CommentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CommentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentVH(LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentVH holder, int position) {
        CommentModel commentModel = list.get(holder.getAdapterPosition());
        holder.name.setText(commentModel.userName);
        holder.comment.setText(commentModel.message);
        if (commentModel.rating == 1) {
            holder.star1.setImageResource(R.drawable.star_solid);
        } else if (commentModel.rating == 2) {
            holder.star1.setImageResource(R.drawable.star_solid);
            holder.star2.setImageResource(R.drawable.star_solid);
        } else if (commentModel.rating == 3) {
            holder.star1.setImageResource(R.drawable.star_solid);
            holder.star2.setImageResource(R.drawable.star_solid);
            holder.star3.setImageResource(R.drawable.star_solid);
        } else if (commentModel.rating == 4) {
            holder.star1.setImageResource(R.drawable.star_solid);
            holder.star2.setImageResource(R.drawable.star_solid);
            holder.star3.setImageResource(R.drawable.star_solid);
            holder.star4.setImageResource(R.drawable.star_solid);
        } else if (commentModel.rating == 5) {
            holder.star1.setImageResource(R.drawable.star_solid);
            holder.star2.setImageResource(R.drawable.star_solid);
            holder.star3.setImageResource(R.drawable.star_solid);
            holder.star4.setImageResource(R.drawable.star_solid);
            holder.star5.setImageResource(R.drawable.star_solid);
        } else if (commentModel.rating == 0) {
            holder.star1.setImageResource(R.drawable.star_regular);
            holder.star2.setImageResource(R.drawable.star_regular);
            holder.star3.setImageResource(R.drawable.star_regular);
            holder.star4.setImageResource(R.drawable.star_regular);
            holder.star5.setImageResource(R.drawable.star_regular);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CommentVH extends RecyclerView.ViewHolder {
        TextView name, comment;
        ImageView star1, star2, star3, star4, star5;

        public CommentVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            comment = itemView.findViewById(R.id.comment);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            star4 = itemView.findViewById(R.id.star4);
            star5 = itemView.findViewById(R.id.star5);
        }
    }

}
