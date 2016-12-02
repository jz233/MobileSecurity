package zjj.app.mobilesecurity.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.base.BaseFragment;
import zjj.app.mobilesecurity.domain.ApkInfo;
import zjj.app.mobilesecurity.parsers.ApkInfoParser;

public class ApkFragment extends BaseFragment {

    private GetApkTask task;

    public ApkFragment() {
        // Required empty public constructor
    }

    public static ApkFragment newInstance() {
        ApkFragment fragment = new ApkFragment();
        return fragment;
    }

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_apk, null);

        return view;
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        task = new GetApkTask();
        task.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        task.cancel(true);
    }

    private class GetApkTask extends AsyncTask<Void, Void, List<ApkInfo>>{

        @Override
        protected List<ApkInfo> doInBackground(Void... params) {
            return ApkInfoParser.getApkInfos(getActivity());
        }

        @Override
        protected void onPostExecute(List<ApkInfo> infos) {
            super.onPostExecute(infos);

        }
    }

}
