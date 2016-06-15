package com.example.administrator.newsdaily.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.newsdaily.R;
import com.example.administrator.newsdaily.model.entity.LoginLog;
import com.example.administrator.newsdaily.ui.base.MyBaseAdapter;

import java.util.List;

/**
 * 登录日志
 */
public class LoginLogAdapter extends MyBaseAdapter {

    public LoginLogAdapter(Context context, List<LoginLog> list) {
        super(context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }


    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_login_log, null);

            viewHolder.loginTimeTv = (TextView) convertView.findViewById(R.id.login_time);
            viewHolder.loginAddTv = (TextView) convertView.findViewById(R.id.login_address);
            viewHolder.loginTypeTv = (TextView) convertView.findViewById(R.id.login_type);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LoginLog log = (LoginLog) myList.get(position);
        viewHolder.loginTimeTv.setText(log.getTime().split(" ")[0]);
        viewHolder.loginAddTv.setText(log.getAddress());
        viewHolder.loginTypeTv.setText(log.getDevice() == 1 ? "PC" : "客户端");
        return convertView;
    }


    class ViewHolder {
        TextView loginTimeTv, loginAddTv, loginTypeTv;
    }

}
