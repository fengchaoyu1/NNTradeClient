package zhaohg.sliding;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class SlidingPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;

    public SlidingPagerAdapter(FragmentManager manager, ArrayList<Fragment> fragments) {
        super(manager);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int pos) {
        return fragments.get(pos);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Long Tab Name" + position;
    }
}
