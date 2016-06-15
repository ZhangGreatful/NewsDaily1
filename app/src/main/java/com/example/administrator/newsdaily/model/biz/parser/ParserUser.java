package com.example.administrator.newsdaily.model.biz.parser;

import com.example.administrator.newsdaily.model.entity.BaseEntity;
import com.example.administrator.newsdaily.model.entity.Register;
import com.example.administrator.newsdaily.model.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 解析用户信息
 * Created by Administrator on 2016/6/14 0014.
 */
public class ParserUser {

    /**
     * 解析用户注册返回信息
     *
     * @param json
     * @return BaseEntity<Register>����
     */
    public static BaseEntity<Register> parserRegister(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<BaseEntity<Register>>() {
        }.getType());
    }

    /**
     * 解析用户中心数据
     *
     * @param json
     * @return BaseEntity<User> ����
     */
    public static BaseEntity<User> parserUser(String json) {
        return new Gson().fromJson(json, new TypeToken<BaseEntity<User>>() {
        }.getType());
    }

    /**
     * 解析上传用户头像
     *
     * @param json
     * @return BaseEntity<Register>
     */
    public static BaseEntity<Register> parserUploadImage(String json) {
        return new Gson().fromJson(json, new TypeToken<BaseEntity<Register>>() {
        }.getType());
    }

}
