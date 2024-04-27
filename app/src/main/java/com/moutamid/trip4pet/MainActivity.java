package com.moutamid.trip4pet;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.moutamid.trip4pet.databinding.ActivityMainBinding;
import com.moutamid.trip4pet.fragments.AroundMeFragment;
import com.moutamid.trip4pet.fragments.SettingFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        // Optionally, you can set icons for the tabs
        binding.tabLayout.getTabAt(0).setIcon(R.drawable.compass);
        binding.tabLayout.getTabAt(1).setIcon(R.drawable.bars);
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AroundMeFragment(); // Replace with your MapFragment
                case 1:
                    return new SettingFragment(); // Replace with your MenuFragment
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2; // Number of tabs
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Return tab titles here if needed
            return null;
        }
    }

}