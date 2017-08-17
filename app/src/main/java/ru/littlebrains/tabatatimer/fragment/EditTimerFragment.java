package ru.littlebrains.tabatatimer.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.Date;

import ru.littlebrains.tabatatimer.FragmentController;
import ru.littlebrains.tabatatimer.R;
import ru.littlebrains.tabatatimer.Utils;
import ru.littlebrains.tabatatimer.api.TimerApi;
import ru.littlebrains.tabatatimer.api.TimerModel;

/**
 * Created by Evgeny on 03.01.2016.
 */
@SuppressLint("ValidFragment")
public class EditTimerFragment extends BaseFragment {

    private TimerModel timerModel;
    private EditText timeRun;
    private EditText timePause;
    private Button btnSave;
    private EditText timerCount;
    private Button btnRemove;
    private EditText nameText;

    public EditTimerFragment(){}
    public EditTimerFragment(TimerModel timer){
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
        setTitle(R.string.title_name_edit);

        if(rootView != null) return  rootView;

        rootView = (FrameLayout) inflater.inflate(R.layout.fragment_edittimer, container, false);

        nameText = (EditText) rootView.findViewById(R.id.name);
        timeRun = (EditText) rootView.findViewById(R.id.time_run);
        timePause = (EditText) rootView.findViewById(R.id.time_pause);
        timerCount = (EditText) rootView.findViewById(R.id.timer_count);
        btnSave = (Button) rootView.findViewById(R.id.button_save);
        btnRemove = (Button) rootView.findViewById(R.id.button_remove);

        timeRun.setText(timerModel.timeRun+"");
        timePause.setText(timerModel.timePause+"");
        timerCount.setText(timerModel.timerCount+"");
        nameText.setText(timerModel.name);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeRun.getText().length() == 0 || timerCount.getText().length() == 0 ||
                        timePause.getText().length() == 0 || nameText.getText().length() == 0) {
                    showToast(R.string.toast_empty_field);
                    return;
                }
                try {
                    timerModel.name = nameText.getText().toString();
                    timerModel.timeRest = 10;
                    timerModel.timeRun = Integer.valueOf(timeRun.getText().toString());
                    timerModel.timePause = Integer.valueOf(timePause.getText().toString());
                    timerModel.timerCount = Integer.valueOf(timerCount.getText().toString());
                    btnSave.setEnabled(false);
                    TimerApi.editTimer(mActivity, timerModel);
                    showToast(R.string.toast_timer_edit);
                    Utils.hideKeyboard(mActivity);
                    FragmentController.clearBackStack();
                }catch (NumberFormatException e){
                    btnSave.setEnabled(true);
                    timeRun.setText(timerModel.timeRun+"");
                    timePause.setText(timerModel.timePause+"");
                    timerCount.setText(timerModel.timerCount+"");
                }
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerApi.deleteTimer(mActivity, timerModel);
                showToast(R.string.toast_timer_remove);
                Utils.hideKeyboard(mActivity);
                FragmentController.clearBackStack();
            }
        });

        return rootView;
    }
}
