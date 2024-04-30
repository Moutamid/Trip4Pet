package com.moutamid.trip4pet.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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

        for (int s : model.activities) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.icon, null);
            ImageView image = customEditTextLayout.findViewById(R.id.image);
            image.setImageResource(s);
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

        for (int s : model.services) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.icon, null);
            ImageView image = customEditTextLayout.findViewById(R.id.image);
            image.setImageResource(s);
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

        });

    }
}