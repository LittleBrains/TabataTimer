package ru.littlebrains.tabatatimer.api;

import android.content.Context;

import java.util.List;

/**
 * Created by Evgeny on 03.01.2016.
 */
public class TimerApi {

    public static void addTimer(Context mContext, TimerModel timer){
        Storage.addTimer(mContext, timer);
    }

    public static void deleteTimer(Context mContext, TimerModel timer){
        Storage.deleteTimer(mContext, timer);
    }

    public static void editTimer(Context mContext, TimerModel timer){
        Storage.editTimer(mContext, timer);
    }

    public static List<TimerModel> getListTimers(Context mContext){
        return Storage.getListTimers(mContext);
    }

}
