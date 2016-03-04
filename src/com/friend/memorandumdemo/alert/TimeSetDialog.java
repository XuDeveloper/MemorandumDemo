/**   
 *    
 * 项目名称：MemorandumDemo   
 * 类名称：TimeSetDialog   
 * 类描述：设置提醒时间的对话框
 * 创建人：Xu/Group Friend  
 * 创建时间：2015-12-8 13:01:35     
 *    
 */
package com.friend.memorandumdemo.alert;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.example.memorandumdemo.R;

public class TimeSetDialog extends Dialog {

	private Button mPositiveButton; // 确定按钮
	private Button mNegativeButton; // 取消按钮
	private Button mCancel; // 取消闹钟按钮
	private TimePicker timePicker; // 时间选择器
	private DatePicker datePicker; // 日期选择器
	private Calendar calendar; // 日历
	
	private String alertTime = ""; // 默认的提醒时间
	private int selectedHour; // 选择的小时
	private int selectedMinute; // 选择的分钟

	public String getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(String alertTime) {
		this.alertTime = alertTime;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	// 初始化时间设置
	private void init() {
		calendar.setTimeInMillis(System.currentTimeMillis());
		datePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), null);
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
		selectedHour = timePicker.getCurrentHour();
		selectedMinute = timePicker.getCurrentMinute();
	}

	public TimeSetDialog(Context context) {
		super(context);
		setContentView(R.layout.timeset_view);

		// 设置标题
		setTitle("设置提醒");

		calendar = Calendar.getInstance();
		timePicker = (TimePicker) findViewById(R.id.timePicker);
		datePicker = (DatePicker) findViewById(R.id.datePicker);
		mPositiveButton = (Button) findViewById(R.id.positiveButton);
		mNegativeButton = (Button) findViewById(R.id.negativeButton);
		mCancel = (Button) findViewById(R.id.cancel_alert);

		init();

		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				selectedHour = hourOfDay;
				selectedMinute = minute;
			}
		});
		
		// 设置点击事件
		mPositiveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				calendar.set(Calendar.YEAR, datePicker.getYear());
				calendar.set(Calendar.MONTH, datePicker.getMonth());
				calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
				calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
				calendar.set(Calendar.MINUTE, selectedMinute);
				setAlertTime(calendar.getTimeInMillis() + "");
				cancel();
			}
		});

		mNegativeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setAlertTime(calendar.getTimeInMillis() + "");
				dismiss();
			}
		});

		mCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setAlertTime("");
				cancel();
			}
		});

	}


}
