package zjj.app.mobilesecurity.activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
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
import zjj.app.mobilesecurity.ui.DirectionalViewPager;
import zjj.app.mobilesecurity.utils.Constants;
import zjj.app.mobilesecurity.utils.ServiceUtils;
import zjj.app.mobilesecurity.utils.SharedPreferencesUtils;
import zjj.app.mobilesecurity.utils.UIUtils;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, SpeedUpFragment.OnCheckCompleteListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tab_layout;
    @BindView(R.id.directional_viewpager)
    DirectionalViewPager viewpager;
    @BindView(R.id.nav_view)
    NavigationView nav_view;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;


    private ActionBarDrawerToggle toggle;
    private HomePagerAdapter pagerAdapter;
    private SharedPreferences sp;
    private boolean allowed = false;

    private int[] colorConditions = {R.color.colorPrimaryGood, R.color.colorPrimaryOk, R.color.colorPrimaryFair};
    private int[] statusColorConditions = {R.color.colorPrimaryDarkGood, R.color.colorPrimaryDarkOk, R.color.colorPrimaryDarkFair};
    private Window window;

    @Override
    public void initView() {
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setUpViewPagerListener();
        pagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(pagerAdapter);

        tab_layout.setupWithViewPager(viewpager);

        //得到屏幕最上方状态栏
        window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

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

    private void setUpViewPagerListener() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int colorFrom;
            private int colorTo;
            private int currentPage;
            private int direction;
            private int colorStatusFrom;
            private int colorStatusTo;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ArgbEvaluator evaluator = new ArgbEvaluator();

                direction = viewpager.getDirection();
                //pager向左移动,而且当前不是第一个pager时
                if ((allowed || viewpager.directionChanged) && direction == DirectionalViewPager.LEFT && currentPage != 0) {
                    colorTo = ContextCompat.getColor(HomeActivity.this, colorConditions[currentPage - 1]);
                    colorStatusTo = ContextCompat.getColor(HomeActivity.this, statusColorConditions[currentPage - 1]);
                    allowed = viewpager.directionChanged = false;
                }
                if ((allowed || viewpager.directionChanged) && direction == DirectionalViewPager.RIGHT && currentPage != 2) {
                    colorTo = ContextCompat.getColor(HomeActivity.this, colorConditions[currentPage + 1]);
                    colorStatusTo = ContextCompat.getColor(HomeActivity.this, statusColorConditions[currentPage + 1]);
                    allowed = viewpager.directionChanged = false;
                }
                if (positionOffset > 0 && positionOffset < 1 && direction != DirectionalViewPager.STATIC) {
                    float offset = positionOffset;
                    if (direction == DirectionalViewPager.LEFT) {
                        offset = 1 - positionOffset;
                    }
                    Object color = evaluator.evaluate(offset, colorFrom, colorTo);
                    tab_layout.setBackgroundColor((Integer) color);
                    toolbar.setBackgroundColor((Integer) color);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Object statusColor = evaluator.evaluate(offset, colorStatusFrom, colorStatusTo);
                        window.setStatusBarColor((Integer) statusColor);
                    }

                }

            }

            @Override
            public void onPageSelected(int position) {
                //刚打开应用时的第一个pager不调用onPageSelected
                Log.d("HomeActivity", "position:" + position);
                changeToolbarColorAnim(ContextCompat.getColor(HomeActivity.this, colorConditions[position]));
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //0 stopped 1 touched 2 fling
                if (state == 1) {
                    allowed = true;
//                    colorFrom = ((ColorDrawable) toolbar.getBackground()).getColor();
                    colorFrom = ContextCompat.getColor(HomeActivity.this, colorConditions[currentPage]);
                    colorStatusFrom = ContextCompat.getColor(HomeActivity.this, statusColorConditions[currentPage]);
                } else {
                    allowed = false;
                    direction = DirectionalViewPager.STATIC;
                }

            }
        });
    }

    private void changeToolbarColorAnim(int colorTo) {
        int colorFrom = ((ColorDrawable) toolbar.getBackground()).getColor();
        UIUtils.colorChangeAnimation(colorFrom, colorTo, 200, new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                tab_layout.setBackgroundColor((Integer) animation.getAnimatedValue());
                toolbar.setBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });
    }


    public Toolbar getToolbar() {
        return toolbar;
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
        copyDatabases("antivirus.db");
    }

    /**
     * 设置主题Theme
     */
    @Override
    public void setAppTheme() {
        setTheme(R.style.AppTheme_Home_Good);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //避免开机自启动服务意外关闭
        ServiceUtils.startMultiServices(this, CallSmsFilterService.class, AppLockService.class);
        //
        boolean enabled = SharedPreferencesUtils.getBoolean(this, Constants.PREF_KEY_AUTO_CLEAN_SCREEN_OFF, false);
        if (enabled) {
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
//            overridePendingTransition(R.anim.new_in, 0);
        } else if (id == R.id.block_call_sms) {
            startActivity(new Intent(this, CallSmsFilterActivity.class));
//            overridePendingTransition(R.anim.new_in, 0);
        } else if (id == R.id.app_lock) {
            startActivity(new Intent(this, AppLockActivity.class));
//            overridePendingTransition(R.anim.new_in, 0);
        } else if (id == R.id.task_manager) {
            startActivity(new Intent(this, TaskManagerActivity.class));
//            overridePendingTransition(R.anim.new_in, 0);
        } else if (id == R.id.app_settings) {

        }

        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 屏蔽手机物理菜单按键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            //do nothing
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onCheckComplete() {
        //change toolbar(theme) color

    }

    private void copyDatabases(final String dbName) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                AssetManager assets = getAssets();
                File file = new File(getFilesDir(), dbName);
                InputStream is = null;
                FileOutputStream fos = null;
                if (file.exists() && file.length() > 0) {
//                    Log.d("HomeActivity", dbName + " exists.");
                } else {
                    try {
                        is = assets.open(dbName);
                        fos = openFileOutput(dbName, MODE_PRIVATE);
                        int len;
                        byte[] buf = new byte[1024];
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }.start();
    }

}
