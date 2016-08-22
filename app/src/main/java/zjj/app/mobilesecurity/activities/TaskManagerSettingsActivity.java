package zjj.app.mobilesecurity.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.base.BaseActivity;

public class TaskManagerSettingsActivity extends BaseActivity {

    private Toolbar toolbar;
    private FragmentManager fm;

    @Override
    public void initView() {
        fm = getSupportFragmentManager();

        setContentView(R.layout.activity_task_mgr_settings);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public FragmentManager getFragmentManagerFromActivity() {
        return fm;
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
