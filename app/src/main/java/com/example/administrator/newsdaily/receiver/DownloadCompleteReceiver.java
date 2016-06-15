package com.example.administrator.newsdaily.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.newsdaily.common.LogUtil;

import java.io.File;

/**
 * Created by Administrator on 2016/6/14 0014.
 */
public class DownloadCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

            Toast.makeText(context, "下载完成！", Toast.LENGTH_LONG).show();

            String fileName = "";
            //The download manager is a system service that handles long-running HTTP downloads.
            DownloadManager downloadManager = (DownloadManager) context
                    .getSystemService(Context.DOWNLOAD_SERVICE);//从下载服务获取下载管理器
            //获取下载队列
            DownloadManager.Query query = new DownloadManager.Query();
            //设置过滤状态：成功
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            // 查询以前下载过的‘成功文件’
            Cursor c = downloadManager.query(query);
            // 移动到最新下载的文件
            if (c.moveToFirst()) {
                fileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            }
            LogUtil.d(LogUtil.TAG, "======文件名称=====" + fileName);
            File f = new File(fileName.replace("file://", ""));// 过滤路径
            installApk(f, context);// 开始安装apk
            context.unregisterReceiver(this); //取消注册广播
        }
    }


    private void installApk(File file, Context context) {
        if (!file.exists()) {
            Log.i("DownLoadReceive", "文件不存在");
            return;
        }
        // 通过Intent安装apk文件，自动打开安装界面
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //由于是在BroadcastReceive中启动activity，所以启动方式必须设置为FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent);
    }
}
