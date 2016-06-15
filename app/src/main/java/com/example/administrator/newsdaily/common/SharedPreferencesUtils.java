package com.example.administrator.newsdaily.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.administrator.newsdaily.model.entity.BaseEntity;
import com.example.administrator.newsdaily.model.entity.Register;
import com.example.administrator.newsdaily.model.entity.User;

/**
 * 保存工具类
 * Created by Administrator on 2016/6/14 0014.
 */
public class SharedPreferencesUtils {
    /**
     * 此方法用于注册或者登录后，保存解析得到的内容
     * 创建实体Register,从Register中获取封装好的数据
     *
     * @param context
     * @param register
     */
    public static void saveRegister(Context context, BaseEntity<Register> register) {
        SharedPreferences sp = context.getSharedPreferences("register", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("message", register.getMessage());
        editor.putInt("status", Integer.parseInt(register.getStatus()));
        Register data = register.getData();
        editor.putString("result", data.getResult());
        editor.putString("token", data.getToken());
        editor.putString("explain", data.getExplain());
        editor.commit();
    }

    /**
     * 保存用户数据
     *创建用户实体User,从User中获取封装的User数据
     * @param context
     * @param user
     */
    public static void saveUser(Context context, BaseEntity<User> user) {
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        User core = user.getData();
        editor.putString("userName", core.getUid());
        editor.putString("headImage", core.getPortrait());
        editor.commit();
    }

    /**
     * 清除用户数据
     *
     * @param context
     */
    public static void clearUser(Context context) {
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 获取token
     *
     * @param context
     * @return
     */
    public static String getToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("register", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }

    /**
     * 获取用户名和用户头像地址ַ
     *
     * @param context
     * @return String����  0 --- username 1 --photo
     */
    public static String[] getUserNameAndPhoto(Context context) {
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        return new String[]{sp.getString("userName", ""), sp.getString("headImage", "")};
    }

    /**
     * 保存用户头像本地路径
     *
     * @param context
     * @param path
     */
    public static void saveUserLocalIcon(Context context, String path) {
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("imagePath", path);
        editor.commit();
    }

    /**
     * 获取保存的本地头像路径
     *
     * @param context
     * @return
     */
    public static String getUserLocalIcon(Context context) {
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        return sp.getString("imagePath", null);
    }
}
