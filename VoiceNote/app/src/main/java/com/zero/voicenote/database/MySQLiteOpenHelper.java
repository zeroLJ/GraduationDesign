package com.zero.voicenote.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.github.yuweiguocn.library.greendao.MigrationHelper;


/**
 * 数据库版本控制，升级时需在gradle文件修改版本号，并在onUpgrade执行MigrationHelper.migrate()方法更新有改动的表
 */
public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    public MySQLiteOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        //----------------------------使用sql实现升级逻辑
        if (oldVersion == newVersion) {
            Log.e("onUpgrade", "数据库是最新版本,无需升级");
            return;
        }
        Log.e("onUpgrade", "数据库从版本" + oldVersion + "升级到版本" + newVersion);
        switch (oldVersion) {
            case 1:
//                String sql = "";
//                db.execSQL(sql);
            case 2:
                //使用MigrationHelper更新表结构时，新增字段不能为int，最好为string类型，否则旧有数据会被删除
//                MigrationHelper.migrate(db,UserDao.class);
            case 3:

            case 4:
//                MigrationHelper.migrate(db,StudentDao.class);
            default:
                break;
        }

        //-----------------------------------
        //--------------------------或者使用GreenDaoUpgradeHelper辅助库实现逻辑
//        MigrationHelper.migrate(db,UserDao.class);
        //----------------------------
    }

}