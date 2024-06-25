package com.moutamid.trip4pet.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.moutamid.trip4pet.R;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {
    private Context context;
    private ArrayList<String> mSliderItems;

    public SliderAdapter(Context context, ArrayList<String> mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        return new SliderAdapterVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null));
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

        viewHolder.itemView.setOnClickListener(v -> {
            showImage();
        });

        Glide.with(context).load(mSliderItems.get(position)).into(viewHolder.imageViewBackground);
    }

    private void showImage() {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.slider_preview);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
        MaterialCardView back = dialog.findViewById(R.id.back);
        SliderView imageSlider = dialog.findViewById(R.id.imageSlider);
        back.setOnClickListener(v -> dialog.dismiss());
        PreviewSliderAdapter adapter = new PreviewSliderAdapter(context, mSliderItems);
        imageSlider.setSliderAdapter(adapter);
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
        }
    }

}
