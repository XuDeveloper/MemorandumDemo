/**   
 *    
 * ��Ŀ���ƣ�MemorandumDemo   
 * �����ƣ�Utils   
 * ��������Utils�࣬�����һЩ�����ķ����ͱ���   
 * �����ˣ�Xu/Group Friend  
 * ����ʱ�䣺2015-12-7 20:25:50   
 *    
 */
package com.friend.memorandumdemo.utils;

import java.util.ArrayList;
import java.util.Map;

import com.friend.memorandumdemo.data.Const;
import com.friend.memorandumdemo.model.UserData;

import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.format.Time;
import android.util.Log;
import android.util.TypedValue;

public class Utils {

	private static ArrayList<UserData> list = new ArrayList<UserData>(); // ȫ��list

	// ��ȡ������ɫ
	public static String getColor(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"settings", Context.MODE_PRIVATE);
		return sharedPreferences.getString("color", Const.LIGHT_YELLOW);
	}

	// ���ñ�����ɫ
	public static void setColor(Context context, String color) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"settings", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("color", color);
		editor.commit();
	}

	// ��ȡ�����С
	public static int getTextSize(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"settings", Context.MODE_PRIVATE);
		return sharedPreferences.getInt("textsize", Const.TEXTSIZE_MEDIUM);
	}

	// ���������С
	public static void setTextSize(Context context, int size) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"settings", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("textsize", size);
		editor.commit();
	}

	// ����list
	public static ArrayList<UserData> getList() {
		return list;
	}

	// ��ȡ��ǰʱ��
	public static String getTime(Time t) {
		StringBuilder builder = new StringBuilder();
		t.setToNow();
		builder.append((t.month + 1) + "��" + t.monthDay + "�� ");
		if (t.hour < 10 && t.minute < 10) {
			builder.append("0" + t.hour + ":0" + t.minute);
		} else if (t.hour < 10 && t.minute >= 10) {
			builder.append("0" + t.hour + ":" + t.minute);
		} else if (t.hour >= 10 && t.minute < 10) {
			builder.append(t.hour + ":0" + t.minute);
		} else {
			builder.append(t.hour + ":" + t.minute);
		}
		return builder.toString();
	}

	// ��ʱ��ת��Ϊ�����ʽ
	public static String timeTransfer(String i) {
		long seconds = Long.parseLong(i) / 1000;
		long minutes = seconds / 60;
		long currentMinute = minutes % 60;
		long hours = minutes / 60;
		long currentHour = hours % 24;
		long days = hours / 24 + 1;

		int year = 1970;
		while (days >= (isLeapYear(year) ? 366 : 365)) {
			days = days - (isLeapYear(year) ? 366 : 365);
			year++;
		}

		int month = 1;
		while (days >= getNumberOfDaysInMonth(year, month)) {
			days = days - getNumberOfDaysInMonth(year, month);
			month++;
		}

		String time = null;
		long hour = (currentHour + 8) % 24; // currentHour+8����Ϊ�����0ʱ�������й��Ƕ��������Լ�8
		if (hour < 10 && currentMinute < 10) {
			time = "0" + hour + ":0" + currentMinute;
		} else if (hour < 10 && currentMinute >= 10) {
			time = "0" + hour + ":" + currentMinute;
		} else if (hour >= 10 && currentMinute < 10) {
			time = hour + ":0" + currentMinute;
		} else {
			time = hour + ":" + currentMinute;
		}

		return year + "��" + month + "��" + days + "�� " + time;

	}

	private static boolean isLeapYear(int year) {
		return year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
	}

	private static int getNumberOfDaysInMonth(int year, int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12)
			return 31;

		if (month == 4 || month == 6 || month == 9 || month == 11)
			return 30;

		if (month == 2)
			return isLeapYear(year) ? 29 : 28;

		return 0;
	}

	// dpת��Ϊpx
	public static int dp2px(int dp, Context context) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}

}
