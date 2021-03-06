package zjj.app.mobilesecurity.activities.callsmsfilter;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.adapters.BlackListAdapter;
import zjj.app.mobilesecurity.base.BaseActivity;
import zjj.app.mobilesecurity.dao.BlackListDao;
import zjj.app.mobilesecurity.domain.BlackListNumber;
import zjj.app.mobilesecurity.fragments.CallLogFragment;
import zjj.app.mobilesecurity.fragments.ContactsFragment;
import zjj.app.mobilesecurity.fragments.InputNumberFragment;
import zjj.app.mobilesecurity.fragments.SmsLogFragment;

public class BlackListActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lv_black_list)
    ListView lv_black_list;
    @BindView(R.id.btn_add_number)
    Button btn_add_number;
    @BindView(R.id.fl_fragment_container)
    FrameLayout fl_fragment_container;
    @BindView(R.id.ll_from_call_log)
    LinearLayout ll_from_call_log;
    @BindView(R.id.ll_from_msg)
    LinearLayout ll_from_msg;
    @BindView(R.id.ll_from_contacts)
    LinearLayout ll_from_contacts;
    @BindView(R.id.ll_manually_input)
    LinearLayout ll_manually_input;
    @BindView(R.id.ll_sliding_panel)
    LinearLayout ll_sliding_panel;
    @BindView(R.id.sliding_up_panel_layout)
    SlidingUpPanelLayout sliding_up_panel_layout;

    private ActionBar actionBar;
    private BlackListDao dao;
    private List<BlackListNumber> numberList;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private BlackListAdapter adapter;

    public void initView() {
        setContentView(R.layout.activity_black_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("黑名单");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        fm = getSupportFragmentManager();


    }

    @Override
    public void initListener() {
        sliding_up_panel_layout.setFadeOnClickListener(this);
        btn_add_number.setOnClickListener(this);
        ll_from_call_log.setOnClickListener(this);
        ll_from_msg.setOnClickListener(this);
        ll_from_contacts.setOnClickListener(this);
        ll_manually_input.setOnClickListener(this);
    }

    @Override
    public void initData() {
        new BlackListTask().execute();

    }

    @Override
    public void setAppTheme() {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO fix return
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.old_in, R.anim.new_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_number) {
            sliding_up_panel_layout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        } else if (v.getId() == R.id.ll_from_call_log) {
            ft = fm.beginTransaction();
            ft.add(R.id.fl_fragment_container, CallLogFragment.newInstance(), "CallLogFragment");
            ft.setCustomAnimations(R.anim.new_in, R.anim.old_out);
            ft.commit();
            //TODO fm.executePendingTransactions();
            fm.executePendingTransactions();

            sliding_up_panel_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);


        } else if (v.getId() == R.id.ll_from_msg) {
            ft = fm.beginTransaction();
            ft.add(R.id.fl_fragment_container, SmsLogFragment.newInstance(), "SmsLogFragment");
            ft.setCustomAnimations(R.anim.new_in, R.anim.old_out);
            ft.commit();
            //TODO fm.executePendingTransactions();
            fm.executePendingTransactions();

            sliding_up_panel_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        } else if (v.getId() == R.id.ll_from_contacts) {
            ft = fm.beginTransaction();
            ft.add(R.id.fl_fragment_container, ContactsFragment.newInstance(), "ContactsFragment");
            ft.setCustomAnimations(R.anim.new_in, R.anim.old_out);
            ft.commit();
            //TODO fm.executePendingTransactions();
            fm.executePendingTransactions();

            sliding_up_panel_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (v.getId() == R.id.ll_manually_input) {
            ft = fm.beginTransaction();
            ft.add(R.id.fl_fragment_container, InputNumberFragment.newInstance(), "InputNumberFragment");
            ft.setCustomAnimations(R.anim.new_in, R.anim.old_out);
            ft.commit();
            //TODO fm.executePendingTransactions();
            fm.executePendingTransactions();

            sliding_up_panel_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (v.getId() == R.id.sliding_up_panel_layout) {
            sliding_up_panel_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    private class BlackListTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dao = new BlackListDao(getApplicationContext());
        }

        @Override
        protected Cursor doInBackground(Void... params) {
            return dao.getAll();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            //每次都重新生成adapter,以确保每次都会更新列表
            adapter = new BlackListAdapter(getApplicationContext(), cursor, new BlackListAdapter.OnImageClickListener() {
                @Override
                public void onImageClicked() {
                    Snackbar.make(findViewById(android.R.id.content), "Deleted", Snackbar.LENGTH_SHORT).show();
                    initData();
                }
            });
            lv_black_list.setAdapter(adapter);

        }
    }

}
