package tech.huqi.quicknote.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tech.huqi.quicknote.R;
import tech.huqi.quicknote.config.Constants;
import tech.huqi.quicknote.config.QuickNote;
import tech.huqi.quicknote.core.lockpattern.LockPatternIndicator;
import tech.huqi.quicknote.core.lockpattern.LockPatternUtil;
import tech.huqi.quicknote.core.lockpattern.LockPatternView;
import tech.huqi.quicknote.util.CommonUtil;
import tech.huqi.quicknote.util.SharedPreferencesUtil;

/**
 * Created by hzhuqi on 2019/4/26
 */
public class LockActivity extends BaseActivity {
    private static final long DELAY_TIME = 1000L;
    private static final String GESTURE_PASSWORD = "gesture_lock_pwd";
    private List<LockPatternView.Cell> mChosenPattern;
    private LockPatternIndicator mLockPatternIndicator;
    private LockPatternView mLockPatternView;
    private TextView mTvForgetPwd;
    private TextView mTvMessage;
    private boolean isForSet = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        initIntent();
        initView();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            // 来自设置页面
            if (!intent.getBooleanExtra(Constants.INTENT_FROM_WELCOME_ACTIVITY, false)) {
                isForSet = true;
            }
        }
    }

    private void initView() {
        mLockPatternIndicator = (LockPatternIndicator) findViewById(R.id.lockPatterIndicator);
        mLockPatternView = (LockPatternView) findViewById(R.id.lockPatternView);
        mTvMessage = (TextView) findViewById(R.id.tv_tips);
        mTvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
        mTvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetLockPattern();
            }
        });
        mLockPatternView.setOnPatternListener(patternListener);
    }

    /**
     * 手势监听
     */
    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            mLockPatternView.removePostClearPatternRunnable();
            //   updateStatus(Status.DEFAULT, null);
            mLockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            if (mChosenPattern == null && pattern.size() >= 4) {
                if (!isForSet && !TextUtils.isEmpty(getSavedPattern())) {
                    if (getSavedPattern().equals(LockPatternUtil.patternToString(pattern))) {
                        setLockPatternSuccess();
                    } else {
                        CommonUtil.showToastOnUiThread(QuickNote.getString(R.string.draw_gesture_pwd_incorrect));
                    }
                } else {
                    mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
                    updateStatus(Status.CORRECT, pattern);
                }
            } else if (mChosenPattern == null && pattern.size() < 4) {
                updateStatus(Status.LESSERROR, pattern);
            } else if (mChosenPattern != null) {
                if (mChosenPattern.equals(pattern)) {
                    updateStatus(Status.CONFIRMCORRECT, pattern);
                } else {
                    updateStatus(Status.CONFIRMERROR, pattern);
                }
            }
        }
    };

    /**
     * 更新状态
     *
     * @param status
     * @param pattern
     */
    private void updateStatus(Status status, List<LockPatternView.Cell> pattern) {
        mTvMessage.setTextColor(getResources().getColor(status.colorId));
        mTvMessage.setText(status.strId);
        switch (status) {
            case DEFAULT:
                mLockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CORRECT:
                updateLockPatternIndicator();
                mLockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case LESSERROR:
                mLockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CONFIRMERROR:
                mLockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                mLockPatternView.postClearPatternRunnable(DELAY_TIME);
                break;
            case CONFIRMCORRECT:
                saveChosenPattern(pattern);
                mLockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                setLockPatternSuccess();
                break;
        }
    }

    /**
     * 更新 Indicator
     */
    private void updateLockPatternIndicator() {
        if (mChosenPattern == null)
            return;
        mLockPatternIndicator.setIndicator(mChosenPattern);
    }

    /**
     * 重新设置手势
     */
    void resetLockPattern() {
        mChosenPattern = null;
        mLockPatternIndicator.setDefaultIndicator();
        updateStatus(Status.DEFAULT, null);
        mLockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
    }

    /**
     * 成功设置了手势密码(跳到首页)
     */
    private void setLockPatternSuccess() {
        Intent intent = new Intent(LockActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 保存手势密码
     */
    private void saveChosenPattern(List<LockPatternView.Cell> cells) {
        SharedPreferencesUtil.getInstance().setString(GESTURE_PASSWORD, LockPatternUtil.patternToString(cells));
    }

    public static String getSavedPattern() {
        return SharedPreferencesUtil.getInstance().getString(GESTURE_PASSWORD, "");
    }

    private enum Status {
        //默认的状态，刚开始的时候（初始化状态）
        DEFAULT(R.string.gesture_create_default, R.color.gesture_create_confirm_correct),
        //第一次记录成功
        CORRECT(R.string.gesture_create_correct, R.color.gesture_create_confirm_correct),
        //连接的点数小于4（二次确认的时候就不再提示连接的点数小于4，而是提示确认错误）
        LESSERROR(R.string.gesture_create_less_error, R.color.gesture_create_confirm_error),
        //二次确认错误
        CONFIRMERROR(R.string.gesture_create_confirm_error, R.color.gesture_create_confirm_error),
        //二次确认正确
        CONFIRMCORRECT(R.string.gesture_create_confirm_correct, R.color.gesture_create_confirm_correct);

        private int strId;
        private int colorId;

        Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
    }
}
