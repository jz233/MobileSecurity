package zjj.app.mobilesecurity.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.activities.applock.AppLockActivity;
import zjj.app.mobilesecurity.activities.appmgr.AppManagerActivity;
import zjj.app.mobilesecurity.activities.callsmsfilter.CallSmsFilterActivity;
import zjj.app.mobilesecurity.activities.taskmgr.TaskManagerActivity;
import zjj.app.mobilesecurity.adapters.HomePagerAdapter;
import zjj.app.mobilesecurity.base.BaseActivity;
import zjj.app.mobilesecurity.fragments.SpeedUpFragment;
import zjj.app.mobilesecurity.services.AppLockService;
import zjj.app.mobilesecurity.services.CallSmsFilterService;
import zjj.app.mobilesecurity.services.CleanTaskService;
import zjj.app.mobilesecurity.utils.Constants;
import zjj.app.mobilesecurity.utils.ServiceUtils;
import zjj.app.mobilesecurity.utils.SharedPreferencesUtils;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, SpeedUpFragment.OnCheckCompleteListener {

    private FloatingActionButton fab;
    private Toolbar toolbar;
    private DrawerLayout drawer_layout;
    private ActionBarDrawerToggle toggle;
    private NavigationView nav_view;
    private ViewPager viewpager;
    private TabLayout tab_layout;
    private SharedPreferences sp;

    @Override
    public void initView() {
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);

        tab_layout.setupWithViewPager(viewpager);

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

        toggle = new ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.setDrawerListener(toggle);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(this);

    }

    @Override
    public void initData() {
        sp = SharedPreferencesUtils.getSharedPreferences(this);

    }

    @Override
    public void setAppTheme() {
        setTheme(R.style.AppTheme_Home);
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
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
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

        drawer_layout.closeDrawer(GravityCompat.START);
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

    @Override
    public void onCheckComplete() {
        //change toolbar(theme) color



    }
}
