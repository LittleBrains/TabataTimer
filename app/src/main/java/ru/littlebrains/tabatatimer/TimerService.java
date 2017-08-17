package ru.littlebrains.tabatatimer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import ru.littlebrains.tabatatimer.api.TimerModel;
import trikita.log.Log;

/**
 * Created by evgeniy on 22.11.2016.
 */

public class TimerService extends Service {
    private static final String LOG_TAG = "TimerService";
    public static final String NOTIFICATION = "ru.littlebrains.timer.receiver";

    private static final int REST = 0;
    private static final int RUN = 1;
    private static final int PAUSE = 2;
    private static final int FINISH = 3;

    public static TimerModel timerModel = new TimerModel();
    private int rest;
    private int run;
    private int pause;
    private int nowRepeat = 0;
    private int nowStatus = 0;
    private MediaPlayer player;
    private AssetFileDescriptor afd;
    private boolean pauseTimer = false;
    private Timer timer;
    private PowerManager.WakeLock wakeLock;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate");

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        timerModel.timeRest = intent.getIntExtra("rest",0);
        timerModel.timeRun = intent.getIntExtra("run",0);
        timerModel.timePause = intent.getIntExtra("pause",0);
        timerModel.timerCount = intent.getIntExtra("count",0);
        timerModel.id = intent.getIntExtra("id",-1);
        Log.d("id", timerModel.id );
        rest = timerModel.timeRest;
        run = timerModel.timeRun;
        pause = timerModel.timePause;
        someTask();
        notification();


        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
        //if(handlerTimer != null) handlerTimer.removeCallbacksAndMessages(null);
        if (runnableTimer != null) runnableTimer.cancel();
        if (timer != null) timer.cancel();
    }

    void someTask() {

        PowerManager mgr = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();

       /* handlerTimer = new Handler();
        handlerTimer.postDelayed(runnableTimer, 1000);*/

        //if (runnableTimer != null) runnableTimer.cancel();
        if(timer!= null) timer.cancel();
        timer = new Timer();
        timer.schedule(runnableTimer, 1000, 1000);
    }

    TimerTask runnableTimer = new TimerTask() {
        @Override
        public void run() {
            Log.d("runnableTimer", "runnableTimer");
                if(pauseTimer) return;
                if(nowStatus == REST) {
                    if(rest > 0) rest--;
                    if(rest < 4 && rest > 0){
                        playBeep(false);
                    }
                    if(rest == 0) {
                        nowStatus = RUN;
                        playBeep(true);
                        nowRepeat++;
                    }
                }
                else if(nowStatus == RUN){
                    if(run > 0) run--;
                    if(run < 4 && run > 0){
                        playBeep(false);
                    }
                    if(run == 0){
                        if(timerModel.timerCount == TimerModel.COUNT_SINGLE_TIMER){
                            nowStatus = FINISH;
                        }else {
                            nowStatus = PAUSE;
                            run = timerModel.timeRun;
                            if (nowRepeat == timerModel.timerCount) {
                                nowStatus = FINISH;
                            } else {
                                nowRepeat++;
                            }
                        }
                        playBeep(true);
                    }
                }else if(nowStatus == PAUSE){
                    if(pause > 0) pause--;
                    if(pause < 4 && pause > 0){
                        playBeep(false);
                    }
                    if(pause == 0){
                        nowStatus = RUN;
                        if(timerModel.timePause != 0){
                            playBeep(true);
                        }
                        pause = timerModel.timePause;
                    }
                }
                Intent intent = new Intent(NOTIFICATION);
                intent.putExtra("status", nowStatus);
                intent.putExtra("rest", rest);
                intent.putExtra("run", run);
                intent.putExtra("pause", pause);
                intent.putExtra("nowRepeat", nowRepeat);
                sendBroadcast(intent);
                notification();
                if(nowStatus == FINISH) {
                    wakeLock.release();
                    if (timer != null) timer.cancel();
                    stopSelf();
                }
        }
    };

    private void playBeep(boolean isLong){
        try {
            if (player != null) {
                player.stop();
                player.reset();
                player.release();
            }
            if(isLong){
                if(nowStatus == PAUSE){
                    afd = getAssets().openFd("rest.wav");
                }
                if(nowStatus == FINISH){
                    afd = getAssets().openFd("finish.wav");
                }
                if(nowStatus == RUN){
                    afd = getAssets().openFd("start.mp3");
                }
            }else {
                afd = getAssets().openFd("beep-07.mp3");
            }
            player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            player.prepare();
            player.start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void notification(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction("open");
        intent.putExtra("timer_id", timerModel.id);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder notification  = new NotificationCompat.Builder(this)
                .setContentTitle(getTextStatus() + " "  + getTime() +getString(R.string.sec))
                .setContentText(timerModel.timerCount == TimerModel.COUNT_SINGLE_TIMER ?
                        "" :
                        nowRepeat + "/" + timerModel.timerCount)
                .setSmallIcon(R.drawable.ic_stat_image_timer)
                .setContentIntent(pIntent)
                .setAutoCancel(true);

        Intent i = new Intent(this, MainActivity.class);
        i.setAction("stop");
        i.putExtra("stop", true);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        notification.addAction(R.drawable.ic_action_av_stop, getString(R.string.stop), pi);

        startForeground(456772, notification.build());
    }

    private String getTime(){
        switch (nowStatus){
            case REST:
                return rest+"";
            case PAUSE:
                return pause+"";
            case RUN:
                return run+"";
            default:
                return "";
        }
    }

    private String getTextStatus(){
        switch (nowStatus){
            case REST:
                return getResources().getString(R.string.rest);
            case PAUSE:
                return getResources().getString(R.string.pausa);
            case RUN:
                return getResources().getString(R.string.run);
            case FINISH:
                return getResources().getString(R.string.finish);
            default:
                return "";
        }
    }


}
