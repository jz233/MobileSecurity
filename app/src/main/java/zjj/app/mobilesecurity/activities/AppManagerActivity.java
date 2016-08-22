package zjj.app.mobilesecurity.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.base.BaseActivity;
import zjj.app.mobilesecurity.fragments.ApkFragment;
import zjj.app.mobilesecurity.fragments.AppUninstallFragment;
import zjj.app.mobilesecurity.fragments.DefaultFragment;
import zjj.app.mobilesecurity.fragments.MoveAppFragment;

public class AppManagerActivity extends BaseActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private TabLayout tab_layout;
    private ViewPager viewpager;
    private String[] pagerTitles = {"软件卸载", "安装包管理", "软件搬家"};
    private FragmentManager fm;

    @Override
    public void initView() {
        setContentView(R.layout.activity_app_manager);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("软件管家");
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);     //重复

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        fm = getSupportFragmentManager();


    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        viewpager.setAdapter(new AppManagerPagerAdapter(fm));
        viewpager.setOffscreenPageLimit(2);
        tab_layout.setupWithViewPager(viewpager);
    }

    private class AppManagerPagerAdapter extends FragmentStatePagerAdapter{

        public AppManagerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pagerTitles[position];
        }

        @Override
        public int getCount() {
            return pagerTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return AppUninstallFragment.newInstance();
            }else if(position == 1){
                return ApkFragment.newInstance();
            }else if(position == 2){
                return MoveAppFragment.newInstance();
            }else{
                return DefaultFragment.newInstance();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home){
            //TODO 结束未完成的检测等
            finish();
            overridePendingTransition(0, R.anim.new_out);
        }
        return super.onOptionsItemSelected(item);
    }
}
