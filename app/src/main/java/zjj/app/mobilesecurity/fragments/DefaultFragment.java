package zjj.app.mobilesecurity.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.base.BaseFragment;

public class DefaultFragment extends BaseFragment {


    public static DefaultFragment newInstance() {
        return new DefaultFragment();
    }


    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_default, null);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

}
