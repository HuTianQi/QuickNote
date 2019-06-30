package tech.huqi.quicknote.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.view.View;

import de.hdodenhof.circleimageview.CircleImageView;
import jaygoo.widget.wlv.WaveLineView;
import tech.huqi.quicknote.R;
import tech.huqi.quicknote.config.QuickNote;
import tech.huqi.quicknote.util.BaiduVoiceHelper;
import tech.huqi.quicknote.util.DialogFactory;

import static tech.huqi.quicknote.config.Constants.INTENT_IMAGE_PATH;
import static tech.huqi.quicknote.config.Constants.INTENT_VOICE_RECOGNIZED;

/**
 * Created by hzhuqi on 2019/4/22
 */
public class VoiceShorthandActivity extends BaseActivity implements BaiduVoiceHelper.VoiceHelperCallback {
    private BaiduVoiceHelper mVoiceHelper;
    private AppCompatTextView mTvVoiceRecognizeResult;
    private CircleImageView mBtnStart;
    private WaveLineView mWaveLineView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_shorthand);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initData();
    }

    private void initView() {
        mBtnStart = findViewById(R.id.btn_voice_recognized_start);
        mWaveLineView = findViewById(R.id.wave_line_view);
        mTvVoiceRecognizeResult = findViewById(R.id.tv_voice_recognized_result);
    }

    private void initData() {
        mVoiceHelper = BaiduVoiceHelper.getInstance(this);
        mVoiceHelper.setVoiceHelperCallback(this);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWaveLineView.startAnim();
                mVoiceHelper.start();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVoiceHelper.onPause();
        mWaveLineView.stopAnim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVoiceHelper.onDestroy();
        mVoiceHelper.stop();
        mWaveLineView.stopAnim();
    }


    @Override
    public void onVoiceStart() {
        mTvVoiceRecognizeResult.setText("");
    }


    @Override
    public void onProcess(String text) {

    }


    @Override
    public void onCompleted(String resultText) {
        mTvVoiceRecognizeResult.setText(resultText);
        mWaveLineView.stopAnim();
    }

    @Override
    public void onFailure(String msg) {
        mTvVoiceRecognizeResult.setText(QuickNote.getString(R.string.recognize_failed) + ":" + msg);
        mWaveLineView.stopAnim();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                DialogFactory.createAndShowDialog(this, QuickNote.getString(R.string.is_use_voice_auto_recognize_result),
                        null, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String result = mTvVoiceRecognizeResult.getText().toString();
                                Intent intent = getIntent();
                                intent.putExtra(INTENT_VOICE_RECOGNIZED, result);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
