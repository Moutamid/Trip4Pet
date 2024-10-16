package com.moutamid.trip4pet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class Constants {
    public static final String DUMMY_IMAGE = "https://firebasestorage.googleapis.com/v0/b/multistreamz-10cba.appspot.com/o/trip4pet%2Fimages%2F01062024113321?alt=media&token=f2840b63-8f4d-48b8-a04a-db72ca4d9de9";
    public static Dialog dialog;
    public static final String STASH_USER = "STASH_USER";
    public static final String MODEL = "MODEL";
    public static final String EURO_LOW = "€";
    public static final String EURO_HIGH = "€€€";
    public static final String CITIES = "CITIES";
    public static final String FILTERS = "FILTERS";
    public static final String PLACES = "PLACES";
    public static final String ISVIP = "ISVIP";
    public static final String PLACE = "PLACE";
    public static final String VISITED = "VISITED";
    public static final String USER = "USER";
    public static final String COORDINATES = "COORDINATES";
    public static final String Metric = "Metric";
    public static final String Vehicle = "Vehicle";
    public static final String Imperial = "Imperial";
    public static final String AROUND_PLACE = "AROUND_PLACE";
    public static final String MEASURE = "MEASURE";
    public static final String EDIT = "EDIT";
    public static final int FAVORITE_SIZE = 200;
    public static final String LANGUAGE = "LANGUAGE";
    public static final String FAVORITE = "FAVORITE";
    public static final String FAVORITE_FOLDER = "FAVORITE_FOLDER";
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

    public static void setLocale(Context context, String lng) {
        Locale locale = new Locale(lng);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
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

//    public static ArrayList<FilterModel> getActivities(Context context) {
//        ArrayList<FilterModel> activities = new ArrayList<>();
//        activities.add(new FilterModel(context.getString(R.string.pet_play_area), R.drawable.ride));
//        activities.add(new FilterModel(context.getString(R.string.scenic_views), R.drawable.binoculars_solid));
//        activities.add(new FilterModel(context.getString(R.string.pet_friendly_swimming), R.drawable.person_swimming_solid));
//        activities.add(new FilterModel(context.getString(R.string.pet_friendly_climbing_sites), R.drawable.climbing_with_rope));
//        activities.add(new FilterModel(context.getString(R.string.pet_friendly_canoe_kayak_bases), R.drawable.kayak));
//        activities.add(new FilterModel(context.getString(R.string.pet_friendly_fishing_spots), R.drawable.fish_fins_solid));
//        activities.add(new FilterModel(context.getString(R.string.pet_friendly_beaches), R.drawable.umbrella_beach_solid));
//        activities.add(new FilterModel(context.getString(R.string.pet_friendly_hiking_trails), R.drawable.person_hiking_solid));
//        activities.add(new FilterModel(context.getString(R.string.pet_friendly_monuments), R.drawable.monument_solid));
//        activities.add(new FilterModel(context.getString(R.string.pet_friendly_mountain_bike_tracks), R.drawable.bicycle_solid));
//        activities.add(new FilterModel(context.getString(R.string.pet_friendly_windsurf_kite_surf_spots), R.drawable.kitesurf));
//        activities.add(new FilterModel(context.getString(R.string.scenic_motorcycle_rides), R.drawable.motorcycle_solid));
//        return activities;
//    }

    public static ArrayList<FilterModel> getServices(Context context) {
        ArrayList<FilterModel> service = new ArrayList<>();
        service.add(new FilterModel(0,context.getString(R.string.lifeguard_services), R.drawable.life_ring_solid));
        service.add(new FilterModel(1,context.getString(R.string.umbrellas_and_beach_chair_rentals), R.drawable.umbrella_beach_solid));
        service.add(new FilterModel(2,context.getString(R.string.restrooms_and_showers), R.drawable.shower_solid));
        service.add(new FilterModel(3,context.getString(R.string.coffee_and_espresso_drinks), R.drawable.mug_hot_solid));
        service.add(new FilterModel(4,context.getString(R.string.pastries_and_snacks), R.drawable.cake_candles_solid));
        service.add(new FilterModel(5,context.getString(R.string.wi_fi), R.drawable.wifi_solid));
        service.add(new FilterModel(6,context.getString(R.string.food_and_drinks), R.drawable.utensils_solid));
        service.add(new FilterModel(7,context.getString(R.string.dog_friendly_swimming_area), R.drawable.person_swimming_solid));
        service.add(new FilterModel(8,context.getString(R.string.places_for_dogs_to_run_and_play), R.drawable.dog_solid));
        service.add(new FilterModel(9,context.getString(R.string.dog_waste_bag_dispensers), R.drawable.dumpster_solid));
        service.add(new FilterModel(10,context.getString(R.string.dog_showers), R.drawable.soap_solid));
        service.add(new FilterModel(11,context.getString(R.string.fenced_in_area_for_dogs_to_run_and_play), R.drawable.xmarks_lines_solid));
        service.add(new FilterModel(12,context.getString(R.string.laundry_facilities), R.drawable.jug_detergent_solid));
        service.add(new FilterModel(13,context.getString(R.string.lodging), R.drawable.hotel_solid));
        service.add(new FilterModel(14,context.getString(R.string.detergent_and_fabric_softener_vending_machines), R.drawable.jug_detergent_solid));
        service.add(new FilterModel(15,context.getString(R.string.pet_pool), R.drawable.water_ladder_solid));
        service.add(new FilterModel(16,context.getString(R.string.private_garden_accessible_to_animals), R.drawable.pagelines));
        service.add(new FilterModel(17,context.getString(R.string.pet_grooming_service), R.drawable.paw_solid));
        service.add(new FilterModel(18,context.getString(R.string.pet_services), R.drawable.shield_dog_solid));
        service.add(new FilterModel(19,context.getString(R.string.education_course_for_animals), R.drawable.school_solid));
        service.add(new FilterModel(20,context.getString(R.string.veterinary_emergency_room), R.drawable.truck_medical_solid));
        service.add(new FilterModel(21,context.getString(R.string.pet_caregiver), R.drawable.hand_holding_hand_solid));
        service.add(new FilterModel(22,context.getString(R.string.animal_oncology), R.drawable.hospital_solid));
        service.add(new FilterModel(23,context.getString(R.string.animal_rehabilitation), R.drawable.stethoscope_solid));
        service.add(new FilterModel(24,context.getString(R.string.animal_neurology), R.drawable.brain_solid));
        service.add(new FilterModel(25,context.getString(R.string.agility_field), R.drawable.person_chalkboard_solid));
        service.add(new FilterModel(26,context.getString(R.string.other), R.drawable.otter_solid));

        return service;
    }

    public static int getServicesIcon(int place) {
        switch (place) {
            case 0:
                return R.drawable.life_ring_solid;
            case 1:
                return R.drawable.umbrella_beach_solid;
            case 2:
                return R.drawable.shower_solid;
            case 3:
                return R.drawable.mug_hot_solid;
            case 4:
                return R.drawable.cake_candles_solid;
            case 5:
                return R.drawable.wifi_solid;
            case 6:
                return R.drawable.utensils_solid;
            case 7:
                return R.drawable.person_swimming_solid;
            case 8:
                return R.drawable.dog_solid;
            case 9:
                return R.drawable.dumpster_solid;
            case 10:
                return R.drawable.soap_solid;
            case 11:
                return R.drawable.xmarks_lines_solid;
            case 12:
                return R.drawable.jug_detergent_solid;
            case 13:
                return R.drawable.hotel_solid;
            case 14:
                return R.drawable.jug_detergent_solid;
            case 15:
                return R.drawable.water_ladder_solid;
            case 16:
                return R.drawable.pagelines;
            case 17:
                return R.drawable.paw_solid;
            case 18:
                return R.drawable.shield_dog_solid;
            case 19:
                return R.drawable.school_solid;
            case 20:
                return R.drawable.truck_medical_solid;
            case 21:
                return R.drawable.hand_holding_hand_solid;
            case 22:
                return R.drawable.hospital_solid;
            case 23:
                return R.drawable.stethoscope_solid;
            case 24:
                return R.drawable.brain_solid;
            case 25:
                return R.drawable.person_chalkboard_solid;
            case 26:
                return R.drawable.otter_solid;
        }
        return 0;
    }

    public static int getTypeOfPlace(int place) {
        switch (place) {
            case 0:
                return R.drawable.spiaggia_senza_cartelli;
            case 1:
                return R.drawable.vet;
            case 2:
                return R.drawable.coffee_bar;
            case 3:
                return R.drawable.ristorante;
            case 4:
                return R.drawable.spiaggia_per_cani;
            case 5:
                return R.drawable.pet_store;
            case 6:
                return R.drawable.area_cani;
            case 7:
                return R.drawable.campeggio;
            case 8:
                return R.drawable.albergo;
            case 9:
                return R.drawable.lavanderia;
            case 10:
                return R.drawable.lavanderia_for_pet;
            case 11:
                return R.drawable.dog_salon_copia;
            case 12:
                return R.drawable.museum;
            case 13:
                return R.drawable.divieto;
        }
        return 0;
    }

    public static ArrayList<FilterModel> getTypeOfPlaces(Context context) {
        ArrayList<FilterModel> typeOfPlace = new ArrayList<>();
        typeOfPlace.add(new FilterModel(0, context.getString(R.string.beach_without_signs), R.drawable.spiaggia_senza_cartelli));
        typeOfPlace.add(new FilterModel(1,context.getString(R.string.veterinarian), R.drawable.vet));
        typeOfPlace.add(new FilterModel(2,context.getString(R.string.coffee_bar), R.drawable.coffee_bar));
        typeOfPlace.add(new FilterModel(3,context.getString(R.string.ristorante), R.drawable.ristorante));
        typeOfPlace.add(new FilterModel(4,context.getString(R.string.dog_beach), R.drawable.spiaggia_per_cani));
        typeOfPlace.add(new FilterModel(5,context.getString(R.string.pet_store), R.drawable.pet_store));
        typeOfPlace.add(new FilterModel(6,context.getString(R.string.dog_area), R.drawable.area_cani));
        typeOfPlace.add(new FilterModel(7,context.getString(R.string.camping), R.drawable.campeggio));
        typeOfPlace.add(new FilterModel(8,context.getString(R.string.albergo), R.drawable.albergo));
        typeOfPlace.add(new FilterModel(9,context.getString(R.string.laundry), R.drawable.lavanderia));
        typeOfPlace.add(new FilterModel(10,context.getString(R.string.laundry_for_pets), R.drawable.lavanderia_for_pet));
        typeOfPlace.add(new FilterModel(11,context.getString(R.string.dog_salon), R.drawable.dog_salon_copia));
        typeOfPlace.add(new FilterModel(12,context.getString(R.string.museum), R.drawable.museum));
        typeOfPlace.add(new FilterModel(13,context.getString(R.string.prohibition), R.drawable.divieto));
        return typeOfPlace;
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
        String appName = "trip4petUpdated_2";

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

    public static SpannableString spanText(String text) {
        String learnMoreText = "*";
        String combinedNameText = text + learnMoreText;
        SpannableString name = new SpannableString(combinedNameText);
        name.setSpan(new ForegroundColorSpan(Color.RED), text.length(), combinedNameText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return name;
    }
}
