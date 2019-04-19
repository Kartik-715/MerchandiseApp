package com.example.merchandiseapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdaptor extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>() ;
    private List<String> fragmentListTitle = new ArrayList<>() ;

    public ViewPagerAdaptor(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i) ;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentListTitle.get(position) ;
    }

    @Override
    public int getCount() {
        return fragmentListTitle.size() ;
    }

    public void AddFragment(Fragment fragment, String title)
    {
        fragmentList.add(fragment) ;
        fragmentListTitle.add(title) ;
        notifyDataSetChanged();
    }

    public void clearFragments()
    {
        fragmentList = new ArrayList<>() ;
        fragmentListTitle = new ArrayList<>() ;
    }
}
