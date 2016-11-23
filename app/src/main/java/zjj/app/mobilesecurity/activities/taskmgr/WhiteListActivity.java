package zjj.app.mobilesecurity.activities.taskmgr;

import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.adapters.WhiteListAdapter;
import zjj.app.mobilesecurity.base.BaseActivity;
import zjj.app.mobilesecurity.dao.WhiteListDao;
import zjj.app.mobilesecurity.domain.AppInfo;
import zjj.app.mobilesecurity.utils.SystemUtils;


public class WhiteListActivity extends BaseActivity {

    private CoordinatorLayout root_view;
    private Toolbar toolbar;
    private RecyclerView rv_white_list;
    private LinearLayout loading;
    private WhiteListAdapter adapter;
    private List<String> whiteList;
    private ActionBar actionBar;

    @Override
    public void initView() {
        setContentView(R.layout.activity_white_list);

        root_view = (CoordinatorLayout) findViewById(R.id.root_view);
        toolbar = (Toolbar) findViewById(R.id.appbar_white_list).findViewById(R.id.toolbar);
        rv_white_list = (RecyclerView) findViewById(R.id.rv_white_list);
        loading = (LinearLayout) findViewById(R.id.loading);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        new GetPackagesTask().execute();
    }

    @Override
    public void setAppTheme() {

    }

    private class GetPackagesTask extends AsyncTask<Void, Void, List<AppInfo>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rv_white_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }

        @Override
        protected List<AppInfo> doInBackground(Void... params) {
            whiteList = new WhiteListDao(WhiteListActivity.this).getAll();
            return SystemUtils.getInstalledPackagesInfo(getApplicationContext());
        }

        @Override
        protected void onPostExecute(List<AppInfo> infos) {
            super.onPostExecute(infos);
            loading.setVisibility(View.INVISIBLE);

            adapter = new WhiteListAdapter(getApplicationContext(), infos, whiteList);
            rv_white_list.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(android.R.id.home == item.getItemId()){
            finish();
            overridePendingTransition(0, R.anim.new_out);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
