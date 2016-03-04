/**   
 *    
 * ��Ŀ���ƣ�MemorandumDemo   
 * �����ƣ�AlertDialogActivity   
 * ������������Ȩ�ޣ�����������ʱ��������Ļ��������һ���Ի���   
 * �����ˣ�Xu/Group Friend  
 * ����ʱ�䣺2015-12-7 20:01:35     
 *    
 */
package com.friend.memorandumdemo.alert;

import java.io.IOException;

import com.example.memorandumdemo.R;
import com.friend.memorandumdemo.data.Const;
import com.friend.memorandumdemo.edit.EditActivity;
import com.friend.memorandumdemo.model.UserData;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class AlertDialogActivity extends Activity {

	private MediaPlayer mediaPlayer = new MediaPlayer(); // �����������ڲ���Ĭ������
	private PowerManager powerManager; // ��Դ������
	private KeyguardManager keyguardManager; // ���̹�����
	private KeyguardLock keyguardLock; // ������
	private WakeLock mWakeLock; // ������
	private Uri uri; // ����·��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��Ҫ��AndroidManifest��������Ȩ�ޣ�������Ļ
		powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.FULL_WAKE_LOCK, "AlertDialog");
		// ������Ļ
		mWakeLock.acquire();

		// ��������
		keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		keyguardLock = keyguardManager.newKeyguardLock("AlertDialog");
		keyguardLock.disableKeyguard();

		try {
			// ��ȡĬ��������·��
			uri = RingtoneManager.getActualDefaultRingtoneUri(this,
					RingtoneManager.TYPE_ALARM);
			if (uri != null && mediaPlayer != null) {
				mediaPlayer.setDataSource(this, uri);
				mediaPlayer.prepare();
				mediaPlayer.setLooping(false);
				mediaPlayer.start();
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final UserData data = (UserData) getIntent().getSerializableExtra(
				Const.ALERT_FLAG);
		
		// �½��Ի���
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.alert_dialog_name);
		builder.setMessage("�����õ�����ʱ�䵽�ˣ�" + data.getTitle());
		builder.setIcon(R.drawable.ic_launcher);
		builder.setPositiveButton(R.string.positive, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ����Ӧ��
				Intent intent = new Intent(AlertDialogActivity.this,
						EditActivity.class);
				// Bundle bundle = new Bundle();
				// bundle.putString(
				// "title",
				// getIntent().getBundleExtra(
				// "android.intent.action.ALARMRECEIVER")
				// .getString("title"));
				// bundle.putString(
				// "content",
				// getIntent().getBundleExtra(
				// "android.intent.action.ALARMRECEIVER")
				// .getString("content"));
				// bundle.putString(
				// "alerttime",
				// getIntent().getBundleExtra(
				// "android.intent.action.ALARMRECEIVER")
				// .getString("alerttime"));
				// bundle.putString(
				// "millis",
				// getIntent().getBundleExtra(
				// "android.intent.action.ALARMRECEIVER")
				// .getString("millis"));
				// intent.putExtra("android.intent.extra.INTENT", bundle);
				intent.putExtra(Const.EDIT_FLAG, data);
				startActivity(intent); // ����ת����Activity
				finish();
			}
		});
		builder.setNegativeButton(R.string.negative, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mediaPlayer.stop();
				finish();

			}
		});
		builder.show();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
		if (powerManager != null) {
			powerManager = null;
		}
		if (keyguardManager != null) {
			keyguardManager = null;
		}
		if (keyguardLock != null) {
			keyguardLock = null;
		}
	}

}
