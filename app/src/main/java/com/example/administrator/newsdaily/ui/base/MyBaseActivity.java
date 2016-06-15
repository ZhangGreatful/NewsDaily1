package com.example.administrator.newsdaily.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.newsdaily.R;
import com.example.administrator.newsdaily.common.LogUtil;
import com.example.administrator.newsdaily.volley.RequestQueue;
import com.example.administrator.newsdaily.volley.toolbox.Volley;

public class MyBaseActivity extends FragmentActivity {

    // 公共的。。。。 Toast  全屏  宽高  OpenActivity
    private       Toast toast;
    public static int   screenW, screenH;
    public Dialog       dialog;
    public RequestQueue mQueue;

    /**
     * 生命周期调试
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onCreate() ");
        screenW = getWindowManager().getDefaultDisplay().getWidth();
        screenH = getWindowManager().getDefaultDisplay().getHeight();
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onStart() ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onRestart() ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onResume() ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onPause() ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onStop() ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(LogUtil.TAG, getClass().getSimpleName() + "onDestroy() ");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.d(LogUtil.TAG, getClass().getSimpleName()
                + "onSaveInstanceState() ");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtil.d(LogUtil.TAG, getClass().getSimpleName()
                + "onRestoreInstanceState() ");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtil.d(LogUtil.TAG, getClass().getSimpleName()
                + "onConfigurationChanged() ");
    }

    /**
     * 公共功能封装
     * @param resId
     */
    public void showToast(int resId) {
        showToast(getString(resId));
    }

    public void showToast(String msg) {
        if (toast == null)
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }

    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null, null);
    }

    public void openActivity(Class<?> pClass, Bundle bundle) {
        openActivity(pClass, bundle, null);
    }

    public void openActivity(Class<?> pClass, Bundle bundle, Uri uri) {
        Intent intent = new Intent(this, pClass);
        if (bundle != null)
            intent.putExtras(bundle);
        if (uri != null)
            intent.setData(uri);
        startActivity(intent);
        // 增加动画=======
        overridePendingTransition(R.anim.anim_activity_right_in,
                R.anim.anim_activity_bottom_out);
    }

    public void openActivity(String action) {
        openActivity(action, null, null);
    }

    public void openActivity(String action, Bundle bundle) {
        openActivity(action, bundle, null);
    }

    public void openActivity(String action, Bundle bundle, Uri uri) {
        Intent intent = new Intent(action);
        if (bundle != null)
            intent.putExtras(bundle);
        if (uri != null)
            intent.setData(uri);
        startActivity(intent);
        // 增加动画
        overridePendingTransition(R.anim.anim_activity_right_in,
                R.anim.anim_activity_bottom_out);
    }

    public void myFinish() {
        super.finish();
        overridePendingTransition(R.anim.anim_activity_bottom_in,
                R.anim.anim_activity_right_out);
    }

    /**
     * @param context    上下文环境
     * @param msg        消息
     * @param cancelable 是否可取消
     * @return 返回Dialog这个对象
     * @Title: showLoadingDialog
     * @Description: 显示一个等待对话框
     */
    public void showLoadingDialog(Context context, String msg,
                                  boolean cancelable) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        //自定义图片
        ImageView iv_img = (ImageView) v.findViewById(R.id.iv_dialogloading_img);
        // 提示文字
        TextView tv_msg = (TextView) v.findViewById(R.id.tv_dialogloading_msg);
        // 加载动画
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
        // 使用ImageView显示动画
        iv_img.startAnimation(animation);
        if (null != msg) {
            // 设置加载信息
            tv_msg.setText(msg);
        }
        // 创建自定义样式dialog
        dialog = new Dialog(context, R.style.loading_dialog);
        // 不可以用“返回键”取消
        dialog.setCancelable(cancelable);
        // 设置布局
        dialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        //显示dialog
        dialog.show();
    }

    /**
     * @Title: cancelDialog
     * @Description: 取消dialog显示
     * @author hj
     */
    public void cancelDialog() {
        if (null != dialog) {
            dialog.dismiss();
        }
    }
}
