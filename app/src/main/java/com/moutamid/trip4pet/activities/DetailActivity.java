package com.moutamid.trip4pet.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.adapters.SliderAdapter;
import com.moutamid.trip4pet.databinding.ActivityDetailBinding;
import com.moutamid.trip4pet.models.FilterModel;
import com.moutamid.trip4pet.models.LocationsModel;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    LocationsModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        model = (LocationsModel) Stash.getObject(Constants.MODEL, LocationsModel.class);

        binding.back.setOnClickListener(v -> onBackPressed());

        binding.imageSlider.setSliderAdapter(new SliderAdapter(this, model.images));

        for (FilterModel s : model.activities) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.icon, null);
            ImageView image = customEditTextLayout.findViewById(R.id.image);
            image.setImageResource(s.icon);
            binding.activitiesIcon.addView(customEditTextLayout);
        }

        binding.totalActivity.setText(model.activities.size() + " Activities");
        binding.totalServices.setText(model.services.size() + " Services");
        binding.name.setText(model.name);
        binding.typeOfPlace.setText(model.typeOfPlace);
        binding.desc.setText(model.description);
        binding.rating.setText(model.rating+"");
        String cord = model.latitude + ", " + model.longitude + " (lat, long)";
        binding.coordinates.setText(cord);
        String address = model.name + ", " + model.city + ", " + model.country;
        binding.location.setText(address);

        for (FilterModel s : model.services) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.icon, null);
            ImageView image = customEditTextLayout.findViewById(R.id.image);
            CardView card = customEditTextLayout.findViewById(R.id.card);
            card.setCardBackgroundColor(getResources().getColor(R.color.green_card));
            image.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            image.setImageResource(s.icon);
            binding.servicesIcon.addView(customEditTextLayout);
        }

        if (model.services.isEmpty()){
            binding.services.setVisibility(View.GONE);
        }
        if (model.activities.isEmpty()){
            binding.activities.setVisibility(View.GONE);
        }

        binding.more.setOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = inflater.inflate(R.layout.detail_menu, null);
            PopupWindow popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.showAsDropDown(v);
            MaterialButton review = customView.findViewById(R.id.review);
            review.setOnClickListener(v1 -> showCodeDialog());
        });

    }

    private void showCodeDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_review);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.show();
    }
}