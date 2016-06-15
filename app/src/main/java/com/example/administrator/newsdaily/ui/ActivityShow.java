package com.example.administrator.newsdaily.ui;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.newsdaily.R;
import com.example.administrator.newsdaily.common.CommonUtil;
import com.example.administrator.newsdaily.common.LogUtil;
import com.example.administrator.newsdaily.common.SystemUtils;
import com.example.administrator.newsdaily.model.biz.CommentsManager;
import com.example.administrator.newsdaily.model.biz.parser.ParserComments;
import com.example.administrator.newsdaily.model.dao.NewsDBManager;
import com.example.administrator.newsdaily.model.entity.News;
import com.example.administrator.newsdaily.model.httpclient.AsyncHttpClient;
import com.example.administrator.newsdaily.model.httpclient.TextHttpResponseHandler;
import com.example.administrator.newsdaily.ui.base.MyBaseActivity;
import com.example.administrator.newsdaily.volley.Response.ErrorListener;
import com.example.administrator.newsdaily.volley.Response.Listener;
import com.example.administrator.newsdaily.volley.VolleyError;

import org.apache.http.Header;

/***
 * 新闻的具体界面
 */
public class ActivityShow extends MyBaseActivity {
    private WebView     webView;
    private ProgressBar progressBar;
    private TextView    tv_commentCount;
    private ImageView   imageViewBack;
    private ImageView   imageViewMenu;
    private News        newsitem;

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SystemUtils.getInstance(this).isNetConn()) {
            setContentView(R.layout.oh_no);
        } else {
            setContentView(R.layout.activity_show);
            tv_commentCount = (TextView) findViewById(R.id.textView2);
            progressBar = (ProgressBar) findViewById(R.id.progressBar1);
            webView = (WebView) findViewById(R.id.webView1);
            imageViewBack = (ImageView) findViewById(R.id.imageView_back);
            imageViewMenu = (ImageView) findViewById(R.id.imageView_menu);
            newsitem = (News) getIntent().getSerializableExtra("newsitem");

            webView.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
            WebChromeClient client = new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    progressBar.setProgress(newProgress);
                    if (newProgress >= 100) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            };
            webView.setWebChromeClient(client);
            webView.loadUrl(newsitem.getLink());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSupportZoom(true);
            // 设置出现缩放工具
            webView.getSettings().setBuiltInZoomControls(true);
            //扩大比例的缩放
            webView.getSettings().setUseWideViewPort(true);
            //自适应屏幕
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webView.getSettings().setLoadWithOverviewMode(true);
            tv_commentCount.setOnClickListener(clickListener);
            imageViewBack.setOnClickListener(clickListener);
            imageViewMenu.setOnClickListener(clickListener);

            initPopupWindow();
            getCommentCountVolley();
        }
    }

    private void initPopupWindow() {
        View popview = getLayoutInflater().inflate(R.layout.item_pop_save, null);
        popupWindow = new PopupWindow(popview, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        TextView tv_savelocal = (TextView) popview.findViewById(R.id.saveLocal);
        tv_savelocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                NewsDBManager manager = new NewsDBManager(ActivityShow.this);
                //TODO
                if (manager.saveLoveNews(newsitem)) {
                    showToast("收藏成功！\n在主界面侧滑菜单中查看");
                } else {
                    showToast("已经收藏过这条新闻了！\n在主界面侧滑菜单中查看");
                }
            }
        });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageView_back:
                    finish();
                    break;
                case R.id.textView2:
                    Bundle bundle = new Bundle();
                    bundle.putInt("nid", newsitem.getNid());
                    openActivity(ActivityComment.class, bundle);
                    break;
                case R.id.imageView_menu:
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    } else if (popupWindow != null) {
                        popupWindow.showAsDropDown(imageViewMenu, 0, 12);
                    }
                    break;
            }
        }
    };

    private void getCommentCount() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(
                CommonUtil.APPURL + "/cmt_num?ver=" + CommonUtil.VERSION_CODE + "&nid=" + newsitem.getNid(),
                new TextHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String responseString) {
                        int num = ParserComments.parserCommentNum(responseString.trim());
                        LogUtil.d("评论数量---", num + "");
                        tv_commentCount.setText(num + "跟帖");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          String responseString, Throwable throwable) {
                        Toast.makeText(ActivityShow.this, "网络异常！",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getCommentCountVolley() {
    }


    @Override
    protected void onResume() {
        super.onResume();
        CommentsManager.commentNum(this, CommonUtil.VERSION_CODE, newsitem.getNid(), new Listener<String>() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                int num = ParserComments.parserCommentNum(response.trim());
                LogUtil.d("评论数量---", num + "");
                tv_commentCount.setText(num + " 跟帖 ");
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Toast.makeText(ActivityShow.this, "网络异常",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
