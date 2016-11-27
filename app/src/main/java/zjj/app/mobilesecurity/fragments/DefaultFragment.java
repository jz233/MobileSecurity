package zjj.app.mobilesecurity.fragments;


import android.view.LayoutInflater;
import android.view.View;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.base.BaseHomePagerFragment;

public class DefaultFragment extends BaseHomePagerFragment {


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
