package com.zero.voicenote;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.blankj.utilcode.util.ActivityUtils;
import com.zero.voicenote.http.HttpUtils;
import com.zero.voicenote.http.OnResponseListener;
import com.zero.voicenote.http.ResultData;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import zero.com.utillib.utils.Logs;

/**
 * A login screen that offers login via email/password.
 */
public class SigninActivity extends BaseActivity {
    private EditText password_et;
    private AutoCompleteTextView user_et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        user_et = findViewById(R.id.user_et);
        password_et = findViewById(R.id.password_et);
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.signin_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Logs.JLlog("onclick");
                signIn(user_et.getText().toString(), password_et.getText().toString());
            }
        });

        findViewById(R.id.login_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signIn(String user, String password){
        Map<String,Object> map = new HashMap<>();
        map.put("name",user);
        map.put("password", password);
        HttpUtils.doPost("Signin", map, new OnResponseListener() {
            @Override
            public void onSuccess(List<Map<String, Object>> data, ResultData resultData) {
                Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET
        };
//        ActivityCompat.requestPermissions(this, permissions, 1000);
        Logs.JLlog("start");
//        PermissionUtils.requestPermissions(ActivityUtils.getTopActivity(), 1000, permissions, new PermissionUtils.OnPermissionListener() {
//            @Override
//            public void onPermissionGranted() {
//
//            }
//
//            @Override
//            public void onPermissionDenied(String[] deniedPermissions) {
//                for (String s : deniedPermissions){
//                    Logs.JLlog(s);
//                }
//            }
//        });
    }
}

