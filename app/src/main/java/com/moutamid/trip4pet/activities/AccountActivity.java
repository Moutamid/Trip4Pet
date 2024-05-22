package com.moutamid.trip4pet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.databinding.ActivityAccountBinding;
import com.moutamid.trip4pet.models.CommentModel;
import com.moutamid.trip4pet.models.LocationsModel;
import com.moutamid.trip4pet.models.UserModel;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {
    ActivityAccountBinding binding;
    MaterialCardView selected;
    TextView selectedText;
    GoogleMap mMap;
    private ArrayList<Marker> currentMarkers = new ArrayList<>();

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
            getAddedPlaces();
        });
        binding.comments.setOnClickListener(v -> {
            selectedText.setTextColor(getResources().getColor(R.color.text));
            selected.setCardBackgroundColor(getResources().getColor(R.color.white));
            selected = binding.comments;
            selectedText = binding.commentsText;
            selectedText.setTextColor(getResources().getColor(R.color.white));
            selected.setCardBackgroundColor(getResources().getColor(R.color.blue));
            getComments();
        });
        binding.visited.setOnClickListener(v -> {
            selectedText.setTextColor(getResources().getColor(R.color.text));
            selected.setCardBackgroundColor(getResources().getColor(R.color.white));
            selected = binding.visited;
            selectedText = binding.visitedText;
            selectedText.setTextColor(getResources().getColor(R.color.white));
            selected.setCardBackgroundColor(getResources().getColor(R.color.blue));
            getVisitedPlaces();
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
        getAddedPlaces();
    }

    ArrayList<LocationsModel> addedPlaces = new ArrayList<>();
    ArrayList<LocationsModel> comments = new ArrayList<>();
    ArrayList<LocationsModel> visitedPlaces = new ArrayList<>();

    private void getAddedPlaces() {
        Constants.showDialog();
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

                        showAddedPlaces();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Constants.dismissDialog();
                        Toast.makeText(AccountActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showAddedPlaces() {
        for (Marker marker : currentMarkers) {
            marker.remove();
        }
        currentMarkers.clear();
        for (LocationsModel model : addedPlaces) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(model.latitude, model.longitude))
                    .title(model.name));
            currentMarkers.add(marker);
        }
        if (!addedPlaces.isEmpty()) {
            LatLng latLng = new LatLng(addedPlaces.get(0).latitude, addedPlaces.get(0).longitude);
            mMap.setMinZoomPreference(4f);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    private void showCommentsPlaces() {
        for (Marker marker : currentMarkers) {
            marker.remove();
        }
        currentMarkers.clear();
        for (LocationsModel model : comments) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(model.latitude, model.longitude))
                    .title(model.name));
            currentMarkers.add(marker);
        }
        if (!comments.isEmpty()) {
            LatLng latLng = new LatLng(comments.get(0).latitude, comments.get(0).longitude);
            mMap.setMinZoomPreference(4f);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    private void showVisitedPlaces() {
        for (Marker marker : currentMarkers) {
            marker.remove();
        }
        currentMarkers.clear();
        for (LocationsModel model : visitedPlaces) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(model.latitude, model.longitude))
                    .title(model.name));
            currentMarkers.add(marker);
        }
        if (!visitedPlaces.isEmpty()) {
            LatLng latLng = new LatLng(visitedPlaces.get(0).latitude, visitedPlaces.get(0).longitude);
            mMap.setMinZoomPreference(4f);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    private void getComments() {
        Constants.showDialog();
        Constants.databaseReference().child(Constants.PLACE)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Constants.dismissDialog();
                        if (snapshot.exists()) {
                            comments.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                    LocationsModel model = dataSnapshot2.getValue(LocationsModel.class);
                                    if (dataSnapshot2.child("comments").exists()) {
                                        for (DataSnapshot commentsShot : dataSnapshot2.child("comments").getChildren()) {
                                            CommentModel commentModel = commentsShot.getValue(CommentModel.class);
                                            if (commentModel.userID.equals(Constants.auth().getCurrentUser().getUid())) {
                                                comments.add(model);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        binding.addedText.setText(addedPlaces.size() + "\n" + getString(R.string.added_places));
                        binding.commentsText.setText(comments.size() + "\n" + getString(R.string.comments));
                        binding.visitedText.setText(visitedPlaces.size() + "\n" + getString(R.string.visited_places));

                        showCommentsPlaces();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Constants.dismissDialog();
                        Toast.makeText(AccountActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    ArrayList<String> visited = new ArrayList<>();
    private void getVisitedPlaces() {
        Constants.showDialog();
        Constants.databaseReference().child(Constants.VISITED).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        visited.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            visited.add(snapshot.child("visited").getValue(String.class));
                        }
                        getLocations();
                    } else {
                        Constants.dismissDialog();
                    }
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(AccountActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void getLocations() {
        Constants.databaseReference().child(Constants.PLACE)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Constants.dismissDialog();
                        if (snapshot.exists()) {
                            visitedPlaces.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                    LocationsModel model = dataSnapshot2.getValue(LocationsModel.class);
                                    for (String visited : visited){
                                        if (visited.equals(model.id)){
                                            visitedPlaces.add(model);
                                        }
                                    }
                                }
                            }
                        }

                        binding.addedText.setText(addedPlaces.size() + "\n" + getString(R.string.added_places));
                        binding.commentsText.setText(comments.size() + "\n" + getString(R.string.comments));
                        binding.visitedText.setText(visitedPlaces.size() + "\n" + getString(R.string.visited_places));

                        showVisitedPlaces();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Constants.dismissDialog();
                        Toast.makeText(AccountActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private static final String TAG = "AccountActivity";
    private OnMapReadyCallback callback = googleMap -> {
        mMap = googleMap;
    };

}