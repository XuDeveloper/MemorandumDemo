/**   
 *    
 * 项目名称：MemorandumDemo   
 * 类名称：SQLiteUtils   
 * 类描述： 数据库操作类
 * 创建人：Xu/Group Friend
 * 创建时间：2015-12-7 16:29:24     
 *    
 */
package com.friend.memorandumdemo.sqlite;

import com.friend.memorandumdemo.model.UserData;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteUtils {

	private static final String DATABASE_NAME = "memorandum_db"; // 数据库名
	
	public static String getDataBaseName(){
		return DATABASE_NAME;
	}
	
	public static void insert(MyDataBaseHelper dbHelper, UserData userData) {
		ContentValues values = new ContentValues(); // 生成ContentValues对象
		// 想该对象当中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致
		values.put("_index", userData.getIndex());
		values.put("title", userData.getTitle());
		values.put("content", userData.getContent());
		values.put("alerttime", userData.getAlertTime());
		values.put("millis", userData.getMillis());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// 调用insert方法，就可以将数据插入到数据库当中
		db.insert("user", null, values);
		db.close();
	}
	
	public static void delete(MyDataBaseHelper dbHelper, String millis) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// 删除表的所有数据
		// db.delete("users",null,null);
		// 从表中删除指定的一条数据
		db.delete("user", "millis=?", new String[]{millis});
		db.close();
	}
	
	
}
