package com.moutamid.trip4pet.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.adapters.CommentsAdapter;
import com.moutamid.trip4pet.adapters.SliderAdapter;
import com.moutamid.trip4pet.databinding.ActivityDetailBinding;
import com.moutamid.trip4pet.models.CommentModel;
import com.moutamid.trip4pet.models.FilterModel;
import com.moutamid.trip4pet.models.LocationsModel;
import com.moutamid.trip4pet.models.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    LocationsModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = (LocationsModel) Stash.getObject(Constants.MODEL, LocationsModel.class);

        binding.back.setOnClickListener(v -> onBackPressed());

        binding.imageSlider.setSliderAdapter(new SliderAdapter(this, model.images));

        if (model.activities != null) {
            for (FilterModel s : model.activities) {
                LayoutInflater inflater = getLayoutInflater();
                View customEditTextLayout = inflater.inflate(R.layout.icon, null);
                ImageView image = customEditTextLayout.findViewById(R.id.image);
                image.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                image.setImageResource(s.icon);
                binding.activitiesIcon.addView(customEditTextLayout);
            }
        }

        binding.contacts.setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + model.contact));
            startActivity(dialIntent);
        });

        binding.directions.setOnClickListener(v -> {
            String destinationLatitude = String.valueOf(model.latitude);
            String destinationLongitude = String.valueOf(model.longitude);
            Uri mapUri = Uri.parse("http://maps.google.com/maps?daddr=" + destinationLatitude + "," + destinationLongitude);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
            // mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        binding.favorites.setOnClickListener(v -> {

        });
        int activitySize = model.activities == null ? 0 : model.activities.size();
        int servicesSize = model.services == null ? 0 : model.services.size();

        binding.totalActivity.setText(activitySize + " " + getString(R.string.activities));
        binding.totalServices.setText(servicesSize + " " + getString(R.string.services));
        binding.name.setText(model.name);
        binding.typeOfPlace.setText(model.typeOfPlace);
        binding.desc.setText(model.description);
        binding.rating.setText(model.rating + "");
        String cord = model.latitude + ", " + model.longitude + " (lat, long)";
        binding.coordinates.setText(cord);
        String address = model.name + ", " + model.city + ", " + model.country;
        binding.location.setText(address);

        if (model.services != null) {
            for (FilterModel s : model.services) {
                LayoutInflater inflater = getLayoutInflater();
                View customEditTextLayout = inflater.inflate(R.layout.icon, null);
                ImageView image = customEditTextLayout.findViewById(R.id.image);
                CardView card = customEditTextLayout.findViewById(R.id.card);
                card.setCardBackgroundColor(getResources().getColor(R.color.green_card));
                //  image.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                image.setImageResource(s.icon);
                binding.servicesIcon.addView(customEditTextLayout);
            }
        }

//        if (model.services == null) {
//            binding.services.setVisibility(View.GONE);
//        }
//        if (model.activities == null) {
//            binding.activities.setVisibility(View.GONE);
//        }

        updateRecycler();

        binding.more.setOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = inflater.inflate(R.layout.detail_menu, null);
            PopupWindow popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.showAsDropDown(v);
            MaterialButton review = customView.findViewById(R.id.review);
            MaterialButton imHere = customView.findViewById(R.id.imHere);
            review.setOnClickListener(v1 -> {
                popupWindow.dismiss();
                showReviewDialog();
            });
            imHere.setOnClickListener(v1 -> {
                popupWindow.dismiss();
                Constants.showDialog();
                Map<String, Object> map = new HashMap<>();
                map.put("visited", model.id);
                Constants.databaseReference().child(Constants.VISITED).child(Constants.auth().getCurrentUser().getUid()).child(model.id)
                        .updateChildren(map).addOnSuccessListener(unused -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, "Added to your visited place", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            Constants.dismissDialog();
                        });
            });
        });

    }

    private void showReviewDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_review);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.show();

        MaterialButton submit = dialog.findViewById(R.id.confirm);
        TextInputLayout comment = dialog.findViewById(R.id.comment);
        SimpleRatingBar rating = dialog.findViewById(R.id.rating);

        submit.setOnClickListener(v -> {
            UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
            CommentModel commentModel = new CommentModel();
            commentModel.LocationId = model.id;
            commentModel.message = comment.getEditText().getText().toString();
            commentModel.rating = rating.getRating();
            commentModel.userID = Constants.auth().getCurrentUser().getUid();
            commentModel.userName = userModel.name;
            Map<String, Object> map = new HashMap<>();
            ArrayList<CommentModel> list = model.comments == null ? new ArrayList<>() : model.comments;
            list.add(commentModel);
            model.comments = new ArrayList<>(list);
            map.put("comments", list);
            Constants.showDialog();
            Constants.databaseReference().child(Constants.PLACE).child(model.userID).child(model.id).updateChildren(map).addOnSuccessListener(unused -> {
                dialog.dismiss();
                updateRecycler();
                Toast.makeText(this, "Comment Added", Toast.LENGTH_SHORT).show();
                Constants.dismissDialog();
            }).addOnFailureListener(e -> {
                dialog.dismiss();
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Constants.dismissDialog();
            });
        });

    }

    private void updateRecycler() {
        if (model.comments != null) {

            float rating = 0;
            for (CommentModel commentModel : model.comments){
                rating += commentModel.rating;
            }
            float total = rating/5;
            binding.rating.setText(String.valueOf(total));

            binding.noComments.setVisibility(View.GONE);
            binding.commentsRC.setVisibility(View.VISIBLE);
        }
        binding.commentsRC.setLayoutManager(new LinearLayoutManager(this));
        binding.commentsRC.setHasFixedSize(false);
        CommentsAdapter adapter = new CommentsAdapter(this, model.comments);
        binding.commentsRC.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
    }
}