package zjj.app.mobilesecurity.activities.callsmsfilter;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.base.BaseActivity;

public class CallSmsFilterActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toobar;
    private LinearLayout ll_call_filter;
    private LinearLayout ll_sms_filter;
    private Button btn_filter_settings;
    private ActionBar actionBar;

    @Override
    public void initView() {
        setContentView(R.layout.activity_call_sms_filter);

        toobar = (Toolbar) findViewById(R.id.appbar_call_sms_filter).findViewById(R.id.toolbar);
        ll_call_filter = (LinearLayout) findViewById(R.id.ll_call_filter);
        ll_sms_filter = (LinearLayout) findViewById(R.id.ll_sms_filter);
        btn_filter_settings = (Button) findViewById(R.id.btn_filter_settings);

        setSupportActionBar(toobar);

        actionBar = getSupportActionBar();
        if(actionBar != null){
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
        if(item.getItemId() == android.R.id.home){
            finish();
            overridePendingTransition(R.anim.old_in, R.anim.new_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ll_call_filter){

        }else if(v.getId() == R.id.ll_sms_filter){

        }else if(v.getId() == R.id.btn_filter_settings){
            startActivity(new Intent(CallSmsFilterActivity.this, BlackListActivity.class));
            overridePendingTransition(R.anim.new_in, R.anim.old_out);

        }
    }
}
