package com.zero.voicenote.database;

import com.blankj.utilcode.util.ActivityUtils;

import org.greenrobot.greendao.database.Database;



/**
 * 获取GreenDao实例，初始化时会根据gradle里设置的版本号自动更新
 * @DaoMaster ： GreenDao的顶级对象，用于创建和删除表（一般不需要调用）
 * @DaoSession  管理所有的Dao对象，Dao对象可以通过 daoSession.getUserDao()获得
 */
public class GreenDaoManager {
    private static final String DB_NAME = "greendao";
    private static GreenDaoManager mInstance;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {
                if (mInstance == null) {
                    mInstance = new GreenDaoManager();
                }
            }
        }
        return mInstance;
    }


    private GreenDaoManager() {
        if (mInstance == null) {
            MySQLiteOpenHelper helper = new MySQLiteOpenHelper(ActivityUtils.getTopActivity(), DB_NAME, null);
            Database db = helper.getWritableDb();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }
}