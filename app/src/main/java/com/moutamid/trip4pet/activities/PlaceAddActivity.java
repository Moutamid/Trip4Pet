package com.moutamid.trip4pet.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.adapters.ImageAdapter;
import com.moutamid.trip4pet.bottomsheets.FilterDialog;
import com.moutamid.trip4pet.databinding.ActivityPlaceAddBinding;
import com.moutamid.trip4pet.listener.ImageListener;
import com.moutamid.trip4pet.models.FilterModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceAddActivity extends AppCompatActivity {
    ActivityPlaceAddBinding binding;
    String COORDINATES;
    ArrayList<FilterModel> activities = new ArrayList<>();
    ArrayList<FilterModel> services = new ArrayList<>();
    private final int limit = 10;
    private static final int PICK_FROM_GALLERY = 1;
    ImageAdapter adapter;
    ArrayList<Uri> carImagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPlaceAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        carImagesList = new ArrayList<>();
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        COORDINATES = getIntent().getStringExtra(Constants.COORDINATES);
        String[] cord = COORDINATES.split(", ");
        binding.latitude.getEditText().setText(cord[0]);
        binding.longitude.getEditText().setText(cord[1]);

        binding.addActivity.setOnClickListener(v -> showFeatures(true));
        binding.addServices.setOnClickListener(v -> showFeatures(false));

        addServices();
        addActivities();

        List<String> list = Arrays.asList(FilterDialog.placesList);
        ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, list);
        binding.placesList.setAdapter(exerciseAdapter);

        binding.btnAddCarPhoto.setOnClickListener(v -> {
            if (carImagesList.size() >= limit) {
                Toast.makeText(this, "Required Number of Images are selected", Toast.LENGTH_SHORT).show();
            } else {
                showDialog();
            }
        });

        binding.AddPhotoLayout.setOnClickListener(v -> {
            if (carImagesList.size() >= limit) {
                Toast.makeText(this, "Required Number of Images are selected", Toast.LENGTH_SHORT).show();
            } else {
                showDialog();
            }
        });

    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bs_get_image);

        MaterialButton openCamera = dialog.findViewById(R.id.btnOpenCamera);
        MaterialButton openGallery = dialog.findViewById(R.id.btnOpenGallery);
        MaterialButton cancel = dialog.findViewById(R.id.btnCancel);

        cancel.setOnClickListener(v -> {
            dialog.cancel();
        });

        openGallery.setOnClickListener(v -> {
            getImageFromGallery();
            dialog.cancel();
        });

        openCamera.setOnClickListener(v -> {
            getImageFromCamera();
            dialog.cancel();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void getImageFromCamera() {
        ImagePicker.with(this)
                .cameraOnly()
                .compress(512)
                .maxResultSize(1080, 1080)
                .start();
    }

    private void getImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // intent.setType("image/*");
        // intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, ""), PICK_FROM_GALLERY);
    }

    private void addActivities() {
        binding.activitiesIcon.removeAllViews();
        binding.totalActivity.setText(activities.size() + " Activities");
        for (FilterModel s : activities) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.icon, null);
            ImageView image = customEditTextLayout.findViewById(R.id.image);
            image.setImageResource(s.icon);
            binding.activitiesIcon.addView(customEditTextLayout);
        }
    }

    private void addServices() {
        binding.servicesIcon.removeAllViews();
        binding.totalServices.setText(services.size() + " Services");
        for (FilterModel s : services) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.icon, null);
            ImageView image = customEditTextLayout.findViewById(R.id.image);
            CardView card = customEditTextLayout.findViewById(R.id.card);
            card.setCardBackgroundColor(getResources().getColor(R.color.green_card));
            image.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            image.setImageResource(s.icon);
            binding.servicesIcon.addView(customEditTextLayout);
        }
    }

    private void showFeatures(boolean b) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.features);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.show();

        TextView textview = dialog.findViewById(R.id.text);
        LinearLayout features = dialog.findViewById(R.id.features);
        MaterialButton apply = dialog.findViewById(R.id.apply);

        String t = b ? "Select All Activities that apply" : "Select All Services that apply";
        textview.setText(t);
        ArrayList<FilterModel> list = b ? getActivities() : getServices();
        int color = b ? R.color.blue : R.color.green;
        for (FilterModel s : list) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.filter_check_layout, null);

            MaterialCheckBox checkbox = customEditTextLayout.findViewById(R.id.checkbox);
            ImageView lock = customEditTextLayout.findViewById(R.id.lock);
            ImageView image = customEditTextLayout.findViewById(R.id.image);
            TextView text = customEditTextLayout.findViewById(R.id.text);
            image.setTag(s.icon);
            image.setImageResource(s.icon);
            lock.setVisibility(View.GONE);
            text.setText(s.name);
            checkbox.setEnabled(true);
            image.setImageTintList(ColorStateList.valueOf(getResources().getColor(color)));
            MaterialCardView card = customEditTextLayout.findViewById(R.id.card);
            card.setOnClickListener(v -> {
                checkbox.setChecked(!checkbox.isChecked());
            });

            features.addView(customEditTextLayout);
        }

        ArrayList<FilterModel> checked = b ? activities : services;
        for (FilterModel check : checked) {
            for (int i = 0; i < features.getChildCount(); i++) {
                View view = features.getChildAt(i);
                if (view instanceof RelativeLayout) {
                    RelativeLayout main = (RelativeLayout) view;
                    MaterialCheckBox checkbox = main.findViewById(R.id.checkbox);
                    TextView text = main.findViewById(R.id.text);
                    if (check.name.trim().equals(text.getText().toString().trim())) {
                        checkbox.setChecked(true);
                    }
                }
            }
        }

        apply.setOnClickListener(v -> {
            ArrayList<FilterModel> finalList = new ArrayList<>();
            for (int i = 0; i < features.getChildCount(); i++) {
                View view = features.getChildAt(i);
                if (view instanceof RelativeLayout) {
                    RelativeLayout main = (RelativeLayout) view;
                    MaterialCheckBox checkbox = main.findViewById(R.id.checkbox);
                    TextView text = main.findViewById(R.id.text);
                    ImageView image = main.findViewById(R.id.image);
                    if (checkbox.isChecked()) {
                        finalList.add(new FilterModel(text.getText().toString(), (Integer) image.getTag()));
                    }
                }
            }
            if (b) {
                activities.clear();
                activities = new ArrayList<>(finalList);
                dialog.dismiss();
                addActivities();
            } else {
                services.clear();
                services = new ArrayList<>(finalList);
                dialog.dismiss();
                addServices();
            }
        });

    }

    private ArrayList<FilterModel> getActivities() {
        ArrayList<FilterModel> activities = new ArrayList<>();
        activities.add(new FilterModel("Playground", R.drawable.ride));
        activities.add(new FilterModel("Point of view", R.drawable.binoculars_solid));
        activities.add(new FilterModel("Swimming possible", R.drawable.person_swimming_solid));
        activities.add(new FilterModel("Climbing (Sites of)", R.drawable.climbing_with_rope));
        activities.add(new FilterModel("Canoe/kayak (Base of)", R.drawable.kayak));
        activities.add(new FilterModel("Fishing spots", R.drawable.fish_fins_solid));
        activities.add(new FilterModel("Beach fisheries", R.drawable.umbrella_beach_solid));
        activities.add(new FilterModel("Departure of hikes", R.drawable.person_hiking_solid));
        activities.add(new FilterModel("Monuments to visit", R.drawable.monument_solid));
        activities.add(new FilterModel("Mountain bike tracks", R.drawable.bicycle_solid));
        activities.add(new FilterModel("Windsurf/kitesurf (Spot of)", R.drawable.kitesurf));
        activities.add(new FilterModel("Beautiful motorcycle rides", R.drawable.motorcycle_solid));
        return activities;
    }

    private ArrayList<FilterModel> getServices() {
        ArrayList<FilterModel> service = new ArrayList<>();
        service.add(new FilterModel("Electricity (access possible)", R.drawable.plug_circle_check_solid));
        service.add(new FilterModel("Drinking water", R.drawable.faucet_drip_solid));
        service.add(new FilterModel("Black water", R.drawable.toilet_solid));
        service.add(new FilterModel("Wastewater", R.drawable.dumpster_solid));
        service.add(new FilterModel("Waste container", R.drawable.dumpster_solid));
        service.add(new FilterModel("Bakery", R.drawable.cake_candles_solid));
        service.add(new FilterModel("Public toilets", R.drawable.restroom_solid));
        service.add(new FilterModel("Showers (possible access)", R.drawable.shower_solid));
        service.add(new FilterModel("Internet access via WiFi", R.drawable.wifi_solid));
        service.add(new FilterModel("Winter-caravanning", R.drawable.snowman_solid));
        service.add(new FilterModel("Pets allowed", R.drawable.dog_solid));
        service.add(new FilterModel("Swimming pool", R.drawable.person_swimming_solid));
        service.add(new FilterModel("Laundry", R.drawable.jug_detergent_solid));
        service.add(new FilterModel("LPG station", R.drawable.gas_pump_solid));
        service.add(new FilterModel("Bottled gas service", R.drawable.fire_flame_simple_solid));
        service.add(new FilterModel("3G/4G internet", R.drawable.signal_solid));
        service.add(new FilterModel("Washing for motorhome", R.drawable.soap_solid));
        return service;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_FROM_GALLERY) {
                try {
                    if (resultCode == RESULT_OK && data != null && data.getClipData() != null) {
                        binding.AddPhotoLayout.setVisibility(View.GONE);
                        binding.AddPhotoLayoutRecycler.setVisibility(View.VISIBLE);
                        int currentImage = 0;

                        while (currentImage < data.getClipData().getItemCount()) {
                            if (currentImage < limit) {
                                carImagesList.add(data.getClipData().getItemAt(currentImage).getUri());
                            }
                            currentImage++;
                        }

                        adapter = new ImageAdapter(this, carImagesList, listener);
                        binding.RecyclerViewImageList.setAdapter(adapter);
                    } else {
                        Toast.makeText(this, "Please Select Multiple Images", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    binding.AddPhotoLayout.setVisibility(View.GONE);
                    binding.AddPhotoLayoutRecycler.setVisibility(View.VISIBLE);

                    carImagesList.add(data.getData());

                    adapter = new ImageAdapter(this, carImagesList, listener);
                    binding.RecyclerViewImageList.setAdapter(adapter);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    ImageListener listener = new ImageListener() {
        @Override
        public void onClick(int pos) {
            showMenu(pos);
        }
    };

    private void showMenu(int pos) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bs_image_menu);

        Button removeImg = dialog.findViewById(R.id.btnRemoveImage);

        removeImg.setOnClickListener(remove -> {
            try {
                if (carImagesList.size() == 1) {
                    carImagesList.remove(pos);
                    adapter.notifyItemRemoved(pos);
                    dialog.cancel();
                    if (carImagesList.isEmpty()) {
                        binding.AddPhotoLayoutRecycler.setVisibility(View.GONE);
                        binding.AddPhotoLayout.setVisibility(View.VISIBLE);
                    }
                }
                carImagesList.remove(pos);
                adapter.notifyItemRemoved(pos);
                dialog.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_CarsDealersHub_BottomSheetAnim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

}