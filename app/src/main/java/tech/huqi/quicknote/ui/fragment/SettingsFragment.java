package tech.huqi.quicknote.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;

import java.util.Locale;

import tech.huqi.quicknote.R;
import tech.huqi.quicknote.config.QuickNote;
import tech.huqi.quicknote.ui.activity.LockActivity;
import tech.huqi.quicknote.ui.activity.MainActivity;
import tech.huqi.quicknote.util.CommonUtil;

/**
 * Created by hzhuqi on 2019/4/22
 */
public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    private static final String KEY_THEME = QuickNote.getString(R.string.preference_key_theme);
    private static final String KEY_LANG = QuickNote.getString(R.string.preference_key_lang);
    private static final String KEY_AUTO_UPDATE = QuickNote.getString(R.string.preference_key_auto_check_update);
    private static final String KEY_GESTURE_PWD = QuickNote.getString(R.string.preference_key_gesture_pwd);
    private static final String KEY_ABOUT = QuickNote.getString(R.string.preference_key_about);
    private static final String KEY_RECOMMEND = QuickNote.getString(R.string.preference_key_recommend);
    private static final String KEY_DONATE = QuickNote.getString(R.string.preference_key_donate);
    private static final String KEY_HIGH_PRAISE = QuickNote.getString(R.string.preference_key_high_praise);

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_setting, rootKey);
        initPreferenceListener();
    }

    private void initPreferenceListener() {
        findPreference(KEY_THEME).setOnPreferenceChangeListener(this);
        findPreference(KEY_LANG).setOnPreferenceChangeListener(this);
        findPreference(KEY_AUTO_UPDATE).setOnPreferenceChangeListener(this);
        findPreference(KEY_GESTURE_PWD).setOnPreferenceChangeListener(this);
        findPreference(KEY_ABOUT).setOnPreferenceClickListener(this);
        findPreference(KEY_RECOMMEND).setOnPreferenceClickListener(this);
//        findPreference(KEY_DONATE).setOnPreferenceClickListener(this);
        findPreference(KEY_HIGH_PRAISE).setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String key = preference.getKey();
        if (key.equals(KEY_THEME)) {
            if (value.equals(QuickNote.getString(R.string.theme_value_default))) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (value.equals(QuickNote.getString(R.string.theme_value_night))) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            getActivity().recreate();
        } else if (key.equals(KEY_LANG)) {
            if (value.equals(QuickNote.getString(R.string.lang_value_chinese))) {
                CommonUtil.changeLocalLanguage(QuickNote.getAppContext(), Locale.SIMPLIFIED_CHINESE);
            } else if (value.equals(QuickNote.getString(R.string.lang_value_english))) {
                CommonUtil.changeLocalLanguage(QuickNote.getAppContext(), Locale.US);
            }
            getActivity().recreate();
        } else if (key.equals(KEY_AUTO_UPDATE)) {
            if (value.equals(false)) { // 提示用户如需更新可到关于界面进行手动更新
                CommonUtil.showSnackBarOnUiThread(getView(), R.string.prompt_close_auto_update);
            }
        } else if (key.equals(KEY_GESTURE_PWD)) {
            if (value.equals(true)) {
                if (TextUtils.isEmpty(QuickNote.getSavedPattern())) { // 如果是初次启用
                    Intent intent = new Intent(getActivity(), LockActivity.class);
                    startActivity(intent);
                }
            }
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals(KEY_ABOUT)) {
            ((MainActivity) getActivity()).switchFragmentByTag(MainActivity.ABOUT_FRAGMENT);
        } else if (key.equals(KEY_RECOMMEND)) {

        } else if (key.equals(KEY_DONATE)) {
            ((MainActivity) getActivity()).switchFragmentByTag(MainActivity.PURCHASE_FRAGMENT);
        } else if (key.equals(KEY_HIGH_PRAISE)) {
            CommonUtil.jumpToMarket(getContext(), CommonUtil.getPackageName(getContext()), null);
        }
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//         setHasOptionsMenu(false);
//        getActivity().supportInvalidateOptionsMenu();
//        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    // todo 为何不起作用
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        ((MainActivity) getActivity()).getToolbar().getMenu().findItem(R.id.menu_search).setVisible(false);
        menu.findItem(R.id.menu_sort).setVisible(false);
        menu.findItem(R.id.switch_mode).setVisible(false);
    }
}
