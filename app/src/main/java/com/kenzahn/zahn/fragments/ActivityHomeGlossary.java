package com.kenzahn.zahn.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivityHomeGlossary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivityHomeGlossary extends Fragment implements ViewPager.OnPageChangeListener, AdapterItemClickListener, ApiController.ApiCallBack, AdapterItemClickListener2 {

    View mView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private List<? extends Fragment> mFragmentList;
    private final ArrayList<String> mFragmentTitle =   new ArrayList<>(Arrays.asList("All", "Unread", "In-Progress", "Completed"));
    ViewPager viewPagerHome;
    TabLayout tabs;

    private int mpos;
    public ActivityHomeGlossary()
    {
        // Required empty public constructor
    }

    public static ActivityHomeGlossary newInstance(String param1, String param2) {
        ActivityHomeGlossary fragment = new ActivityHomeGlossary();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        this.mFragmentList = Arrays.asList(AllBooksFragmentGlossary.newInstance(mParam1, ""), UnReadBooksFragmentGlossary.newInstance("", ""), InProgressBooksFragmentGlossary.newInstance("", ""), ReadBooksFragmentGlossary.newInstance("", ""));
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
           // Toast.makeText(getContext(), ""+mParam1, Toast.LENGTH_SHORT).show();
        }
    }
}
