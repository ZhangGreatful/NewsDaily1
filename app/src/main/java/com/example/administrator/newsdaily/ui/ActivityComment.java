package com.example.administrator.newsdaily.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.newsdaily.R;
import com.example.administrator.newsdaily.common.CommonUtil;
import com.example.administrator.newsdaily.common.LogUtil;
import com.example.administrator.newsdaily.common.SharedPreferencesUtils;
import com.example.administrator.newsdaily.common.SystemUtils;
import com.example.administrator.newsdaily.model.biz.CommentsManager;
import com.example.administrator.newsdaily.model.biz.NewsManager;
import com.example.administrator.newsdaily.model.biz.parser.ParserComments;
import com.example.administrator.newsdaily.model.entity.Comment;
import com.example.administrator.newsdaily.ui.adapter.CommentsAdapter;
import com.example.administrator.newsdaily.ui.base.MyBaseActivity;
import com.example.administrator.newsdaily.view.xlistview.XListView;
import com.example.administrator.newsdaily.view.xlistview.XListView.IXListViewListener;
import com.example.administrator.newsdaily.volley.Response.ErrorListener;
import com.example.administrator.newsdaily.volley.Response.Listener;
import com.example.administrator.newsdaily.volley.VolleyError;

import java.util.List;

/**
 * 评论界面
 ***/
public class ActivityComment extends MyBaseActivity {
    /**
     * 新闻id
     */
    private int             nid;
    /**
     * 评论列表
     */
    private XListView       listView;
    /**
     * 评论列表适配器
     */
    private CommentsAdapter adapter;
    /***/
    private int             mode;
    /**
     * 发送评论按钮
     */
    private ImageView       imageView_send;
    /**
     * 返回按钮
     */
    private ImageView       imageView_back;
    /**
     * 评论编辑框
     */
    private EditText        editText_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        nid = getIntent().getIntExtra("nid", -1);
        Log.d(LogUtil.TAG, "nid------------->" + nid);
        listView = (XListView) findViewById(R.id.listview);
        imageView_send = (ImageView) findViewById(R.id.imageview);
        imageView_back = (ImageView) findViewById(R.id.imageView_back);
        editText_content = (EditText) findViewById(R.id.edittext_comment);
        adapter = new CommentsAdapter(this, listView);
        listView.setAdapter(adapter);
//		listView下拉刷新
        listView.setPullRefreshEnable(true);
//		listView上拉加载
        listView.setPullLoadEnable(true);
        listView.setXListViewListener(listViewListener);
        loadNextComment();

        imageView_back.setOnClickListener(clickListener);
        imageView_send.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageView_back:
                    finish();
                    break;
                case R.id.imageview:
                    String ccontent = editText_content.getText().toString();
                    if (ccontent == null || ccontent.equals("")) {
                        Toast.makeText(ActivityComment.this, "要先写评论内容",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    imageView_send.setEnabled(false);
                    String imei = SystemUtils.getInstance(ActivityComment.this)
                            .getIMEI();
                    String token = SharedPreferencesUtils
                            .getToken(ActivityComment.this);
                    if (TextUtils.isEmpty(token)) {
                        Toast.makeText(ActivityComment.this, "对不起，您还没有登录.", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    showLoadingDialog(ActivityComment.this, "", true);

                    CommentsManager.sendCommnet(ActivityComment.this, nid,
                            new Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    // TODO Auto-generated method stub
                                    LogUtil.d(LogUtil.TAG, "发表评论返回信息----->"
                                            + response.toString());
                                    int status = ParserComments
                                            .parserSendComment(response.trim());
                                    if (status == 0) {
                                        showToast("评论成功！");
                                        editText_content.setText(null);
                                        editText_content.clearFocus();
                                        loadNextComment();
                                    } else {
                                        showToast("评论失败！");
                                    }
                                    imageView_send.setEnabled(true);
                                    dialog.cancel();
                                }
                            }, new ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO Auto-generated method stub
                                    showToast("服务器连接异常！");
                                    imageView_send.setEnabled(true);
                                    dialog.cancel();
                                }

                            }, CommonUtil.VERSION_CODE + "", token, imei, ccontent);

                    break;
            }
        }
    };

    private IXListViewListener listViewListener = new IXListViewListener() {
        @Override
        public void onRefresh() {
            // 加载最新数据。。。。。。。。。。。。。。。。。。。
            loadNextComment();
            // 加载完毕
            listView.stopLoadMore();
            listView.stopRefresh();
            listView.setRefreshTime(CommonUtil.getSystime());
        }

        @Override
        public void onLoadMore() {
            // 加载下面更多的数据。。。。。。。。。。。。。。。。。。。
            int count = adapter.getCount();
            if (count > 1) { // 如果当前的ListView不存在一条item是不允许用户加载更多
                loadPreComment();
            }
            listView.stopLoadMore();
            listView.stopRefresh();
        }
    };

    /**
     * 加载下面的XX条数据
     */
    protected void loadPreComment() {
        Comment comment = adapter
                .getItem(listView.getLastVisiblePosition() - 2);
        mode = NewsManager.MODE_PREVIOUS;
        if (SystemUtils.getInstance(this).isNetConn()) {
            CommentsManager.loadComments(this, CommonUtil.VERSION_CODE + "",
                    listener, errorListener, nid, 2, comment.getCid());
        }
    }

    /**
     * 请求最新的评论
     */
    protected void loadNextComment() {
        int curId = adapter.getAdapterData().size() <= 0 ? 0 : adapter.getItem(
                0).getCid();
        LogUtil.d(LogUtil.TAG, "loadnextcomment--->currentId=" + curId);
        mode = NewsManager.MODE_NEXT;
        if (SystemUtils.getInstance(this).isNetConn()) {
            CommentsManager.loadComments(
                    this,
                    CommonUtil.VERSION_CODE + "",
                    listener,
                    errorListener,
                    nid, // 新闻id
                    1,   // 方向1下拉
                    curId);//
        }
    }

    Listener<String> listener      = new Listener<String>() {

        @Override
        public void onResponse(String response) {
            // TODO Auto-generated method stub

            List<Comment> comments = ParserComments.parserComment(response);
            if (comments == null || comments.size() < 1) {
                return;
            }
            boolean flag = mode == NewsManager.MODE_NEXT ? true : false;
            adapter.appendData(comments, flag);
            adapter.update();

        }
    };
    ErrorListener    errorListener = new ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO Auto-generated method stub
            Toast.makeText(ActivityComment.this, "服务器连接错误", Toast.LENGTH_SHORT)
                    .show();
        }
    };
}
