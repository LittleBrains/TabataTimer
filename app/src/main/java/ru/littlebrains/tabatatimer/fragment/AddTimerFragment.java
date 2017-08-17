package ru.littlebrains.tabatatimer.fragment;

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
import ru.littlebrains.tabatatimer.TimeDialog;
import ru.littlebrains.tabatatimer.Utils;
import ru.littlebrains.tabatatimer.api.TimerApi;
import ru.littlebrains.tabatatimer.api.TimerModel;

/**
 * Created by Evgeny on 03.01.2016.
 */
public class AddTimerFragment extends BaseFragment {

    private EditText timeRun;
    private EditText timePause;
    private Button btnSave;
    private EditText timerCount;
    private EditText nameText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setTitle(R.string.title_create_timer);

        if(rootView != null) return  rootView;

        rootView = (FrameLayout) inflater.inflate(R.layout.fragment_addtimer, container, false);

        nameText = (EditText) rootView.findViewById(R.id.name);
        timeRun = (EditText) rootView.findViewById(R.id.time_run);
        timePause = (EditText) rootView.findViewById(R.id.time_pause);
        timerCount = (EditText) rootView.findViewById(R.id.timer_count);
        btnSave = (Button) rootView.findViewById(R.id.button_save);

        /*TimeDialog timeRunDialog = new TimeDialog(getActivity(), timeRun);
        timeRun.setOnClickListener(timeRunDialog);
        timeRun.setOnFocusChangeListener(timeRunDialog);

        TimeDialog timePauseDialog = new TimeDialog(getActivity(), timePause);
        timePause.setOnClickListener(timePauseDialog);
        timePause.setOnFocusChangeListener(timePauseDialog);*/

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeRun.getText().length() == 0 || timerCount.getText().length() == 0 ||
                        timePause.getText().length() == 0 || nameText.getText().length() == 0) {
                    showToast(R.string.toast_empty_field);
                    return;
                }
                try {

                    TimerModel timer = new TimerModel();
                    timer.id = (int) new Date().getTime();
                    timer.name = nameText.getText().toString();
                    timer.timeRest = 10;
                    timer.timeRun = (Integer.valueOf(timeRun.getText().toString()));
                    timer.timePause = (Integer.valueOf(timePause.getText().toString()));
                    timer.timerCount = (Integer.valueOf(timerCount.getText().toString()));
                    btnSave.setEnabled(false);
                    TimerApi.addTimer(mActivity, timer);
                    showToast(R.string.toast_timer_created);
                    Utils.hideKeyboard(mActivity);
                    FragmentController.backFragmet();
                }catch (NumberFormatException e) {
                    timeRun.setText("");
                    timePause.setText("");
                    timerCount.setText("");
                }
            }
        });

        return rootView;
    }
}
