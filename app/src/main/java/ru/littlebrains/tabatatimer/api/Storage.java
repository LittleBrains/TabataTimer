package ru.littlebrains.tabatatimer.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.littlebrains.tabatatimer.R;
import ru.littlebrains.tabatatimer.Utils;

/**
 * Created by Evgeny on 03.01.2016.
 */
public class Storage {

    private static final String STORAGE_NAME = "TABATA_TIMER";

    public static void addTimer(Context mContext, TimerModel timer){
        JSONArray jsonArray = getJson(mContext);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", timer.id);
            jsonObject.put("name", timer.name);
            jsonObject.put("timerest", timer.timeRest);
            jsonObject.put("timerun", timer.timeRun);
            jsonObject.put("timepause", timer.timePause);
            jsonObject.put("timercount", timer.timerCount);
            jsonArray.put(jsonObject);
            saveJson(mContext, jsonArray);
        }catch (JSONException e){
            Log.d("StorageData", e.getMessage());
        }
    }


    public static void deleteTimer(Context context, TimerModel timer) {
        JSONArray jsonArray = getJson(context);
        try {
            for(int i = 0; i < jsonArray.length(); i++){
                Log.d("StorageData", jsonArray.getJSONObject(i).getInt("id") + " " + timer.id);
                if(jsonArray.getJSONObject(i).getInt("id") == timer.id){
                    saveJson(context, remove(i, jsonArray));
                    return;
                }
            }
        }catch (JSONException e){
            Log.d("StorageData", e.getMessage());
        }
    }

    public static List<TimerModel> getListTimers(Context context) {
        JSONArray jsonArray = getJson(context);
        List<TimerModel> list = new ArrayList<>();
        try {
            for(int i = 0; i < jsonArray.length(); i++){
                TimerModel timer = new TimerModel();
                timer.id = jsonArray.getJSONObject(i).getInt("id");

                timer.timeRest = jsonArray.getJSONObject(i).getInt("timerest");
                timer.timeRun = jsonArray.getJSONObject(i).getInt("timerun");
                timer.timePause = jsonArray.getJSONObject(i).getInt("timepause");
                timer.timerCount = jsonArray.getJSONObject(i).getInt("timercount");
                if(jsonArray.getJSONObject(i).has("name")) {
                    timer.name = jsonArray.getJSONObject(i).getString("name");
                }else{
                    if(timer.timerCount == TimerModel.COUNT_SINGLE_TIMER){
                        timer.name = context.getResources().getString(R.string.simple);
                    }else{
                        timer.name = context.getResources().getString(R.string.tabata_timer);
                    }
                }
                list.add(timer);
            }
        }catch (JSONException e){
            Log.d("StorageData", e.getMessage());
        }

        return list;
    }

    public static TimerModel editTimer(Context context,TimerModel timer) {
        Log.d("StorageData", "editTimer");
        JSONArray jsonArray = getJson(context);
        List<TimerModel> list = new ArrayList<>();
        try {
            for(int i = 0; i < jsonArray.length(); i++){
                if(jsonArray.getJSONObject(i).getInt("id") == timer.id) {
                    jsonArray.getJSONObject(i).put("id", timer.id);
                    jsonArray.getJSONObject(i).put("name", timer.name);
                    jsonArray.getJSONObject(i).put("timerest", timer.timeRest);
                    jsonArray.getJSONObject(i).put("timerun", timer.timeRun);
                    jsonArray.getJSONObject(i).put("timepause", timer.timePause);
                    jsonArray.getJSONObject(i).put("timercount", timer.timerCount);
                }
            }
            saveJson(context, jsonArray);
        }catch (JSONException e){
            Log.d("StorageData", e.getMessage());
        }
        return null;
    }

    private static void saveJson(Context context, JSONArray json) {
        SharedPreferences.Editor spe = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).edit();
        spe.putString("json3", json.toString());
        spe.commit();
        Log.d("StorageData", "saveJson " + json.toString());
    }

    private static JSONArray getJson(Context context) {
        SharedPreferences sp = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        String json = sp.getString("json3", "");
        Log.d("StorageData", "getJson " + json.toString());
        if(json.length() == 0){
            return new JSONArray();
        } else{
            try {
                return new JSONArray(json);
            } catch (JSONException e) {
                return new JSONArray();
            }
        }
    }

    private static JSONArray remove(final int idx, final JSONArray from) {
        final List<JSONObject> objs = asList(from);
        objs.remove(idx);

        final JSONArray ja = new JSONArray();
        for (final JSONObject obj : objs) {
            ja.put(obj);
        }

        return ja;
    }

    private static List<JSONObject> asList(final JSONArray ja) {
        final int len = ja.length();
        final ArrayList<JSONObject> result = new ArrayList<JSONObject>(len);
        for (int i = 0; i < len; i++) {
            final JSONObject obj = ja.optJSONObject(i);
            if (obj != null) {
                result.add(obj);
            }
        }
        return result;
    }

}
