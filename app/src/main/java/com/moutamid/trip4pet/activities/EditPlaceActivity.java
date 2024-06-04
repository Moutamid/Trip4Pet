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

import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.adapters.ImageAdapter;
import com.moutamid.trip4pet.bottomsheets.FilterDialog;
import com.moutamid.trip4pet.databinding.ActivityEditPlaceBinding;
import com.moutamid.trip4pet.listener.ImageListener;
import com.moutamid.trip4pet.models.FilterModel;
import com.moutamid.trip4pet.models.LocationsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EditPlaceActivity extends AppCompatActivity {
    ActivityEditPlaceBinding binding;
    String COORDINATES;
    String PLACE;
    ArrayList<FilterModel> activities = new ArrayList<>();
    ArrayList<FilterModel> services = new ArrayList<>();
    private final int limit = 6;
    private static final int PICK_FROM_GALLERY = 1;
    ImageAdapter adapter;
    ArrayList<String> images;
    ArrayList<Uri> imagesList;
    ArrayList<Uri> editedList;
    String ID;
    LocationsModel model;
    Map<Integer, Integer> positionMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEditPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.setLocale(getBaseContext(), Stash.getString(Constants.LANGUAGE, "en"));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        model = (LocationsModel) Stash.getObject(Constants.EDIT, LocationsModel.class);

        ID = model.id;

        images = new ArrayList<>();
        imagesList = new ArrayList<>();
        editedList = new ArrayList<>();

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.btnAddCarPhoto.setOnClickListener(v -> {
            if (imagesList.size() >= limit) {
                Toast.makeText(this, getString(R.string.required_number_of_images_are_selected), Toast.LENGTH_SHORT).show();
            } else {
                getImageFromGallery();
            }
        });

        binding.AddPhotoLayout.setOnClickListener(v -> {
            if (imagesList.size() >= limit) {
                Toast.makeText(this, getString(R.string.required_number_of_images_are_selected), Toast.LENGTH_SHORT).show();
            } else {
                getImageFromGallery();
            }
        });

        List<String> list = Arrays.asList(FilterDialog.placesList);
        ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, list);
        binding.placesList.setAdapter(exerciseAdapter);

        binding.addActivity.setOnClickListener(v -> showFeatures(true));
        binding.addServices.setOnClickListener(v -> showFeatures(false));

        updateViews();

        binding.addPlace.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                if (editedList.isEmpty()) {
                    uploadData();
                } else {
                    uploadImages();
                }
            }
        });


    }

    private void updateViews() {
        binding.name.getEditText().setText(model.name);
        binding.contact.getEditText().setText(model.contact);
        binding.place.getEditText().setText(model.typeOfPlace);
        binding.desc.getEditText().setText(model.description);
        binding.location.getEditText().setText(model.address);
        binding.isAccessible.setChecked(model.isAccessibleToAnimals);
        binding.city.getEditText().setText(model.city);
        binding.country.getEditText().setText(model.country);
        binding.latitude.getEditText().setText(String.valueOf(model.latitude));
        binding.longitude.getEditText().setText(String.valueOf(model.longitude));

        if (model.activities != null) activities = new ArrayList<>(model.activities);
        if (model.services != null) services = new ArrayList<>(model.services);
        addServices();
        addActivities();

        images = new ArrayList<>(model.images);

        for (String link : model.images) {
            imagesList.add(Uri.parse(link));
        }
        if (!imagesList.isEmpty()) {
            binding.AddPhotoLayoutRecycler.setVisibility(View.VISIBLE);
            binding.AddPhotoLayout.setVisibility(View.GONE);
        }
        adapter = new ImageAdapter(this, imagesList, listener);
        binding.RecyclerViewImageList.setAdapter(adapter);
    }

    private void uploadImages() {
        for (Uri uri : editedList) {
            Constants.storageReference().child("images").child(new SimpleDateFormat("ddMMyyyyhhmmss", Locale.getDefault()).format(new Date().getTime()))
                    .putFile(uri).addOnSuccessListener(taskSnapshot -> {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(link -> {
                            images.add(link.toString());
                            uploadData();
                        });
                    })
                    .addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void uploadData() {
        LocationsModel model = new LocationsModel();
        model.id = this.ID;
        model.userID = this.model.userID;
        model.name = binding.name.getEditText().getText().toString();
        model.contact = binding.contact.getEditText().getText().toString();
        model.typeOfPlace = binding.place.getEditText().getText().toString();
        model.description = binding.desc.getEditText().getText().toString();
        model.address = binding.location.getEditText().getText().toString();
        model.city = binding.city.getEditText().getText().toString();
        model.country = binding.country.getEditText().getText().toString();
        model.latitude = Double.parseDouble(binding.latitude.getEditText().getText().toString());
        model.longitude = Double.parseDouble(binding.longitude.getEditText().getText().toString());
        model.images = new ArrayList<>(images);
        model.activities = new ArrayList<>(activities);
        model.services = new ArrayList<>(services);
        model.isAccessibleToAnimals = binding.isAccessible.isChecked();
        model.timestamp = new Date().getTime();
        if (this.model.comments != null) {
            model.comments = new ArrayList<>(this.model.comments);
        }
        Constants.databaseReference().child(Constants.PLACE).child(model.userID).child(model.id)
                .setValue(model).addOnSuccessListener(unused -> {
                    Constants.dismissDialog();
                    Stash.put(Constants.MODEL, model);
                    Toast.makeText(this, R.string.place_added_successfully, Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private boolean valid() {
//        if (imagesList.isEmpty()) {
//            Toast.makeText(this, "Add images", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if (binding.name.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.name_is_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.contact.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.contact_is_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.place.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.type_of_place_is_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.desc.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.description_is_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.location.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.address_is_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.city.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.city_is_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.country.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.country_is_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.latitude.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.latitude_is_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.longitude.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.longitude_is_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
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
        binding.totalActivity.setText(activities.size() + " " + getString(R.string.activities));
        for (FilterModel s : activities) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.icon, null);
            ImageView image = customEditTextLayout.findViewById(R.id.image);
            image.setImageResource(s.icon);
            image.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
            binding.activitiesIcon.addView(customEditTextLayout);
        }
    }

    private void addServices() {
        binding.servicesIcon.removeAllViews();
        binding.totalServices.setText(services.size() + " " + getString(R.string.services));
        for (FilterModel s : services) {
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

        String t = b ? getString(R.string.select_all_activities_that_apply) : getString(R.string.select_all_services_that_apply);
        textview.setText(t);
        ArrayList<FilterModel> list = b ? Constants.getActivities(this) : Constants.getServices(this);
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
            if (b) {
                image.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
            }
            MaterialCardView card = customEditTextLayout.findViewById(R.id.card);
            card.setOnClickListener(v -> checkbox.setChecked(!checkbox.isChecked()));

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
                                imagesList.add(data.getClipData().getItemAt(currentImage).getUri());
                                editedList.add(data.getClipData().getItemAt(currentImage).getUri());
                                int pos = imagesList.size() - 1;
                                positionMap.put(pos, editedList.size() - 1);
                            }
                            currentImage++;
                        }

                        adapter = new ImageAdapter(this, imagesList, listener);
                        binding.RecyclerViewImageList.setAdapter(adapter);
                    } else {
                        Toast.makeText(this, getString(R.string.please_select_multiple_images), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    binding.AddPhotoLayout.setVisibility(View.GONE);
                    binding.AddPhotoLayoutRecycler.setVisibility(View.VISIBLE);

                    imagesList.add(data.getData());
                    editedList.add(data.getData());
                    int pos = imagesList.size() - 1;
                    positionMap.put(pos, editedList.size() - 1);

                    adapter = new ImageAdapter(this, imagesList, listener);
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
                if (positionMap.containsKey(pos)) {
                    int editedPos = positionMap.remove(pos);
                    editedList.remove(editedPos);

                    // Update editedList positions in the map
                    for (Map.Entry<Integer, Integer> entry : positionMap.entrySet()) {
                        if (entry.getValue() > editedPos) {
                            positionMap.put(entry.getKey(), entry.getValue() - 1);
                        }
                    }
                }
                if (imagesList.size() == 1) {
                    imagesList.remove(pos);
                    adapter.notifyItemRemoved(pos);
                    dialog.cancel();
                    if (imagesList.isEmpty()) {
                        binding.AddPhotoLayoutRecycler.setVisibility(View.GONE);
                        binding.AddPhotoLayout.setVisibility(View.VISIBLE);
                    }
                }
                if (pos < images.size()) {
                    images.remove(pos);
                }
                imagesList.remove(pos);
                adapter.notifyItemRemoved(pos);

                Map<Integer, Integer> newPositionMap = new HashMap<>();
                for (Map.Entry<Integer, Integer> entry : positionMap.entrySet()) {
                    if (entry.getKey() > pos) {
                        newPositionMap.put(entry.getKey() - 1, entry.getValue());
                    } else {
                        newPositionMap.put(entry.getKey(), entry.getValue());
                    }
                }
                positionMap = newPositionMap;
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