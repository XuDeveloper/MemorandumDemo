/**   
 *    
 * 项目名称：MemorandumDemo   
 * 类名称：UserData   
 * 类描述：此类作为数据模型类 
 * 创建人：Xu/Group Friend  
 * 创建时间：2015-12-6 23:41:49      
 *    
 */
package com.friend.memorandumdemo.model;

import java.io.Serializable;
import java.util.Timer;

import com.friend.memorandumdemo.data.Const;
import com.friend.memorandumdemo.edit.EditActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class UserData implements Serializable {

	private int index;
	private boolean isNew;
	private String title;
	private String content;
	private String alertTime;
	private String millis;
	private PendingIntent pendingIntent;

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(String alertTime) {
		this.alertTime = alertTime;
	}

	public String getMillis() {
		return millis;
	}

	public void setMillis(String millis) {
		this.millis = millis;
	}

	public PendingIntent getPendingIntent() {
		return pendingIntent;
	}

	public void setPendingIntent(PendingIntent pendingIntent) {
		this.pendingIntent = pendingIntent;
	}

}
