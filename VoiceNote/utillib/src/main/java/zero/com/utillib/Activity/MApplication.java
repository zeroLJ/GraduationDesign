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
import com.blankj.utilcode.util.Utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import zero.com.utillib.R;
import zero.com.utillib.utils.view.Alert;
import zero.com.utillib.utils.CacheValue;
import zero.com.utillib.utils.Logs;


public class MApplication extends Application {


    /**
     * 维护Activity 的list
     */
    private static List<Activity> mActivitys = Collections
            .synchronizedList(new LinkedList<Activity>());


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


    private static MApplication instance;

    public static MApplication getInstance() {
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

        registerActivityListener();

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

    /**
     * 添加一个activity到管理里
     */
    public void pushActivity(Activity activity) {
        Alert.setBtnDg(null);
        mActivitys.add(activity);
        Logs.wslog("添加一个"+activity.getLocalClassName());
    }

    /**
     * 删除一个activity在管理里
     */
    public void popActivity(Activity activity) {
        Alert.setBtnDg(null);
        mActivitys.remove(activity);
        Logs.wslog("删除一个"+activity.getLocalClassName());
    }



    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return null;
        }
        Activity activity = mActivitys.get(mActivitys.size()-1);
        return activity;
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public static void finishCurrentActivity() {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return;
        }
        Activity activity = mActivitys.get(mActivitys.size()-1);
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return;
        }
        if (activity != null) {
            mActivitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return;
        }
        for (Activity activity : mActivitys) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 指定结束前几个Activity
     * @param topNum
     */
    public static void finishTopNumActivity(int topNum) {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return;
        }
        if(mActivitys.size()<topNum){
            return;
        }
        for (int i=mActivitys.size()-1;i>=0;i--) {
            if(topNum>0){
                finishActivity(mActivitys.get(i));
                topNum--;
            }
        }
    }

    /**
     * 按照指定类名找到activity
     *
     */
    public static Activity findActivity(Class<?> cls) {
        Activity targetActivity = null;
        if (mActivitys != null) {
            for (Activity activity : mActivitys) {
                if (activity.getClass().equals(cls)) {
                    targetActivity = activity;
                    break;
                }
            }
        }
        return targetActivity;
    }

    /**
     * 获取当前最顶部activity的实例
     */
    public Activity getTopActivity() {
        Activity mBaseActivity = null;
        synchronized (mActivitys) {
            final int size = mActivitys.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActivitys.get(size);
        }
        return mBaseActivity;

    }

    /**
     * 获取当前最顶部的acitivity 名字
     */
    public String getTopActivityName() {
        Activity mBaseActivity = null;
        synchronized (mActivitys) {
            final int size = mActivitys.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = mActivitys.get(size);
        }
        return mBaseActivity.getClass().getName();
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            activity.finish();
        }
        mActivitys.clear();
    }

    public static void toActiviy(Class cls){
        if (mActivitys == null||mActivitys.isEmpty()) {
            return;
        }
        int top = -1;
        for(int i=mActivitys.size()-1;i>=0;i--){
            if(mActivitys.get(i).getClass().equals(cls)){
                top = i;
            }
        }
        if(top==-1){
            MApplication.currentActivity().startActivity(new Intent(MApplication.currentActivity(),cls));
            MApplication.currentActivity().finish();
        }else{
            MApplication.finishTopNumActivity(mActivitys.size()-top-1);
        }

    }

    public static Activity getNumActivity(int topNum) {
        if (mActivitys == null||mActivitys.isEmpty()) {
            return null;
        }
        if(mActivitys.size()<topNum){
            return null;
        }
        return mActivitys.get(mActivitys.size()-topNum-1);
    }

    /**
     * 退出应用程序
     */
    public  static void appExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
        }
    }

    private void registerActivityListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    /**
                     *  监听到 Activity创建事件 将该 Activity 加入list
                     */
                    pushActivity(activity);

                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {
                    //Logs.wslog("--onActivityStopped--");
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    Logs.wslog("--------onActivityDestroyed");
                    if (null==mActivitys&&mActivitys.isEmpty()){
                        return;
                    }
                    if (mActivitys.contains(activity)){
                        /**
                         *  监听到 Activity销毁事件 将该Activity 从list中移除
                         */
                        popActivity(activity);
                    }
                }
            });
        }
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
