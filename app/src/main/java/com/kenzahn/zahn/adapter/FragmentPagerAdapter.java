package com.kenzahn.zahn.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragment;
    private final ArrayList<String> titles;
    public FragmentPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragment, ArrayList<String> titles) {
        super(fragmentManager);
        this.fragment = fragment;
        this.titles = titles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragment.get(position);
    }

    @Override
    public int getCount() {
        return this.fragment.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
