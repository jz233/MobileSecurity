package zjj.app.mobilesecurity.activities;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.base.BaseActivity;
import zjj.app.mobilesecurity.services.AppLockService;
import zjj.app.mobilesecurity.services.CallSmsFilterService;
import zjj.app.mobilesecurity.services.CleanTaskService;
import zjj.app.mobilesecurity.utils.Constants;
import zjj.app.mobilesecurity.utils.ServiceUtils;
import zjj.app.mobilesecurity.utils.SharedPreferencesUtils;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fab;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private SharedPreferences sp;

    @Override
    public void initView() {
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        sp = SharedPreferencesUtils.getSharedPreferences(this);

       /* new Thread(){
            @Override
            public void run() {
                super.run();
                TrafficStats ts = new TrafficStats();
//                long bytes = TrafficStats.getTotalRxBytes();
                PackageManager pm = getPackageManager();
                List<PackageInfo> infos = pm.getInstalledPackages(0);
                for(PackageInfo info : infos){
                    long bytes = TrafficStats.getUidRxBytes(info.applicationInfo.uid);
                    String packageName = info.applicationInfo.packageName;
                    Log.d("HomeActivity", packageName + " -- bytes:" + bytes);
                }
            }
        }.start();*/


    }

    @Override
    public void initListener() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void initData() {

        /*FileUtils.displayDirAndFiles(getFilesDir().getPath());

        if (BuildConfig.DEBUG){
            Log.d("HomeActivity", Environment.getDataDirectory().getAbsolutePath());
            Log.d("HomeActivity", Environment.getRootDirectory().getAbsolutePath());
            Log.d("HomeActivity", Environment.getDownloadCacheDirectory().getAbsolutePath());
            Log.d("HomeActivity", Environment.getExternalStorageDirectory().getAbsolutePath());

        }

        new Thread(){
            @Override
            public void run() {
                super.run();
                ApkInfoParser.getApkInfos(getApplicationContext());
            }
        }.start();*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        //避免开机自启动服务意外关闭
        ServiceUtils.startMultiServices(this, CallSmsFilterService.class, AppLockService.class);
        //
        boolean enabled = SharedPreferencesUtils.getBoolean(this, Constants.PREF_KEY_AUTO_CLEAN_SCREEN_OFF, false);
        if(enabled){
           startService(new Intent(this, CleanTaskService.class));
        }

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.app_manager) {
            startActivity(new Intent(this, AppManagerActivity.class));

            overridePendingTransition(R.anim.new_in, 0);
        }else if(id  == R.id.block_call_sms){
            startActivity(new Intent(this, CallSmsFilterActivity.class));
            overridePendingTransition(R.anim.new_in, 0);


        }else if(id  == R.id.app_lock){
            startActivity(new Intent(this, AppLockActivity.class));
//            overridePendingTransition(R.anim.new_in, 0);

        }else if(id  == R.id.task_manager){
            startActivity(new Intent(this, TaskManagerActivity.class));
            overridePendingTransition(R.anim.new_in, 0);

        }else if(id  == R.id.app_settings){

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * 屏蔽手机物理菜单按键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_MENU){
            //do nothing
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
