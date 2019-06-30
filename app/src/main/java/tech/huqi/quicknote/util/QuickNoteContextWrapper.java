package tech.huqi.quicknote.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

/**
 * Created by hzhuqi on 2019/4/28
 */
public class QuickNoteContextWrapper extends ContextWrapper {
    public QuickNoteContextWrapper(Context base) {
        super(base);
    }

    public static Context wrap(Context context, Locale locale) {
        Context newContext = context;
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//8.0及以上系统
            config.setLocale(locale);
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            newContext = context.createConfigurationContext(config);//对于8.0系统必须先调用该语句，否则后面的updateConfiguration不起作用
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4及以上系统
            config.setLocale(locale);
            newContext = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
        }
        return new QuickNoteContextWrapper(newContext);
    }
}
