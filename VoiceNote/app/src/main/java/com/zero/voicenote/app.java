package com.zero.voicenote;

import com.blankj.utilcode.util.SPUtils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.zero.voicenote.util.Constant;

import zero.com.utillib.Activity.MApplication;
import zero.com.utillib.http.HttpUtils;
import zero.com.utillib.utils.CrashHandler;

/**
 * Created by zero on 2018/2/8.
 */

public class App extends MApplication {
    public static SPUtils spUtils;
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance("com.zero.voicenote.SigninActivity").init(this);  //传入参数必须为Activity，否则无法回到登录页面。
        spUtils = SPUtils.getInstance();
        HttpUtils.URL = spUtils.getString(Constant.Url, HttpUtils.URL);
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5a8b9c86");
    }
}
