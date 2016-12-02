package zjj.app.mobilesecurity.activities.callsmsfilter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.base.BaseActivity;

public class CallSmsFilterActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_call_filter)
    LinearLayout ll_call_filter;
    @BindView(R.id.ll_sms_filter)
    LinearLayout ll_sms_filter;
    @BindView(R.id.btn_filter_settings)
    Button btn_filter_settings;

    Toolbar toolbar;

    private ActionBar actionBar;

    @Override
    public void initView() {
        setContentView(R.layout.activity_call_sms_filter);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.appbar_call_sms_filter).findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("骚扰拦截");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void initListener() {
        ll_call_filter.setOnClickListener(this);
        ll_sms_filter.setOnClickListener(this);
        btn_filter_settings.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void setAppTheme() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.old_in, R.anim.new_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_call_filter) {

        } else if (v.getId() == R.id.ll_sms_filter) {

        } else if (v.getId() == R.id.btn_filter_settings) {
            startActivity(new Intent(CallSmsFilterActivity.this, BlackListActivity.class));
            overridePendingTransition(R.anim.new_in, R.anim.old_out);

        }
    }

}
