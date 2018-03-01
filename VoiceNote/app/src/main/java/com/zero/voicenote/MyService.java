package com.zero.voicenote;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ljl on 2018/2/23.
 */
public class MyService extends Service {
    private Timer timer = new Timer();
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
//        mBuilder.setContentTitle("测试标题")//设置通知栏标题
//                .setContentText("测试内容") //设置通知栏显示内容
//                // .setNumber(number) //设置通知集合的数量
//                .setTicker("测试通知来啦") //通知首次出现在通知栏，带上升动画效果的
//                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
//                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
//                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
//                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
//                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
//                .setProgress(100,50,false)
//                .setSmallIcon(R.mipmap.back);//设置通知小ICON
        startForeground(1, mBuilder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent service = new Intent(this, MyService2.class);
        startService(service);
        bindService(service, sc, Context.BIND_IMPORTANT);
        if (timer!=null){
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isWorked("com.zero.voicenote.MyService2")){
                    Intent service = new Intent(MyService.this, MyService2.class);
                    startService(service);
                    bindService(service, sc, Context.BIND_IMPORTANT);
                }
            }
        },10000,10000);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new Binder();
    }

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            Toast.makeText(getApplicationContext(),"绑定MyService2",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 连接出现了异常断开了，MyService2被杀掉了
//            Toast.makeText(getApplicationContext(),"MyService2挂了",Toast.LENGTH_SHORT).show();
            Intent service = new Intent(MyService.this, MyService2.class);
            startService(service);
            bindService(service, sc, Context.BIND_IMPORTANT);
            timer.cancel();
        }
    };

    private boolean isWorked(String className) {
        ActivityManager myManager = (ActivityManager) this
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(className)) {
                return true;
            }
        }
        return false;
    }
}
