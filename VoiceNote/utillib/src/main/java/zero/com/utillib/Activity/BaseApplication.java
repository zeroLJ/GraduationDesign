package zero.com.utillib.Activity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import zero.com.utillib.R;
import zero.com.utillib.utils.CacheValue;
import zero.com.utillib.utils.Logs;
import zero.com.utillib.utils.view.Alert;

//删除activity管理相关方法，使用ActivityUtils里的方法
public class BaseApplication extends Application {

    /**
     * 用于保存暂时的数据，程序退出时会清空
     */
    private static Map<String, Object> activityParams = new HashMap<>();

    public static Object getActivityParams(String key) {
        if (activityParams.size() <= 0) {
            activityParams = (Map<String, Object>) JSON.parse(CacheValue.getCacheStringValue("activityParams", "{}"));
        }
        return activityParams.get(key);
    }

    public static void setActivityParams(String key, Object value) {
        activityParams.put(key, value);
        CacheValue.setCacheValue("activityParams", JSON.toJSONString(activityParams));
    }

    public static void removeActivityParams(String key) {
        activityParams.remove(key);
        CacheValue.setCacheValue("activityParams", JSON.toJSONString(activityParams));
    }

    public static void clearActivityParams() {
        activityParams.clear();
        CacheValue.setCacheValue("activityParams", "{}");
    }


    private static BaseApplication instance;

    public static BaseApplication getInstance() {
        return instance;
    }

    public static SoundPool sp = null;
    public static int m_error, m_right, m_warn;;
    private static int control_warn = -1, old_control_warn = -1;

    public static void playWarn(int count) {
        stopWarn();
        control_warn = sp.play(m_warn, 1, 1, 0, count, 1);
    }

    public static void stopWarn() {
        if (old_control_warn != control_warn) {
            sp.stop(control_warn);
            old_control_warn = control_warn;
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        instance = this;
        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        m_right = sp.load(this, R.raw.right, 1);
        m_error = sp.load(this, R.raw.wrong, 1);
        m_warn = sp.load(this, R.raw.abc, 1);
    }

    /**
     * 获取当前PackageInfo
     * @return
     */
    public static PackageInfo getPackageInfo() {
        PackageManager packageManager = instance.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(instance.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo;
    }





    public static boolean isDebuggable() {
        boolean debuggable = false;
        PackageManager pm = getInstance().getPackageManager();
        try{
            ApplicationInfo appinfo = pm.getApplicationInfo(getInstance().getPackageName(), 0);
            debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        }catch(PackageManager.NameNotFoundException e){
            /*debuggable variable will remain false*/
        }
        return debuggable;
    }

}
