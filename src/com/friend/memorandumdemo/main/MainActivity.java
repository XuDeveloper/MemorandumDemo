/**   
 *    
 * 项目名称：MemorandumDemo   
 * 类名称：MainActivity   
 * 类描述：主界面，里面包含有一个主要的ListView（可滑到出现删除按钮的ListView） ，以及左侧的DrawerLayout抽屉布局  
 * 创建人：Xu/Group Friend 
 * 创建时间：2015-12-6 22:55:51       
 *    
 */
package com.friend.memorandumdemo.main;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memorandumdemo.R;
import com.friend.memorandumdemo.data.Const;
import com.friend.memorandumdemo.edit.EditActivity;
import com.friend.memorandumdemo.model.UserData;
import com.friend.memorandumdemo.myui.MyAdapter;
import com.friend.memorandumdemo.myui.SwipeMenu;
import com.friend.memorandumdemo.myui.SwipeMenuCreator;
import com.friend.memorandumdemo.myui.SwipeMenuItem;
import com.friend.memorandumdemo.myui.SwipeMenuListView;
import com.friend.memorandumdemo.myui.SwipeMenuListView.OnMenuItemClickListener;
import com.friend.memorandumdemo.sqlite.MyDataBaseHelper;
import com.friend.memorandumdemo.sqlite.SQLiteUtils;
import com.friend.memorandumdemo.utils.Utils;

public class MainActivity extends Activity {

	private DrawerLayout drawerLayout; // drawerlayout界面
	private LinearLayout mainLayout; // 主layout
	private SwipeMenuListView mainListView; // 主布局中的ListView
	private ListView leftListView; // 抽屉布局中的ListView
	private ImageButton addButton; // 新建按钮
	private ImageButton leftDrawerButton; // 开启左侧抽屉按钮
	private TextView mainTitle; // 主标题
	private TextView listViewTitle; // 副标题
	private ArrayList<UserData> menuList = Utils.getList(); // 获取主界面ListView中的ArrayList
	private MyAdapter mainAdapter; // 主界面ListView中的适配器
	private ArrayAdapter<String> leftAdapter; // 抽屉布局ListView的适配器
	private MyDrawerToggle mDrawerToggle; // 自定义ActionBarDrawerToggle
	private MyDataBaseHelper dbHelper = new MyDataBaseHelper(MainActivity.this,
			SQLiteUtils.getDataBaseName()); // 创建数据库

	private String color; // 默认的主界面颜色
	private int size; // 默认的字体大小
	private boolean isNew = false; // 标志位，以判断是新建还是编辑
	private int sel_bg = Const.LIGHT_YELLOW_INDEX; // 初始背景颜色的下标
	private int sel_ts = Const.TEXTSIZE_BIG_INDEX; // 初始字体大小的下标

	// 创建一个Handler来更新应用程序里的UI元素
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 判断msg.what来判定是进行哪种改变UI效果操作
			switch (msg.what) {
			// 改变颜色
			case Const.CHANGE_COLOR:
				color = (String) msg.obj;
				Utils.setColor(MainActivity.this, color);
				mainListView.setBackgroundColor(Color.parseColor(color));
				mainLayout.setBackgroundColor(Color.parseColor(color));
				break;

			// 改变字体大小
			case Const.CHANGE_TEXTSIZE:
				size = (Integer) msg.obj;
				Utils.setTextSize(MainActivity.this, size);
				mainAdapter = new MyAdapter(MainActivity.this, size);
				mainListView.setAdapter(mainAdapter);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// 初始化自定义ActionBar
		initCustomActionBar();

		// 初始化主布局
		initView();

		if (menuList.isEmpty()) {
			loadFromDatabase(menuList); // 先检查缓存，若没有数据再从数据库加载
		}

		listViewTitle.setText("共" + menuList.size() + "项");

	}

	// 初始化自定义ActionBar
	private void initCustomActionBar() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		mainLayout = (LinearLayout) findViewById(R.id.main_layout);
		addButton = (ImageButton) findViewById(R.id.add);
		leftDrawerButton = (ImageButton) findViewById(R.id.ic_left);
		mainTitle = (TextView) findViewById(R.id.main_text);
		listViewTitle = (TextView) findViewById(R.id.title_text);

