package ru.littlebrains.tabatatimer.fragment;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import ru.littlebrains.tabatatimer.FragmentController;
import ru.littlebrains.tabatatimer.R;
import ru.littlebrains.tabatatimer.TimerService;
import ru.littlebrains.tabatatimer.api.TimerModel;
import trikita.log.Log;

/**
 * Created by Evgeny on 03.01.2016.
 */
@SuppressLint("ValidFragment")
public class TimerFragment extends BaseFragment {

    private static final int REST = 0;
    private static final int RUN = 1;
    private static final int PAUSE = 2;
    private static final int FINISH = 3;

    private TimerModel timerModel;
    private TextView statusView;
    private TextView time;
    private TextView count;
    private int rest;
    private int run;
    private int pause;
    private int nowRepeat = 0;
    private int nowStatus = 0;
    private MediaPlayer player;
    private AssetFileDescriptor afd;
    private boolean pauseTimer = false;
    private Handler handlerTimer;
    private Intent i;
    private FloatingActionButton fabStop;

    public TimerFragment(){}

    public TimerFragment(TimerModel timer){
        this.timerModel = timer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if(timerModel == null) {
            FragmentController.newFragment(new MainFragment(), R.layout.fragment_main, false);
            return null;
        }
        setTitle("");

        toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.yellow)));
        actionBar.setElevation(0);
        if(rootView != null) return  rootView;

        rootView = (FrameLayout) inflater.inflate(R.layout.fragment_timer, container, false);


        statusView = (TextView) rootView.findViewById(R.id.status);
        time = (TextView) rootView.findViewById(R.id.time);
        count = (TextView) rootView.findViewById(R.id.count);
        fabStop = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fabStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.stopService(new Intent(getContext(), TimerService.class));
                FragmentController.backFragmet();
            }
        });


        rest = timerModel.timeRest;
        run = timerModel.timeRun;
        pause = timerModel.timePause;

        if(!isMyServiceRunning(TimerService.class)) {
            Intent i = new Intent(getContext(), TimerService.class);
            i.putExtra("rest", timerModel.timeRest);
            i.putExtra("run", timerModel.timeRun);
            i.putExtra("pause", timerModel.timePause);
            i.putExtra("count", timerModel.timerCount);
            i.putExtra("id", timerModel.id);
            Log.d("id dd", timerModel.id );
            getContext().startService(i);
        }

        updateScreen();

        return rootView;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.d("service true");
                return true;
            }
        }
        Log.d("service false");
        return false;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                Log.d(bundle);
                Log.d(intent);
                nowStatus = bundle.getInt("status",0);
                rest = bundle.getInt("rest",0);
                run = bundle.getInt("run",0);
                pause = bundle.getInt("pause",0);
                nowRepeat = bundle.getInt("nowRepeat",0);
                updateScreen();
                Log.d("BroadcastReceiver", "BroadcastReceiver");
            }
        }
    };

    private void updateScreen() {
        if(timerModel == null) return;
        if (timerModel.timerCount != TimerModel.COUNT_SINGLE_TIMER) {
            count.setText(nowRepeat + " / " + timerModel.timerCount);
        } else {
            count.setVisibility(View.GONE);
        }
        if (!isAdded()) return;
        Resources res = getActivity().getResources();
        switch (nowStatus) {
            case REST:
                rootView.setBackgroundResource(R.color.yellow);
                statusView.setText(R.string.rest);
                time.setText("" + rest);
                toolbar.setBackgroundDrawable(new ColorDrawable(res.getColor(R.color.yellow)));
                setStatusBarColor(R.color.yellow);
                break;
            case RUN:
                rootView.setBackgroundResource(R.color.green);
                statusView.setText(R.string.run);
                time.setText("" + run);
                toolbar.setBackgroundDrawable(new ColorDrawable(res.getColor(R.color.green)));
                setStatusBarColor(R.color.green);
                break;
            case PAUSE:
                rootView.setBackgroundResource(R.color.red);
                statusView.setText(R.string.pausa);
                time.setText("" + pause);
                toolbar.setBackgroundDrawable(new ColorDrawable(res.getColor(R.color.red)));
                setStatusBarColor(R.color.red);
                break;
            case FINISH:
                mActivity.stopService(new Intent(getContext(), TimerService.class));
                rootView.setBackgroundResource(R.color.yellow);
                statusView.setText(R.string.finish);
                time.setText("" + 0);
                toolbar.setBackgroundDrawable(new ColorDrawable(res.getColor(R.color.yellow)));
                setStatusBarColor(R.color.yellow);
                break;
        }
    }

    private void setStatusBarColor(int idColor){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(idColor));
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        mActivity.registerReceiver(receiver, new IntentFilter(TimerService.NOTIFICATION));
        pauseTimer = false;
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        updateScreen();
    }

    @Override
    public void onPause(){
        super.onPause();
        mActivity.unregisterReceiver(receiver);
        pauseTimer = true;

        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        setStatusBarColor(R.color.colorPrimaryDark);
    }

    @Override
    public void onDestroy(){
        setStatusBarColor(R.color.colorPrimaryDark);
        if(toolbar != null) {
            toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

        }
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                pauseTimer = true;
                if(timerModel.timerCount == TimerModel.COUNT_SINGLE_TIMER){
                    FragmentController.newFragment(new EditSimpleTimerFragment(timerModel), R.layout.fragment_edittimer, true);
                }else {
                    FragmentController.newFragment(new EditTimerFragment(timerModel), R.layout.fragment_edittimer, true);
                }
                break;
        }
        return true;
    }
}
