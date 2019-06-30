package tech.huqi.quicknote.config;

/**
 * Created by hzhuqi on 2019/4/17
 */

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.Locale;

import tech.huqi.quicknote.BuildConfig;
import tech.huqi.quicknote.R;
import tech.huqi.quicknote.ui.activity.LockActivity;
import tech.huqi.quicknote.util.SharedPreferencesUtil;

/**
 * App的一些全局配置信息
 */
public class QuickNote {
    private static Context sContext;
    private static Handler sHandler;

    public static void init(Context context) {
        sContext = context;
        sHandler = new Handler(Looper.getMainLooper());
        initTheme();
        initDD(sContext);
    }

    private static void initTheme() {
        String themeKey = getString(R.string.preference_key_theme);
        String themeValueNight = getString(R.string.theme_value_night);
        String themeValueDefault = getString(R.string.theme_value_default);
        if (SharedPreferencesUtil.getInstance().getString(themeKey, themeValueDefault).equals(themeValueNight)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    public static Locale getLangLocale() {
        Locale locale = Locale.getDefault();
        String langKey = getString(R.string.preference_key_lang);
        String langValueDefault = getString(R.string.lang_value_default);
        String langValueChinese = getString(R.string.lang_value_chinese);
        String langValueEnglish = getString(R.string.lang_value_english);
        if (SharedPreferencesUtil.getInstance().getString(langKey, langValueChinese).equals(langValueDefault)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 8.0及以上系统
                locale = QuickNote.getAppContext().getResources().getConfiguration().getLocales().get(0);
            } else {
                locale = QuickNote.getAppContext().getResources().getConfiguration().locale;
            }
        } else if (SharedPreferencesUtil.getInstance().getString(langKey, langValueChinese).equals(langValueEnglish)) {
            locale = Locale.US;
        } else if (SharedPreferencesUtil.getInstance().getString(langKey, langValueChinese).equals(langValueChinese)) {
            locale = Locale.SIMPLIFIED_CHINESE;
        }
        return locale;
    }

    private static void initDD(Context context) {
        if (BuildConfig.DEBUG) {
            try {
                Class<?> debugDB = Class.forName("com.amitshekhar.DebugDB");
                Method getAddressLog = debugDB.getMethod("getAddressLog");
                Object value = getAddressLog.invoke(null);
                Toast.makeText(context, (String) value, Toast.LENGTH_LONG).show();
            } catch (Exception ignore) {
                // do nothing
            }
        }
    }

    public static Handler getMainThreadHandler() {
        return sHandler;
    }

    public static Context getAppContext() {
        return sContext;
    }

    public static String getString(int id) {
        return getAppContext().getResources().getString(id);
    }

    public static int getColor(int colorId) {
        return getAppContext().getResources().getColor(colorId);
    }

    public static int getDimen(int dimenId) {
        return getAppContext().getResources().getDimensionPixelSize(dimenId);
    }

    public static String getSavedPattern() {
        return LockActivity.getSavedPattern();
    }

    public static boolean isSetPatternLock() {
        return SharedPreferencesUtil.getInstance().getBoolean(getString(R.string.preference_key_gesture_pwd), false);
    }
}
