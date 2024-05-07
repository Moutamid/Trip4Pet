package com.moutamid.trip4pet;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    public static final String MODEL = "MODEL";
    public static final String COORDINATES = "COORDINATES";
    public static FirebaseAuth auth() {
        return FirebaseAuth.getInstance();
    }

    public static DatabaseReference databaseReference() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("ChefDarbariApp");
        db.keepSynced(true);
        return db;
    }

    public static ArrayList<LocationsModel> getList(Context context) {
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

        ArrayList<Integer> activities2 = new ArrayList<>();
        activities2.add(R.drawable.ride);
        activities2.add(R.drawable.kayak);

        ArrayList<Integer> services2 = new ArrayList<>();
        services2.add(R.drawable.toilet_solid);
        services2.add(R.drawable.wifi_solid);

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

        ArrayList<Integer> activities3 = new ArrayList<>();
        ArrayList<Integer> services3 = new ArrayList<>();
        services3.add(R.drawable.plug_circle_check_solid);
        services3.add(R.drawable.gas_pump_solid);
        services3.add(R.drawable.restroom_solid);

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
