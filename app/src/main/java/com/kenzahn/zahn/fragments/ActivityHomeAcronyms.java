package com.kenzahn.zahn.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kenzahn.zahn.R;
import com.kenzahn.zahn.adapter.FragmentPagerAdapter;
import com.kenzahn.zahn.interfaces.AdapterItemClickListener;
import com.kenzahn.zahn.interfaces.AdapterItemClickListener2;
import com.kenzahn.zahn.model.HomeDuckData;
import com.kenzahn.zahn.rest.ApiController;
import com.kenzahn.zahn.widget.UtilsMethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityHomeAcronyms extends Fragment implements ViewPager.OnPageChangeListener, AdapterItemClickListener, ApiController.ApiCallBack, AdapterItemClickListener2 {

    View mView;
    private List<? extends Fragment> mFragmentList;
    private final ArrayList<String> mFragmentTitle =   new ArrayList<>(Arrays.asList("All", "Unread", "In-Progress", "Completed"));
    ViewPager viewPagerHome;
    TabLayout tabs;
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int mpos;
    public ActivityHomeAcronyms()
    {
        // Required empty public constructor
    }

    public static ActivityHomeAcronyms newInstance(String p1,String p2) {
        ActivityHomeAcronyms fragment = new ActivityHomeAcronyms();
        Bundle args = new Bundle();
        args.putString("", "");
        args.putString("", "");
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_home_screen, container, false);
        viewPagerHome = mView.findViewById(R.id.viewPagerHome);
        tabs = mView.findViewById(R.id.tabs);
        loadProductList();
        return mView;
    }

    private void loadProductList() {
        this.mFragmentList = Arrays.asList(AllBooksFragmentAcronyms.newInstance("", ""), UnReadBooksFragmentAcronyms.newInstance("", ""), InProgressBooksFragmentAcronyms.newInstance("", ""), ReadBooksFragmentAcronyms.newInstance("", ""));
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
         //   Toast.makeText(getContext(), ""+mParam1, Toast.LENGTH_SHORT).show();
        }
    }
}
