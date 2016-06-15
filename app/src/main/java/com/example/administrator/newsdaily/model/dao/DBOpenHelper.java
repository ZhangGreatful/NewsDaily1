package com.example.administrator.newsdaily.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类,用于创建数据库
 * Created by Administrator on 2016/6/14 0014.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(Context context) {
        super(context, "newsdb.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table type(_id integer primary key autoincrement,subid integer,subgroup text)");
        db.execSQL("create table news (_id integer primary key autoincrement,type integer,nid integer,stamp text,icon text,title text,summary text,link text)");
        db.execSQL("create table lovenews(_id integer primary key autoincrement,type integer,nid integer,stamp text,icon text,title text,summary text,link text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
