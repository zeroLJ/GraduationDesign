package com.zero.voicenote;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.zero.voicenote.util.Constant;


import zero.com.utillib.http.HttpUtils;
import zero.com.utillib.http.OnResponseListener;
import zero.com.utillib.http.ResultData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zero.com.utillib.utils.Logs;
import zero.com.utillib.utils.object.StringUtils;
import zero.com.utillib.utils.view.Alert;

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
        setTitle("登录");
        user_et = findViewById(R.id.user_et);
        password_et = findViewById(R.id.password_et);
        user_et.setText(App.spUtils.getString(Constant.Name));
        password_et.setText(App.spUtils.getString(Constant.Password));
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

        findViewById(R.id.skip_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                HttpUtils.USER  = null;
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.set_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(SigninActivity.this);
                View view = View.inflate(SigninActivity.this, R.layout.dialog_set_url, null);
                final EditText url_et = view.findViewById(R.id.url_et);
                url_et.setText(HttpUtils.URL);
                builder.setView(view);
                builder.setTitle("请输入服务器地址");
                builder.setPositiveButton("确定",null);
                dialog = builder.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = url_et.getText().toString();
                        if (StringUtils.isWebLink(url)){
                            HttpUtils.URL = url;
                            App.spUtils.put(Constant.Url, url);
                            dialog.cancel();
                        }else {
                            Alert.toast("地址有误");
                        }
                    }
                });
            }
        });
    }

    private void signIn(final String user,final String password){
        showProgressDialog("登录中");
        Map<String,Object> map = new HashMap<>();
        map.put("name",user);
        map.put("password", password);
        HttpUtils.doPost("Signin", map, new OnResponseListener() {
            @Override
            public void onSuccess(List<Map<String, Object>> data, ResultData resultData) {
                Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                App.spUtils.put(Constant.Name, user);
                App.spUtils.put(Constant.Password, password);
                HttpUtils.USER = user;
                HttpUtils.PASSWORD = password;
            }

            @Override
            public void OnFinal() {
                super.OnFinal();
                dismissProgressDialog();
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

