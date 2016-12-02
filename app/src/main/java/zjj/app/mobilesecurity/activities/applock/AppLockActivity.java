package zjj.app.mobilesecurity.activities.applock;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.adapters.AppLockAdapter;
import zjj.app.mobilesecurity.base.BaseActivity;
import zjj.app.mobilesecurity.dao.AppLockDao;
import zjj.app.mobilesecurity.domain.AppInfo;
import zjj.app.mobilesecurity.parsers.AppInfoParser;
import zjj.app.mobilesecurity.utils.Constants;
import zjj.app.mobilesecurity.utils.SharedPreferencesUtils;

public class AppLockActivity extends BaseActivity {
    //TODO dealing with Performing stop of activity that is not resumed: {zjj.app.mobilesecurity/zjj.app.mobilesecurity.activities.applock.AppLockActivity

    @BindView(R.id.iv_img_applock)
    ImageView iv_img_applock;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_desc1)
    TextView tv_desc1;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.rv_app_lock_list)
    RecyclerView rv_app_lock_list;

    private LinearLayout loading;
    private ActionBar actionBar;
    private AppLockTask task;
    private AppLockAdapter adapter;
    private int count;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_desc1.setVisibility(View.VISIBLE);
            if (count == 1) {
                tv_desc1.setText("1 app has been locked for protection");
            } else {
                tv_desc1.setText(count + " apps have been locked for protection");
            }

        }
    };
    private AlertDialog dialog;

    @Override
    public void initView() {
        setContentView(R.layout.activity_app_lock);
        ButterKnife.bind(this);
        setTheme(R.style.AppTheme_AppLock);
        loading = (LinearLayout) findViewById(R.id.loading);

        //如果没有设置密码，则进入设置
        String pattern = SharedPreferencesUtils.getString(this, "pattern", null);
        if (TextUtils.isEmpty(pattern)) {
            startActivityForResult(new Intent(this, AppLockSetPatternActivity.class), Constants.REQ_SET_PATTERN);
        } else {
            startActivityForResult(new Intent(this, AppLockConfirmPatternActivity.class),
                    Constants.REQ_CONFIRM_PATTERN);
        }


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
    protected void onResume() {
        super.onResume();
        //是否有权查看使用情况
        boolean allowed = usageAccessCheck();
        showUsageAccessDialog(allowed);
    }

    private void showUsageAccessDialog(boolean allowed) {
        if (!allowed) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("仅差一步, 即可体验应用锁");
            builder.setPositiveButton("前往设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(AppLockActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, Constants.REQ_USAGE_ACCESS);
                    dialog.dismiss();
                }
            });
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.dismiss();
                        finish();
                    }
                    return true;
                }
            });
            if (dialog == null) {
                dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
            }
            dialog.show();
        }
    }

    private boolean usageAccessCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            PackageManager pm = getPackageManager();
            try {
                ApplicationInfo info = pm.getApplicationInfo(getPackageName(), 0);
                AppOpsManager aom = (AppOpsManager) getSystemService(APP_OPS_SERVICE);
                int mode = aom.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName);
                return (mode == AppOpsManager.MODE_ALLOWED);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void setAppTheme() {
        setTheme(R.style.AppTheme_AppLock);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_with_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (android.R.id.home == itemId) {
            finish();
            overridePendingTransition(0, R.anim.new_out);
        } else if (R.id.menu_settings == itemId) {
            startActivity(new Intent(this, AppLockSettingsActivity.class));
            overridePendingTransition(R.anim.new_in, 0);
        }
        return super.onOptionsItemSelected(item);
    }


    private class AppLockTask extends AsyncTask<Void, Void, List<AppInfo>> {

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

            new Thread() {
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
                    if (isPositive) {
                        count++;
                    } else {
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
        if (requestCode == Constants.REQ_USAGE_ACCESS) {
            Toast.makeText(this, "return from usage stats setting", Toast.LENGTH_SHORT).show();
            showUsageAccessDialog(usageAccessCheck());
            return;
        }
        checkForPatternResult(this, requestCode, resultCode);
    }

    public void checkForPatternResult(Activity activity, int requestCode, int resultCode) {

        if (requestCode == Constants.REQ_SET_PATTERN && resultCode == RESULT_OK) {

        } else if (requestCode == Constants.REQ_SET_PATTERN && resultCode == RESULT_CANCELED) {
            activity.finish();
        } else if (requestCode == Constants.REQ_CONFIRM_PATTERN && resultCode == RESULT_CANCELED) {
            activity.finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
