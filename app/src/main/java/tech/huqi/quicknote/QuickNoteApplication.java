package tech.huqi.quicknote;

import android.app.Application;

import tech.huqi.quicknote.config.QuickNote;

/**
 * Created by hzhuqi on 2019/4/15
 */
public class QuickNoteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        QuickNote.init(this);
    }
}
