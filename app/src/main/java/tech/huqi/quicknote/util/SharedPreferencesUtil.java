package tech.huqi.quicknote.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Map;

import tech.huqi.quicknote.config.Constants;
import tech.huqi.quicknote.config.QuickNote;

/**
 * Created by hzhuqi on 2019/4/23
 */
public class SharedPreferencesUtil {
    private static final Map<String, SharedPreferencesUtil> SP_MAP = new HashMap<>();
    private static SharedPreferences sp;

    public static SharedPreferencesUtil getInstance() {
        return getInstance(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferencesUtil getInstance(String name) {
        return getInstance(name, Context.MODE_PRIVATE);
    }

    private SharedPreferencesUtil(String name, int mode) {
        sp = QuickNote.getAppContext().getSharedPreferences(name, mode);

    }

    private SharedPreferencesUtil(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferencesUtil getInstance(String name, int mode) {
        SharedPreferencesUtil spUtil = SP_MAP.get(name);
        if (spUtil == null) {
            synchronized (SharedPreferencesUtil.class) {
                spUtil = SP_MAP.get(name);
                if (spUtil == null) {
                    //    spUtil = new SharedPreferencesUtil(name, mode);
                    spUtil = new SharedPreferencesUtil(QuickNote.getAppContext());
                    SP_MAP.put(name, spUtil);
                }
            }
        }
        return spUtil;
    }

    public String getString(String key, final String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public void setString(final String key, final String value) {
        sp.edit().putString(key, value).apply();
    }

    public boolean getBoolean(final String key, final boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public boolean hasKey(final String key) {
        return sp.contains(key);
    }

    public void setBoolean(final String key, final boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    public void setInt(final String key, final int value) {
        sp.edit().putInt(key, value).apply();
    }

    public int getInt(final String key, final int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public void setFloat(final String key, final float value) {
        sp.edit().putFloat(key, value).apply();
    }

    public float getFloat(final String key, final float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public void setLong(final String key, final long value) {
        sp.edit().putLong(key, value).apply();
    }

    public long getLong(final String key, final long defaultValue) {
        return sp.getLong(key, defaultValue);
    }
}
