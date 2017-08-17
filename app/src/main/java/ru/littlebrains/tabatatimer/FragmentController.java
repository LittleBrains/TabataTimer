package ru.littlebrains.tabatatimer;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;

public class FragmentController {

	public static final String UPDATE = "update";
	public static FragmentManager mFragmentManager;
	public static ActionBar actionbar;


	public static void newFragment(Fragment fragment, int tag, boolean canGoBack) {
		newFragment(fragment, String.valueOf(tag), canGoBack);
	}
	public static void newFragment(final Fragment fragment, final String tag, final boolean canGoBack) {
		final int WHAT = 1;
		Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == WHAT) changeFragment(fragment, tag, canGoBack);
			}
		};
		handler.sendEmptyMessage(WHAT);

	}

	private static void changeFragment(Fragment fragment, String tag, boolean canGoBack) {
		if(canGoBack){
            mFragmentManager
					.beginTransaction()
					.replace(R.id.container, fragment, tag)
					.addToBackStack(null)
					.commitAllowingStateLoss();
		} else{
            mFragmentManager.beginTransaction()
					.replace(R.id.container, fragment, tag).commitAllowingStateLoss();
		}
		if(canGoBack && mFragmentManager.getBackStackEntryCount() >= 0){
			actionbar.setDisplayHomeAsUpEnabled(true);
		} else{
			actionbar.setDisplayHomeAsUpEnabled(false);
		}

	}

	public static void clearBackStack(){
		mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}


    public static boolean backFragmet() {
        final int WHAT = 1;
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try {
                    if (msg.what == WHAT) mFragmentManager.popBackStack();
                }catch(IllegalStateException e){

                }
            }
        };
        if (mFragmentManager.getBackStackEntryCount() == 1) {
            actionbar.setDisplayHomeAsUpEnabled(false);
        }
        handler.sendEmptyMessage(WHAT);

        if(mFragmentManager.getBackStackEntryCount() > 0){
            return false;
        }else {
            return true;
        }
    }
}
