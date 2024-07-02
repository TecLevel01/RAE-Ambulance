package com.oli.raeambulance.activities;

import static com.oli.raeambulance.util.Strings.saccounttype;
import static com.oli.raeambulance.util.Strings.stimestamp;
import static com.oli.raeambulance.util.Strings.susers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oli.raeambulance.R;
import com.oli.raeambulance.util.Strings;
import com.oli.raeambulance.util.Util;

import java.util.Date;
import java.util.HashMap;

public class Register extends AppCompatActivity {
    String p = Strings.spatient, a = Strings.sambulance;
    private EditText etEmail, etPw, etUname, etPhone, etAmbNo, etHospital;
    private View regView2;
    private FirebaseFirestore db;
    private TextView tvTitle, regAsTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        regView2 = findViewById(R.id.regView2);
        regView2.setVisibility(View.GONE);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(String.format("%s %s", p, tvTitle.getText()));

        regAsTv = findViewById(R.id.regAsBtn);
        regAsTv.setOnClickListener(view -> {
            String s;
            if (regView2.getVisibility() == View.GONE) {
                regView2.setVisibility(View.VISIBLE);
                regAsTv.setText(regAsTv.getText().toString().replace(a, p));
                s = tvTitle.getText().toString().replace(p, a);

            } else {
                regView2.setVisibility(View.GONE);
                regAsTv.setText(getString(R.string.register_as_ambulance));
                s = tvTitle.getText().toString().replace(a, p);
            }
            tvTitle.setText(s);
        });
    }

    public void SignUp(View view) {
        etEmail = findViewById(R.id.etEmail);
        etPw = findViewById(R.id.etPwrd);
        etUname = findViewById(R.id.etUname);
        etPhone = findViewById(R.id.etPhone);
        etAmbNo = findViewById(R.id.etAmbNo);
        etHospital = findViewById(R.id.etHospital);

        String sEmail, sPw, sUname, sPhone, sAmbNo, sHospital;

        sEmail = etEmail.getText().toString().trim();
        sPw = etPw.getText().toString().trim();
        sUname = etUname.getText().toString().trim();
        sPhone = etPhone.getText().toString().trim();
        sAmbNo = etAmbNo.getText().toString().trim();
        sHospital = etHospital.getText().toString().trim();

        FirebaseAuth auth = FirebaseAuth.getInstance();


        if (!sEmail.isEmpty()) {
            if (!sPw.isEmpty()) {
                final ProgressDialog dialog = ProgressDialog.show(this, "",
                        "Logging in...",
                        true);
                dialog.show();
                auth.createUserWithEmailAndPassword(sEmail, sPw).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        db = FirebaseFirestore.getInstance();
                        HashMap<String, Object> userInfo = new HashMap<>();
                        userInfo.put("email", user.getEmail());
                        userInfo.put("Uname", sUname);
                        userInfo.put("phone", sPhone);
                        int aType = 0;

                        //Registering Ambulance
                        if (regView2.getVisibility() == View.VISIBLE) {
                            userInfo.put("hospital", sHospital);
                            userInfo.put("ambNo", sAmbNo);
                            userInfo.put(stimestamp, new Date());
                            aType = 1;
                        }
                        userInfo.put(saccounttype, aType);
                        Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show();

                        db.collection(susers).document(user.getUid())
                                .set(userInfo).addOnSuccessListener(unused -> {
                                    UserProfileChangeRequest userProfileUpdating = new UserProfileChangeRequest
                                            .Builder().setDisplayName(sUname).build();
                                    user.updateProfile(userProfileUpdating);
                                    Util.checkAccountType(auth.getUid(), this).addOnCompleteListener(task1 -> {
                                        Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
                                        Util.ActivityLoader(this, Navigation.class);
                                        Login.mActivity.finish();
                                    });
                                });


                    } else {
                        String msg = task.getException().getMessage();
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                });
            } else {
                etPw.requestFocus();
            }
        } else {
            etEmail.requestFocus();
        }
    }
}