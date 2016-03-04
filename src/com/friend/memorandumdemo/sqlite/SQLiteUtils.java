/**   
 *    
 * ��Ŀ���ƣ�MemorandumDemo   
 * �����ƣ�SQLiteUtils   
 * �������� ���ݿ������
 * �����ˣ�Xu/Group Friend
 * ����ʱ�䣺2015-12-7 16:29:24     
 *    
 */
package com.friend.memorandumdemo.sqlite;

import com.friend.memorandumdemo.model.UserData;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteUtils {

	private static final String DATABASE_NAME = "memorandum_db"; // ���ݿ���
	
	public static String getDataBaseName(){
		return DATABASE_NAME;
	}
	
	public static void insert(MyDataBaseHelper dbHelper, UserData userData) {
		ContentValues values = new ContentValues(); // ����ContentValues����
		// ��ö����в����ֵ�ԣ����м���������ֵ��ϣ�����뵽��һ�е�ֵ��ֵ��������ݿ⵱�е���������һ��
		values.put("_index", userData.getIndex());
		values.put("title", userData.getTitle());
		values.put("content", userData.getContent());
		values.put("alerttime", userData.getAlertTime());
		values.put("millis", userData.getMillis());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// ����insert�������Ϳ��Խ����ݲ��뵽���ݿ⵱��
		db.insert("user", null, values);
		db.close();
	}
	
	public static void delete(MyDataBaseHelper dbHelper, String millis) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// ɾ�������������
		// db.delete("users",null,null);
		// �ӱ���ɾ��ָ����һ������
		db.delete("user", "millis=?", new String[]{millis});
		db.close();
	}
	
	
}
