/**   
 *    
 * 项目名称：MemorandumDemo   
 * 类名称：AlarmReceiver   
 * 类描述：广播接收器，用于接收闹钟提醒   
 * 创建人：Xu/Group Friend  
 * 创建时间：2015-12-7 21:01:35     
 *    
 */
package com.friend.memorandumdemo.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		intent.setClass(context, AlertDialogActivity.class);
		// 必须增加，否则报错
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		context.startActivity(intent);
	}

}
