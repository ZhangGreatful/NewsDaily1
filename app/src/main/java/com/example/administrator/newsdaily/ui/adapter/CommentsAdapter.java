package com.example.administrator.newsdaily.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.newsdaily.R;
import com.example.administrator.newsdaily.common.LoadImage;
import com.example.administrator.newsdaily.common.LogUtil;
import com.example.administrator.newsdaily.model.entity.Comment;
import com.example.administrator.newsdaily.ui.base.MyBaseAdapter;

/**
 * 评论适配器
 */
public class CommentsAdapter extends MyBaseAdapter<Comment> {
    private ListView listView;

    //		构造函数
    public CommentsAdapter(Context context, ListView listView) {
        super(context);
        this.listView = listView;
    }

    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        HoldView holdView = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_comment, null);
            holdView = new HoldView(convertView);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }
        Comment comment = myList.get(position);

        LogUtil.d(LogUtil.TAG, "position--->" + position + "--- cid=" + comment.getCid());
        holdView.tv_comment.setText(comment.getContent());
        holdView.tv_time.setText(comment.getStamp());
        holdView.tv_user.setText(comment.getUid());

        return convertView;
    }

    public class HoldView {
        public ImageView iv_list_image;
        public TextView  tv_user;
        public TextView  tv_time;
        public TextView  tv_comment;

        public HoldView(View view) {
            iv_list_image = (ImageView) view.findViewById(R.id.imageView1);
            tv_user = (TextView) view.findViewById(R.id.textView2);
            tv_time = (TextView) view.findViewById(R.id.textView3);
            tv_comment = (TextView) view.findViewById(R.id.textView1);
        }
    }

    private LoadImage.ImageLoadListener listener = new LoadImage.ImageLoadListener() {

        @Override
        public void imageLoadOk(Bitmap bitmap, String url) {
            ImageView iv = (ImageView) listView.findViewWithTag(url);
            if (iv != null)
                iv.setImageBitmap(bitmap);
        }
    };
}
