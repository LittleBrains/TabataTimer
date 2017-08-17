package ru.littlebrains.tabatatimer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

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
public class AddSimpleTimerFragment extends BaseFragment {

    private Button btnSave;
    private NumberPicker minutes;
    private NumberPicker seconds;
    private EditText nameText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        setTitle(R.string.title_create_single_timer);

        if(rootView != null) return  rootView;

        rootView = (FrameLayout) inflater.inflate(R.layout.fragment_addsimpletimer, container, false);

        btnSave = (Button) rootView.findViewById(R.id.button_save);
        nameText = (EditText) rootView.findViewById(R.id.name);


        minutes = (NumberPicker) rootView.findViewById(R.id.numberPicker);
        minutes.setMaxValue(60);
        minutes.setMinValue(0);
        minutes.setValue(0);
        seconds = (NumberPicker) rootView.findViewById(R.id.numberPicker2);
        seconds.setMaxValue(59);
        seconds.setMinValue(0);
        seconds.setValue(0);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((minutes.getValue() == 0 && seconds.getValue() == 0)  || nameText.getText().length() == 0) {
                    showToast(R.string.toast_empty_field);
                    return;
                }
                btnSave.setEnabled(false);
                TimerModel timer = new TimerModel();
                timer.id = (int) new Date().getTime();
                timer.name = nameText.getText().toString();
                timer.timeRest = 5;
                timer.timeRun = minutes.getValue()*60+seconds.getValue();
                timer.timerCount = TimerModel.COUNT_SINGLE_TIMER;
                TimerApi.addTimer(mActivity, timer);
                showToast(R.string.toast_timer_created);
                Utils.hideKeyboard(mActivity);
                FragmentController.backFragmet();
            }
        });

        return rootView;
    }
}
