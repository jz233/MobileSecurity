package zjj.app.mobilesecurity.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
    private SharedPreferencesUtils(){}

    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor(Context context){
        return getSharedPreferences(context).edit();
    }

    public static void putString(Context context, String key, String value){
        getEditor(context).putString(key, value).apply();
    }

    public static String getString(Context context, String key, String defaultValue){
        return getSharedPreferences(context).getString(key, defaultValue);
    }

    public static void putBoolean(Context context, String key, boolean value){
        getEditor(context).putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue){
        return getSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static int getInt(Context context, String key, int defaultValue){
        return getSharedPreferences(context).getInt(key, defaultValue);
    }

    public static void putInt(Context context, String key, int value){
        getEditor(context).putInt(key, value).apply();
    }

    public static long getLong(Context context, String key, long defaultValue){
        return getSharedPreferences(context).getLong(key, defaultValue);
    }

    public static void putLong(Context context, String key, long value){
        getEditor(context).putLong(key, value).apply();
    }



}
