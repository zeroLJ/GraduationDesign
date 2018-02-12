package com.zero.voicenote;
import zero.com.utillib.Activity.MApplication;
import zero.com.utillib.utils.CrashHandler;

/**
 * Created by zero on 2018/2/8.
 */

public class app extends MApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance("com.zero.voicenote.SigninActivity").init(this);  //传入参数必须为Activity，否则无法回到登录页面。
    }
}
