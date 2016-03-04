/**   
 *    
 * ��Ŀ���ƣ�MemorandumDemo   
 * �����ƣ�EditActivity   
 * ���������༭���� 
 * �����ˣ�Xu/Group Friend 
 * ����ʱ�䣺2015-12-6 22:57:51       
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

	private EditText titleEditText; // �༭����ı༭��
	private EditText contentEditText; // �༭���ݵı༭��
	private ImageButton mBack; // ���ذ�ť
	private ImageButton mSave; // ���水ť
	private ImageButton mSetAlert; // �������Ӱ�ť
	private TextView mAlertTextView; // ��ʾ��������ʱ��
	private TimeSetDialog mTimeSetDialog; // �������ӵĶԻ���

	private String title; // ����
	private String content; // ����
	private String millis; // ������������������ݿ����
	private String tempMillis; // ���������������ݿ����
	private String alertTime = ""; // ��ʼ����ʱ��
	private String tempAlertTime; // ����ʱ��
	private PendingIntent pendingIntent;
	private Calendar calendar; // ����
	private int index = 0; // �±�
	private boolean isNew = false; // ��־λ���ж��Ƿ�Ϊ�½�
	private UserData data;
	private MyDataBaseHelper dbHelper; // ���ݿ�

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
					.setText("���������ѣ�" + Utils.timeTransfer(tempAlertTime));
		} else {
			mAlertTextView.setText("δ�������ѣ�");
		}

	}

	// ��ʼ��
	private void initView() {
		titleEditText = (EditText) findViewById(R.id.ediTextTitle);
		contentEditText = (EditText) findViewById(R.id.editTextContent);
		mBack = (ImageButton) findViewById(R.id.back);
		mSave = (ImageButton) findViewById(R.id.save);
		mSetAlert = (ImageButton) findViewById(R.id.alert);
		mAlertTextView = (TextView) findViewById(R.id.alerttime);

		// ���ü����¼�
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
		// ������津�����¼�
		case R.id.save:
			save();
			break;

		// ������ش������¼�
		case R.id.back:
			onBackPressed();
			break;

		// ����������Ӵ������¼�
		case R.id.alert:
			alert();
			break;
		}
	}

	/**
	 * �����¼�
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(EditActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * ��ת��AlarmReceiver
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
			// ����AlarmManager
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			pendingIntent = PendingIntent.getBroadcast(EditActivity.this, index,
					intent, 0);
			LogUtils.i("pendingintent-alertSet", pendingIntent.toString());
			data.setPendingIntent(pendingIntent);
			// RTC_WAKEUPͬ����ʾ�ö�ʱ����Ĵ���ʱ���1970��1��1��0�㿪ʼ���𣬵��ỽ��CPU��
			// ����1970��1��1��0�������ʱ���ټ����ӳ�ִ�е�ʱ�䡣
			alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
					data.getPendingIntent());
		}
	}

	/**
	 * ��������
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
	 * ��������
	 */
	private void save() {
		ArrayList<UserData> list = Utils.getList();
		// ��þ�����������
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
			// ��������
			alertSet(alertTime);
		}

		// �ж����½����Ǳ༭
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
	 * ���Alert��ť�������¼�
	 */
	private void alert() {
		mTimeSetDialog = new TimeSetDialog(this);
		mTimeSetDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// ��ȡ�����õ�ʱ��
				alertTime = mTimeSetDialog.getAlertTime();
				if (!alertTime.equals("")) {
					mAlertTextView.setText("���������ѣ�"
							+ Utils.timeTransfer(alertTime));
				} else {
					mAlertTextView.setText("δ��������");
				}
				calendar = mTimeSetDialog.getCalendar();
				data.setAlertTime(alertTime);
			}
		});
		mTimeSetDialog.show();
		alertTime = mTimeSetDialog.getAlertTime();
	}
}
