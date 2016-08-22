package zjj.app.mobilesecurity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.utils.Constants;
import zjj.app.mobilesecurity.utils.SharedPreferencesUtils;

public class AppLockSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {


    private SwitchPreferenceCompat sPref;
    private boolean relock_screen_off;
    private Context context;
    private ListPreference lPref;
    private int relock_time;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initListener();
        initData();

    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.app_lock_preferences);
    }


    private void initView() {
        sPref = (SwitchPreferenceCompat) findPreference(Constants.PREF_KEY_RELOCK_SCREEN_OFF);
        lPref = (ListPreference) findPreference(Constants.PREF_KEY_RELOCK_TIME);
    }

    private void initListener() {
        sPref.setOnPreferenceChangeListener(this);
        lPref.setOnPreferenceChangeListener(this);
    }

    private void initData() {
        relock_screen_off = SharedPreferencesUtils.getBoolean(context, Constants.PREF_KEY_RELOCK_SCREEN_OFF, false);
        sPref.setChecked(relock_screen_off);

        relock_time = SharedPreferencesUtils.getInt(context, Constants.PREF_KEY_RELOCK_TIME, 0);
        lPref.setValue(String.valueOf(relock_time));

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        String key = preference.getKey();
        if(key != null){
            if(Constants.PREF_KEY_RELOCK_SCREEN_OFF.equals(key)){
                boolean value = (boolean) o;
                SharedPreferencesUtils.putBoolean(context, Constants.PREF_KEY_RELOCK_SCREEN_OFF, value);
                return true;
            }else if(Constants.PREF_KEY_RELOCK_TIME.equals(key)){
                String value = (String) o;
                SharedPreferencesUtils.putInt(context, Constants.PREF_KEY_RELOCK_TIME, Integer.parseInt(value));
                return true;
            }
        }

        return false;
    }

}
