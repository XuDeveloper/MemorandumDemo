/**   
 *    
 * ��Ŀ���ƣ�MemorandumDemo   
 * �����ƣ�MainActivity   
 * �������������棬���������һ����Ҫ��ListView���ɻ�������ɾ����ť��ListView�� ���Լ�����DrawerLayout���벼��  
 * �����ˣ�Xu/Group Friend 
 * ����ʱ�䣺2015-12-6 22:55:51       
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

	private DrawerLayout drawerLayout; // drawerlayout����
	private LinearLayout mainLayout; // ��layout
	private SwipeMenuListView mainListView; // �������е�ListView
	private ListView leftListView; // ���벼���е�ListView
	private ImageButton addButton; // �½���ť
	private ImageButton leftDrawerButton; // ���������밴ť
	private TextView mainTitle; // ������
	private TextView listViewTitle; // ������
	private ArrayList<UserData> menuList = Utils.getList(); // ��ȡ������ListView�е�ArrayList
	private MyAdapter mainAdapter; // ������ListView�е�������
	private ArrayAdapter<String> leftAdapter; // ���벼��ListView��������
	private MyDrawerToggle mDrawerToggle; // �Զ���ActionBarDrawerToggle
	private MyDataBaseHelper dbHelper = new MyDataBaseHelper(MainActivity.this,
			SQLiteUtils.getDataBaseName()); // �������ݿ�

	private String color; // Ĭ�ϵ���������ɫ
	private int size; // Ĭ�ϵ������С
	private boolean isNew = false; // ��־λ�����ж����½����Ǳ༭
	private int sel_bg = Const.LIGHT_YELLOW_INDEX; // ��ʼ������ɫ���±�
	private int sel_ts = Const.TEXTSIZE_BIG_INDEX; // ��ʼ�����С���±�

	// ����һ��Handler������Ӧ�ó������UIԪ��
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// �ж�msg.what���ж��ǽ������ָı�UIЧ������
			switch (msg.what) {
			// �ı���ɫ
			case Const.CHANGE_COLOR:
				color = (String) msg.obj;
				Utils.setColor(MainActivity.this, color);
				mainListView.setBackgroundColor(Color.parseColor(color));
				mainLayout.setBackgroundColor(Color.parseColor(color));
				break;

			// �ı������С
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

		// ��ʼ���Զ���ActionBar
		initCustomActionBar();

		// ��ʼ��������
		initView();

		if (menuList.isEmpty()) {
			loadFromDatabase(menuList); // �ȼ�黺�棬��û�������ٴ����ݿ����
		}

		listViewTitle.setText("��" + menuList.size() + "��");

	}

	// ��ʼ���Զ���ActionBar
	private void initCustomActionBar() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		mainLayout = (LinearLayout) findViewById(R.id.main_layout);
		addButton = (ImageButton) findViewById(R.id.add);
		leftDrawerButton = (ImageButton) findViewById(R.id.ic_left);
		mainTitle = (TextView) findViewById(R.id.main_text);
		listViewTitle = (TextView) findViewById(R.id.title_text);

		// �����Զ���ActionBar
		getActionBar().setCustomView(R.layout.main_custom_actionbar);
		mDrawerToggle = new MyDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				mainTitle.setText("����");
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

		// ���ñ���
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

		getActionBar().setDisplayShowHomeEnabled(false);// ȥ������
		getActionBar().setDisplayShowTitleEnabled(false);// ȥ������
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setDisplayShowCustomEnabled(true);
	}

	// ��ʼ������
	private void initView() {
		mainListView = (SwipeMenuListView) findViewById(R.id.listview_main);
		leftListView = (ListView) findViewById(R.id.listview_left);

		// ���ó�ʼ��������ɫ
		color = Utils.getColor(this);
		size = Utils.getTextSize(this);
		mainListView.setBackgroundColor(Color.parseColor(color));
		mainLayout.setBackgroundColor(Color.parseColor(color));

		// ����������ListView��������
		mainAdapter = new MyAdapter(MainActivity.this, size);
		mainListView.setAdapter(mainAdapter);
		// ����һ��MenuCreator��һ��ɾ����ť
		createMenuCreatorAndDelete();
		// ����ɾ����ť�ĵ���¼�
		mainListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				// index = 0��ʾ������ǵ�һ�ɾ����ť
				if (index == 0 && isNew == false) {
					delete(position);
				}
			}
		});

		// ���ñ༭����¼�ĵ���¼�
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

		// �����������ListView��������
		leftAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new String[] { "��������",
						"�������" });
		leftListView.setAdapter(leftAdapter);
		// �������ListView�ĵ���¼���UIЧ�����ģ�
		leftListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					// ��ʾ�ı䱳����ɫ�ĶԻ���
					createColorChangeDialog();
					break;

				case 1:
					// ��ʾ�ı������С�ĶԻ���
					createTextSizeChangeDialog();
					break;
				}
			}

		});

	}

	/**
	 * �����󻬵�ɾ����ť
	 */
	private void createMenuCreatorAndDelete() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// ����ɾ����ť
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// ���ð�ť������ɫ
				deleteItem.setBackground(new ColorDrawable(Color
						.parseColor(Const.RED)));
				// ���ÿ��
				deleteItem.setWidth(Utils.dp2px(80, MainActivity.this));
				// ����ͼ��
				deleteItem.setIcon(R.drawable.ic_delete);
				// ��ӵ�menu��
				menu.addMenuItem(deleteItem);
			}
		};

		mainListView.setMenuCreator(creator);

	}

	/**
	 * �����ݿ��ȡ����
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

	// ��ʾ�ı䱳����ɫ�ĶԻ���
	private void createColorChangeDialog() {
		// ��ʼ��������ɫ
		String[] items = { Const.LIGHT_YELLOW_NAME, Const.LIGHT_GREEN_NAME,
				Const.LIGHT_BLUE_NAME, Const.LIGHT_GRAY_NAME };
		// ����AlertBuilder
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("����");
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
		builder.setPositiveButton("ȷ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// �õ��޸ĺ����ɫ������message֪ͨ������
				Message message = new Message();
				message.obj = color;
				message.what = Const.CHANGE_COLOR;
				handler.sendMessage(message);
				Toast.makeText(MainActivity.this, "���óɹ���", Toast.LENGTH_SHORT)
						.show();
			}

		});
		builder.setNegativeButton("ȡ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		AlertDialog dialog = builder.create();
		// ���ô򿪹رնԻ����Ч��
		dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
		dialog.show();
	}

	// ��ʾ�ı������С�ĶԻ���
	private void createTextSizeChangeDialog() {
		String[] sizes = { Const.TEXTSIZE_SMALL_NAME,
				Const.TEXTSIZE_MEDIUM_NAME, Const.TEXTSIZE_BIG_NAME };
		// ����AlertBuilder
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("����");
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

		builder.setPositiveButton("ȷ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Message message = new Message();
				message.obj = size;
				message.what = Const.CHANGE_TEXTSIZE;
				handler.sendMessage(message);
				Toast.makeText(MainActivity.this, "���óɹ���", Toast.LENGTH_SHORT)
						.show();
			}
		});

		builder.setNegativeButton("ȡ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		AlertDialog dialog = builder.create();
		// ���ô򿪹رնԻ����Ч��
		dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
		dialog.show();
	}

	/**
	 * ����µ�һ������¼����
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
	 * ɾ��һ������¼����
	 */
	private void delete(int position) {
		UserData data = (UserData) menuList.get(position);
		menuList.remove(position);
		SQLiteUtils.delete(dbHelper, data.getMillis());
		// ȡ��AlarmManager��
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(data.getPendingIntent());
		mainAdapter.notifyDataSetChanged();
		listViewTitle.setText("��" + menuList.size() + "��");
	}

	/**
	 * �༭һ������¼����
	 */

	private void edit(int position) {
		Intent intent = new Intent(MainActivity.this, EditActivity.class);
		isNew = false;
		UserData userData = (UserData) menuList.get(position);
		// �������ݵ�EditActivity
		intent.putExtra(Const.EDIT_FLAG, userData);
		startActivity(intent);
		finish();
	}

}
