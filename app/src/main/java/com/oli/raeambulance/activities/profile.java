package com.oli.raeambulance.activities;

import static com.oli.raeambulance.util.Strings.susers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oli.raeambulance.R;
import com.oli.raeambulance.object.User;
import com.oli.raeambulance.util.Util;

import java.util.HashMap;

public class profile extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseFirestore db;
    private DocumentReference userDetail;
    private EditText etEmail, etUname, etPhone, etAmbNo, etHospital;
    private View ambView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mInit();
        Util.backBtn(this);
        getUserData();
    }


    private void mInit() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        userDetail = db.collection(susers).document(user.getUid());
        etEmail = findViewById(R.id.etEmail);
        etUname = findViewById(R.id.etUname);
        etPhone = findViewById(R.id.etPhone);
        ambView = findViewById(R.id.AmbView);
        etAmbNo = findViewById(R.id.etAmbNo);
        etHospital = findViewById(R.id.etHospital);
        hideView(0);


    }

    private void hideView(int i) {
        if (i == 0) {
            ambView.setVisibility(View.GONE);
        } else {
            ambView.setVisibility(View.VISIBLE);
        }
    }

    private void getUserData() {
        if (user != null) {
            userDetail.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    User user = task.getResult().toObject(User.class);
                    if (user != null) {
                        etEmail.setText(user.getEmail());
                        etPhone.setText(user.getPhone());
                        etUname.setText(user.getUname());
                        etAmbNo.setText(user.getAmbNo());
                        etHospital.setText(user.getHospital());
                        if (user.getAmbNo() != null) {
                            hideView(1);
                        }

                    }
                }
            });
        }
    }

    public void UpdateProfile(View view) {
        final ProgressDialog progress = ProgressDialog.show(this,
                "", "Updating...", true);
        progress.show();
        String uname = etUname.getText().toString().trim();
        String uphone = etPhone.getText().toString().trim();
        HashMap<String, Object> newUseData = new HashMap<>();

        newUseData.put("uname", uname);
        newUseData.put("phone", uphone);
        userDetail.update(newUseData).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
            }else {
                String msg = task.getException().getMessage();
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
            progress.dismiss();
        });

    }

    public void SignOut(View view) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                "Do you want to logout?", Snackbar.LENGTH_SHORT);
        snackbar.setAction("Yes", view1 -> {
            FirebaseAuth.getInstance().signOut();
            Util.ActivityLoader(this, Login.class);
            finish();
            Navigation.MActivity.finish();
        });
        snackbar.show();
    }
}
