package zjj.app.mobilesecurity.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.base.BaseActivity;

public class AppLockSettingsActivity extends BaseActivity {

    private Toolbar toolbar;
    @Override
    public void initView() {
        setContentView(R.layout.activity_app_lock_settings);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(android.R.id.home == itemId){
            finish();
            overridePendingTransition(0, R.anim.new_out);
        }
        return super.onOptionsItemSelected(item);
    }
}
