/**   
*    
* 项目名称：MemorandumDemo   
* 类名称：WidgetProvider   
* 类描述：WidgetProvider，用来提供桌面小控件   
* 创建人：Xu/Group Friend   
* 创建时间：2015-12-7 21:03:01   
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
		// 启动服务
		context.startService(new Intent(context, WidgetService.class));
	}
	
	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		// 停止服务
		context.stopService(new Intent(context, WidgetService.class));
	}
}
