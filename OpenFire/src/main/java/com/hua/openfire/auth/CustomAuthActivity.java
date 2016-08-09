package com.hua.openfire.auth;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hua.openfire.R;
import com.hua.openfire.base.BaseActivity;

public class CustomAuthActivity extends BaseActivity implements View.OnClickListener {

    private static final String tag = "CustomAuthActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private String mCustomToken;
    private TokenBroadcastReceiver mTokenReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_auth);
        findViewById(R.id.button_sign_in).setOnClickListener(this);

//        mTokenReceiver = new TokenBroadcastReceiver() {
//            @Override
//            public void onNewToken(String token) {
//                Log.i(tag, "taken = " + token);
//                setCustomToken(token);
//            }
//        };

        createToken();

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.i(tag, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.i(tag, "onAuthStateChanged:signed_out");
                }
                updateUI(user);
            }
        };
    }

    private void createToken() {
        String uid = "openfire-60df9";

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
        registerReceiver(mTokenReceiver, TokenBroadcastReceiver.getFilter());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuth != null && mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
        unregisterReceiver(mTokenReceiver);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            ((TextView) findViewById(R.id.text_sign_in_status)).setText("User ID: " + user.getUid());
        } else {
            ((TextView) findViewById(R.id.text_sign_in_status)).setText("Error: sign in failed.");
        }
    }

    private void setCustomToken(String token) {
        mCustomToken = token;
        String status;
        if (mCustomToken != null) {
            status = "Token : " + mCustomToken;
        } else {
            status = "Token : null";
        }
        findViewById(R.id.button_sign_in).setEnabled((mCustomToken != null));
        ((TextView) findViewById(R.id.text_token_status)).setText(status);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sign_in:
                startSignIn();
                break;
        }
    }

    private void startSignIn() {
        showProgressDialog();
        mAuth.signInWithCustomToken(mCustomToken)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i(tag, "signInWithCustomToken : onComplete:" + task.isSuccessful());
                        hideProgressDialog();
                        if (!task.isSuccessful()) {
                            Log.w(tag, "signInWithCustomToken", task.getException());
                            showToast("Authentication failed.");
                        }
                    }
                });
    }
}
