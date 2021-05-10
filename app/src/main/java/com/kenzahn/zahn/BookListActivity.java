package com.kenzahn.zahn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.kenzahn.zahn.adapter.FragmentPagerAdapter;
import com.kenzahn.zahn.database.DatabaseHandler;
import com.kenzahn.zahn.database.UpdatePre;
import com.kenzahn.zahn.fragments.AllFragment;
import com.kenzahn.zahn.fragments.LearnedFragment;
import com.kenzahn.zahn.fragments.ToLearnFragment;
import com.kenzahn.zahn.model.LoginModel;
import com.kenzahn.zahn.rest.ApiController;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.utils.Constants;
import com.kenzahn.zahn.widget.TypeFaceTextView;
import com.kenzahn.zahn.widget.UtilsMethods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookListActivity extends AppCompatActivity implements LifecycleObserver, ViewPager.OnPageChangeListener, ApiController.ApiCallBack {
    public DatabaseHandler databaseHandler;
    private AppPreference mAppPreference;
    String FlashCardSetID,mainTitle;
    TextView tvtitle,tvStart;
    private LoginModel loginRes;
    TabLayout tabs;
    private ViewPager viewpagerList;
    private ArrayList<String> mFragmentTitle;
    private List<? extends Fragment> mFragmentList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TypeFaceTextView toolbarTitle = findViewById(R.id.toolbarTitle);
        tvtitle = findViewById(R.id.tvtitle);
        tabs = findViewById(R.id.tabs);
        viewpagerList = findViewById(R.id.viewpagerList);
        tvStart = findViewById(R.id.tvStart);
        TypeFaceTextView tv=findViewById(R.id.tv_result);
        tv.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseHandler = new DatabaseHandler(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        this.mAppPreference = AppApplication.getPreferenceInstance();
        getUserData();
        FlashCardSetID = mAppPreference.readString("FlashCardSetID");
        String titleCard = mAppPreference.readString("cardcontent");
         mainTitle = mAppPreference.readString("type");
        toolbarTitle.setText(""+mainTitle);
        tvtitle.setText(""+titleCard);
        int readBooksAll = databaseHandler.getReadCountAll(FlashCardSetID);
        int readBooks = databaseHandler.getReadCount(FlashCardSetID);
        int readBooksTolearn = databaseHandler.getReadCountToLearn(FlashCardSetID);
        mFragmentTitle =   new ArrayList<>(Arrays.asList("All - "+readBooksAll, "To Learn - "+readBooksTolearn, "Learned - "+readBooks));
        viewpagerList.setOffscreenPageLimit(3);
        mFragmentList =  Arrays.asList(AllFragment.newInstance(FlashCardSetID), ToLearnFragment.newInstance(FlashCardSetID), LearnedFragment.newInstance(FlashCardSetID));
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), (List<Fragment>) mFragmentList, mFragmentTitle);
        viewpagerList.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewpagerList);
        UtilsMethods.changeTabsFont(tabs, this);
        viewpagerList.addOnPageChangeListener(this);
        tvStart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(BookListActivity.this, BookDetailsActivity.class);
                mAppPreference.writeString("FlashCardSetID", FlashCardSetID);
                mAppPreference.writeInteger("SortOrder",0);
                mAppPreference.writeString("type",mainTitle);
                startActivity(intent);
            }
        });

    }

    private void getUserData()
    {
        String userData = mAppPreference.readString(Constants.USER_DATA);
        if (userData != null)
        {
            Gson gson = new Gson();
            String userDataNew = mAppPreference.readString(Constants.USER_DATA);
            loginRes = gson.fromJson(userDataNew, LoginModel.class);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int readBooksAll = databaseHandler.getReadCountAll(FlashCardSetID);
        int readBooks = databaseHandler.getReadCount(FlashCardSetID);
        int readBooksTolearn = databaseHandler.getReadCountToLearn(FlashCardSetID);
        updateValueToMainDeck(readBooksTolearn);
        tabs.getTabAt(0).setText("All - "+(readBooksAll-1));
        tabs.getTabAt(1).setText("To Learn - "+ (readBooks-1));
        tabs.getTabAt(2).setText("Learned - "+readBooksTolearn);
    }

    private void updateValueToMainDeck(int readBooks)
    {
        //int readBooks = databaseHandler.getReadCount(this.FlashCardSetID);
        Log.e("readBooks", "readBooks" + readBooks);
        String query2 = "UPDATE flashcard  SET CompletedCards ='" + readBooks + "' WHERE FlashCardSetID = '" + this.FlashCardSetID + "' ";
        databaseHandler.updateQuery(query2);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            sync();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void sync() {
        UpdatePre updatePre = databaseHandler.getUserPrefernce(loginRes.getResponse().getUserid());
        if (updatePre.getPreferencesJson().size() > 0 && updatePre.getPreferencesJson() != null)
        {
            Log.e("Data",""+updatePre.getPreferencesJson());
            AppApplication.mApiController.getUpdatePre(updatePre, 6, this);
        }
    }

    @Override
    public void OnSuccess(int var1, Object var2) {

    }

    @Override
    public void OnErrorResponse(int var1, Object var2) {

    }

    @Override
    public void onFailure(int var1, Object var2) {

    }
}
