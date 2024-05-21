package com.moutamid.trip4pet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.databinding.ActivityAccountBinding;
import com.moutamid.trip4pet.models.LocationsModel;
import com.moutamid.trip4pet.models.UserModel;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {
    ActivityAccountBinding binding;
    MaterialCardView selected;
    TextView selectedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        binding.back.setOnClickListener(v -> onBackPressed());
        binding.setting.setOnClickListener(v -> startActivity(new Intent(this, AccountSettingActivity.class)));

        selected = binding.addedPlaces;
        selectedText = binding.addedText;

        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
        binding.title.setText(userModel.name);

        binding.addedPlaces.setOnClickListener(v -> {
            selectedText.setTextColor(getResources().getColor(R.color.text));
            selected.setCardBackgroundColor(getResources().getColor(R.color.white));
            selected = binding.addedPlaces;
            selectedText = binding.addedText;
            selectedText.setTextColor(getResources().getColor(R.color.white));
            selected.setCardBackgroundColor(getResources().getColor(R.color.blue));
        });
        binding.comments.setOnClickListener(v -> {
            selectedText.setTextColor(getResources().getColor(R.color.text));
            selected.setCardBackgroundColor(getResources().getColor(R.color.white));
            selected = binding.comments;
            selectedText = binding.commentsText;
            selectedText.setTextColor(getResources().getColor(R.color.white));
            selected.setCardBackgroundColor(getResources().getColor(R.color.blue));
        });
        binding.visited.setOnClickListener(v -> {
            selectedText.setTextColor(getResources().getColor(R.color.text));
            selected.setCardBackgroundColor(getResources().getColor(R.color.white));
            selected = binding.visited;
            selectedText = binding.visitedText;
            selectedText.setTextColor(getResources().getColor(R.color.white));
            selected.setCardBackgroundColor(getResources().getColor(R.color.blue));
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
        Constants.showDialog();

        getAddedPlaces();
        getComments();
        getVisitedPlaces();

    }

    ArrayList<LocationsModel> addedPlaces = new ArrayList<>();
    ArrayList<LocationsModel> comments = new ArrayList<>();
    ArrayList<LocationsModel> visitedPlaces = new ArrayList<>();

    private void getAddedPlaces() {
        Constants.databaseReference().child(Constants.PLACE).child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Constants.dismissDialog();
                        if (snapshot.exists()) {
                            addedPlaces.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                LocationsModel model = dataSnapshot.getValue(LocationsModel.class);
                                addedPlaces.add(model);
                            }
                        }

                        binding.addedText.setText(addedPlaces.size() + "\n" + getString(R.string.added_places));
                        binding.commentsText.setText(comments.size() + "\n" + getString(R.string.comments));
                        binding.visitedText.setText(visitedPlaces.size() + "\n" + getString(R.string.visited_places));

                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        if (mapFragment != null) {
                            mapFragment.getMapAsync(callbackAddedPlaces);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Constants.dismissDialog();
                        Toast.makeText(AccountActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getComments() {
// TODO
    }

    private void getVisitedPlaces() {
        // TODO
    }

    private static final String TAG = "AccountActivity";
    private OnMapReadyCallback callbackAddedPlaces = googleMap -> {

        Log.d(TAG, "Size : " + addedPlaces.size());

        for (LocationsModel model : addedPlaces) {
            Log.d(TAG, "Name: " + model.latitude);
            googleMap.addMarker(new MarkerOptions().position(new LatLng(model.latitude, model.longitude)).title(model.name));
        }

        if (!addedPlaces.isEmpty()) {
            LatLng latLng = new LatLng(addedPlaces.get(0).latitude, addedPlaces.get(0).longitude);
            //       googleMap.setMaxZoomPreference(20f);
            googleMap.setMinZoomPreference(4f);

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

    };

}