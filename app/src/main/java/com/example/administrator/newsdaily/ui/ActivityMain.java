package com.example.administrator.newsdaily.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.newsdaily.R;
import com.example.administrator.newsdaily.ui.base.MyBaseActivity;
import com.example.administrator.newsdaily.view.slidingmenu.SlidingMenu;

/**主界面**/
public class ActivityMain extends MyBaseActivity {
	private Fragment fragmentMenu, fragmentMenuRight;
	private Fragment fragmentType, fragmentMain, fragmentLogin,fragmentRegister,fragmentFavorite,fragmentForgetPass;
	public static SlidingMenu slidingMenu;
	private ImageView iv_set, iv_user;
	private TextView textView_title;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
		textView_title=(TextView) findViewById(R.id.textView1);
		iv_set = (ImageView) findViewById(R.id.imageView_set);
		iv_user = (ImageView) findViewById(R.id.imageView_user);
		iv_set.setOnClickListener(onClickListener);
		iv_user.setOnClickListener(onClickListener);
		initSlidingMenu();
		showFragmentMain();
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageView_set:
				if (slidingMenu != null && slidingMenu.isMenuShowing()) {
					slidingMenu.showContent();
				} else if (slidingMenu != null) {
					slidingMenu.showMenu();
				}
				break;
			case R.id.imageView_user:
				if (slidingMenu != null && slidingMenu.isMenuShowing()) {
					slidingMenu.showContent();
				} else if (slidingMenu != null) {
					slidingMenu.showSecondaryMenu();
				}
				break;
			}
		}
	};
	/**初始化侧滑菜单**/
	public void initSlidingMenu() {
		fragmentMenu = new FragmentMenu();
		fragmentMenuRight = new FragmentMenuRight();
		
		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		
		slidingMenu.setMenu(R.layout.layout_menu);
		slidingMenu.setSecondaryMenu(R.layout.layout_menu_right);
		
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.layout_menu, fragmentMenu).commit();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.layout_menu_right, fragmentMenuRight).commit();
	}

	
	@Override
	public void onBackPressed() {
		if (slidingMenu.isMenuShowing()) {
			slidingMenu.showContent();
		} else {
			exitTwice();
		}
	}
	
	//两次退出
	private boolean isFirstExit=true;
	private void exitTwice(){
		if(isFirstExit){
			Toast.makeText(this, "再按一次退出！", Toast.LENGTH_SHORT).show();
			isFirstExit=false;
			new Thread(){
				public void run() {
					try {
						Thread.sleep(3000);
						isFirstExit=true;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				};
			}.start(); 
		}else{
			finish();
		}
	}

	/**
	 * 显示：“显示新闻更多分类Fragment”
	 */
	public void showFragmentType() {
		setTitle("分类");
		slidingMenu.showContent();
		if (fragmentType == null)
			fragmentType = new FragmentType();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.layout_content, fragmentType).commit();
	}
	/**
	 * 显示:“显示新闻列表的Fragment”
	 */
	public void 	showFragmentMain() {
		setTitle("资讯");
		slidingMenu.showContent();
		if (fragmentMain == null)
			fragmentMain = new FragmentMain();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.layout_content, fragmentMain).commit();
	}
	
	/**
	 * 显示:“登录的Fragment”
	 */
	public void showFragmentLogin() {
		setTitle("用户登录");
		slidingMenu.showContent();
		if (fragmentLogin == null)
			fragmentLogin = new FragmentLogin();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.layout_content, fragmentLogin).commit();
	}

	/**
	 * 显示:“注册的Fragment”
	 */
	public void showFragmentRegister() {
		setTitle("用户注册");
		if (fragmentRegister == null)
			fragmentRegister = new FragmentRegister();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.layout_content, fragmentRegister).commit();
	}
	/**
	 * 显示:“忘记密码的Fragment”
	 */
	public void showFragmentForgetPass() {
		setTitle("忘记密码");
		if (fragmentForgetPass == null)
			fragmentForgetPass = new FragmentForgetPass();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.layout_content, fragmentForgetPass).commit();
	}
	/**
	 * 显示:“显示收藏新闻列表的Fragment”
	 */
	public void showFragmentFavorite() {
		setTitle("收藏新闻");
		slidingMenu.showContent();
		if(fragmentFavorite==null)
			fragmentFavorite=new FragmentFavorite();
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.layout_content, fragmentFavorite).commit();
	}

	/**
	 * 右侧是否登录的切换
	 */
	public void changeFragmentUser() {
			((FragmentMenuRight) fragmentMenuRight).changeView();
	}
	
	/**
	 * 更换当前界面的Title
	 */
	private void setTitle(String title){
		textView_title.setText(title);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}
	
}
