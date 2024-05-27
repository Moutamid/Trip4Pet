package com.moutamid.trip4pet.bottomsheets;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fxn.stash.Stash;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.SubscriptionActivity;
import com.moutamid.trip4pet.databinding.FavoritesLayoutBinding;
import com.moutamid.trip4pet.listener.BottomSheetDismissListener;
import com.moutamid.trip4pet.models.LocationsModel;

import java.util.ArrayList;

public class FavoruiteDialog extends BottomSheetDialogFragment {
    FavoritesLayoutBinding binding;
    private BottomSheetDismissListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FavoritesLayoutBinding.inflate(getLayoutInflater(), container, false);

        Constants.setLocale(requireContext(), Stash.getString(Constants.LANGUAGE, "en"));
        binding.close.setOnClickListener(v -> dismiss());

        ArrayList<LocationsModel> favorite = Stash.getArrayList(Constants.FAVORITE, LocationsModel.class);
        binding.size.setText(favorite.size() + "/" + Constants.FAVORITE_SIZE);

        binding.myFavorites.setOnClickListener(v -> {
            dismiss();
            ListDialog listDialog = new ListDialog(favorite);
            listDialog.setListener(() -> {

            });
            listDialog.show(requireActivity().getSupportFragmentManager(), listDialog.getTag());
        });

        binding.unlock.setOnClickListener(v -> {
            dismiss();
            startActivity(new Intent(requireContext(), SubscriptionActivity.class));
            requireActivity().finish();
        });

        userFolders();

        binding.create.setOnClickListener(v -> {
            Dialog dialog = new Dialog(requireContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.create_folder);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);
            dialog.show();

            TextInputLayout name = dialog.findViewById(R.id.name);
            MaterialButton create = dialog.findViewById(R.id.create);

            create.setOnClickListener(v1 -> {
                String n = name.getEditText().getText().toString();
                if (!n.isEmpty()) {
                    ArrayList<String> folders = Stash.getArrayList(Constants.FAVORITE_FOLDER, String.class);
                    folders.add(n.trim());
                    Stash.put(Constants.FAVORITE_FOLDER, folders);
                    dialog.dismiss();
                    userFolders();
                }
            });

        });

        return binding.getRoot();
    }

    private void userFolders() {
        binding.buttons.removeAllViews();
        if (Stash.getBoolean(Constants.ISVIP)) {
            binding.create.setEnabled(true);
            binding.unlock.setVisibility(View.GONE);
            ArrayList<String> folders = Stash.getArrayList(Constants.FAVORITE_FOLDER, String.class);
            for (String folder : folders) {
                LayoutInflater inf = getLayoutInflater();
                View customEditTextLayout = inf.inflate(R.layout.favorite_folders, null);
                TextView name = customEditTextLayout.findViewById(R.id.name);
                TextView size = customEditTextLayout.findViewById(R.id.size);
                MaterialCardView myFavorites = customEditTextLayout.findViewById(R.id.myFavorites);
                name.setText(folder);
                ArrayList<LocationsModel> locations = Stash.getArrayList(folder.replace(" ", "_"), LocationsModel.class);
                size.setText("Total: " + locations.size());

                myFavorites.setOnClickListener(v -> {
                    dismiss();
                    ListDialog listDialog = new ListDialog(locations);
                    listDialog.setListener(() -> {
                    });
                    listDialog.show(requireActivity().getSupportFragmentManager(), listDialog.getTag());
                });
                binding.buttons.addView(customEditTextLayout);
            }
        } else {
            binding.create.setEnabled(false);
            binding.unlock.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onBottomSheetDismissed();
        }
    }

    public void setListener(BottomSheetDismissListener listener) {
        this.listener = listener;
    }

}
