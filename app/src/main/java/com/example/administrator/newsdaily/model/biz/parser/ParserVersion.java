package com.example.administrator.newsdaily.model.biz.parser;

import com.example.administrator.newsdaily.model.entity.Version;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * 解析版本信息
 * Created by Administrator on 2016/6/14 0014.
 */
public class ParserVersion {
    /**
     * 解析版本更新
     *
     * @param json
     * @return
     */
    public static Version parserJson(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Version>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}
