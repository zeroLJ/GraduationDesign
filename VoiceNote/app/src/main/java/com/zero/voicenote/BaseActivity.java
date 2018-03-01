package com.zero.voicenote;

import zero.com.utillib.Activity.BaseToolbarActivity;
import zero.com.utillib.http.HttpUtils;
import zero.com.utillib.utils.Logs;
import zero.com.utillib.utils.object.StringUtils;

/**
 * Created by zero on 2018/2/8.
 */

public class BaseActivity extends BaseToolbarActivity {
    @Override
    protected void onStart() {
        super.onStart();
        initListener();
    }


    protected void initListener(){

    }

    protected boolean hasSignin(){
        if (!StringUtils.isEmpty(HttpUtils.USER)){
            Logs.JLlog("true"+HttpUtils.USER);
            return true;
        }
        return false;
    }
}
