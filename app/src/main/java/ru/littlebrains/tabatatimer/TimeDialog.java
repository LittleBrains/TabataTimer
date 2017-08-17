package ru.littlebrains.tabatatimer;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class TimeDialog implements OnTimeSetListener, OnClickListener, OnFocusChangeListener  { 

	public static final int START = 0;
	public static final int END = 1;
    private Context mContext;  
    private EditText btn;  
    private TimePickerDialog tpd;
	

    public TimeDialog(Context mContext, EditText btn){  
        this.mContext = mContext;
    	this.btn = btn; 
    }  
          
    @Override
	public void onTimeSet(TimePicker view, int hour, int minut) {
    	if (!view.isShown()) return;
    	Log.i("", addNol(hour) + ":" + addNol(minut));
    	btn.setText(addNol(hour) + ":" + addNol(minut));  
        btn.setTag(addNol(hour) + ":" + addNol(minut));
	}
    
    private String addNol(int val) {
		return val < 10 ? "0" + val : "" + val;
	}

    @Override  
    public void onClick(View v) {  
    	
    	try{
	    	String time = (String) v.getTag();
	    	int h = Integer.valueOf(time.split(":")[0]);
	    	int m = Integer.valueOf(time.split(":")[1]);
	        tpd = new TimePickerDialog(mContext, this, h, m, true);
	        tpd.show(); 
    	}
    	catch(Exception e){
    		tpd = new TimePickerDialog(mContext, this, 0, 0, true);
            tpd.show(); 
    	}
    }

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(!hasFocus) return;
		try{
	    	String time = (String) v.getTag();
	    	int h = Integer.valueOf(time.split(":")[0]);
	    	int m = Integer.valueOf(time.split(":")[1]);
	        tpd = new TimePickerDialog(mContext, this, h, m, true);
	        tpd.show(); 
    	}
    	catch(Exception e){
    		tpd = new TimePickerDialog(mContext, this, 0, 0, true);
            tpd.show(); 
    	}
	} 
}
