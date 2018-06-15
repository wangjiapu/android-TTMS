package com.example.other.ttms.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    /**
     * 上下文
     */
    private static Context mContext;

    public static final String FILENAME="TTMS";
    public static final String USERNAME="@#$%";//昵称
    public static final String USENUMBER="%$#@";//电话
    public static final String URI="URI";
    public static final String URL="URL";

    /**
     * 初始化SharedPreferences，必须使用该方法
     */
    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 抛出异常
     */
    private static void throwInit(){
        if (mContext == null) {
            throw new NullPointerException("在使用该方法前，需要使用init()方法，推荐将init()放入Application中");
        }
    }
    /**
     * 插入字符串
     * @param name 文件名
     * @param key key
     * @param value 值
     */
    public static void putString(String name, String key, String value) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.apply();
    }

    /**
     * 获取字符串
     * @param name 文件名
     * @param key key
     * @param defaultValue 没获取到时的默认值
     * @return 字符串
     */
    public static String getString(String name, String key, String defaultValue) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    /**
     * 插入整型
     * @param name 文件名
     * @param key key
     * @param value 值
     */
    public static void putInt(String name, String key, int value) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    /**
     * 获取整型
     * @param name 文件名
     * @param key key
     * @param defaultValue 没获取到时的默认值
     * @return 整型
     */
    public static int getInt(String name, String key, int defaultValue) {
        throwInit();
        SharedPreferences sp = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

}
