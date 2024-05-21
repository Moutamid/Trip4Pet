package com.moutamid.trip4pet.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.trip4pet.Constants;
import com.moutamid.trip4pet.models.Cities;
import com.moutamid.trip4pet.R;
import com.moutamid.trip4pet.activities.AddPlaceActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CitiesVH> implements Filterable {
    Activity context;
    ArrayList<Cities> list;
    ArrayList<Cities> currentItems;

    public CitiesAdapter(Activity context, ArrayList<Cities> list) {
        this.context = context;
        this.list = list;
        this.currentItems = new ArrayList<>(list.subList(0, 20));
    }

    @NonNull
    @Override
    public CitiesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CitiesVH(LayoutInflater.from(context).inflate(R.layout.cities, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesVH holder, int position) {
        Cities cities = currentItems.get(holder.getAdapterPosition());
        String name = cities.name + ", " + cities.state_name + ", " + cities.country_name;
        holder.name.setText(name);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddPlaceActivity.class);
            intent.putExtra("GIVEN", true);
            String COORDINATES = cities.latitude + ", " + cities.longitude;
            String PLACE = cities.name + ", " + cities.country_name;
            intent.putExtra(Constants.COORDINATES, COORDINATES);
            intent.putExtra(Constants.PLACE, PLACE);
            context.startActivity(intent);
            context.finish();
        });

    }

    @Override
    public int getItemCount() {
        return currentItems.size();
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Cities> filterList;
            if (constraint.toString().isEmpty()){
                filterList = new ArrayList<>(currentItems);
            } else {
                filterList = (ArrayList<Cities>) list.stream()
                        .filter(item -> item.name.toLowerCase().contains(constraint.toString().toLowerCase()))
                        .collect(Collectors.toList());
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            currentItems.clear();
            currentItems.addAll((Collection<? extends Cities>) results.values);
            notifyDataSetChanged();
        }
    };

    @SuppressLint("StaticFieldLeak")
    public void filter(final String query) {
        Constants.showDialog();
        new AsyncTask<Void, Void, ArrayList<Cities>>() {

            @Override
            protected ArrayList<Cities> doInBackground(Void... voids) {
                ArrayList<Cities> filterList = (ArrayList<Cities>) list.stream()
                        .filter(item -> item.name.toLowerCase().contains(query.toString().toLowerCase()))
                        .collect(Collectors.toList());
                return filterList;
            }

            @Override
            protected void onPostExecute(ArrayList<Cities> filterList) {
                Constants.dismissDialog();
                currentItems.clear();
                currentItems.addAll(filterList);
                notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class CitiesVH extends RecyclerView.ViewHolder {
        TextView name;

        public CitiesVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }

}
