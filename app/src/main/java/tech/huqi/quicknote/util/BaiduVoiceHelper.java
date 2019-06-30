package tech.huqi.quicknote.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import tech.huqi.quicknote.R;
import tech.huqi.quicknote.config.QuickNote;

/**
 * Created by hzhuqi on 2019/4/22
 * 参考自百度语音识别SDK官方文档
 */
public class BaiduVoiceHelper implements EventListener {
    private static final String TAG = "QuickNote.BaiduVoiceHelper";
    private static final String ASR_START = "asr.ready";
    private static final String ASR_PROCESSING = "asr.partial";
    private static final String ASR_FINISH = "asr.finish";
    private static final String ASR_EXIT = "asr.exit";
    private static BaiduVoiceHelper sBaiduVoiceHelper;
    private VoiceHelperCallback mVoiceHelperCallback;
    private String mResultRecognizedText;
    private EventManager mAsr;
    private boolean enableOffline;
    private boolean isRecognizeFailed = false;

    private BaiduVoiceHelper(Context context, boolean enableOffline) {
        this.enableOffline = enableOffline;
        mAsr = EventManagerFactory.create(context, "asr");
        mAsr.registerListener(this);
        if (enableOffline) {
            loadOfflineEngine();
        }
    }

    public static BaiduVoiceHelper getInstance(Context context) {
        return getInstance(context, false);
    }

    public static BaiduVoiceHelper getInstance(Context context, boolean enableOffline) {
        if (sBaiduVoiceHelper == null) {
            synchronized (BaiduVoiceHelper.class) {
                if (sBaiduVoiceHelper == null) {
                    sBaiduVoiceHelper = new BaiduVoiceHelper(context, enableOffline);
                }
            }
        }
        return sBaiduVoiceHelper;
    }

    public void setVoiceHelperCallback(VoiceHelperCallback voiceHelperCallback) {
        this.mVoiceHelperCallback = voiceHelperCallback;
    }

    public void start() {
        if (mVoiceHelperCallback == null) {
            throw new RuntimeException("you must set a VoiceHelperCallback before start voice recognized");
        }
        isRecognizeFailed = false;
        Map<String, Object> params = new LinkedHashMap<>();
        String event = SpeechConstant.ASR_START;
        if (enableOffline) {
            params.put(SpeechConstant.DECODER, 2);
        }

        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        // params.put(SpeechConstant.NLU, "enable");
        // params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音
        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        // params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号
        String json = new JSONObject(params).toString();
        try {
            mAsr.send(event, json, null, 0, 0);
        } catch (Exception e) {
            mVoiceHelperCallback.onFailure(e.toString());
        }
    }

    public void stop() {
        mAsr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
    }


    public void onPause() {
        mAsr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
    }

    public void onDestroy() {
        mAsr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        if (enableOffline) {
            unloadOfflineEngine();
        }

        // 必须与registerListener成对出现，否则可能造成内存泄露
        mAsr.unregisterListener(this);
    }

    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        Log.d(TAG, "[onEvent] name is:" + name + " params is:" + params);
        if (!TextUtils.isEmpty(params)) {
            try {
                JSONObject paramsJson = new JSONObject(params);
                String errorText;
                if (paramsJson.getInt("error") != 0) {
                    errorText = paramsJson.getString("desc");
                    if (errorText.contains("no speech")) {
                        errorText = QuickNote.getString(R.string.voice_recognize_no_speech);
                    }
                    mVoiceHelperCallback.onFailure(errorText);
                    isRecognizeFailed = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (!isRecognizeFailed) {
            if (ASR_START.equals(name)) {
                mVoiceHelperCallback.onVoiceStart();
            } else if (ASR_PROCESSING.equals(name)) {
                JSONObject partialVoice;
                String text = null;
                try {
                    partialVoice = new JSONObject(params);
                    text = partialVoice.getString("best_result");
                } catch (JSONException e) {
                    e.printStackTrace();
                    mVoiceHelperCallback.onFailure(e.toString());
                }
                mVoiceHelperCallback.onProcess(text);
                if (params != null && params.contains("\"final_result\"")) {
                    mResultRecognizedText = text;
                }
            } else if (ASR_FINISH.equals(name)) {
                mVoiceHelperCallback.onCompleted(mResultRecognizedText);
            }
        }
    }

    private void loadOfflineEngine() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(SpeechConstant.DECODER, 2);
        params.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets://baidu_speech_grammar.bsg");
        mAsr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, new JSONObject(params).toString(), null, 0, 0);
    }

    private void unloadOfflineEngine() {
        mAsr.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0);
    }

    public interface VoiceHelperCallback {
        /**
         * 开始语音解析
         */
        void onVoiceStart();

        /**
         * 语音解析过程中的回调
         *
         * @param text:语音解析过程中实时结果
         */
        void onProcess(String text);

        /**
         * 语音解析完成回调
         *
         * @param resultText：语音解析过程的最终结果
         */
        void onCompleted(String resultText);

        /**
         * 语音解析失败回调
         *
         * @param msg：语音解析失败的原因，包括错误与取消这2种情况
         */
        void onFailure(String msg);
    }
}
