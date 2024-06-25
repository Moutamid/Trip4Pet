package com.moutamid.trip4pet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moutamid.trip4pet.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class PreviewSliderAdapter extends SliderViewAdapter<PreviewSliderAdapter.SliderAdapterVH> {
    private Context context;
    private ArrayList<String> mSliderItems;

    public PreviewSliderAdapter(Context context, ArrayList<String> mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        return new SliderAdapterVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_image, null));
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        Glide.with(context).load(mSliderItems.get(position)).into(viewHolder.imageViewBackground);
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    class SliderAdapterVH extends ViewHolder {
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.image);
        }
    }

}
