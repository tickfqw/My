package com.example.tick.my;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;

/**
 * Created by tick on 2016/5/19.
 */
public class MyService extends Service {

    private NotificationManager myNotiManager;
    private mBinder binder = new mBinder();
    private MediaPlayer mediaPlayer = new MediaPlayer();

    private void initMediaPlayer() {
        try {
            File file = new File(Environment.getDataDirectory(), "music.mp3");
            //Log.d("MSG", Environment.getDataDirectory().toString());
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlayer();

        myNotiManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

    }

    class mBinder extends Binder {

        public void start(){
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();

            }else{
                mediaPlayer.pause();
            }
        }
        public void stop(){
            mediaPlayer.reset();
            initMediaPlayer();
        }
        public void Noti(){
            setNotiType(R.drawable.ic_launcher,"Playing");
        }
        public void pause(){
            mediaPlayer.pause();
        }
        public boolean isPlaying(){
            return mediaPlayer.isPlaying();
        }
    }

    private void setNotiType(int iconId, String text) {
        Intent notifyIntent=new Intent(this,MyService.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent appIntent=PendingIntent.getActivity(MyService.this,0,notifyIntent,0);

        Notification myNoti=new Notification.Builder(MyService.this)
                .setSmallIcon(iconId)
                .setTicker(text)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(appIntent)
                .setContentTitle("MyMusic")
                .build();
      /* myNoti.icon=iconId;
       myNoti.tickerText=text;
        myNoti.defaults=Notification.DEFAULT_SOUND;
        myNoti.setLatestEventInfo(MainActivity.this, "MSN登录状态", text, appIntent);*/

        myNotiManager.notify(0,myNoti);
    }
}