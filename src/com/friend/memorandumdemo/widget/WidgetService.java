/**   
*    
* ��Ŀ���ƣ�MemorandumDemo   
* �����ƣ�WidgetService   
* ��������   
* �����ˣ�Xu/Group Friend  
* ����ʱ�䣺2015-12-7 20:15:57       
*    
*/
package com.friend.memorandumdemo.widget;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.example.memorandumdemo.R;
import com.friend.memorandumdemo.model.UserData;
import com.friend.memorandumdemo.utils.Utils;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class WidgetService extends Service {

	private ArrayList<UserData> lists = Utils.getList(); // ��ȡlist
	private Timer mTimer; // ��ʱ��

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mTimer = new Timer();
		// ������ʱ����
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				updateView();
			}
		}, 0, 1);
	}

	// ��������
	private void updateView() {
		// TODO Auto-generated method stub
		RemoteViews rv = new RemoteViews(getPackageName(),
				R.layout.widget_layout);
		rv.setTextViewText(R.id.widget_text1, "Never Forget");
		rv.setTextViewText(R.id.widget_text2, "����" + lists.size() + "����¼");
		rv.setTextViewText(R.id.widget_text3, "����" + getAlertCount() + "������������");
		rv.setImageViewResource(R.id.widget_image, R.drawable.ic_launcher);
		AppWidgetManager manager = AppWidgetManager
				.getInstance(getApplicationContext());
		ComponentName cn = new ComponentName(getApplicationContext(),
				WidgetProvider.class);
		manager.updateAppWidget(cn, rv);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mTimer = null;
	}

	// ��ȡ�����������Ŀ
	private int getAlertCount() {
		int count = 0;
		for (int i = 0; i < lists.size(); i++) {
			if (lists.get(i).getAlertTime().equals("")) {
				count++;
			}
		}
		return lists.size() - count;
	}

}
