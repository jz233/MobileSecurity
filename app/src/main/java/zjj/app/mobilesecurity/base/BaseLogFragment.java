package zjj.app.mobilesecurity.base;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.dao.BlackListDao;

public abstract class BaseLogFragment extends BaseFragment implements View.OnClickListener {

    private Toolbar toolbar;
    public LinearLayout loading;
    private Button btn_ok;
    private Button btn_cancel;
    public ListView lv_log;
    private ActionBar actionBar;
    public BlackListDao dao;

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_log, null);

        toolbar = (Toolbar) view.findViewById(R.id.appbar).findViewById(R.id.toolbar);

        context.setSupportActionBar(toolbar);
        actionBar = context.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("添加到黑名单");
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        loading = (LinearLayout) view.findViewById(R.id.loading);
        lv_log = (ListView) view.findViewById(R.id.lv_log);
        btn_ok = (Button) view.findViewById(R.id.btn_ok);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        return view;
    }

    @Override
    public void initListener() {
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        setupListListener();

    }

    @Override
    public void initData() {
        dao = new BlackListDao(context);
        setupListData();
    }

    public abstract void setupListListener();

    public abstract void setupListData();

    public abstract void addNumberToBlackList();

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_ok) {
            addNumberToBlackList();

        } else if (id == R.id.btn_cancel) {

        }

        context.getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
