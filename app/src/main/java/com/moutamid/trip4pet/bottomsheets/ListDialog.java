package com.moutamid.trip4pet.bottomsheets;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.listener.BottomSheetDismissListener;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.adapters.LocationAdapter;
import com.moutamid.trip4pet.databinding.ListDialogBinding;
import com.moutamid.trip4pet.models.LocationsModel;

import java.util.ArrayList;
import java.util.Date;

public class ListDialog extends BottomSheetDialogFragment {
    ListDialogBinding binding;
    private BottomSheetDismissListener listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ListDialogBinding.inflate(getLayoutInflater(), container, false);

        binding.close.setOnClickListener(v -> dismiss());

        ArrayList<LocationsModel> list = Constants.getList(requireContext());
        binding.listRC.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.listRC.setHasFixedSize(false);

        LocationAdapter adapter = new LocationAdapter(requireContext(), list);
        binding.listRC.setAdapter(adapter);

        return binding.getRoot();
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
