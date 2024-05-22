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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
    public static final String CITIES = "CITIES";
    public static final String FILTERS = "FILTERS";
    public static final String ISVIP = "ISVIP";
    public static final String PLACE = "PLACE";
    public static final String VISITED = "VISITED";
    public static final String USER = "USER";
    public static final String COORDINATES = "COORDINATES";
    public static final String Metric = "Metric";
    public static final String Vehicle = "Vehicle";
    public static final String Imperial = "Imperial";
    public static final String MEASURE = "MEASURE";
    public static final int FAVORITE_SIZE = 200;
    public static final String LICENSE_KEY = "";
    public static final String VIP_MONTH = "vip.month.com.moutamid.trip4pet";
    public static final String VIP_YEAR = "vip.year.com.moutamid.trip4pet";

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
    public static StorageReference storageReference() {
        return FirebaseStorage.getInstance().getReference().child("trip4pet");
    }

    public static ArrayList<FilterModel> getActivities(Context context) {
        ArrayList<FilterModel> activities = new ArrayList<>();
        activities.add(new FilterModel(context.getString(R.string.pet_play_area), R.drawable.ride));
        activities.add(new FilterModel(context.getString(R.string.scenic_views), R.drawable.binoculars_solid));
        activities.add(new FilterModel(context.getString(R.string.pet_friendly_swimming), R.drawable.person_swimming_solid));
        activities.add(new FilterModel(context.getString(R.string.pet_friendly_climbing_sites), R.drawable.climbing_with_rope));
        activities.add(new FilterModel(context.getString(R.string.pet_friendly_canoe_kayak_bases), R.drawable.kayak));
        activities.add(new FilterModel(context.getString(R.string.pet_friendly_fishing_spots), R.drawable.fish_fins_solid));
        activities.add(new FilterModel(context.getString(R.string.pet_friendly_beaches), R.drawable.umbrella_beach_solid));
        activities.add(new FilterModel(context.getString(R.string.pet_friendly_hiking_trails), R.drawable.person_hiking_solid));
        activities.add(new FilterModel(context.getString(R.string.pet_friendly_monuments), R.drawable.monument_solid));
        activities.add(new FilterModel(context.getString(R.string.pet_friendly_mountain_bike_tracks), R.drawable.bicycle_solid));
        activities.add(new FilterModel(context.getString(R.string.pet_friendly_windsurf_kite_surf_spots), R.drawable.kitesurf));
        activities.add(new FilterModel(context.getString(R.string.scenic_motorcycle_rides), R.drawable.motorcycle_solid));
        return activities;
    }

    public static ArrayList<FilterModel> getServices(Context context) {
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
