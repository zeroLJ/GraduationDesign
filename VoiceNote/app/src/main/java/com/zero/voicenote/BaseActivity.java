package com.zero.voicenote;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.yanzhenjie.sofia.Sofia;

import zero.com.utillib.Activity.BaseStatusAndToolBarActivity;
import zero.com.utillib.http.HttpUtils;
import zero.com.utillib.utils.Logs;
import zero.com.utillib.utils.object.StringUtils;

/**
 * Created by zero on 2018/2/8.
 */

public class BaseActivity extends BaseStatusAndToolBarActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initListener();
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        Sofia.with(this)
                .statusBarBackground(getResources().getColor(R.color.colorPrimaryDark))
                .statusBarLightFont();
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
