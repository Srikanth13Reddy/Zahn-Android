package com.kenzahn.zahn.newactivities;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
import com.kenzahn.zahn.AppApplication;
import com.kenzahn.zahn.BookDetailsActivity;
import com.kenzahn.zahn.R;
import com.kenzahn.zahn.adapter.FragmentPagerAdapter;
import com.kenzahn.zahn.database.DatabaseHandler;
import com.kenzahn.zahn.database.DatabaseHandler2;
import com.kenzahn.zahn.fragments.LearnedFragment;
import com.kenzahn.zahn.fragments.ToLearnFragment;
import com.kenzahn.zahn.model.CardContentRes2;
import com.kenzahn.zahn.newfragments.AllFragment2;
import com.kenzahn.zahn.newfragments.LearnedFragment2;
import com.kenzahn.zahn.newfragments.ToLearnFragment2;
import com.kenzahn.zahn.rest.ApiClient;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.widget.TypeFaceTextView;
import com.kenzahn.zahn.widget.UtilsMethods;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BookListActivity2 extends AppCompatActivity implements LifecycleObserver, ViewPager.OnPageChangeListener {
    public DatabaseHandler2 databaseHandler;
    private AppPreference mAppPreference;
    String ExamID;
    TextView tvtitle,tvStart,tv_result;
    TabLayout tabs;
    private ViewPager viewpagerList;
    private ArrayList<String> mFragmentTitle;
    private List<? extends Fragment> mFragmentList;
    private AlertDialog ad;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        tvtitle = findViewById(R.id.tvtitle);
        tabs = findViewById(R.id.tabs);
        viewpagerList = findViewById(R.id.viewpagerList);
        tvStart = findViewById(R.id.tvStart);
        tv_result = findViewById(R.id.tv_result);
        tv_result.setVisibility(View.VISIBLE);
        TypeFaceTextView toolbarTitle = findViewById(R.id.toolbarTitle);
        setSupportActionBar(toolbar);
        toolbarTitle.setText(" Q&A Quick Prep");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        databaseHandler = new DatabaseHandler2(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        this.mAppPreference = AppApplication.getPreferenceInstance();
        ExamID = mAppPreference.readString("ExamID");
        String titleCard = mAppPreference.readString("cardcontent");
        tvtitle.setText(""+titleCard);
        int readBooksAll = databaseHandler.getReadCountAll(ExamID);
        int readBooks = databaseHandler.getReadCount(ExamID);
        int readBooksTolearn = databaseHandler.getReadCountToLearn(ExamID);
        mFragmentTitle =   new ArrayList<>(Arrays.asList("All - "+readBooksAll, "To Answer - "+readBooksTolearn, "Answered - "+readBooks));
        viewpagerList.setOffscreenPageLimit(3);
        mFragmentList =  Arrays.asList(AllFragment2.newInstance(ExamID), ToLearnFragment2.newInstance(ExamID), LearnedFragment2.newInstance(ExamID));
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), (List<Fragment>) mFragmentList, mFragmentTitle);
        viewpagerList.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewpagerList);
        UtilsMethods.changeTabsFont(tabs, this);
        viewpagerList.addOnPageChangeListener(this);
        tvStart.setOnClickListener(v ->
        {
            Intent intent = new Intent(BookListActivity2.this, BookDetailsActivity2.class);
            mAppPreference.writeString("ExamID", ExamID);
            mAppPreference.writeInteger("SortOrder",0);
            startActivity(intent);
        });

        tv_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             checkresult();
            }
        });

    }

    void checkresult()
    {
        ArrayList<CardContentRes2>  response1 = databaseHandler.getFlashCardListContent(ExamID);
        Log.e("Res Totalquestions",""+response1.size());
        ArrayList<Integer> arrayList=new ArrayList<>();
        ArrayList<Integer> arrayList2=new ArrayList<>();
        arrayList.clear();
        arrayList2.clear();
        for (int i=0;i<response1.size();i++)
        {

            Log.e("Ansers",""+response1.get(i).getSelectedAnswer());

            if (!response1.get(i).getSelectedAnswer().equalsIgnoreCase("SAns"))
            {
                arrayList.add(i);
                // String name= ApiClient.getCorrectAns(response1.get(i));
                String name= response1.get(i).getCorrectAnswer();
                String name1= response1.get(i).getSelectedAnswer();
                // String name1= ApiClient.getCorrectAns2(response1.get(i));
                Log.e("Res_ Correct Ans",""+name);
                Log.e("Res_ SCorrect Ans",""+response1.get(i).getSelectedAnswer());
                if (name.equalsIgnoreCase(name1))
                {
                    arrayList2.add(i);
                }
            }
        }

        double total=response1.size();
        double correct=arrayList2.size();
        double result=(correct/total)*100;
        showDialog_(response1.size(),arrayList.size(),arrayList2.size(),result);
        Log.e("Res Selected Questions",""+arrayList.size());
        Log.e("Res Correct Questions",""+arrayList2.size());
        Log.e("Res Result",""+result);

    }

    @SuppressLint("SetTextI18n")
    void showDialog_(int total, int selected, int correct, double result)
    {
        LayoutInflater layoutInflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View v=layoutInflater.inflate(R.layout.result,null,false);
        TypeFaceTextView tv_total=v.findViewById(R.id.tv_total);
        TypeFaceTextView tv_answered=v.findViewById(R.id.tv_answered);
        TypeFaceTextView tv_correct=v.findViewById(R.id.tv_correct);
        TypeFaceTextView tv_result=v.findViewById(R.id.tv_result);
        TypeFaceTextView tv_wrong=v.findViewById(R.id.tv_wrong);
        Button btn_acpt=v.findViewById(R.id.btn_acpt);
        DecimalFormat df = new DecimalFormat("#.00");
        String value = df.format(result);
        tv_answered.setText(""+selected);
        tv_correct.setText(""+correct);
        tv_total.setText(""+total);
        tv_wrong.setText(""+(selected-correct));
        if(value.equalsIgnoreCase(".00"))
        {
            tv_result.setText("0 %");
        }else {
            tv_result.setText(""+value+"  %");
        }
        AlertDialog.Builder alb=new AlertDialog.Builder(this);
        alb.setView(v);
        alb.setCancelable(false);
        btn_acpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
//      alb.setTitle("Result");
//      alb.setMessage("Total Questions     : "+total+"\n\n"+
//                     "Answered Questions  : "+selected+"\n\n"+
//                     "Correct Answers     : "+correct+"\n\n"+
//                     "Result              : "+value+"");
        ad=alb.create();
        Animation transition_in_view = AnimationUtils.loadAnimation(this, R.anim.customer_anim);//customer animation appearance
        v.setAnimation( transition_in_view );
        v.startAnimation( transition_in_view );
        Objects.requireNonNull(ad.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ad.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        ad.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        int readBooksAll = databaseHandler.getReadCountAll(ExamID);
        int readBooks = databaseHandler.getReadCount(ExamID);
        int readBooksTolearn = databaseHandler.getReadCountToLearn(ExamID);
        updateValueToMainDeck(readBooksTolearn);
        tabs.getTabAt(0).setText("All - "+(readBooksAll));
        tabs.getTabAt(1).setText("To Answer - "+ (readBooks));
        tabs.getTabAt(2).setText("Answered - "+readBooksTolearn);
    }

    private void updateValueToMainDeck(int readBooks)
    {
        //int readBooks = databaseHandler.getReadCount(this.ExamID);
        Log.e("readBooks", "readBooks" + readBooks);
        String query2 = "UPDATE flashcard  SET CompletedCards ='" + readBooks + "' WHERE ExamID = '" + this.ExamID + "' ";
        databaseHandler.updateQuery(query2);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
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
}

