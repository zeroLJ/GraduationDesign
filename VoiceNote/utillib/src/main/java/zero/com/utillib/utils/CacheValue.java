package zero.com.utillib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import zero.com.utillib.Activity.MApplication;

/**
 * Created by ws on 2017/7/21.
 * SharedPreference工具
 */

public class CacheValue {


    private static String SHAREDNANE="newwm";

    private static SharedPreferences mSpf = MApplication.getInstance().getSharedPreferences(SHAREDNANE, Context.MODE_PRIVATE);


    public static void removeCacheValue(String name) {        if (mSpf == null) {
            mSpf = MApplication.getInstance().getSharedPreferences(SHAREDNANE, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor spEdit = mSpf.edit();
        spEdit.remove(name);
        spEdit.commit();
    }


    /**
     * 保存数据到SharedPreferences缓存
     *
     * @param name
     * @param value
     */
    public static void setCacheValue(String name, Object value) {
        if (mSpf == null) {
            mSpf = MApplication.getInstance().getSharedPreferences(SHAREDNANE, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor spEdit = mSpf.edit();
        if (value instanceof String) {
            spEdit.putString(name, String.valueOf(value));
        } else if (value instanceof Integer) {
            spEdit.putInt(name, ((Integer) value).intValue());
        } else if (value instanceof Float) {
            spEdit.putFloat(name, ((Float) value).floatValue());
        } else if (value instanceof Long) {
            spEdit.putLong(name, ((Long) value).longValue());
        } else if (value instanceof Boolean) {
            spEdit.putBoolean(name, ((Boolean) value).booleanValue());
        }
        spEdit.commit();
    }

    /**
     * 获取SharedPreferences缓存数据
     *
     * @param name
     * @param defValue
     * @return
     */
    public static String getCacheStringValue(String name, String defValue) {
        if (mSpf == null) {
            mSpf = MApplication.getInstance().getSharedPreferences(SHAREDNANE, Context.MODE_PRIVATE);
        }
        return mSpf.getString(name, defValue);
    }


    /**
     * 获取SharedPreferences缓存数据
     *
     * @param name
     * @param defValue
     * @return
     */
    public static int getCacheIntValue(String name, int defValue) {
        if (mSpf == null) {
            mSpf = MApplication.getInstance().getSharedPreferences(SHAREDNANE, Context.MODE_PRIVATE);
        }
        return mSpf.getInt(name, defValue);
    }

    /**
     * 获取SharedPreferences缓存数据
     *
     * @param name
     * @param defValue
     * @return
     */
    public static long getCacheLongValue(String name, long defValue) {
        if (mSpf == null) {
            mSpf = MApplication.getInstance().getSharedPreferences(SHAREDNANE, Context.MODE_PRIVATE);
        }
        return mSpf.getLong(name, defValue);
    }

    /**
     * 获取SharedPreferences缓存数据
     *
     * @param name
     * @param defValue
     * @return
     */
    public static boolean getCacheBooleanValue(String name, boolean defValue) {
        if (mSpf == null) {
            mSpf = MApplication.getInstance().getSharedPreferences(SHAREDNANE, Context.MODE_PRIVATE);
        }
        return mSpf.getBoolean(name,defValue);
    }
}
