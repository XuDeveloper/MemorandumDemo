/**   
 *    
 * 项目名称：MemorandumDemo   
 * 类名称：MyDataBaseHelper   
 * 类描述：数据库类
 * 创建人：Xu/Group Friend  
 * 创建时间：2015-12-7 18:26:22      
 *    
 */
package com.friend.memorandumdemo.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1; // 版本号
	
	// 建表语句
	public static final String CREATE = "create table User ("
			+ "_index integer, " + "title text, " + "content text, "
			+ "alerttime text, " + "millis text)";

	public MyDataBaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, VERSION);
	}

	public MyDataBaseHelper(Context context, String name, int version) {
		this(context, name, null, VERSION);
	}

	public MyDataBaseHelper(Context context, String name) {
		this(context, name, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 使用execSQL函数执行SQL语句
		db.execSQL(CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
