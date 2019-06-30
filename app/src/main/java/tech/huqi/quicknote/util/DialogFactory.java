package tech.huqi.quicknote.util;

/**
 * Created by hzhuqi on 2019/4/21
 */

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.EditText;

import tech.huqi.quicknote.R;
import tech.huqi.quicknote.config.QuickNote;

public class DialogFactory {
    private static final int PADDING = 20;
    private static final String CANCEL_TIP = QuickNote.getString(R.string.popup_window_cancel);
    private static final String SURE_TIP = QuickNote.getString(R.string.popup_window_sure);

    public static void createAndShowDialog(Context context, String title, String content,
                                           DialogInterface.OnClickListener positiveListener) {
        createAndShowDialog(context, title, content, CANCEL_TIP, null, SURE_TIP, positiveListener);
    }

    public static void createAndShowDialog(Context context, String title, String content,
                                           DialogInterface.OnClickListener negativeListener,
                                           DialogInterface.OnClickListener positiveListener) {
        createAndShowDialog(context, title, content, CANCEL_TIP, negativeListener, SURE_TIP, positiveListener);
    }

    public static void createAndShowDialog(Context context, String title, String content,
                                           String negativeButtonTip, DialogInterface.OnClickListener negativeListener,
                                           String positiveButtonTip, DialogInterface.OnClickListener positiveListener) {
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setNegativeButton(negativeButtonTip, negativeListener);
        builder.setPositiveButton(positiveButtonTip, positiveListener);
        builder.show();
    }

    public static EditText showInputDialog(Context context, String title, String content, DialogInterface.OnClickListener positiveListener) {
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(content);
        AppCompatEditText input = new AppCompatEditText(context);
        builder.setView(input, PADDING, PADDING, PADDING, PADDING);
        builder.setNegativeButton(CANCEL_TIP, null);
        builder.setPositiveButton(SURE_TIP, positiveListener);
        builder.show();
        return input;
    }

    public static void showInputDialog(Context context, String title, View contentView, DialogInterface.OnClickListener positiveListener) {
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(null);
        builder.setView(contentView, 3 * PADDING, PADDING, 3 * PADDING, PADDING);
        builder.setNegativeButton(QuickNote.getString(R.string.popup_window_cancel), null);
        builder.setPositiveButton(QuickNote.getString(R.string.popup_window_sure), positiveListener);
        builder.show();
    }
}
