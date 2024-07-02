package com.oli.raeambulance.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.oli.raeambulance.R;
import com.oli.raeambulance.util.Strings;
import com.oli.raeambulance.util.Util;

public class Login extends AppCompatActivity {
    public static Activity mActivity;
    EditText etEmail, etPw;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mActivity = this;
        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPw = findViewById(R.id.etPwrd);
    }

    public void LoginAction(View view) {

        String mEmail = etEmail.getText().toString().trim();
        String mPw = etPw.getText().toString().trim();

        if (!mEmail.isEmpty()) {
            if (!mPw.isEmpty()) {

                final ProgressDialog dialog = ProgressDialog.show(this, "",
                        "Logging In");
                dialog.show();

                mAuth.signInWithEmailAndPassword(mEmail, mPw).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Util.checkAccountType(mAuth.getUid(), mActivity).addOnCompleteListener(task1 -> {
                            if (task1.isComplete()) {
                                Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
                                Util.ActivityLoader(this, Navigation.class);
                                finish();
                            }
                        });
                    } else {
                        String msg = task.getException().getMessage();
                        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();

                });

            } else {
                etPw.requestFocus();
                etPw.setError(Strings.passError);
            }
        } else {
            etEmail.requestFocus();
            etEmail.setError(Strings.emailError);
        }
    }

    public void PasswordReset(View view) {
    }

    public void Register(View view) {
        Util.ActivityLoader(this, Register.class);
    }
}