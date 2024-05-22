package com.moutamid.trip4pet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
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
        holder.ratingBar.setRating(commentModel.rating);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CommentVH extends RecyclerView.ViewHolder {
        TextView name, comment;
        SimpleRatingBar ratingBar;
        public CommentVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            comment = itemView.findViewById(R.id.comment);
            ratingBar = itemView.findViewById(R.id.rating);
        }
    }

}
