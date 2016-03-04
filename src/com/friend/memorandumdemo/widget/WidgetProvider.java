/**   
*    
* ��Ŀ���ƣ�MemorandumDemo   
* �����ƣ�WidgetProvider   
* ��������WidgetProvider�������ṩ����С�ؼ�   
* �����ˣ�Xu/Group Friend   
* ����ʱ�䣺2015-12-7 21:03:01   
*    
*/
package com.friend.memorandumdemo.widget;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class WidgetProvider extends AppWidgetProvider {

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		// ��������
		context.startService(new Intent(context, WidgetService.class));
	}
	
	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		// ֹͣ����
		context.stopService(new Intent(context, WidgetService.class));
	}
}
