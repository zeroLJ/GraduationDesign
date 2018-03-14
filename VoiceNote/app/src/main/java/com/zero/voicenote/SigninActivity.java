package com.zero.voicenote;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;


import com.zero.voicenote.util.Constant;

import java.util.List;
import java.util.Map;

import zero.com.utillib.http.HttpUtils;
import zero.com.utillib.http.OnResponseListener;
import zero.com.utillib.http.ResultData;
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
        if (App.spUtils.getBoolean(Constant.IsLogin)){
            HttpUtils.USER = App.spUtils.getString(Constant.Name);
            HttpUtils.PASSWORD = App.spUtils.getString(Constant.Password);
            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        setTitle("登录");
        user_et = findViewById(R.id.user_et);
        password_et = findViewById(R.id.password_et);
        user_et.setText(App.spUtils.getString(Constant.Name));
        password_et.setText(App.spUtils.getString(Constant.Password));
//        Intent intent = new Intent(this, MyService.class);
//        startService(intent);
//        Intent intent2 = new Intent(this, MyService2.class);
//        startService(intent2);
    }

//    @Override
    protected void initListener() {
//        super.initListener();
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
                HttpUtils.USER  = Constant.Default_User;
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
        if (StringUtils.isEmpty(user)){
            Alert.toast("用户名不能为空！");
            return;
        }
        if (StringUtils.isEmpty(password)){
            Alert.toast("密码不能为空！");
            return;
        }
        showProgressDialog("登录中");
        HttpUtils.USER = user;
        HttpUtils.PASSWORD = password;
        HttpUtils.doPost("Signin", new OnResponseListener() {
            @Override
            public void onSuccess(List<Map<String, Object>> data, ResultData resultData) {
                Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                App.spUtils.put(Constant.IsLogin, true);
                App.spUtils.put(Constant.Name, user);
                App.spUtils.put(Constant.Password, password);
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
        requestPermissions();
    }


    private void requestPermissions(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                int permission = ActivityCompat.checkSelfPermission(this,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
//                if(permission!= PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(this,new String[]
//                            {Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                    Manifest.permission.LOCATION_HARDWARE,Manifest.permission.READ_PHONE_STATE,
//                                    Manifest.permission.WRITE_SETTINGS,Manifest.permission.READ_EXTERNAL_STORAGE,
//                                    Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_CONTACTS},0x0010);
//                }
//                if(permission != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(this,new String[] {
//                            Manifest.permission.ACCESS_COARSE_LOCATION,
//                            Manifest.permission.ACCESS_FINE_LOCATION},0x0010);
//                }

                ActivityCompat.requestPermissions(this,new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.LOCATION_HARDWARE,Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.WRITE_SETTINGS,Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_CONTACTS,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                 Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.CAMERA},0x0010);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i<permissions.length; i++ ){
            Logs.JLlog(permissions[i]+ "  " + grantResults[i]);
        }
    }
}

