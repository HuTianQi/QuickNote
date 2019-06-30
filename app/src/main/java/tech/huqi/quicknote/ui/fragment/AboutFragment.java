package tech.huqi.quicknote.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import tech.huqi.quicknote.R;
import tech.huqi.quicknote.config.QuickNote;
import tech.huqi.quicknote.util.CommonUtil;

/**
 * Created by hzhuqi on 2019/4/6
 */
public class AboutFragment extends Fragment {
    private Button mBtnCheckUpdate;
    private TextView mTvAppVersion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initView(View root) {
        mBtnCheckUpdate = root.findViewById(R.id.btn_about_check_update);
        mTvAppVersion = root.findViewById(R.id.tv_about_version);
    }

    private void initData() {
        String appVersion = CommonUtil.getAppVersion();
        if (TextUtils.isEmpty(appVersion)) {
            mTvAppVersion.setText(appVersion);
        }
        mBtnCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndUpdateVersion();
            }
        });
    }

    private void checkAndUpdateVersion() {
        // 开源版本直接返回最新版本
        CommonUtil.showToastOnUiThread(QuickNote.getString(R.string.check_update_no_new_version));
    }
}
