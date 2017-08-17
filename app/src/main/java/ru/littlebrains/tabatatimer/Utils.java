package ru.littlebrains.tabatatimer;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;

public class Utils {

	public static void hideKeyboard(Activity activity) {
		try {
			InputMethodManager imm = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(activity.getWindow().getCurrentFocus()
					.getWindowToken(), 0);
		} catch (Exception e) {
		}
	}

	public static void showKeyboard(Context mContext) {
		((InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE))
				.toggleSoftInput(InputMethodManager.SHOW_FORCED,
						InputMethodManager.HIDE_IMPLICIT_ONLY);
	}


	public static int stringTimeToMinute(String time) {
		try{
			String[] t = time.split(":");
			
			return Integer.valueOf(t[0]) * 60 +  Integer.valueOf(t[1]);
		} catch (Exception e){
			return 0;
		}
	}
	
	public static String minuteToStringTime(int time) {
		try{
			int hourse = time/60;
			int minute = time-hourse*60;
			return (hourse < 9 ? "0" + hourse : hourse) +":"+(minute < 9 ? "0" + minute : minute) ;
		} catch (Exception e){
			return "";
		}
	}
}
