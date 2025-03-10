/*
 * Copyright (C) 2021 The exTHmUI Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.preferences.game;

import com.android.internal.logging.nano.MetricsProto;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import androidx.preference.PreferenceScreen;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import java.util.ArrayList;

import com.android.settings.preferences.PackageListPreference;
import org.aospextended.support.preference.SystemSettingSeekBarPreference;

public class GamingModeSettings extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private PackageListPreference mGamingPrefList;
    private SwitchPreference mUseMenuSwitch;
    private SwitchPreference mShowFPS;
    private Preference mDanmaku;
    private SystemSettingSeekBarPreference mOpacity;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.exthm_settings_gaming);

        final PreferenceScreen prefScreen = getPreferenceScreen();

        mUseMenuSwitch = (SwitchPreference) findPreference("gaming_mode_use_overlay_menu");
        mShowFPS = (SwitchPreference) findPreference("gaming_mode_show_fpsinfo");
        mDanmaku = (Preference) findPreference("gaming_mode_notification_danmaku");
        mOpacity = (SystemSettingSeekBarPreference) findPreference("gaming_mode_menu_opacity");

        boolean fpsEnabled = Settings.System.getInt(getContentResolver(),
                            Settings.System.GAMING_MODE_SHOW_FPSINFO, 0) == 1;

        mShowFPS.setChecked(fpsEnabled);
        mShowFPS.setOnPreferenceChangeListener(this);
        
        boolean menuEnabled = Settings.System.getInt(getContentResolver(),
                            Settings.System.GAMING_MODE_USE_OVERLAY_MENU, 1) == 1;
        mUseMenuSwitch.setChecked(menuEnabled);
        mUseMenuSwitch.setOnPreferenceChangeListener(this);

        mDanmaku.setEnabled(menuEnabled);
        mOpacity.setEnabled(menuEnabled);
        
        mGamingPrefList = (PackageListPreference) findPreference("gaming_mode_app_list");
        mGamingPrefList.setRemovedListKey(Settings.System.GAMING_MODE_REMOVED_APP_LIST);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mUseMenuSwitch) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(resolver, Settings.System.GAMING_MODE_USE_OVERLAY_MENU,
                    value ? 1 : 0);
            mDanmaku.setEnabled(value);
            mOpacity.setEnabled(value);
            return true;
	} else if (preference == mShowFPS) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(resolver, Settings.System.GAMING_MODE_SHOW_FPSINFO,
                    value ? 1 : 0);
            return true;
        }
        return false;
    }
    
    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.EXTHMUI_SETTINGS;
    }
}
