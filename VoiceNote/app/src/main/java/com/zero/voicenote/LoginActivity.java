package com.zero.voicenote;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;


import zero.com.utillib.http.HttpUtils;
import zero.com.utillib.http.OnResponseListener;
import zero.com.utillib.http.ResultData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zero.com.utillib.utils.view.Alert;

public class LoginActivity extends BaseActivity {
    private EditText password_et, check_password_et;
    private AutoCompleteTextView user_et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setReturnEnable(true);
        user_et = findViewById(R.id.user_et);
        password_et = findViewById(R.id.password_et);
        check_password_et = findViewById(R.id.check_password_et);
    }

    @Override
    protected void initListener() {
        super.initListener();
        findViewById(R.id.login_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                login(user_et.getText().toString().trim(), password_et.getText().toString().trim(), check_password_et.getText().toString().trim());
            }
        });
    }

    private void login(String user, String password, String checkPassword){
        if (!password.equals(checkPassword)){
            Alert.toast("两次输入的密码不一致！");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("name", user);
        map.put("password", password);
        HttpUtils.doPost("Login", map, new OnResponseListener() {
            @Override
            public void onSuccess(List<Map<String, Object>> data, ResultData resultData) {

            }
        });
    }
}

