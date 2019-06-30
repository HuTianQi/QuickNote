package tech.huqi.quicknote.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.LocaleList;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Locale;

import tech.huqi.quicknote.R;
import tech.huqi.quicknote.config.Constants;
import tech.huqi.quicknote.config.QuickNote;

/**
 * Created by hzhuqi on 2019/4/10
 */
public class CommonUtil {
    private static final String SHARE_APP_IMAGE = "share.jpg";

    /**
     * 获取屏幕宽度，单位像素
     *
     * @param context
     * @return
     */
    public static int getScreenWidthPixels(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度，单位像素
     *
     * @param context
     * @return
     */
    public static int getScreenHeightPixels(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取手机状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Rect rect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    public static int getActionBarHeight(Context context) {
        TypedArray actionbarSizeTypedArray = context.obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        return (int) actionbarSizeTypedArray.getDimension(0, 0);
    }

    /**
     * 将字符串转为整数，如果出现异常，返回默认值
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int string2Int(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            // do nothing
        }
        return defValue;
    }

    public static void showToastOnUiThread(final int resId) {
        QuickNote.getMainThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(QuickNote.getAppContext(), QuickNote.getString(resId), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void showToastOnUiThread(final String msg) {
        QuickNote.getMainThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(QuickNote.getAppContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void showSnackBarOnUiThread(final View anchor, final int resId) {
        QuickNote.getMainThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(anchor, QuickNote.getString(resId), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public static void showSnackBarOnUiThread(final View anchor, final String msg) {
        QuickNote.getMainThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(anchor, msg, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public static void changeLocalLanguage(Context context, Locale locale) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//8.0及以上系统
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            config.setLocale(locale);
            config.setLocales(localeList);
            context.createConfigurationContext(config);//对于8.0系统必须先调用该语句，否则后面的updateConfiguration不起作用
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4及以上系统
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        resources.updateConfiguration(config, dm);//所有的系统都需要调用该API
    }

    public static void restartApp(Activity activity, Class<?> homeClass) {
        Intent intent = new Intent(activity, homeClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public static void shareApp(Context context) {
        shareApp(context, false);
    }

    public static void shareApp(Context context, boolean isShareAppIcon) {
        String shareAppTip = QuickNote.getString(R.string.share_app_content);
        if (isShareAppIcon) {
            new File(context.getFilesDir(), SHARE_APP_IMAGE).deleteOnExit();
            try {
                FileOutputStream fos = context.openFileOutput(SHARE_APP_IMAGE, Context.MODE_WORLD_READABLE);
                Bitmap pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_app);
                pic.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("sms_body", shareAppTip);
        intent.putExtra("android.intent.extra.TEXT", shareAppTip);
        if (isShareAppIcon) {
            File shareImage = new File(context.getFilesDir(), SHARE_APP_IMAGE);
            if (shareImage.exists()) {
                intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(shareImage));
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(Intent.createChooser(intent, QuickNote.getString(R.string.share_app_title)));
    }

    public static String getAppVersion() {
        Context context = QuickNote.getAppContext();
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 跳转应用商店.
     *
     * @param context   {@link Context}
     * @param appPkg    包名
     * @param marketPkg 应用商店包名
     * @return {@code true} 跳转成功 <br> {@code false} 跳转失败
     */
    public static boolean jumpToMarket(Context context, String appPkg, String marketPkg) {
        Uri uri = Uri.parse("market://details?id=" + appPkg);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (marketPkg != null) {// 如果没给市场的包名，则系统会弹出市场的列表让你进行选择。
            intent.setPackage(marketPkg);
        }
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
