package com.moutamid.trip4pet.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moutamid.trip4pet.Stash;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.databinding.ActivityNameBinding;
import com.moutamid.trip4pet.models.UserModel;

import java.util.HashMap;
import java.util.Map;

public class NameActivity extends AppCompatActivity {
    ActivityNameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.setLocale(getBaseContext(), Stash.getString(Constants.LANGUAGE, "en"));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

        binding.name.getEditText().setText(userModel.name);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText(R.string.change_name);

        binding.change.setOnClickListener(v -> {
            if (!binding.name.getEditText().getText().toString().isEmpty()) {
                Constants.showDialog();
                Map<String, Object> map = new HashMap<>();
                map.put("name", binding.name.getEditText().getText().toString());
                Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid()).updateChildren(map).addOnSuccessListener(unused -> {
                    Constants.dismissDialog();
                    userModel.name = binding.name.getEditText().getText().toString();
                    Stash.put(Constants.STASH_USER, userModel);
                    Toast.makeText(this, R.string.name_updated, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(this, R.string.name_is_empty, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
    }
}