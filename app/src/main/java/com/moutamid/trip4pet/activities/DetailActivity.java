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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    LocationsModel model;
    boolean isFavorite = false;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.setLocale(getBaseContext(), Stash.getString(Constants.LANGUAGE, "en"));

        binding.back.setOnClickListener(v -> onBackPressed());

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
            showFavoriteDialog();
        });

        binding.more.setOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = inflater.inflate(R.layout.detail_menu, null);
            PopupWindow popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.showAsDropDown(v);
            MaterialButton review = customView.findViewById(R.id.review);
            MaterialButton imHere = customView.findViewById(R.id.imHere);
            MaterialButton edit = customView.findViewById(R.id.edit);
            MaterialButton addPhoto = customView.findViewById(R.id.addPhoto);

            review.setOnClickListener(v1 -> {
                popupWindow.dismiss();
                showReviewDialog();
            });

            edit.setOnClickListener(v1 -> {
                popupWindow.dismiss();
                Stash.put(Constants.EDIT, model);
                startActivity(new Intent(this, EditPlaceActivity.class));
            });

            addPhoto.setOnClickListener(v1 -> {
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
                            Toast.makeText(this, R.string.added_to_your_visited_place, Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            Constants.dismissDialog();
                        });
            });
        });
    }


    private void showFavoriteDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_folder);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.show();

        MaterialCardView myFavorites = dialog.findViewById(R.id.myFavorites);
        MaterialCardView unlock = dialog.findViewById(R.id.unlock);
        TextView favoriteSize = dialog.findViewById(R.id.size);
        LinearLayout buttons = dialog.findViewById(R.id.buttons);

        ArrayList<LocationsModel> favorite = Stash.getArrayList(Constants.FAVORITE, LocationsModel.class);
        favoriteSize.setText(favorite.size() + "/" + Constants.FAVORITE_SIZE);
        myFavorites.setOnClickListener(v -> {
            dialog.dismiss();
            if (!isFavorite) {
                if (favorite.size() < Constants.FAVORITE_SIZE) {
                    favorite.add(model);
                    Stash.put(Constants.FAVORITE, favorite);
                    Toast.makeText(this, R.string.added_to_favorite, Toast.LENGTH_SHORT).show();
                    isFavorite = true;
                    index = favorite.size() - 1;
                }
            } else {
                favorite.remove(index);
                Stash.put(Constants.FAVORITE, favorite);
                Toast.makeText(this, R.string.removed_from_favorites, Toast.LENGTH_SHORT).show();
                isFavorite = false;
                index = 0;
            }
        });

        buttons.removeAllViews();
        if (Stash.getBoolean(Constants.ISVIP)) {
            unlock.setVisibility(View.GONE);
            ArrayList<String> folders = Stash.getArrayList(Constants.FAVORITE_FOLDER, String.class);
            for (String folder : folders) {
                LayoutInflater inf = getLayoutInflater();
                View customLayout = inf.inflate(R.layout.favorite_folders, null);
                TextView name = customLayout.findViewById(R.id.name);
                TextView size = customLayout.findViewById(R.id.size);
                MaterialCardView favorites = customLayout.findViewById(R.id.myFavorites);
                name.setText(folder);
                ArrayList<LocationsModel> locations = Stash.getArrayList(folder.replace(" ", "_"), LocationsModel.class);
                size.setText("Total: " + locations.size());
                favorites.setOnClickListener(v -> {
                    dialog.dismiss();
                    int id = 0;
                    boolean isFvrt = false;
                    for (int i = 0; i < locations.size(); i++) {
                        LocationsModel favoriteModel = locations.get(i);
                        if (model.id.equals(favoriteModel.id)) {
                            isFvrt = true;
                            id = i;
                            break;
                        }
                    }
                    if (!isFvrt) {
                        locations.add(model);
                        Stash.put(folder.replace(" ", "_"), locations);
                        Toast.makeText(this, R.string.added_to_favorite, Toast.LENGTH_SHORT).show();
                    } else {
                        locations.remove(id);
                        Stash.put(folder.replace(" ", "_"), locations);
                        Toast.makeText(this, R.string.removed_from_favorites, Toast.LENGTH_SHORT).show();
                    }

                });
                buttons.addView(customLayout);
            }
        } else {
            unlock.setVisibility(View.VISIBLE);
        }
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
                Toast.makeText(this, R.string.comment_added, Toast.LENGTH_SHORT).show();
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
            for (CommentModel commentModel : model.comments) {
                rating += commentModel.rating;
            }
            float total = rating / model.comments.size();
            String rate = String.format(Locale.getDefault(), "%.2f", total) + " (" + model.comments.size() + ")";
            if (model.comments.size() > 1) binding.rating.setText(rate);
            else binding.rating.setText(model.comments.get(0).rating + " (1)");

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
        model = (LocationsModel) Stash.getObject(Constants.MODEL, LocationsModel.class);
        if (model.images != null) {
            if (!model.images.isEmpty()) {
                binding.imageSlider.setSliderAdapter(new SliderAdapter(this, model.images));
            } else {
                binding.imageSlider.setSliderAdapter(new SliderAdapter(this, new ArrayList<>(Arrays.asList(Constants.DUMMY_IMAGE))));
            }
        } else {
            binding.imageSlider.setSliderAdapter(new SliderAdapter(this, new ArrayList<>(Arrays.asList(Constants.DUMMY_IMAGE))));
        }

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
        ArrayList<LocationsModel> favorite = Stash.getArrayList(Constants.FAVORITE, LocationsModel.class);
        for (int i = 0; i < favorite.size(); i++) {
            LocationsModel favoriteModel = favorite.get(i);
            if (model.id.equals(favoriteModel.id)) {
                isFavorite = true;
                index = i;
                break;
            }
        }

        int activitySize = model.activities == null ? 0 : model.activities.size();
        int servicesSize = model.services == null ? 0 : model.services.size();

        binding.totalActivity.setText(activitySize + " " + getString(R.string.activities));
        binding.totalServices.setText(servicesSize + " " + getString(R.string.services));
        binding.name.setText(model.name);
        binding.typeOfPlace.setText(model.typeOfPlace);
        binding.desc.setText(model.description);
        int size = model.comments != null ? model.comments.size() : 0;
        binding.rating.setText(model.rating + " (" + size + ")");
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

        updateRecycler();

    }
}