package com.example.administrator.newsdaily.onkeyshare;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.FakeActivity;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;

import static cn.sharesdk.framework.utils.BitmapHelper.captureView;
import static cn.sharesdk.framework.utils.R.getBitmapRes;
import static cn.sharesdk.framework.utils.R.getStringRes;

/**
 * ��ݷ�������
 * <p/>
 * ͨ����ͬ��setter���ò�����Ȼ�����{@link #show(Context)}����������ݷ���
 */
public class OnekeyShare extends FakeActivity implements
        OnClickListener, PlatformActionListener, Callback {
    private static final int MSG_TOAST            = 1;
    private static final int MSG_ACTION_CCALLBACK = 2;
    private static final int MSG_CANCEL_NOTIFY    = 3;
    // ҳ��
    private FrameLayout                   flPage;
    // �����б�
    private PlatformGridView              grid;
    // ȡ����ť
    private Button                        btnCancel;
    // �������Ķ���
    private Animation                     animShow;
    // ����ȥ�Ķ���
    private Animation                     animHide;
    private boolean                       finishing;
    private boolean                       canceled;
    private HashMap<String, Object>       reqMap;
    private ArrayList<CustomerLogo>       customers;
    private int                           notifyIcon;
    private String                        notifyTitle;
    private boolean                       silent;
    private PlatformActionListener        callback;
    private ShareContentCustomizeCallback customizeCallback;
    private boolean                       dialogMode;
    private boolean                       disableSSO;
    private HashMap<String, String>       hiddenPlatforms;
    private View                          bgView;

    public OnekeyShare() {
        reqMap = new HashMap<String, Object>();
        customers = new ArrayList<CustomerLogo>();
        callback = this;
        hiddenPlatforms = new HashMap<String, String>();
    }

    public void show(Context context) {
        ShareSDK.initSDK(context);
        super.show(context, null);
    }

    /**
     * ����ʱNotification��ͼ�������
     */
    public void setNotification(int icon, String title) {
        notifyIcon = icon;
        notifyTitle = title;
    }

    /**
     * address�ǽ����˵�ַ��������Ϣ���ʼ�ʹ�ã�������Բ��ṩ
     */
    public void setAddress(String address) {
        reqMap.put("address", address);
    }

    /**
     * title���⣬��ӡ��ʼǡ����䡢��Ϣ��΢�ţ��������ѡ�����Ȧ���ղأ���
     * ���ţ��������ѡ�����Ȧ������������QQ�ռ�ʹ�ã�������Բ��ṩ
     */
    public void setTitle(String title) {
        reqMap.put("title", title);
    }

    /**
     * titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ�ã�������Բ��ṩ
     */
    public void setTitleUrl(String titleUrl) {
        reqMap.put("titleUrl", titleUrl);
    }

    /**
     * text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
     */
    public void setText(String text) {
        reqMap.put("text", text);
    }

    /**
     * ��ȡtext�ֶε�ֵ
     */
    public String getText() {
        return reqMap.containsKey("text") ? String.valueOf(reqMap.get("text")) : null;
    }

    /**
     * imagePath�Ǳ��ص�ͼƬ·������Linked-In�������ƽ̨��֧������ֶ�
     */
    public void setImagePath(String imagePath) {
        if (!TextUtils.isEmpty(imagePath))
            reqMap.put("imagePath", imagePath);
    }

    /**
     * imageUrl��ͼƬ������·��������΢������������QQ�ռ��Linked-In֧�ִ��ֶ�
     */
    public void setImageUrl(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl))
            reqMap.put("imageUrl", imageUrl);
    }

    /**
     * url��΢�ţ��������ѡ�����Ȧ�ղأ������ţ��������Ѻ�����Ȧ����ʹ�ã�������Բ��ṩ
     */
    public void setUrl(String url) {
        reqMap.put("url", url);
    }

    /**
     * filePath�Ǵ�����Ӧ�ó���ı���·��������΢�ţ����ţ����Ѻ�Dropbox��ʹ�ã�������Բ��ṩ
     */
    public void setFilePath(String filePath) {
        reqMap.put("filePath", filePath);
    }

    /**
     * comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ�ã�������Բ��ṩ
     */
    public void setComment(String comment) {
        reqMap.put("comment", comment);
    }

    /**
     * site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ�ã�������Բ��ṩ
     */
    public void setSite(String site) {
        reqMap.put("site", site);
    }

    /**
     * siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ�ã�������Բ��ṩ
     */
    public void setSiteUrl(String siteUrl) {
        reqMap.put("siteUrl", siteUrl);
    }

    /**
     * foursquare����ʱ�ĵط���
     */
    public void setVenueName(String venueName) {
        reqMap.put("venueName", venueName);
    }

    /**
     * foursquare����ʱ�ĵط�����
     */
    public void setVenueDescription(String venueDescription) {
        reqMap.put("venueDescription", venueDescription);
    }

    /**
     * �����γ�ȣ�����΢������Ѷ΢����foursquare֧�ִ��ֶ�
     */
    public void setLatitude(float latitude) {
        reqMap.put("latitude", latitude);
    }

    /**
     * ����ؾ��ȣ�����΢������Ѷ΢����foursquare֧�ִ��ֶ�
     */
    public void setLongitude(float longitude) {
        reqMap.put("longitude", longitude);
    }

    /**
     * �Ƿ�ֱ�ӷ���
     */
    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    /**
     * ���ñ༭ҳ�ĳ�ʼ��ѡ��ƽ̨
     */
    public void setPlatform(String platform) {
        reqMap.put("platform", platform);
    }

    /**
     * �����Զ�����ⲿ�ص�
     */
    public void setCallback(PlatformActionListener callback) {
        this.callback = callback;
    }

    /**
     * ���ز����ص�
     */
    public PlatformActionListener getCallback() {
        return callback;
    }

    /**
     * �������ڷ�������У����ݲ�ͬƽ̨�Զ���������ݵĻص�
     */
    public void setShareContentCustomizeCallback(ShareContentCustomizeCallback callback) {
        customizeCallback = callback;
    }

    /**
     * �����Զ���������ݵĻص�
     */
    public ShareContentCustomizeCallback getShareContentCustomizeCallback() {
        return customizeCallback;
    }

    /**
     * �����Լ�ͼ��͵���¼��������ظ�������Ӷ��
     */
    public void setCustomerLogo(Bitmap logo, String label, OnClickListener ocListener) {
        CustomerLogo cl = new CustomerLogo();
        cl.label = label;
        cl.logo = logo;
        cl.listener = ocListener;
        customers.add(cl);
    }

    /**
     * ����һ���ܿ��أ������ڷ���ǰ����Ҫ��Ȩ�������sso����
     */
    public void disableSSOWhenAuthorize() {
        disableSSO = true;
    }

    /**
     * ���ñ༭ҳ�����ʾģʽΪDialogģʽ
     */
    public void setDialogMode() {
        dialogMode = true;
        reqMap.put("dialogMode", dialogMode);
    }

    /**
     * ���һ�����ص�platform
     */
    public void addHiddenPlatform(String platform) {
        hiddenPlatforms.put(platform, platform);
    }

    /**
     * ����һ��������ͼ�����View
     */
    public void setViewToShare(View viewToShare) {
        try {
            Bitmap bm = captureView(viewToShare, viewToShare.getWidth(), viewToShare.getHeight());
            reqMap.put("viewToShare", bm);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void setEditPageBackground(View bgView) {
        this.bgView = bgView;
    }

    public void onCreate() {
        // ��ʾ��ʽ����platform��silent�����ֶο��Ƶ�
        // ���platform�����ˣ���������ʾ�Ź��񣬷��򶼻���ʾ��
        // ���silentΪtrue����ʾ������༭ҳ�棬�������롣
        // ����ֻ�ж�platform����Ϊ�Ź�����ʾ�Ժ��¼�����PlatformGridView����
        // ��platform��silent��Ϊtrue����ֱ�ӽ������
        // ��platform�����ˣ�����silentΪfalse�����ж��Ƿ��ǡ�ʹ�ÿͻ��˷�����ƽ̨��
        // ��Ϊ��ʹ�ÿͻ��˷�����ƽ̨����ֱ�ӷ����������༭ҳ��
        HashMap<String, Object> copy = new HashMap<String, Object>();
        copy.putAll(reqMap);
        if (copy.containsKey("platform")) {
            String name = String.valueOf(copy.get("platform"));
            if (silent) {
                HashMap<Platform, HashMap<String, Object>> shareData
                        = new HashMap<Platform, HashMap<String, Object>>();
                shareData.put(ShareSDK.getPlatform(name), copy);
                share(shareData);
            } else if (ShareCore.isUseClientToShare(name)) {
                HashMap<Platform, HashMap<String, Object>> shareData
                        = new HashMap<Platform, HashMap<String, Object>>();
                shareData.put(ShareSDK.getPlatform(name), copy);
                share(shareData);
            } else {
                Platform pp = ShareSDK.getPlatform(name);
                if (pp instanceof CustomPlatform) {
                    HashMap<Platform, HashMap<String, Object>> shareData
                            = new HashMap<Platform, HashMap<String, Object>>();
                    shareData.put(ShareSDK.getPlatform(name), copy);
                    share(shareData);
                } else {
                    EditPage page = new EditPage();
                    page.setBackGround(bgView);
                    bgView = null;
                    page.setShareData(copy);
                    if (dialogMode) {
                        page.setDialogMode();
                    }
                    page.showForResult(activity, null, new FakeActivity() {
                        public void onResult(HashMap<String, Object> data) {
                            if (data != null && data.containsKey("editRes")) {
                                @SuppressWarnings("unchecked")
                                HashMap<Platform, HashMap<String, Object>> editRes
                                        = (HashMap<Platform, HashMap<String, Object>>) data.get("editRes");
                                share(editRes);
                            }
                        }
                    });
                }
            }
            finish();
            return;
        }

        finishing = false;
        canceled = false;
        initPageView();
        initAnim();
        activity.setContentView(flPage);

        // ���ù����б�����
        grid.setData(copy, silent);
        grid.setHiddenPlatforms(hiddenPlatforms);
        grid.setCustomerLogos(customers);
        grid.setParent(this);
        btnCancel.setOnClickListener(this);

        // ��ʾ�б�
        flPage.clearAnimation();
        flPage.startAnimation(animShow);

        // �򿪷���˵���ͳ��
        ShareSDK.logDemoEvent(1, null);
    }

    private void initPageView() {
        flPage = new FrameLayout(getContext());
        flPage.setOnClickListener(this);

        // �����б��������Ϊ�ˡ��¶��롱�����ⲿ������һ��FrameLayout
        LinearLayout llPage = new LinearLayout(getContext()) {
            public boolean onTouchEvent(MotionEvent event) {
                return true;
            }
        };
        llPage.setOrientation(LinearLayout.VERTICAL);
        int resId = getBitmapRes(getContext(), "share_vp_back");
        if (resId > 0) {
            llPage.setBackgroundResource(resId);
        }
        LayoutParams lpLl = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lpLl.gravity = Gravity.BOTTOM;
        llPage.setLayoutParams(lpLl);
        flPage.addView(llPage);

        // �����б�
        grid = new PlatformGridView(getContext());
        grid.setEditPageBackground(bgView);
        LinearLayout.LayoutParams lpWg = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        grid.setLayoutParams(lpWg);
        llPage.addView(grid);

        // ȡ����ť
        btnCancel = new Button(getContext());
        btnCancel.setTextColor(0xffffffff);
        btnCancel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        resId = getStringRes(getContext(), "cancel");
        if (resId > 0) {
            btnCancel.setText(resId);
        }
        btnCancel.setPadding(0, 0, 0, cn.sharesdk.framework.utils.R.dipToPx(getContext(), 5));
        resId = getBitmapRes(getContext(), "btn_cancel_back");
        if (resId > 0) {
            btnCancel.setBackgroundResource(resId);
        }
        LinearLayout.LayoutParams lpBtn = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, cn.sharesdk.framework.utils.R.dipToPx(getContext(), 45));
        int dp_10 = cn.sharesdk.framework.utils.R.dipToPx(getContext(), 10);
        lpBtn.setMargins(dp_10, dp_10, dp_10, dp_10);
        btnCancel.setLayoutParams(lpBtn);
        llPage.addView(btnCancel);
    }

    private void initAnim() {
        animShow = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0);
        animShow.setDuration(300);

        animHide = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1);
        animHide.setDuration(300);
    }

    public void onClick(View v) {
        if (v.equals(flPage) || v.equals(btnCancel)) {
            canceled = true;
            finish();
        }
    }

    public boolean onKeyEvent(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            canceled = true;
        }
        return super.onKeyEvent(keyCode, event);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (grid != null) {
            grid.onConfigurationChanged();
        }
    }

    public boolean onFinish() {
        if (finishing) {
            return super.onFinish();
        }

        if (animHide == null) {
            finishing = true;
            super.finish();
            return super.onFinish();
        }

        // ȡ������˵���ͳ��
        if (canceled) {
            ShareSDK.logDemoEvent(2, null);
        }
        finishing = true;
        animHide.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                flPage.setVisibility(View.GONE);
                OnekeyShare.super.finish();
            }
        });
        flPage.clearAnimation();
        flPage.startAnimation(animHide);
        return super.onFinish();
    }

    /**
     * ѭ��ִ�з���
     */
    public void share(HashMap<Platform, HashMap<String, Object>> shareData) {
        boolean started = false;
        for (Entry<Platform, HashMap<String, Object>> ent : shareData.entrySet()) {
            Platform plat = ent.getKey();
            plat.SSOSetting(disableSSO);
            String name = plat.getName();
            boolean isWechat = "WechatMoments".equals(name) || "Wechat".equals(name)
                    || "WechatFavorite".equals(name);
            if (isWechat && !plat.isValid()) {
                Message msg = new Message();
                msg.what = MSG_TOAST;
                int resId = getStringRes(getContext(), "wechat_client_inavailable");
                msg.obj = activity.getString(resId);
                UIHandler.sendMessage(msg, this);
                continue;
            }

            boolean isGooglePlus = "GooglePlus".equals(name);
            if (isGooglePlus && !plat.isValid()) {
                Message msg = new Message();
                msg.what = MSG_TOAST;
                int resId = getStringRes(getContext(), "google_plus_client_inavailable");
                msg.obj = activity.getString(resId);
                UIHandler.sendMessage(msg, this);
                continue;
            }

            boolean isPinterest = "Pinterest".equals(name);
            if (isPinterest && !plat.isValid()) {
                Message msg = new Message();
                msg.what = MSG_TOAST;
                int resId = getStringRes(getContext(), "pinterest_client_inavailable");
                msg.obj = activity.getString(resId);
                UIHandler.sendMessage(msg, this);
                continue;
            }

            if ("Instagram".equals(name)) {
                Intent test = new Intent(Intent.ACTION_SEND);
                test.setPackage("com.instagram.android");
                test.setType("image/*");
                ResolveInfo ri = activity.getPackageManager().resolveActivity(test, 0);
                if (ri == null) {
                    Message msg = new Message();
                    msg.what = MSG_TOAST;
                    int resId = getStringRes(getContext(), "instagram_client_inavailable");
                    msg.obj = activity.getString(resId);
                    UIHandler.sendMessage(msg, this);
                    continue;
                }
            }

            boolean isYixin = "YixinMoments".equals(name) || "Yixin".equals(name);
            if (isYixin && !plat.isValid()) {
                Message msg = new Message();
                msg.what = MSG_TOAST;
                int resId = getStringRes(getContext(), "yixin_client_inavailable");
                msg.obj = activity.getString(resId);
                UIHandler.sendMessage(msg, this);
                continue;
            }

            HashMap<String, Object> data = ent.getValue();
            int shareType = Platform.SHARE_TEXT;
            String imagePath = String.valueOf(data.get("imagePath"));
            if (imagePath != null && (new File(imagePath)).exists()) {
                shareType = Platform.SHARE_IMAGE;
                if (imagePath.endsWith(".gif")) {
                    shareType = Platform.SHARE_EMOJI;
                } else if (data.containsKey("url") && !TextUtils.isEmpty(data.get("url").toString())) {
                    shareType = Platform.SHARE_WEBPAGE;
                }
            } else {
                Bitmap viewToShare = (Bitmap) data.get("viewToShare");
                if (viewToShare != null && !viewToShare.isRecycled()) {
                    shareType = Platform.SHARE_IMAGE;
                    if (data.containsKey("url")) {
                        Object url = data.get("url");
                        if (url != null && !TextUtils.isEmpty(url.toString())) {
                            shareType = Platform.SHARE_WEBPAGE;
                        }
                    }
                } else {
                    Object imageUrl = data.get("imageUrl");
                    if (imageUrl != null && !TextUtils.isEmpty(String.valueOf(imageUrl))) {
                        shareType = Platform.SHARE_IMAGE;
                        if (String.valueOf(imageUrl).endsWith(".gif")) {
                            shareType = Platform.SHARE_EMOJI;
                        } else if (data.containsKey("url")) {
                            Object url = data.get("url");
                            if (url != null && !TextUtils.isEmpty(url.toString())) {
                                shareType = Platform.SHARE_WEBPAGE;
                            }
                        }
                    }
                }
            }
            data.put("shareType", shareType);

            if (!started) {
                started = true;
                if (equals(callback)) {
                    int resId = getStringRes(getContext(), "sharing");
                    if (resId > 0) {
                        showNotification(2000, getContext().getString(resId));
                    }
                }
                finish();
            }
            plat.setPlatformActionListener(callback);
            ShareCore shareCore = new ShareCore();
            shareCore.setShareContentCustomizeCallback(customizeCallback);
            shareCore.share(plat, data);
        }
    }

    public void onComplete(Platform platform, int action,
                           HashMap<String, Object> res) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    public void onError(Platform platform, int action, Throwable t) {
        t.printStackTrace();

        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);

        // ����ʧ�ܵ�ͳ��
        ShareSDK.logDemoEvent(4, platform);
    }

    public void onCancel(Platform platform, int action) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_TOAST: {
                String text = String.valueOf(msg.obj);
                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_ACTION_CCALLBACK: {
                switch (msg.arg1) {
                    case 1: {
                        // �ɹ�
                        int resId = getStringRes(getContext(), "share_completed");
                        if (resId > 0) {
                            showNotification(2000, getContext().getString(resId));
                        }
                    }
                    break;
                    case 2: {
                        // ʧ��
                        String expName = msg.obj.getClass().getSimpleName();
                        if ("WechatClientNotExistException".equals(expName)
                                || "WechatTimelineNotSupportedException".equals(expName)
                                || "WechatFavoriteNotSupportedException".equals(expName)) {
                            int resId = getStringRes(getContext(), "wechat_client_inavailable");
                            if (resId > 0) {
                                showNotification(2000, getContext().getString(resId));
                            }
                        } else if ("GooglePlusClientNotExistException".equals(expName)) {
                            int resId = getStringRes(getContext(), "google_plus_client_inavailable");
                            if (resId > 0) {
                                showNotification(2000, getContext().getString(resId));
                            }
                        } else if ("QQClientNotExistException".equals(expName)) {
                            int resId = getStringRes(getContext(), "qq_client_inavailable");
                            if (resId > 0) {
                                showNotification(2000, getContext().getString(resId));
                            }
                        } else if ("YixinClientNotExistException".equals(expName)
                                || "YixinTimelineNotSupportedException".equals(expName)) {
                            int resId = getStringRes(getContext(), "yixin_client_inavailable");
                            if (resId > 0) {
                                showNotification(2000, getContext().getString(resId));
                            }
                        } else {
                            int resId = getStringRes(getContext(), "share_failed");
                            if (resId > 0) {
                                showNotification(2000, getContext().getString(resId));
                            }
                        }
                    }
                    break;
                    case 3: {
                        // ȡ��
                        int resId = getStringRes(getContext(), "share_canceled");
                        if (resId > 0) {
                            showNotification(2000, getContext().getString(resId));
                        }
                    }
                    break;
                }
            }
            break;
            case MSG_CANCEL_NOTIFY: {
                NotificationManager nm = (NotificationManager) msg.obj;
                if (nm != null) {
                    nm.cancel(msg.arg1);
                }
            }
            break;
        }
        return false;
    }

    // ��״̬����ʾ�������
    private void showNotification(long cancelTime, String text) {
        try {
            Context app = getContext().getApplicationContext();
            NotificationManager nm = (NotificationManager) app
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            final int id = Integer.MAX_VALUE / 13 + 1;
            nm.cancel(id);

            long when = System.currentTimeMillis();
            Notification notification = new Notification(notifyIcon, text, when);
            PendingIntent pi = PendingIntent.getActivity(app, 0, new Intent(), 0);
            notification.setLatestEventInfo(app, notifyTitle, text, pi);
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            nm.notify(id, notification);

            if (cancelTime > 0) {
                Message msg = new Message();
                msg.what = MSG_CANCEL_NOTIFY;
                msg.obj = nm;
                msg.arg1 = id;
                UIHandler.sendMessageDelayed(msg, cancelTime, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
