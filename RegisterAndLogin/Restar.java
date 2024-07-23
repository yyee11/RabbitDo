package com.example.todo.RegisterAndLogin;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.todo.MainActivity;

public class Restar {
    public class killSelfService extends Service {
        /**
         * 关闭应用后多久重新启动
         */
        private long stopDelayed = 50;
        private Handler handler;
        private String PackageName;

        public killSelfService() {
            handler = new Handler();
        }

        @Override
        public int onStartCommand(final Intent intent, int flags, int startId) {
            stopDelayed = intent.getLongExtra("Delayed", 50);
            Log.i("killSelfService", "stopDelayed:" + stopDelayed);
            PackageName = intent.getStringExtra("PackageName");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent LaunchIntent = new Intent(killSelfService.this, MainActivity.class);
                    LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(LaunchIntent);
                    Log.i("killSelfService", "启动app");
                    killSelfService.this.stopSelf();
                }
            }, stopDelayed);
            return super.onStartCommand(intent, flags, startId);
        }


        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

    }
}
