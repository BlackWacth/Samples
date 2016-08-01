package com.hua.openfire.auth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.hua.openfire.R;

public class CustomAuthActivity extends AppCompatActivity {

    private static final String tag = "hzw";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private String mCustomToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_auth);
    }
}
