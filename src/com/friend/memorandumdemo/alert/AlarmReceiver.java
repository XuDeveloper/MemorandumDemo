/**   
 *    
 * ��Ŀ���ƣ�MemorandumDemo   
 * �����ƣ�AlarmReceiver   
 * ���������㲥�����������ڽ�����������   
 * �����ˣ�Xu/Group Friend  
 * ����ʱ�䣺2015-12-7 21:01:35     
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
		// �������ӣ����򱨴�
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		context.startActivity(intent);
	}

}
