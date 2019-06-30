package tech.huqi.quicknote.ui.activity;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import pub.devrel.easypermissions.EasyPermissions;
import tech.huqi.quicknote.config.QuickNote;
import tech.huqi.quicknote.util.QuickNoteContextWrapper;

/**
 * Created by hzhuqi on 2019/4/28
 */
public class BaseActivity extends AppCompatActivity {
    protected final int RC_ALL_PERM = 10000;
    protected final int RC_READ_EXTERNAL_STORAGE = 10001;
    protected final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = QuickNoteContextWrapper.wrap(newBase, QuickNote.getLangLocale());
        super.attachBaseContext(context);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
