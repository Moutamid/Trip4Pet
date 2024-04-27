package com.moutamid.trip4pet.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.activities.AccountActivity;
import com.moutamid.trip4pet.activities.AddPlaceActivity;
import com.moutamid.trip4pet.activities.SettingActivity;
import com.moutamid.trip4pet.databinding.FragmentSettingBinding;


public class SettingFragment extends Fragment {
    FragmentSettingBinding binding;
    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(getLayoutInflater(), container, false);

        binding.setting.setOnClickListener(v -> startActivity(new Intent(requireContext(), SettingActivity.class)));
        binding.account.setOnClickListener(v -> startActivity(new Intent(requireContext(), AccountActivity.class)));
        binding.add.setOnClickListener(v -> startActivity(new Intent(requireContext(), AddPlaceActivity.class)));

        return binding.getRoot();
    }
}