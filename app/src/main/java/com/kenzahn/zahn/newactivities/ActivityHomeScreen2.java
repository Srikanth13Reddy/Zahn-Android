package com.kenzahn.zahn.newactivities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.kenzahn.zahn.R;
import com.kenzahn.zahn.adapter.FragmentPagerAdapter;
import com.kenzahn.zahn.fragments.AllBooksFragment;
import com.kenzahn.zahn.fragments.InProgressBooksFragment;
import com.kenzahn.zahn.fragments.ReadBooksFragment;
import com.kenzahn.zahn.fragments.UnReadBooksFragment;
import com.kenzahn.zahn.interfaces.AdapterItemClickListener;
import com.kenzahn.zahn.interfaces.AdapterItemClickListener2;
import com.kenzahn.zahn.model.HomeDuckData;
import com.kenzahn.zahn.newfragments.AllBooksFragment2;
import com.kenzahn.zahn.newfragments.InProgressBooksFragment2;
import com.kenzahn.zahn.newfragments.ReadBooksFragment2;
import com.kenzahn.zahn.newfragments.UnReadBooksFragment2;
import com.kenzahn.zahn.rest.ApiController;
import com.kenzahn.zahn.widget.UtilsMethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityHomeScreen2 extends Fragment implements ViewPager.OnPageChangeListener, AdapterItemClickListener, ApiController.ApiCallBack, AdapterItemClickListener2 {

    View mView;
    private List<? extends Fragment> mFragmentList;
    private final ArrayList<String> mFragmentTitle =   new ArrayList<>(Arrays.asList("All", "Unread", "In-Progress", "Completed"));
    ViewPager viewPagerHome;
    TabLayout tabs;

    private int mpos;
    public ActivityHomeScreen2()
    {
        // Required empty public constructor
    }

    public static ActivityHomeScreen2 newInstance() {
        ActivityHomeScreen2 fragment = new ActivityHomeScreen2();
        Bundle args = new Bundle();
        args.putString("", "");
        args.putString("", "");
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_home_screen2, container, false);
        viewPagerHome = mView.findViewById(R.id.viewPagerHome);
        tabs = mView.findViewById(R.id.tabs);
        loadProductList();
        return mView;
    }

    private void loadProductList() {
        this.mFragmentList = Arrays.asList(AllBooksFragment2.newInstance("", ""), UnReadBooksFragment2.newInstance("", ""),InProgressBooksFragment2.newInstance("", ""),ReadBooksFragment2.newInstance("", ""));
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(this.getFragmentManager(), (List<Fragment>) mFragmentList, mFragmentTitle);
        viewPagerHome.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewPagerHome);
        UtilsMethods.changeTabsFont(tabs, getActivity());
        viewPagerHome.addOnPageChangeListener(this);
    }

    public void callSetUpCurrent(int pos) {
        this.mpos = pos;
        viewPagerHome.setCurrentItem(pos);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemClick(int var1, String var2, Object var3, View var4) {

    }

    @Override
    public void OnSuccess(int type, Object response) {
        if (type == 4) {
            try {
                if (response != null) {
                    HomeDuckData flashCardsJson = (HomeDuckData) response;
                }else{

                }
            } catch (Exception ignored) {

            }
        }

    }

    @Override
    public void OnErrorResponse(int var1, Object var2) {

    }

    @Override
    public void onFailure(int var1, Object var2) {

    }

    @Override
    public void onItemClick(String var1, String var2, Object var3, View var4) {

    }


}
