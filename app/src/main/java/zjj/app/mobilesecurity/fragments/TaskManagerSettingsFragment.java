package zjj.app.mobilesecurity.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.activities.TaskManagerSettingsActivity;
import zjj.app.mobilesecurity.activities.WhiteListActivity;
import zjj.app.mobilesecurity.services.CleanTaskService;
import zjj.app.mobilesecurity.utils.Constants;
import zjj.app.mobilesecurity.utils.ServiceUtils;
import zjj.app.mobilesecurity.utils.SharedPreferencesUtils;


public class TaskManagerSettingsFragment extends PreferenceFragment {

    private SwitchPreferenceCompat sPref;
    private Preference pref;
    private Activity context;
    private FragmentManager fm;
    private Intent service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        fm = ((TaskManagerSettingsActivity) context).getFragmentManagerFromActivity();
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
        addPreferencesFromResource(R.xml.task_mgr_preferences);
    }


    private void initView() {
        sPref = (SwitchPreferenceCompat) findPreference(Constants.PREF_KEY_AUTO_CLEAN_SCREEN_OFF);
        pref = findPreference(Constants.PREF_KEY_WHITE_LIST);

    }

    private void initData() {
        boolean enabled = SharedPreferencesUtils.getBoolean(getActivity(), Constants.PREF_KEY_AUTO_CLEAN_SCREEN_OFF, false);
        sPref.setChecked(enabled);
    }

    private void initListener() {
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
//                if (BuildConfig.DEBUG) Log.d("TaskManagerSettingsFrag", "Pref clicked");
                startActivity(new Intent(context, WhiteListActivity.class));
                context.overridePendingTransition(R.anim.new_in, 0);
                return true;
            }
        });

        sPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String key = preference.getKey();
                if(key != null && key.equals(Constants.PREF_KEY_AUTO_CLEAN_SCREEN_OFF)){
                    boolean value = (boolean) o;
                    SharedPreferencesUtils.putBoolean(context, Constants.PREF_KEY_AUTO_CLEAN_SCREEN_OFF, value);
                    if(value){
                        service = ServiceUtils.startService(context, CleanTaskService.class);
                    }else{
                        ServiceUtils.stopService(context, service, CleanTaskService.class);
                    }
                    return true;
                }
                return false;
            }
        });
    }



}
