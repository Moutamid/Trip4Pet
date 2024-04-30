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
import com.moutamid.trip4pet.BottomSheetDismissListener;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.adapters.LocationAdapter;
import com.moutamid.trip4pet.databinding.FilterFragmentBinding;
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

        ArrayList<LocationsModel> list = getList();
        binding.listRC.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.listRC.setHasFixedSize(false);

        LocationAdapter adapter = new LocationAdapter(requireContext(), list);
        binding.listRC.setAdapter(adapter);

        return binding.getRoot();
    }

    private ArrayList<LocationsModel> getList() {
        ArrayList<LocationsModel> list = new ArrayList<>();

        ArrayList<String> images1 = new ArrayList<>();
        images1.add("https://wpassets.graana.com/blog/wp-content/uploads/2023/08/Shakarparian.jpg");
        images1.add("https://live.staticflickr.com/1573/24289734385_009bb6cc0f_b.jpg");
        ArrayList<Integer> activities1 = new ArrayList<>();
        activities1.add(R.drawable.ride);
        activities1.add(R.drawable.person_hiking_solid);
        activities1.add(R.drawable.monument_solid);

        ArrayList<Integer> services1 = new ArrayList<>();
        services1.add(R.drawable.wifi_solid);
        services1.add(R.drawable.plug_circle_check_solid);
        services1.add(R.drawable.fire_flame_simple_solid);
        services1.add(R.drawable.snowman_solid);

        list.add(new LocationsModel(
                "Garden Avenue", "Pakistan", "Islamabad", getString(R.string.lorem), "free", "+123456789", "Parking lot Day/Night",
                33.70411, 73.08887, images1, activities1, services1, new Date().getTime(), 4.0
        ));

        ArrayList<String> images2 = new ArrayList<>();
        images2.add("https://upload.wikimedia.org/wikipedia/commons/thumb/7/73/Regattafeld_vor_Laboe.jpg/1200px-Regattafeld_vor_Laboe.jpg");
        images2.add("https://d3mvlb3hz2g78.cloudfront.net/wp-content/uploads/2018/11/thumb_720_450_dreamstime_l_99978767.jpg");
        images2.add("https://olympic.ca/wp-content/uploads/2011/08/CP127536519.jpg?quality=100&w=1131");

        ArrayList<Integer> activities2 = new ArrayList<>();
        activities2.add(R.drawable.ride);
        activities2.add(R.drawable.kayak);

        ArrayList<Integer> services2 = new ArrayList<>();
        services2.add(R.drawable.toilet_solid);
        services2.add(R.drawable.wifi_solid);

        list.add(new LocationsModel(
                "Sailing", "Pakistan", "Karachi", getString(R.string.lorem), "free", "+123456789", "Camping",
                33.70411, 73.08887, images2, activities2, services2, new Date().getTime(), 3.0
        ));

        ArrayList<String> images3 = new ArrayList<>();
        images3.add("https://upload.wikimedia.org/wikipedia/commons/6/61/Black_Hawk_flying_over_a_valley_in_Bamyan.jpg");

        ArrayList<Integer> activities3 = new ArrayList<>();
        ArrayList<Integer> services3 = new ArrayList<>();
        services3.add(R.drawable.plug_circle_check_solid);
        services3.add(R.drawable.gas_pump_solid);
        services3.add(R.drawable.restroom_solid);

        list.add(new LocationsModel(
                "Bamyan", "Afghanistan", "Asian Highway 77", getString(R.string.lorem), "free", "+123456789", "Rest Area",
                33.70411, 73.08887, images3, activities3, services3, new Date().getTime(), 4.7
        ));
        return list;
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
