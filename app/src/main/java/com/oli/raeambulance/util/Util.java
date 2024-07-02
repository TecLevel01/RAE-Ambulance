package com.oli.raeambulance.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oli.raeambulance.R;
import com.oli.raeambulance.object.User;

import java.util.Calendar;

public class Util {
    public static CollectionReference userRef = FirebaseFirestore.getInstance().collection(Strings.susers);
    public static CollectionReference requestRef = FirebaseFirestore.getInstance()
            .collection(Strings.srequests);
    public static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public static void SetSharedPrefs(Context context, String key, String o){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(key, o).apply();
    }

    public static String GetSharedPrefs(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, null);
    }

    public static Task<DocumentSnapshot> checkAccountType(String uid, Context context) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        return userRef.document(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User user = task.getResult().toObject(User.class);

                String s = null;
                if (user.getAccountType() == 0) {
                    s = "0";
                } else if (user.getAccountType() == 1) {
                    s = "1";
                } else if (user.getAccountType() == 2) {
                    s = "2";
                } else if (user.getAccountType() == 3) {
                    s = "3";
                }
                //setting of shared preference
                SetSharedPrefs(context, Strings.saccounttype, s);

                // save AmbNo
                if (user.getAmbNo() != null) {
                    SetSharedPrefs(context, Strings.sAmbNo, user.getAmbNo());
                }

            /*
                0- User
                1- Driver or Ambulance
                2- Admin
                3- Approved Driver
                */
            }
        });
    }

    public static void ActivityLoader(Context context, Class<?> aClass) {
        Intent intent = new Intent(context, aClass);
        context.startActivity(intent);

    }

    public static void backBtn(Activity activity){
        View view = activity.findViewById(R.id.backBtn);
        view.setOnClickListener(view1 -> {
            activity.finish();
        });
    }

    public static String getGreetings(Calendar c) {
        c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay < 12) {
            return "Morning";
        }
        if (timeOfDay < 16) {
            return "Afternoon";
        }
        if (timeOfDay < 21) {
            return "Evening";
        } else {
            return "Night";
        }
    }

}
