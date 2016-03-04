/**   
 *    
 * ��Ŀ���ƣ�MemorandumDemo   
 * �����ƣ�TimeSetDialog   
 * ����������������ʱ��ĶԻ���
 * �����ˣ�Xu/Group Friend  
 * ����ʱ�䣺2015-12-8 13:01:35     
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

	private Button mPositiveButton; // ȷ����ť
	private Button mNegativeButton; // ȡ����ť
	private Button mCancel; // ȡ�����Ӱ�ť
	private TimePicker timePicker; // ʱ��ѡ����
	private DatePicker datePicker; // ����ѡ����
	private Calendar calendar; // ����
	
	private String alertTime = ""; // Ĭ�ϵ�����ʱ��
	private int selectedHour; // ѡ���Сʱ
	private int selectedMinute; // ѡ��ķ���

	public String getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(String alertTime) {
		this.alertTime = alertTime;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	// ��ʼ��ʱ������
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

		// ���ñ���
		setTitle("��������");

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
		
		// ���õ���¼�
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
