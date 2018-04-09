package com.zero.voicenote;

import android.content.pm.ConfigurationInfo;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.zero.voicenote.util.Constant;

import zero.com.utillib.Activity.BaseApplication;
import zero.com.utillib.Activity.MApplication;
import zero.com.utillib.http.HttpUtils;
import zero.com.utillib.utils.CrashHandler;
import zero.com.utillib.utils.Logs;

/**
 * Created by zero on 2018/2/8.
 */

public class App extends BaseApplication {
    public static SPUtils spUtils;
    @Override
    public void onCreate() {
        super.onCreate();
//        CrashHandler.getInstance("com.zero.voicenote.SigninActivity").init(this);  //传入参数必须为Activity，否则无法回到登录页面。
        CrashHandler.getInstance(SigninActivity.class).init(this);
        spUtils = SPUtils.getInstance();
        HttpUtils.URL = spUtils.getString(Constant.Url, HttpUtils.URL);
        Beta.autoCheckUpgrade = false;
        Bugly.init(getApplicationContext(), "f605fa12cd", false);
//        CrashReport.closeCrashReport();
    }
}
