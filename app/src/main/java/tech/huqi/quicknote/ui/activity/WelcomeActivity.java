package tech.huqi.quicknote.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import tech.huqi.quicknote.R;
import tech.huqi.quicknote.config.Constants;
import tech.huqi.quicknote.config.QuickNote;

/**
 * Created by hzhuqi on 2019/4/26
 */
public class WelcomeActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        delay();
    }

    private void delay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (QuickNote.isSetPatternLock()) {
                    intent = new Intent(WelcomeActivity.this, LockActivity.class);
                    intent.putExtra(Constants.INTENT_FROM_WELCOME_ACTIVITY, true);
                } else {
                    intent = new Intent(WelcomeActivity.this, MainActivity.class);
                }
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }, Constants.DELAY_TIME);
    }
}
