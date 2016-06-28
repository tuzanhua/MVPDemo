package com.example.timer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;

import com.example.timer.MyApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 项目名称 :MVPDemo
 * 类描述:  sputis  sp 保存数据的类
 * 创建人 : 001
 * 创建时间:2016/6/2 14:41
 * 修改时间:2016/6/2 14:41
 * 修改备注:
 */
public class SpUtil {
    // 保存sp 的文件名
    public static final String FILE_NAME = "timertoexitapp";

    //获取全局的Context

    public static Context mContext = MyApplication.getContext();

    public static void put(String key, Object value) {

        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();

        if (value == null) {
            return;
        }
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else {
            edit.putString(key, value.toString());
        }
        SharedPreferencesCompat.apply(edit);
    }

    /**
     *  得到保存数据的方法,我们根据默认值得到保存数据的具体类型,然后调用相对于的方法获取值
     *
     *  泛型的使用
     */

    public static < T extends  Object> T get(String key,T defaultObject){

        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if(defaultObject instanceof String){
            return (T) sp.getString(key, (String) defaultObject);
        }else if (defaultObject instanceof Integer)
        {
            return (T)(Integer)sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean)
        {
            return (T)(Boolean)sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float)
        {
            return (T)(Float)sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long)
        {
            return (T)(Long)sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }


    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }
}