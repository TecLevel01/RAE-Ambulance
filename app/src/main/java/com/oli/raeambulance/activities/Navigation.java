package com.oli.raeambulance.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.oli.raeambulance.R;
import com.oli.raeambulance.util.Strings;
import com.oli.raeambulance.util.Util;

import java.util.Calendar;

public class Navigation extends AppCompatActivity {
    public static Activity MActivity;
    private FirebaseUser user;
    private TextView tvGrt, tvUname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        MActivity = this;
        user = FirebaseAuth.getInstance().getCurrentUser();
        tvGrt = findViewById(R.id.tvGreeting);
        tvUname = findViewById(R.id.tvUname);

        getUserData();
    }

    private void getUserData() {
        if (user != null) {
            if (user.getDisplayName() != null) {
                tvUname.setText(String.format("%s%s", user.getDisplayName(), Strings.hello_emoji));
            }
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setGreeting();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Util.ActivityLoader(this, Login.class);
            finish();
        } else {
            user.reload();
        }
    }

    public void setGreeting() {
        if (user != null) {
            tvGrt.setText(String.format("Good %s", Util.getGreetings(Calendar.getInstance())));
        }
    }

    public void openProfile(View view) {
        Util.ActivityLoader(this, profile.class);
    }
}