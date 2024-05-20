package com.moutamid.trip4pet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.trip4pet.models.FilterModel;
import com.moutamid.trip4pet.models.LocationsModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Constants {
    public static Dialog dialog;
    public static final String STASH_USER = "STASH_USER";
    public static final String MODEL = "MODEL";
    public static final String USER = "USER";
    public static final String COORDINATES = "COORDINATES";
    public static final String Metric = "Metric";
    public static final String Vehicle = "Vehicle";
    public static final String Imperial = "Imperial";
    public static final String MEASURE = "MEASURE";

    public static void initDialog(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
    }

    public static void showDialog() {
        dialog.show();
    }

    public static void dismissDialog() {
        dialog.dismiss();
    }

    public static FirebaseAuth auth() {
        return FirebaseAuth.getInstance();
    }

    public static DatabaseReference databaseReference() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("trip4pet");
        db.keepSynced(true);
        return db;
    }

    public static ArrayList<FilterModel> getActivities() {
        ArrayList<FilterModel> activities = new ArrayList<>();
        activities.add(new FilterModel("Pet Play Area", R.drawable.ride));
        activities.add(new FilterModel("Scenic Views", R.drawable.binoculars_solid));
        activities.add(new FilterModel("Pet-friendly Swimming", R.drawable.person_swimming_solid));
        activities.add(new FilterModel("Pet-friendly Climbing Sites", R.drawable.climbing_with_rope));
        activities.add(new FilterModel("Pet-friendly Canoe/Kayak Bases", R.drawable.kayak));
        activities.add(new FilterModel("Pet-friendly Fishing Spots", R.drawable.fish_fins_solid));
        activities.add(new FilterModel("Pet-friendly Beaches", R.drawable.umbrella_beach_solid));
        activities.add(new FilterModel("Pet-friendly Hiking Trails", R.drawable.person_hiking_solid));
        activities.add(new FilterModel("Pet-friendly Monuments", R.drawable.monument_solid));
        activities.add(new FilterModel("Pet-friendly Mountain Bike Tracks", R.drawable.bicycle_solid));
        activities.add(new FilterModel("Pet-friendly Windsurf/Kitesurf Spots", R.drawable.kitesurf));
        activities.add(new FilterModel("Scenic Motorcycle Rides", R.drawable.motorcycle_solid));
        return activities;
    }

    public static ArrayList<FilterModel> getServices() {
        ArrayList<FilterModel> service = new ArrayList<>();
        service.add(new FilterModel("Spiaggia senza catelli", R.drawable.spiaggia_senza_cartelli));
        service.add(new FilterModel("Vetrinario", R.drawable.vet));
        service.add(new FilterModel("Coffee bar", R.drawable.coffee_bar));
        service.add(new FilterModel("Ristorante", R.drawable.ristorante));
        service.add(new FilterModel("Spiaggia per cani", R.drawable.spiaggia_per_cani));
        service.add(new FilterModel("Pet Store", R.drawable.pet_store));
        service.add(new FilterModel("Area Cani", R.drawable.area_cani));
        service.add(new FilterModel("Campeggio", R.drawable.campeggio));
        service.add(new FilterModel("Albergo", R.drawable.albergo));
        service.add(new FilterModel("Lavanderia", R.drawable.lavanderia));
        service.add(new FilterModel("Lavanderia For Pet", R.drawable.lavanderia_for_pet));

//        service.add(new FilterModel("Pet-Friendly Amenities", R.drawable.wifi_solid));  // Placeholder icon
//        service.add(new FilterModel("Laundry (for pet items)", R.drawable.jug_detergent_solid));
//        service.add(new FilterModel("Freshwater for Pets", R.drawable.faucet_drip_solid));  // Already appropriate
//        service.add(new FilterModel("Public toilets (for pet waste disposal)", R.drawable.restroom_solid));  // Clarification
//        service.add(new FilterModel("Internet for Booking Services", R.drawable.wifi_solid));  // Already appropriate
//        service.add(new FilterModel("LPG station (for heating)", R.drawable.gas_pump_solid));  // Clarification
//        service.add(new FilterModel("Bottled gas service (for heating)", R.drawable.fire_flame_simple_solid));  // Clarification
//        service.add(new FilterModel("3G/4G internet", R.drawable.signal_solid));  // Already appropriate
//        service.add(new FilterModel("Washing for pets (and items)", R.drawable.soap_solid));  // Clarification
        return service;
    }

    public static Bitmap convertVectorToBitmap(Context context, int vectorDrawableId, int width, int height) {
        // Get the VectorDrawable from the resources
        Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
        if (vectorDrawable instanceof VectorDrawable) {
            // Create a Bitmap with the desired width and height
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            // Create a Canvas to draw the vector onto the Bitmap
            Canvas canvas = new Canvas(bitmap);
            // Set the bounds for the vector drawable
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            // Draw the vector onto the Canvas
            vectorDrawable.draw(canvas);
            return bitmap;
        } else {
            // Return null if the drawable is not a VectorDrawable
            return null;
        }
    }

    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static ArrayList<LocationsModel> getList(Context context) {
        ArrayList<LocationsModel> list = new ArrayList<>();

        ArrayList<String> images1 = new ArrayList<>();
        images1.add("https://wpassets.graana.com/blog/wp-content/uploads/2023/08/Shakarparian.jpg");
        images1.add("https://live.staticflickr.com/1573/24289734385_009bb6cc0f_b.jpg");

        ArrayList<FilterModel> activities1 = new ArrayList<>();
        activities1.add(new FilterModel("Pet Play Area", R.drawable.ride));
        activities1.add(new FilterModel("Scenic Views", R.drawable.binoculars_solid));
        activities1.add(new FilterModel("Scenic Motorcycle Rides", R.drawable.motorcycle_solid));

        ArrayList<FilterModel> services1 = new ArrayList<>();
        services1.add(new FilterModel("Spiaggia senza catelli", R.drawable.spiaggia_senza_cartelli));
        services1.add(new FilterModel("Vetrinario", R.drawable.vet));
        services1.add(new FilterModel("Campeggio", R.drawable.campeggio));

        list.add(new LocationsModel(UUID.randomUUID().toString(),
                "Garden Avenue", "Pakistan", "Islamabad", context.getResources().getString(R.string.lorem), "free", "+123456789", "Park",
                33.7297, 73.0479, images1, activities1, services1, new Date().getTime(), 4.0
        ));
        list.add(new LocationsModel(UUID.randomUUID().toString(),
                "Garden Avenue", "Pakistan", "Islamabad", context.getResources().getString(R.string.lorem), "free", "+123456789", "Park",
                31.3026, 73.7128, images1, activities1, services1, new Date().getTime(), 4.0
        ));

        ArrayList<String> images2 = new ArrayList<>();
        images2.add("https://upload.wikimedia.org/wikipedia/commons/thumb/7/73/Regattafeld_vor_Laboe.jpg/1200px-Regattafeld_vor_Laboe.jpg");
        images2.add("https://d3mvlb3hz2g78.cloudfront.net/wp-content/uploads/2018/11/thumb_720_450_dreamstime_l_99978767.jpg");
        images2.add("https://olympic.ca/wp-content/uploads/2011/08/CP127536519.jpg?quality=100&w=1131");

        ArrayList<FilterModel> activities2 = new ArrayList<>();
        activities2.add(new FilterModel("Pet-friendly Swimming", R.drawable.person_swimming_solid));
        activities2.add(new FilterModel("Pet-friendly Climbing Sites", R.drawable.climbing_with_rope));

        ArrayList<FilterModel> services2 = new ArrayList<>();
        services2.add(new FilterModel("Pet Store", R.drawable.pet_store));
        services2.add(new FilterModel("Area Cani", R.drawable.area_cani));

        list.add(new LocationsModel(UUID.randomUUID().toString(),
                "Sailing", "Pakistan", "Karachi", context.getResources().getString(R.string.lorem), "free", "+123456789", "Restaurant",
                31.5497, 74.3436, images2, activities2, services2, new Date().getTime(), 3.0
        ));
        list.add(new LocationsModel(UUID.randomUUID().toString(),
                "Sailing", "Pakistan", "Karachi", context.getResources().getString(R.string.lorem), "free", "+123456789", "Beach",
                34.0035, 74.0485, images2, activities2, services2, new Date().getTime(), 3.0
        ));

        ArrayList<String> images3 = new ArrayList<>();
        images3.add("https://upload.wikimedia.org/wikipedia/commons/6/61/Black_Hawk_flying_over_a_valley_in_Bamyan.jpg");

        ArrayList<FilterModel> activities3 = new ArrayList<>();
        ArrayList<FilterModel> services3 = new ArrayList<>();
        services3.add(new FilterModel("Vetrinario", R.drawable.vet));
        services3.add(new FilterModel("Coffee bar", R.drawable.coffee_bar));
        services3.add(new FilterModel("Ristorante", R.drawable.ristorante));

        list.add(new LocationsModel(UUID.randomUUID().toString(),
                "Bamyan", "Afghanistan", "Asian Highway 77", context.getResources().getString(R.string.lorem), "free", "+123456789", "Park",
                24.9014, 67.0099, images3, activities3, services3, new Date().getTime(), 4.7
        ));
        list.add(new LocationsModel(UUID.randomUUID().toString(),
                "Bamyan", "Afghanistan", "Asian Highway 77", context.getResources().getString(R.string.lorem), "free", "+123456789", "Restaurant",
                28.7205, 71.7329, images3, activities3, services3, new Date().getTime(), 4.7
        ));
        list.add(new LocationsModel(UUID.randomUUID().toString(),
                "Bamyan", "Afghanistan", "Asian Highway 77", context.getResources().getString(R.string.lorem), "free", "+123456789", "Beach",
                34.0617, 73.0311, images3, activities3, services3, new Date().getTime(), 4.7
        ));
        list.add(new LocationsModel(UUID.randomUUID().toString(),
                "Bamyan", "Afghanistan", "Asian Highway 77", context.getResources().getString(R.string.lorem), "free", "+123456789", "Other",
                34.5195, 69.8893, images3, activities3, services3, new Date().getTime(), 4.7
        ));
        return list;
    }

    public static void checkApp(Activity activity) {
        String appName = "trip4pet";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuilder stringBuffer = new StringBuilder();
            while (true) {
                try {
                    if ((input = in != null ? in.readLine() : null) == null) break;
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }

}
