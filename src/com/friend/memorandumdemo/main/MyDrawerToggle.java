/**   
 *    
 * 项目名称：MemorandumDemo   
 * 类名称：MyDrawerToggle   
 * 类描述：此类会自定义ActionBarDrawerToggle，以实现自定义ActionBar效果
 * 创建人：Xu/Never Forget  
 * 创建时间：2015-12-10 16:15:20      
 * @version    
 *    
 */
package com.friend.memorandumdemo.main;

import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

public class MyDrawerToggle extends ActionBarDrawerToggle {

	private DrawerLayout mDrawerLayout;

	public MyDrawerToggle(Activity activity, DrawerLayout drawerLayout,
			int drawerImageRes, int openDrawerContentDescRes,
			int closeDrawerContentDescRes) {
		super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes,
				closeDrawerContentDescRes);
		mDrawerLayout = drawerLayout;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	// 将ActionBar上的图标与Drawer结合起来
    	return false;
	}

	public void switchDrawer() {
		if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
		} else {
			mDrawerLayout.openDrawer(GravityCompat.START);
		}
	}

}
