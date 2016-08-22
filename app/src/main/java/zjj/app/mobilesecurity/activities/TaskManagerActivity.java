package zjj.app.mobilesecurity.activities;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.adapters.DividerDecoration;
import zjj.app.mobilesecurity.adapters.TaskListAdapter;
import zjj.app.mobilesecurity.base.BaseActivity;
import zjj.app.mobilesecurity.domain.TaskInfo;
import zjj.app.mobilesecurity.utils.SystemUtils;

public class TaskManagerActivity extends BaseActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private RecyclerView rv_task_list;
    private LinearLayout loading;
    private Button btn_select_all;
    private Button btn_clear;
    private Button btn_unselect_all;
    private View root_view;
    private ActionBar actionBar;
    private TaskListAdapter adapter;
    private TaskListTask task;
    private List<TaskInfo> infos;
    private ActivityManager am;

    @Override
    public void initView() {
        setContentView(R.layout.activity_task_manager);

        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        root_view = findViewById(R.id.root_view);
        rv_task_list = (RecyclerView) findViewById(R.id.rv_task_list);
        loading = (LinearLayout) findViewById(R.id.loading);
        btn_select_all = (Button) findViewById(R.id.btn_select_all);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_unselect_all = (Button) findViewById(R.id.btn_unselect_all);

        toolbar = (Toolbar) findViewById(R.id.appbar_task_manager).findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("进程管理");
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void initListener() {
        btn_select_all.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        btn_unselect_all.setOnClickListener(this);

    }

    @Override
    public void initData() {
        task = new TaskListTask();
        task.execute();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.btn_select_all == id){
            for(TaskInfo info : infos){
                info.setSelected(true);
            }
            adapter.notifyDataSetChanged();
        }else if(R.id.btn_unselect_all == id){
            for(TaskInfo info : infos){
                info.setSelected(false);
            }
            adapter.notifyDataSetChanged();
        }else if(R.id.btn_clear == id){
            List<TaskInfo> tasks = adapter.getClearList();
            if(tasks.size() == 0){
                Snackbar.make(root_view, "请先选中要清理的进程", Snackbar.LENGTH_SHORT).show();
                return;
            }
            int totalMemSize = 0;
            for(TaskInfo task : tasks){
                String pkgName = task.getPkgName();
                infos.contains(pkgName);
                totalMemSize += task.getMemSize();
                am.killBackgroundProcesses(pkgName);
            }
            Snackbar.make(root_view, "加速成功，共清理内存: " + Formatter.formatFileSize(getApplicationContext(), totalMemSize*1024), Snackbar.LENGTH_SHORT).show();
            initData();

        }
    }

    class TaskListTask extends AsyncTask<Void,Void, List<TaskInfo>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading.setVisibility(View.VISIBLE);
            setButtonClickable(false);

            rv_task_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rv_task_list.addItemDecoration(new DividerDecoration(getApplicationContext(), 1));
        }

        @Override
        protected List<TaskInfo> doInBackground(Void... params) {
            infos = SystemUtils.getRunningTasks(getApplicationContext());
            return infos;
        }

        @Override
        protected void onPostExecute(List<TaskInfo> infos) {
            super.onPostExecute(infos);
            loading.setVisibility(View.INVISIBLE);
            setButtonClickable(true);

            //每次都要重新生成adapter
            adapter = new TaskListAdapter(getApplicationContext(), infos);
            rv_task_list.setAdapter(adapter);
        }
    }

    /**
     * 避免未加载完数据时点击按钮出错
     * @param enabled
     */
    private void setButtonClickable(boolean enabled) {
        btn_select_all.setClickable(enabled);
        btn_unselect_all.setClickable(enabled);
        btn_clear.setClickable(enabled);
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
            overridePendingTransition(R.anim.old_in, R.anim.new_out);
            return true;
        }else if(R.id.menu_settings == itemId){
            startActivity(new Intent(getApplicationContext(), TaskManagerSettingsActivity.class));
            overridePendingTransition(R.anim.new_in, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
