package com.example.tick.my;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button play;
    //private Button pause;
    private Button stop;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    Intent intent=getIntent();
    private MyService.mBinder binder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MyService.mBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent startIntent = new Intent(this,MyService.class);
        startService(startIntent);
        bindService(startIntent, connection, BIND_AUTO_CREATE);
        play=(Button) findViewById(R.id.play);
        //pause=(Button) findViewById(R.id.pause);
        stop=(Button) findViewById(R.id.stop);
        play.setOnClickListener(this);
//        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        //intent.setClass(this, MyService.class);
    }


    public void onClick(View v){
        switch (v.getId()){
            case R.id.play:
                binder.start();
                break;
            case R.id.stop:
                binder.stop();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        binder.Noti();
    }

    protected void onDestory(){
        super.onDestroy();
        Intent stopIntent = new Intent(this,MyService.class);
        unbindService(connection);
        stopService(stopIntent);
    }
}