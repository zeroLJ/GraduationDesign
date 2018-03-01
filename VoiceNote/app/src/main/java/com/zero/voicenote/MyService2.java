package com.zero.voicenote;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MyService2 extends Service {
    private Timer timer = new Timer();
    public MyService2() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new Binder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
                if (!isWorked("com.zero.voicenote.MyService")){
                    Intent service = new Intent(MyService2.this, MyService.class);
                    startService(service);
                    bindService(service, sc, Context.BIND_IMPORTANT);
                }
            }
        },10000,10000);
        return START_STICKY;
    }

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            Toast.makeText(getApplicationContext(),"绑定MyService",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 连接出现了异常断开了，MyService2被杀掉了
//            Toast.makeText(getApplicationContext(),"MyService挂了",Toast.LENGTH_SHORT).show();
            Intent service = new Intent(MyService2.this, MyService.class);
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
