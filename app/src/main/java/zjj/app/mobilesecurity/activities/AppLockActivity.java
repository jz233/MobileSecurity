package zjj.app.mobilesecurity.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.adapters.AppLockAdapter;
import zjj.app.mobilesecurity.base.BaseActivity;
import zjj.app.mobilesecurity.dao.AppLockDao;
import zjj.app.mobilesecurity.domain.AppInfo;
import zjj.app.mobilesecurity.parsers.AppInfoParser;
import zjj.app.mobilesecurity.utils.Constants;
import zjj.app.mobilesecurity.utils.SharedPreferencesUtils;

public class AppLockActivity extends BaseActivity{

    //TODO dealing with Performing stop of activity that is not resumed: {zjj.app.mobilesecurity/zjj.app.mobilesecurity.activities.AppLockActivity

    private Toolbar toolbar;
    private RecyclerView rv_app_lock_list;
//    private TextView tv_desc1;
    private LinearLayout loading;
    private ActionBar actionBar;
    private AppLockTask task;
    private AppLockAdapter adapter;
    private int count;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            /*tv_desc1.setVisibility(View.VISIBLE);
            if(count == 1){
                tv_desc1.setText("1 app has been locked for protection");
            }else{
                tv_desc1.setText(count + " apps have been locked for protection");
            }*/

        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_app_lock);

        //如果没有设置密码，则进入设置
        String pattern = SharedPreferencesUtils.getString(this, "pattern", null);
        if(TextUtils.isEmpty(pattern)){
            startActivityForResult(new Intent(this, AppLockSetPatternActivity.class), Constants.REQ_SET_PATTERN);
        }else{
            startActivityForResult(new Intent(this, AppLockConfirmPatternActivity.class),
                    Constants.REQ_CONFIRM_PATTERN);
        }

        rv_app_lock_list = (RecyclerView) findViewById(R.id.rv_app_lock_list);
//        tv_desc1 = (TextView) findViewById(R.id.header_app_lock).findViewById(R.id.tv_desc1);
        loading = (LinearLayout) findViewById(R.id.loading);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("应用锁");

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        task = new AppLockTask();
        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_with_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(android.R.id.home == itemId){
            finish();
            overridePendingTransition(0, R.anim.new_out);
        }else if(R.id.menu_settings == itemId){
            startActivity(new Intent(this, AppLockSettingsActivity.class));
            overridePendingTransition(R.anim.new_in, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    private class AppLockTask extends AsyncTask<Void, Void, List<AppInfo>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_app_lock_list.setLayoutManager(manager);
            rv_app_lock_list.setHasFixedSize(true);
        }

        @Override
        protected List<AppInfo> doInBackground(Void... params) {
            List<AppInfo> infos = new AppInfoParser().getAllAppsInfo(getApplicationContext());
            return infos;
        }

        @Override
        protected void onPostExecute(final List<AppInfo> infos) {
            super.onPostExecute(infos);
            loading.setVisibility(View.INVISIBLE);

            new Thread(){
                @Override
                public void run() {
                    super.run();
                    count = new AppLockDao(getApplicationContext()).getCount();
                    handler.sendEmptyMessage(0);

                }
            }.start();

            adapter = new AppLockAdapter(getApplicationContext(), infos, new AppLockAdapter.OnAppCountChangedListener() {
                @Override
                public void onAppCountChanged(boolean isPositive) {
                    if(isPositive){
                        count++;
                    }else{
                        count--;
                    }
                    handler.sendEmptyMessage(0);
                }
            });
            rv_app_lock_list.setAdapter(adapter);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        checkForPatternResult(this, requestCode, resultCode);
    }

    public void checkForPatternResult(Activity activity, int requestCode, int resultCode){

        if(requestCode == Constants.REQ_SET_PATTERN && resultCode == RESULT_OK){

        }else if(requestCode == Constants.REQ_SET_PATTERN && resultCode == RESULT_CANCELED){
            activity.finish();
        }else if(requestCode == Constants.REQ_CONFIRM_PATTERN &&  resultCode == RESULT_CANCELED){
            activity.finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
