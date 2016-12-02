package zjj.app.mobilesecurity.activities.taskmgr;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.base.BaseActivity;

public class TaskManagerSettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private FragmentManager fm;

    @Override
    public void initView() {
        setContentView(R.layout.activity_task_mgr_settings);

        fm = getSupportFragmentManager();
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("进程清理设置");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void setAppTheme() {

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
