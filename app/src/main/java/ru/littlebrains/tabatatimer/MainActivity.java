package ru.littlebrains.tabatatimer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;

import java.util.List;

import io.fabric.sdk.android.Fabric;
import ru.littlebrains.tabatatimer.api.TimerApi;
import ru.littlebrains.tabatatimer.api.TimerModel;
import ru.littlebrains.tabatatimer.fragment.MainFragment;
import ru.littlebrains.tabatatimer.fragment.TimerFragment;
import trikita.log.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentController.mFragmentManager = getSupportFragmentManager();
        FragmentController.actionbar = getSupportActionBar();

        FragmentController.newFragment(new MainFragment(), R.layout.fragment_main, false);
        onNewIntent(getIntent());

    }

    @Override
    public void onResume(){
        super.onResume();
        /*if(getIntent() != null && getIntent().getExtras() != null){
            int id = getIntent().getIntExtra("timer_id", -1);
            boolean stop = getIntent().getBooleanExtra("stop", false);
            Log.d("onResume id", id, "stop", stop);

            if(stop){
                stopService(new Intent(this, TimerService.class));
                FragmentController.newFragment(new MainFragment(), R.layout.fragment_main, false);
                return;
            }
            List<TimerModel> timerModelList = TimerApi.getListTimers(this);
            for( TimerModel tm : timerModelList){
                if(tm.id == id){
                    FragmentController.newFragment(new TimerFragment(tm), R.layout.fragment_timer, true);
                }
            }
        }*/
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // getIntent() should always return the most recent
        setIntent(intent);
        if(getIntent() != null && getIntent().getExtras() != null){
            int id = getIntent().getIntExtra("timer_id", -1);
            boolean stop = getIntent().getBooleanExtra("stop", false);
            Log.d("onNewIntent id", id, "stop", stop);

            if(stop){
                stopService(new Intent(this, TimerService.class));
                FragmentController.backFragmet();
                return;
            }
            List<TimerModel> timerModelList = TimerApi.getListTimers(this);
            for( TimerModel tm : timerModelList){
                if(tm.id == id){
                    if(getSupportFragmentManager().findFragmentByTag(R.layout.fragment_timer+"") == null) {
                        FragmentController.newFragment(new TimerFragment(tm), R.layout.fragment_timer, true);
                    }
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Utils.hideKeyboard(this);
                FragmentController.backFragmet();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
