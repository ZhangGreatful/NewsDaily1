package com.example.administrator.newsdaily.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.newsdaily.R;
import com.example.administrator.newsdaily.common.LoadImage;
import com.example.administrator.newsdaily.model.entity.News;
import com.example.administrator.newsdaily.ui.base.MyBaseAdapter;

/**
 * 新闻数据适配器
 */
public class NewsAdapter extends MyBaseAdapter<News> {
    private Bitmap    defaultBitmap;
    private LoadImage loadImage;
    private ListView  listView;

    /**
     * 新闻类构造函数,初始化时调用
     * @param context  上下文
     * @param listView  列表ListView
     */
    public NewsAdapter(Context context, ListView listView) {
        super(context);
//        设置默认图片
        defaultBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.defaultpic);
        loadImage = new LoadImage(context, listener);
        this.listView = listView;
    }

    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_news, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        News news = myList.get(position);
        viewHolder.tv_title.setText(news.getTitle());
        viewHolder.tv_text.setText(news.getSummary());
        viewHolder.tv_from.setText(news.getStamp());
        viewHolder.iv_icon.setImageBitmap(defaultBitmap);// 设置每一个itemd的默认图片

        String url = news.getIcon();
//        给图片设置标签
        viewHolder.iv_icon.setTag(url);
//        获取图片
        loadImage.geBitmap(url, viewHolder.iv_icon);

        return convertView;
    }

    /**
     * 标签类
     */
    public class ViewHolder {
        public ImageView iv_icon;
        public TextView  tv_title;
        public TextView  tv_text;
        public TextView  tv_from;

        public ViewHolder(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.imageView1);
            tv_title = (TextView) view.findViewById(R.id.textView1);
            tv_text = (TextView) view.findViewById(R.id.textView2);
            tv_from = (TextView) view.findViewById(R.id.textView3);
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
