package com.moutamid.trip4pet.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fxn.stash.Stash;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.databinding.ActivityPasswordBinding;
import com.moutamid.trip4pet.models.UserModel;

import java.util.HashMap;
import java.util.Map;

public class PasswordActivity extends AppCompatActivity {
    ActivityPasswordBinding binding;
    FirebaseUser user;
    AuthCredential authCredential;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        user = Constants.auth().getCurrentUser();
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText(R.string.change_password);

        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

        binding.change.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                authCredential = EmailAuthProvider.getCredential(user.getEmail(), binding.oldPassword.getEditText().getText().toString());
                user.reauthenticate(authCredential).addOnSuccessListener(unused -> {
                    user.updatePassword(binding.newPassword.getEditText().getText().toString())
                            .addOnSuccessListener(unused1 -> {
                                Map<String, Object> map = new HashMap<>();
                                map.put("password", binding.newPassword.getEditText().getText().toString());
                                Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid()).updateChildren(map).addOnSuccessListener(unused2 -> {
                                    Constants.dismissDialog();
                                    userModel.password = binding.newPassword.getEditText().getText().toString();
                                    Stash.put(Constants.STASH_USER, userModel);
                                    Toast.makeText(this, "Password Updated successfully", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }).addOnFailureListener(e -> {
                                    Constants.dismissDialog();
                                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                });
                            })
                            .addOnFailureListener(e -> {
                                Constants.dismissDialog();
                                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            });
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, "Current Password Doesn't Match", Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
    }

    private boolean valid() {
        if (binding.oldPassword.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Old Password is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.newPassword.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "New Password is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.rePassword.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!binding.rePassword.getEditText().getText().toString().equals(binding.newPassword.getEditText().getText().toString())) {
            Toast.makeText(this, "Password is not matched", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}