package com.hhtc.dialer.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class DialerTabPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> pagers;

    public DialerTabPagerAdapter(FragmentManager fm, List<Fragment> pagers) {
        super(fm);
        this.pagers = pagers;
    }

    @Override
    public Fragment getItem(int index) {
        return pagers.get(index);
    }

    @Override
    public int getCount() {
        return pagers.size();
    }
}
