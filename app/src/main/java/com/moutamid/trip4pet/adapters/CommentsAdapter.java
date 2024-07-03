package com.moutamid.trip4pet.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.models.Cities;
import com.moutamid.trip4pet.models.CommentModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentVH> {
    Context context;
    ArrayList<CommentModel> list;
    private static final String TAG = "CommentsAdapter";

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
        holder.date.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(commentModel.timestamp));
        holder.translate.setOnClickListener(v -> {
            if (holder.translate.getText().toString().equals(context.getString(R.string.translate))) {
                TranslateAPI type = new TranslateAPI(Language.AUTO_DETECT, Stash.getString(Constants.LANGUAGE, "en"), commentModel.message);
                type.setTranslateListener(new TranslateAPI.TranslateListener() {
                    @Override
                    public void onSuccess(String translatedText) {
                        Log.d(TAG, "onSuccess: " + translatedText);
                        holder.comment.setText(translatedText);
                        holder.translate.setText(R.string.original);
                    }

                    @Override
                    public void onFailure(String ErrorText) {
                        Log.d(TAG, "onFailure: " + ErrorText);
                    }
                });
            } else {
                holder.comment.setText(commentModel.message);
                holder.translate.setText(R.string.translate);
            }
        });

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
        TextView name, comment, date;
        ImageView star1, star2, star3, star4, star5;
        MaterialButton translate;
        public CommentVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            translate = itemView.findViewById(R.id.translate);
            comment = itemView.findViewById(R.id.comment);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            star4 = itemView.findViewById(R.id.star4);
            star5 = itemView.findViewById(R.id.star5);
        }
    }

}
