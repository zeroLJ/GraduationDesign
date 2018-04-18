package com.zero.voicenote;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;


import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zero.voicenote.database.DaoUtils;
import com.zero.voicenote.database.User;
import com.zero.voicenote.database.UserDao;
import com.zero.voicenote.util.Constant;
import com.zero.voicenote.util.LoginUtil;
import com.zero.voicenote.util.ShareUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zero.com.utillib.http.HttpUtils;
import zero.com.utillib.http.OnResponseListener;
import zero.com.utillib.http.ResultData;
import zero.com.utillib.utils.Logs;
import zero.com.utillib.utils.object.ObjUtils;
import zero.com.utillib.utils.object.StringUtils;
import zero.com.utillib.utils.view.Alert;

/**
 * 登陆界面
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
        String name = App.spUtils.getString(Constant.Name);
        if (!name.endsWith("_qq") && !name.endsWith("_sina") && !name.endsWith("_wx")){
            user_et.setText(App.spUtils.getString(Constant.Name));
            password_et.setText(App.spUtils.getString(Constant.Password));
        }

//        Intent intent = new Intent(this, MyService.class);
//        startService(intent);
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

        findViewById(R.id.qq_login).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                iUiListener = LoginUtil.LoginQQ(SigninActivity.this);
            }
        });

        findViewById(R.id.sina_login).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler = LoginUtil.LoginSina(SigninActivity.this);
            }
        });
    }


    //接收QQ授权结果
    private IUiListener iUiListener;
    //新浪授权接口实例，用于接收返回数据
    private SsoHandler mSsoHandler;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
            mSsoHandler = null;
        }
        if (iUiListener!= null){
            //此句很关键，不写此句则无法获取QQ授权后返回的信息
            Tencent.onActivityResultData(requestCode,resultCode,data,iUiListener);

            if (LoginUtil.getTencent()!=null){
                Logs.JLlog(LoginUtil.getTencent().getOpenId());
                UserInfo info = new UserInfo(this, LoginUtil.getTencent().getQQToken());
                info.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        final JSONObject json = (JSONObject)o;
                        try {
                            //tips 可以在此处用SharedPreferences把要用到的变量存起来
                            //还有qq等级等其他信息可以获取，按需要查看官方文档获取或打印json.toString查看
                            final String nickname = json.getString("nickname");//获取昵称
                            String sex = json.getString("gender");//获取性别
                            HttpUtils.USER = LoginUtil.getTencent().getOpenId() + "_qq";
                            HttpUtils.PASSWORD = LoginUtil.getTencent().getOpenId() + "_qq";
                            int length = HttpUtils.PASSWORD.length();
                            if (length > 16){
                                HttpUtils.PASSWORD = HttpUtils.PASSWORD.substring(length-16, length);
                            }
                            Map<String,Object> map = new HashMap<>();
                            map.put("nickname", nickname);
                            File file = new File(LoginUtil.getStorePath(SigninActivity.this));
                            if (file!=null){
                                map.put("file", file);
                            }
                            HttpUtils.doPostFile("SigninOther", map, new OnResponseListener() {
                                @Override
                                public void onSuccess(List<Map<String, Object>> data, ResultData resultData) {
                                    App.spUtils.put(Constant.IsLogin, true);
                                    App.spUtils.put(Constant.Name, HttpUtils.USER);
                                    App.spUtils.put(Constant.Password,  HttpUtils.PASSWORD);
                                    App.spUtils.put(Constant.Nickname,  nickname);
//                                    List<User> list = DaoUtils.query(User.class, UserDao.Properties.Name_qq.eq(HttpUtils.USER));
//                                    User user;
//                                    if (list.size() <= 0){
//                                        user = new User();
//                                        user.setName_qq(HttpUtils.USER);
//                                        user.setPassword(HttpUtils.PASSWORD);
//                                        user.setNickname(nickname);
//                                        DaoUtils.insert(user);
//                                    }
//                                    if (StringUtils.isNotEmpty(ObjUtils.objToStr(resultData.getResultMap().get("newUser")))){
//
//                                    }
//                                    DaoUtils.insert()
                                    Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
//                        Toast.makeText(activity,nickname+sex, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(UiError uiError) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
            iUiListener = null;
        }
    }

    private void signIn(final String user, final String password){
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
                App.spUtils.put(Constant.IsLogin, true);
                App.spUtils.put(Constant.Name, user);
                App.spUtils.put(Constant.Password, password);
                Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
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

