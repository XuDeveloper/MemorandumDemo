/**   
 *    
 * 项目名称：MemorandumDemo   
 * 类名称：EditActivity   
 * 类描述：编辑界面 
 * 创建人：Xu/Group Friend 
 * 创建时间：2015-12-6 22:57:51       
 *    
 */
package com.friend.memorandumdemo.edit;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.memorandumdemo.R;
import com.friend.memorandumdemo.alert.TimeSetDialog;
import com.friend.memorandumdemo.data.Const;
import com.friend.memorandumdemo.main.MainActivity;
import com.friend.memorandumdemo.model.UserData;
import com.friend.memorandumdemo.sqlite.MyDataBaseHelper;
import com.friend.memorandumdemo.sqlite.SQLiteUtils;
import com.friend.memorandumdemo.utils.LogUtils;
import com.friend.memorandumdemo.utils.Utils;

public class EditActivity extends Activity implements OnClickListener {

	private EditText titleEditText; // 编辑标题的编辑框
	private EditText contentEditText; // 编辑内容的编辑框
	private ImageButton mBack; // 返回按钮
	private ImageButton mSave; // 保存按钮
	private ImageButton mSetAlert; // 设置闹钟按钮
	private TextView mAlertTextView; // 显示设置闹钟时间
	private TimeSetDialog mTimeSetDialog; // 设置闹钟的对话框

	private String title; // 标题
	private String content; // 内容
	private String millis; // 具体毫秒数，用于数据库更新
	private String tempMillis; // 毫秒数，用于数据库更新
	private String alertTime = ""; // 初始闹钟时间
	private String tempAlertTime; // 闹钟时间
	private PendingIntent pendingIntent;
	private Calendar calendar; // 日历
	private int index = 0; // 下标
	private boolean isNew = false; // 标志位，判断是否为新建
	private UserData data;
	private MyDataBaseHelper dbHelper; // 数据库

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_activity);
		initView();

		receiveData();

		titleEditText.setText(title);
		titleEditText.setSelection(title.length());

		contentEditText.setText(content);
		contentEditText.setSelection(content.length());

		if (!tempAlertTime.equals("")) {
			mAlertTextView
					.setText("已设置提醒：" + Utils.timeTransfer(tempAlertTime));
		} else {
			mAlertTextView.setText("未设置提醒！");
		}

	}

	// 初始化
	private void initView() {
		titleEditText = (EditText) findViewById(R.id.ediTextTitle);
		contentEditText = (EditText) findViewById(R.id.editTextContent);
		mBack = (ImageButton) findViewById(R.id.back);
		mSave = (ImageButton) findViewById(R.id.save);
		mSetAlert = (ImageButton) findViewById(R.id.alert);
		mAlertTextView = (TextView) findViewById(R.id.alerttime);

		// 设置监听事件
		mBack.setOnClickListener(this);
		mSave.setOnClickListener(this);
		mSetAlert.setOnClickListener(this);

		calendar = Calendar.getInstance();

		dbHelper = new MyDataBaseHelper(EditActivity.this,
				SQLiteUtils.getDataBaseName());

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 点击保存触发的事件
		case R.id.save:
			save();
			break;

		// 点击返回触发的事件
		case R.id.back:
			onBackPressed();
			break;

		// 点击设置闹钟触发的事件
		case R.id.alert:
			alert();
			break;
		}
	}

	/**
	 * 返回事件
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(EditActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 跳转到AlarmReceiver
	 */
	private void alertSet(String alerttime) {
		Intent intent = new Intent("android.intent.action.ALARMRECEIVER");
		data.setIndex(index);
		data.setTitle(titleEditText.getText().toString());
		data.setContent(contentEditText.getText().toString());
		data.setAlertTime(alerttime);
		data.setMillis(String.valueOf(System.currentTimeMillis()));
		intent.putExtra(Const.ALERT_FLAG, data);
		if (Long.parseLong(alerttime) > System.currentTimeMillis()) {
			// 启动AlarmManager
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			pendingIntent = PendingIntent.getBroadcast(EditActivity.this, index,
					intent, 0);
			LogUtils.i("pendingintent-alertSet", pendingIntent.toString());
			data.setPendingIntent(pendingIntent);
			// RTC_WAKEUP同样表示让定时任务的触发时间从1970年1月1日0点开始算起，但会唤醒CPU。
			// 传入1970年1月1日0点至今的时间再加上延迟执行的时间。
			alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
					data.getPendingIntent());
		}
	}

	/**
	 * 接收数据
	 */
	private void receiveData() {
		data = (UserData) getIntent().getSerializableExtra(Const.EDIT_FLAG);
		index = data.getIndex();
		title = data.getTitle();
		tempAlertTime = data.getAlertTime();
		tempMillis = data.getMillis();
		content = data.getContent();
		isNew = data.isNew();
		pendingIntent = data.getPendingIntent();
	}

	/**
	 * 保存数据
	 */
	private void save() {
		ArrayList<UserData> list = Utils.getList();
		// 获得具体标题和内容
		title = titleEditText.getText().toString();
		content = contentEditText.getText().toString();
		millis = String.valueOf(System.currentTimeMillis());

		data = new UserData();

		data.setIndex(index);
		data.setTitle(title);
		data.setContent(content);
		data.setMillis(millis);

		if (!tempAlertTime.equals(alertTime) && !alertTime.equals("")) {
			data.setAlertTime(alertTime);
		} else if (alertTime.equals("")) {
			data.setAlertTime(alertTime);
		} else {
			data.setAlertTime(tempAlertTime);
		}
		
		if (!alertTime.equals("")) {
			// 设置闹钟
			alertSet(alertTime);
		}

		// 判断是新建还是编辑
		if (isNew == true) {
			if (!title.equals("")) {
				list.add(data);
				SQLiteUtils.insert(dbHelper, data);
			} else {
				Intent intent = new Intent(EditActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		} else {
			list.set(index - 1, data);
			SQLiteUtils.delete(dbHelper, tempMillis);
			SQLiteUtils.insert(dbHelper, data);
		}
		Intent intent = new Intent(EditActivity.this, MainActivity.class);
		startActivity(intent);
		finish();

	}

	/**
	 * 点击Alert按钮触发的事件
	 */
	private void alert() {
		mTimeSetDialog = new TimeSetDialog(this);
		mTimeSetDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// 获取已设置的时间
				alertTime = mTimeSetDialog.getAlertTime();
				if (!alertTime.equals("")) {
					mAlertTextView.setText("已设置提醒："
							+ Utils.timeTransfer(alertTime));
				} else {
					mAlertTextView.setText("未设置提醒");
				}
				calendar = mTimeSetDialog.getCalendar();
				data.setAlertTime(alertTime);
			}
		});
		mTimeSetDialog.show();
		alertTime = mTimeSetDialog.getAlertTime();
	}
}
