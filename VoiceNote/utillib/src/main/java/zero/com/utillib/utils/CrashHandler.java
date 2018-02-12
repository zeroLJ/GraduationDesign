package zero.com.utillib.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import zero.com.utillib.Activity.MApplication;
import zero.com.utillib.utils.view.Alert;

/**
 * Created by ws on 2017/7/28.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private static CrashHandler INSTANCE = new CrashHandler();

    private Context mContext;

    private CrashHandler() {
    }

    private static String threadClassName="";

    public static String getThreadClassName() {
        return threadClassName;
    }

    public static CrashHandler getInstance(String threadClassName) {
        CrashHandler.threadClassName = threadClassName;
        return INSTANCE;
    }

    public void init(Context ctx) {
        mContext = ctx;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {
        Logs.errlog("程序异常!", ex);
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                StringWriter stringWriter = new StringWriter();
                try {
                    long currentTime = System.currentTimeMillis();
                    String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTime));
                    //建立记录Crash信息的文本
                    File file = new File(mContext.getExternalFilesDir(null),"errorMsg.txt");
                    Logs.JLlog(file.getAbsolutePath());
                    PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                    printWriter.println(time);
                    printWriter.println();
                    ex.printStackTrace(printWriter);
                    PrintWriter pw = new PrintWriter(stringWriter);
                    ex.printStackTrace(pw);
                    printWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Logs.JLlog("记录Crash信息失败");
                }
                MApplication.playWarn(-1);
                Alert.alertDialogOneBtn("错误信息:\n"+stringWriter.toString(),"程序异常请联系管理员！", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MApplication.stopWarn();
                        MApplication.finishAllActivity();
                        Class loginClass = null;
                        try {
                            loginClass = Class.forName(threadClassName);//"com.ub_wms.activity.LoginActivity");
                        }catch (Exception e){}
                        Intent intent = new Intent(mContext, loginClass);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
                    }
                });

                Looper.loop();
            }
        }.start();
    }



}
