package zjj.app.mobilesecurity.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import zjj.app.mobilesecurity.fragments.DefaultFragment;
import zjj.app.mobilesecurity.fragments.SpeedUpFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter{

    private String[] titles = {"加速", "清理", "杀毒"};

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return SpeedUpFragment.newInstance();
            default:
                return DefaultFragment.newInstance();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
