package com.moutamid.trip4pet.bottomsheets;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.listener.BottomSheetDismissListener;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.databinding.FilterFragmentBinding;
import com.moutamid.trip4pet.models.FilterModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterDialog extends BottomSheetDialogFragment {
    FilterFragmentBinding binding;
    private BottomSheetDismissListener listener;
    public static String[] placesList = {
            "Park",
            "Restaurant",
            "Beach",
            "Other",
    };
    public static String[] vehicleHeight = {
            "5.10'", "6.2'", "6.6'", "6.10'", "7.2'", "7.6'", "7.10'", "8.2'", "8.6'", "8.10'", "9.2'", "9.6'", "9.10'",
            "10.2'", "10.6'", "10.10'", "11.2'", "11.6'", "11.10'", "12.2'", "12.6'", "12.10'",
    };
    ArrayList<FilterModel> service = new ArrayList<>();
    ArrayList<FilterModel> activities = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FilterFragmentBinding.inflate(getLayoutInflater(), container, false);

        binding.apply.setOnClickListener(v -> {
            dismiss();
        });

        addPlaces();
        addActivities();
        addServices();

        List<String> list = Arrays.asList(vehicleHeight);
        ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, list);
        binding.heightList.setAdapter(exerciseAdapter);

        return binding.getRoot();
    }

    private void addPlaces() {
        for (String s : placesList) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.filter_check_layout, null);
            MaterialCheckBox checkbox = customEditTextLayout.findViewById(R.id.checkbox);
            TextView text = customEditTextLayout.findViewById(R.id.text);
            ImageView lock = customEditTextLayout.findViewById(R.id.lock);
            ImageView image = customEditTextLayout.findViewById(R.id.image);

            image.setVisibility(View.GONE);
            lock.setVisibility(View.GONE);
            text.setText(s);
            checkbox.setEnabled(true);

            MaterialCardView card = customEditTextLayout.findViewById(R.id.card);
            card.setOnClickListener(v -> {
                checkbox.setChecked(!checkbox.isChecked());
            });

            binding.places.addView(customEditTextLayout);
        }
    }

    private void addServices() {
        service = new ArrayList<>(Constants.getServices());
        for (FilterModel s : service) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.filter_check_layout, null);

            MaterialCheckBox checkbox = customEditTextLayout.findViewById(R.id.checkbox);
            ImageView lock = customEditTextLayout.findViewById(R.id.lock);

            ImageView image = customEditTextLayout.findViewById(R.id.image);
            TextView text = customEditTextLayout.findViewById(R.id.text);

            image.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));

            image.setImageResource(s.icon);
            text.setText(s.name);
            binding.services.addView(customEditTextLayout);
        }
    }

    private void addActivities() {
        activities = new ArrayList<>(Constants.getActivities());

        for (FilterModel s : activities) {
            LayoutInflater inflater = getLayoutInflater();
            View customEditTextLayout = inflater.inflate(R.layout.filter_check_layout, null);

            MaterialCheckBox checkbox = customEditTextLayout.findViewById(R.id.checkbox);
            ImageView lock = customEditTextLayout.findViewById(R.id.lock);

            ImageView image = customEditTextLayout.findViewById(R.id.image);
            TextView text = customEditTextLayout.findViewById(R.id.text);
            image.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
            image.setImageResource(s.icon);
            text.setText(s.name);
            binding.activities.addView(customEditTextLayout);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set BottomSheet behavior to go full screen
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            BottomSheetBehavior.from((View) getView().getParent()).setState(BottomSheetBehavior.STATE_EXPANDED);
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

//    StepClickListner listner = model -> {
//        ArrayList<AddStepsChildModel> list = Stash.getArrayList(Constants.Steps, AddStepsChildModel.class);
//        list.add(model);
//        Stash.put(Constants.Steps, list);
//        this.dismiss();
//    };

}
