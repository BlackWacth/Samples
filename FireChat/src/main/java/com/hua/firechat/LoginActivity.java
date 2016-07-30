package com.hua.firechat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String tag = "hzw";
    private TextInputEditText mEmail, mPassword;
    private Button mLogin;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String userUID;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (TextInputEditText) findViewById(R.id.et_email_edit_text);
        mPassword = (TextInputEditText) findViewById(R.id.et_pwd_edit_text);
        mLogin = (Button) findViewById(R.id.btn_login);
        mLogin.setOnClickListener(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Log.i(tag, "登录 ： " + user.getUid());
                    userUID = user.getUid();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else {
                    Log.i(tag, "登出");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onClick(View v) {
        login();
    }

    private void login() {
        mEmail.setError(null);
        mPassword.setError(null);

        final String email = mEmail.getText().toString();
        final String pwd = mPassword.getText().toString();

        if(TextUtils.isEmpty(email)) {
            mEmail.setError("email不能为空");
            mEmail.requestFocus();
            return ;
        }

        if(TextUtils.isEmpty(pwd)) {
            mPassword.setError("密码不能为空");
            mPassword.requestFocus();
            return ;
        }

        showProgress("正在登陆…………");
        mFirebaseAuth.signInWithEmailAndPassword(email, pwd)
                     .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             cancel();
                             if(task.isSuccessful()) {
                                 showText("登录成功");
                             } else {
                                 new AlertDialog.Builder(LoginActivity.this)
                                         .setTitle("登陆失败")
                                         .setMessage("无此账号， 是否注册")
                                         .setPositiveButton("注册", new DialogInterface.OnClickListener() {
                                             @Override
                                             public void onClick(DialogInterface dialog, int which) {
                                                 showProgress("正在注册…………");
                                                 register(email, pwd);
                                             }
                                         })
                                         .setNegativeButton("取消", null)
                                         .show();
                             }
                         }
                     });
    }

    private void register(String email, String pwd) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        cancel();
                        String msg = task.isSuccessful() ? "注册成功" : "注册失败";
                        showText(msg);
                    }
                });
    }

    private void showText(String text) {
        Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private void showProgress(String msg) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(msg);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private void cancel() {
        if(mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }
}
