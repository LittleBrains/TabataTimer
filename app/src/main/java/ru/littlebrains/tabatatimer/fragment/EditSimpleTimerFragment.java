package ru.littlebrains.tabatatimer.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import ru.littlebrains.tabatatimer.FragmentController;
import ru.littlebrains.tabatatimer.R;
import ru.littlebrains.tabatatimer.TimeDialog;
import ru.littlebrains.tabatatimer.Utils;
import ru.littlebrains.tabatatimer.api.TimerApi;
import ru.littlebrains.tabatatimer.api.TimerModel;

import static ru.littlebrains.tabatatimer.R.string.seconds;

/**
 * Created by Evgeny on 03.01.2016.
 */
@SuppressLint("ValidFragment")
public class EditSimpleTimerFragment extends BaseFragment {

    private TimerModel timerModel;
    private Button btnSave;
    private Button btnRemove;
    private NumberPicker minutes;
    private NumberPicker seconds;
    private EditText nameText;

    public EditSimpleTimerFragment(){}
    public EditSimpleTimerFragment(TimerModel timer){
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

        rootView = (FrameLayout) inflater.inflate(R.layout.fragment_editsimpletimer, container, false);

        nameText = (EditText) rootView.findViewById(R.id.name);

        minutes = (NumberPicker) rootView.findViewById(R.id.numberPicker);
        minutes.setMaxValue(60);
        minutes.setMinValue(0);

        seconds = (NumberPicker) rootView.findViewById(R.id.numberPicker2);
        seconds.setMaxValue(59);
        seconds.setMinValue(0);

        btnSave = (Button) rootView.findViewById(R.id.button_save);
        btnRemove = (Button) rootView.findViewById(R.id.button_remove);

        minutes.setValue(timerModel.timeRun/60);
        seconds.setValue(timerModel.timeRun-timerModel.timeRun/60*60);
        nameText.setText(timerModel.name);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((minutes.getValue() == 0 && seconds.getValue() == 0) || nameText.getText().length() == 0) {
                    showToast(R.string.toast_empty_field);
                    return;
                }
                btnSave.setEnabled(false);
                timerModel.name = nameText.getText().toString();
                timerModel.timeRest = 5;
                timerModel.timeRun = minutes.getValue()*60+seconds.getValue();
                timerModel.timerCount = TimerModel.COUNT_SINGLE_TIMER;
                TimerApi.editTimer(mActivity, timerModel);
                showToast(R.string.toast_timer_edit);
                Utils.hideKeyboard(mActivity);
                FragmentController.clearBackStack();
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