		// 设置自定义ActionBar
		getActionBar().setCustomView(R.layout.main_custom_actionbar);
		mDrawerToggle = new MyDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				mainTitle.setText("设置");
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				mainTitle.setText(Const.mTitle);
			}
		};
		drawerLayout.setDrawerListener(mDrawerToggle);

		mainTitle = (TextView) getActionBar().getCustomView().findViewById(
				R.id.main_text);
		addButton = (ImageButton) getActionBar().getCustomView().findViewById(
				R.id.add);
		leftDrawerButton = (ImageButton) getActionBar().getCustomView()
				.findViewById(R.id.ic_left);

		// 设置标题
		mainTitle.setText(Const.mTitle);

		addButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				add();
			}
		});

		leftDrawerButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mDrawerToggle.switchDrawer();
			}
		});

		getActionBar().setDisplayShowHomeEnabled(false);// 去掉导航
		getActionBar().setDisplayShowTitleEnabled(false);// 去掉标题
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setDisplayShowCustomEnabled(true);
	}

	// 初始化布局
	private void initView() {
		mainListView = (SwipeMenuListView) findViewById(R.id.listview_main);
		leftListView = (ListView) findViewById(R.id.listview_left);

		// 设置初始化背景颜色
		color = Utils.getColor(this);
		size = Utils.getTextSize(this);
		mainListView.setBackgroundColor(Color.parseColor(color));
		mainLayout.setBackgroundColor(Color.parseColor(color));

		// 设置主界面ListView的适配器
		mainAdapter = new MyAdapter(MainActivity.this, size);
		mainListView.setAdapter(mainAdapter);
		// 创建一个MenuCreator和一个删除按钮
		createMenuCreatorAndDelete();
		// 设置删除按钮的点击事件
		mainListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				// index = 0表示点击的是第一项即删除按钮
				if (index == 0 && isNew == false) {
					delete(position);
				}
			}
		});

		// 设置编辑备忘录的点击事件
		mainListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView textView = (TextView) view.findViewById(R.id.title);
				if (!textView.getText().toString().equals("")) {
					edit(position);
				}

			}
		});

		// 设置左侧抽屉的ListView的适配器
		leftAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new String[] { "更换背景",
						"字体调整" });
		leftListView.setAdapter(leftAdapter);
		// 设置左侧ListView的点击事件（UI效果更改）
		leftListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					// 显示改变背景颜色的对话框
					createColorChangeDialog();
					break;

				case 1:
					// 显示改变字体大小的对话框
					createTextSizeChangeDialog();
					break;
				}
			}

		});

	}

	/**
	 * 创建左滑的删除按钮
	 */
	private void createMenuCreatorAndDelete() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// 创建删除按钮
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// 设置按钮背景颜色
				deleteItem.setBackground(new ColorDrawable(Color
						.parseColor(Const.RED)));
				// 设置宽度
				deleteItem.setWidth(Utils.dp2px(80, MainActivity.this));
				// 设置图标
				deleteItem.setIcon(R.drawable.ic_delete);
				// 添加到menu中
				menu.addMenuItem(deleteItem);
			}
		};

		mainListView.setMenuCreator(creator);

	}

	/**
	 * 从数据库读取数据
	 */
	private void loadFromDatabase(ArrayList<UserData> list) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("user", new String[] { "_index", "title",
				"content", "alerttime", "millis" }, null, null, null, null,
				null);
		if (cursor.moveToFirst()) {
			do {
				int index = cursor.getInt(cursor.getColumnIndex("_index"));
				String title = cursor.getString(cursor.getColumnIndex("title"));
				String content = cursor.getString(cursor
						.getColumnIndex("content"));
				String alertTime = cursor.getString(cursor
						.getColumnIndex("alerttime"));
				String millis = cursor.getString(cursor
						.getColumnIndex("millis"));
				UserData data = new UserData();
				data.setIndex(index);
				data.setTitle(title);
				data.setContent(content);
				data.setAlertTime(alertTime);
				data.setMillis(millis);
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				final Intent intent = new Intent("android.intent.action.ALARMRECEIVER");
				intent.putExtra(Const.ALERT_FLAG, data);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, index, intent,
						0);
				data.setPendingIntent(pendingIntent);
				if (!data.getAlertTime().equals("")
						&& (Long.parseLong(data.getAlertTime()) > System
								.currentTimeMillis())) {
					alarmManager.set(AlarmManager.RTC_WAKEUP,
							Long.parseLong(data.getAlertTime()),
							data.getPendingIntent());
				} else {
					pendingIntent = null;
				}
				data.setPendingIntent(pendingIntent);
				list.add(data);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
	}

	// 显示改变背景颜色的对话框
	private void createColorChangeDialog() {
		// 初始化四种颜色
		String[] items = { Const.LIGHT_YELLOW_NAME, Const.LIGHT_GREEN_NAME,
				Const.LIGHT_BLUE_NAME, Const.LIGHT_GRAY_NAME };
		// 创建AlertBuilder
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("设置");
		builder.setSingleChoiceItems(items, sel_bg, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					color = Const.LIGHT_YELLOW;
					sel_bg = Const.LIGHT_YELLOW_INDEX;
					break;

				case 1:
					color = Const.LIGHT_GREEN;
					sel_bg = Const.LIGHT_GREEN_INDEX;
					break;

				case 2:
					color = Const.LIGHT_BLUE;
					sel_bg = Const.LIGHT_BLUE_INDEX;
					break;

				case 3:
					color = Const.LIGHT_GRAY;
					sel_bg = Const.LIGHT_GRAY_INDEX;
					break;
				}
			}
		});
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 得到修改后的颜色并发送message通知主进程
				Message message = new Message();
				message.obj = color;
				message.what = Const.CHANGE_COLOR;
				handler.sendMessage(message);
				Toast.makeText(MainActivity.this, "设置成功！", Toast.LENGTH_SHORT)
						.show();
			}

		});
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		AlertDialog dialog = builder.create();
		// 设置打开关闭对话框的效果
		dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
		dialog.show();
	}

	// 显示改变字体大小的对话框
	private void createTextSizeChangeDialog() {
		String[] sizes = { Const.TEXTSIZE_SMALL_NAME,
				Const.TEXTSIZE_MEDIUM_NAME, Const.TEXTSIZE_BIG_NAME };
		// 创建AlertBuilder
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("设置");
		builder.setSingleChoiceItems(sizes, sel_ts, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					size = Const.TEXTSIZE_SMALL;
					sel_ts = Const.TEXTSIZE_SMALL_INDEX;
					break;

				case 1:
					size = Const.TEXTSIZE_MEDIUM;
					sel_ts = Const.TEXTSIZE_MEDIUM_INDEX;
					break;

				case 2:
					size = Const.TEXTSIZE_BIG;
					sel_ts = Const.TEXTSIZE_BIG_INDEX;
					break;
				}
			}
		});

		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Message message = new Message();
				message.obj = size;
				message.what = Const.CHANGE_TEXTSIZE;
				handler.sendMessage(message);
				Toast.makeText(MainActivity.this, "设置成功！", Toast.LENGTH_SHORT)
						.show();
			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		AlertDialog dialog = builder.create();
		// 设置打开关闭对话框的效果
		dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
		dialog.show();
	}

	/**
	 * 添加新的一个备忘录事项
	 */
	private void add() {
		Intent intent = new Intent(MainActivity.this, EditActivity.class);
		UserData data = new UserData();
		data.setTitle("");
		data.setContent("");
		data.setAlertTime("");
		data.setMillis("0");
		data.setNew(true);
		data.setIndex(menuList.size() + 1);
		data.setPendingIntent(null);
		intent.putExtra(Const.EDIT_FLAG, data);
		startActivity(intent);
		finish();
	}

	/**
	 * 删除一个备忘录事项
	 */
	private void delete(int position) {
		UserData data = (UserData) menuList.get(position);
		menuList.remove(position);
		SQLiteUtils.delete(dbHelper, data.getMillis());
		// 取消AlarmManager！
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(data.getPendingIntent());
		mainAdapter.notifyDataSetChanged();
		listViewTitle.setText("共" + menuList.size() + "项");
	}

	/**
	 * 编辑一个备忘录事项
	 */

	private void edit(int position) {
		Intent intent = new Intent(MainActivity.this, EditActivity.class);
		isNew = false;
		UserData userData = (UserData) menuList.get(position);
		// 传输数据到EditActivity
		intent.putExtra(Const.EDIT_FLAG, userData);
		startActivity(intent);
		finish();
	}

}
