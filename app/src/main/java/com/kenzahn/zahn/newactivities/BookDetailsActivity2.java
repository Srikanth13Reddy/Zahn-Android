package com.kenzahn.zahn.newactivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kenzahn.zahn.AppApplication;
import com.kenzahn.zahn.R;
import com.kenzahn.zahn.database.DatabaseHandler;
import com.kenzahn.zahn.database.DatabaseHandler2;
import com.kenzahn.zahn.model.CardContentRes;
import com.kenzahn.zahn.model.CardContentRes2;
import com.kenzahn.zahn.rest.ApiClient;
import com.kenzahn.zahn.rest.ApiController;
import com.kenzahn.zahn.utils.AnimationItem;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.widget.TypeFaceTextView;
import com.kenzahn.zahn.widget.TypeFaceTextViewBold;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class BookDetailsActivity2 extends AppCompatActivity implements ApiController.ApiCallBack, ViewPager.OnPageChangeListener {
    public DatabaseHandler2 databaseHandler;
    private AppPreference mAppPreference;
    String FlashCardSetID;
    int SortOrder;
    BookDetailsActivity2.CardPagerAdapter adapter;
    ViewPager view_pager;
    private ArrayList<CardContentRes2> response1;
    private CardContentRes2 responseRemove;
    float MOVE_THRESHOLD_DP;
    boolean mMoveOccured = false;
    float mDownPosX = 0.0F;
    float mDownPosY = 0.0F;
    TextView txtCount;
    CheckBox isCheckLearn;
    ImageView ivPrevious,ivNext,ivShuffle;
    View progressLayout;
    AnimationItem[] mAnimationItems;
    private AnimationItem mSelectedItem;
    public AlertDialog shuffle_dialog;
    private AlertDialog ad;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        view_pager = findViewById(R.id.view_pager);
        txtCount = findViewById(R.id.txtCount);
        TypeFaceTextView toolbarTitle = findViewById(R.id.toolbarTitle);
        isCheckLearn = findViewById(R.id.isCheckLearn);
        ivPrevious = findViewById(R.id.ivPrevious);
        ivNext = findViewById(R.id.ivNext);
        ivShuffle = findViewById(R.id.ivShuffle);
        progressLayout = findViewById(R.id.progressLayout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle.setText("Q&A Quick Prep");
        MOVE_THRESHOLD_DP = 20 * getResources().getDisplayMetrics().density;
        databaseHandler = new DatabaseHandler2(this);
        this.mAppPreference = AppApplication.getPreferenceInstance();
        FlashCardSetID = mAppPreference.readString("ExamID");
        SortOrder = mAppPreference.readInteger("SortOrder");
        response1 = databaseHandler.getFlashCardListContent(FlashCardSetID);
        adapter = new BookDetailsActivity2.CardPagerAdapter(this, response1);
        view_pager.setAdapter(adapter);
        view_pager.setCurrentItem(SortOrder);
        int tot = view_pager.getCurrentItem() + 1;
        txtCount.setText("" + (tot) + " of " + (this.response1.size()));
        updateBookReadMark(SortOrder);
        updateValueToMainDeck();
        checkboxCheck(SortOrder);
        ivPrevious.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                view_pager.setCurrentItem(getItem(-1), true);
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                view_pager.setCurrentItem(getItem(+1), true);
            }
        });

        ivShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progressLayout.setVisibility(View.GONE);
                        loadData();
                    }
                }, 3000);
                mAnimationItems = getAnimationItems();
                mSelectedItem = mAnimationItems[0];
                runLayoutAnimation(view_pager, mSelectedItem);
            }
        });

        isCheckLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (isCheckLearn.isChecked())
                {
                    int getCurrentItem = view_pager.getCurrentItem();
                    String FlashCardID = response1.get(getCurrentItem).getExamQuestionID();
                    int checkValue = 1;
                    response1.get(getCurrentItem).setKnownContent(true);
                    Log.e("getCurrentItem", "getCurrentItem" + FlashCardID);
                    String query = "UPDATE flashcardcontent3  SET isKnownContent ='" + checkValue + "' WHERE ExamQuestionID = '" + FlashCardID + "' ";
                    databaseHandler.updateQuery(query);
                } else
                {
                    int getCurrentItem = view_pager.getCurrentItem();
                    String FlashCardID = response1.get(getCurrentItem).getExamQuestionID();
                    int checkValue = 0;
                    response1.get(getCurrentItem).setKnownContent(false);
                    String query = "UPDATE flashcardcontent3  SET isKnownContent ='" + checkValue + "' WHERE ExamQuestionID = '" + FlashCardID + "' ";
                    databaseHandler.updateQuery(query);
                }
            }
        });

        view_pager.addOnPageChangeListener(this);
    }

    private void loadData()
    {
        responseRemove = response1.get(0);
        response1.remove(response1.get(0));
        Collections.shuffle(response1);
        response1.add(0, responseRemove);
        for (int i =0;i<response1.size();i++){
            String query = "UPDATE flashcardcontent3  SET SortOrder ='" + i + "' WHERE ExamQuestionID = '" + this.response1.get(i).getExamQuestionID() + "' ";
            databaseHandler.updateQuery(query);
        }
        txtCount.setText("1 of "+ (this.response1.size()-1));
        adapter.notifyDataSetChanged();
        view_pager.setAdapter(adapter);
        LayoutInflater inflater = this.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_dialog, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(alertLayout);
        TextView btn_yes = alertLayout.findViewById(R.id.btn_yes);
        TextView tv_mess = alertLayout.findViewById(R.id.tv_mess);
        TextView tv_title = alertLayout.findViewById(R.id.tv_title);
        tv_mess.setText("Questions shuffled successfully");
        tv_title.setText("Questions Shuffle");
        btn_yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show();
        deleteDialog.setCancelable(false);
    }

    private int getItem(int i)
    {
        return view_pager.getCurrentItem() + i;
    }

    private final void runLayoutAnimation(ViewPager recyclerView, AnimationItem item) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, item.getResourceId());
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    protected final AnimationItem[] getAnimationItems()
    {
        return new AnimationItem[]{new AnimationItem("Slide from right", R.anim.layout_animation_from_right)};
    }

    private void updateValueToMainDeck()
    {
        ArrayList<CardContentRes2>  mResponseLearn = databaseHandler.getFlashCardListContentLearned(FlashCardSetID);
        int readBooks = databaseHandler.getReadCountMainDeck(this.FlashCardSetID);
        Log.e("readBooks", "readBooks" + readBooks);
        String query2 = "UPDATE flashcard2  SET CompletedCards ='" + mResponseLearn.size() + "' WHERE ExamID = '" + this.FlashCardSetID + "' ";
        databaseHandler.updateQuery(query2);
    }

    private void updateBookReadMark(int p0)
    {

        for (int i = 0; i < response1.size(); i++)
        {
            if (i <= p0)
            {
                String query = "UPDATE flashcardcontent3 " +
                        " SET isKnownReadCount ='1' WHERE ExamQuestionID = '" + response1.get(i).getExamQuestionID() + "' ";
                databaseHandler.updateQuery(query);
            }
        }
        this.updateValueToMainDeck();
    }

    private void checkboxCheck(int p0)
    {

        if (p0==response1.size())
        {
            isCheckLearn.setVisibility(View.GONE);
            txtCount.setVisibility(View.VISIBLE);
            isCheckLearn.setChecked(response1.get(p0).getKnownContent());
        }
        else {
//            if (p0 == 0) {
//                isCheckLearn.setVisibility(View.GONE);
//                txtCount.setVisibility(View.GONE);
//            } else {
//                isCheckLearn.setVisibility(View.GONE);
//                txtCount.setVisibility(View.VISIBLE);
//                isCheckLearn.setChecked(response1.get(p0).getKnownContent());
//            }
            isCheckLearn.setVisibility(View.GONE);
            txtCount.setVisibility(View.VISIBLE);
            isCheckLearn.setChecked(response1.get(p0).getKnownContent());


        }




    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position)
    {
        checkboxCheck(position);
        updateBookReadMark(position);
        updateValueToMainDeck();
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
        int tot = view_pager.getCurrentItem() + 1;
        txtCount.setText("" + (tot) + " of " + (this.response1.size()));
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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class CardPagerAdapter extends PagerAdapter {
        Context context;
        ArrayList<CardContentRes2> response1;
           TypeFaceTextView tv_ep;

        public CardPagerAdapter(Context context, ArrayList<CardContentRes2> response1) {
            this.context = context;
            this.response1 = response1;
        }

        @Override
        public int getCount() {
            return response1.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
        public Object instantiateItem(ViewGroup container, int position)
        {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.item_viewpager2, container, false);
            TypeFaceTextViewBold q= itemView.findViewById(R.id.tv_question);
            TypeFaceTextViewBold tv_1= itemView.findViewById(R.id.tv_1);
            TypeFaceTextViewBold tv_2= itemView.findViewById(R.id.tv_2);
            TypeFaceTextViewBold tv_3= itemView.findViewById(R.id.tv_3);
            TypeFaceTextViewBold tv_4= itemView.findViewById(R.id.tv_4);
            TypeFaceTextViewBold tv_5= itemView.findViewById(R.id.tv_5);
            TypeFaceTextViewBold tv_6= itemView.findViewById(R.id.tv_6);
             getIsVisibility(response1.get(position),tv_1,tv_2,tv_3,tv_4,tv_5,tv_6);
            TypeFaceTextView tv_ep= itemView.findViewById(R.id.tv_ep);
            RadioGroup rg= itemView.findViewById(R.id.rg);
            RadioButton rb1 = itemView.findViewById(R.id.rb1);
            RadioButton rb2= itemView.findViewById(R.id.rb2);
            RadioButton rb3= itemView.findViewById(R.id.rb3);
            RadioButton rb4= itemView.findViewById(R.id.rb4);
            RadioButton rb5= itemView.findViewById(R.id.rb5);
            RadioButton rb6= itemView.findViewById(R.id.rb6);
            RelativeLayout rv_a= itemView.findViewById(R.id.rv_a);
            Button rst_button= itemView.findViewById(R.id.rst_button);
            if (position+1==response1.size())
            {
                rst_button.setVisibility(View.VISIBLE);
            }else {
                rst_button.setVisibility(View.GONE);
            }
            if (response1.get(position).getAnswerC().isEmpty())
            {
                rb3.setVisibility(View.GONE);
            }
            if (response1.get(position).getAnswerD().isEmpty())
            {
                rb4.setVisibility(View.GONE);
            }
            if (response1.get(position).getAnswerE().isEmpty())
            {
                rb5.setVisibility(View.GONE);
            }
            if (response1.get(position).getAnswerF().isEmpty())
            {
                rb6.setVisibility(View.GONE);
            }
            rb1.setText(this.response1.get(position).getAnswerA());
            rb2.setText(this.response1.get(position).getAnswerB());
            rb3.setText(this.response1.get(position).getAnswerC());
            rb4.setText(this.response1.get(position).getAnswerD());
            rb5.setText(this.response1.get(position).getAnswerE());
            rb6.setText(this.response1.get(position).getAnswerF());
            Log.e("SelectedAns",response1.get(position).getSelectedAnswer());
            q.setText(this.response1.get(position).getQuestion());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tv_ep.setText( "\n"+"Answer: "+Html.fromHtml(response1.get(position).getExplanation(), Html.FROM_HTML_MODE_COMPACT));
                q.setText( ""+Html.fromHtml(this.response1.get(position).getQuestion(), Html.FROM_HTML_MODE_COMPACT) );
               // tv_ep.setText("\n"+"Explanation : "+response1.get(position).getExplanation());
            }else {
                tv_ep.setText(Html.fromHtml("\n"+"Answer: "+response1.get(position).getExplanation()));
                q.setText(Html.fromHtml(this.response1.get(position).getQuestion()));
            }

            checkCondition(this.response1,position,rb1,rb2,rb3,rb4,rv_a,tv_ep,rb5,rb6);

            rg.setOnCheckedChangeListener((group, checkedId) -> {

                rglistionar(response1,position,rb1,rb2,rb3,rb4,rv_a,tv_ep,group,checkedId,rb5,rb6);


            });

            rv_a.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Already Answered", Toast.LENGTH_SHORT).show();
                }
            });

            rst_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkresult(response1.get(position),response1);
                }
            });


             container.addView(itemView);
            return itemView;
        }

        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((ScrollView) object);
        }
    }

    @SuppressLint("SetTextI18n")
    private void getIsVisibility(CardContentRes2 cardContentRes2, TypeFaceTextViewBold tv_1, TypeFaceTextViewBold tv_2, TypeFaceTextViewBold tv_3, TypeFaceTextViewBold tv_4, TypeFaceTextViewBold tv_5, TypeFaceTextViewBold tv_6)
    {
        if (cardContentRes2.getAnswerI()!=null)
        {
            if (cardContentRes2.getAnswerI().isEmpty())
            {
                tv_1.setVisibility(View.GONE);
            }else {
                tv_1.setVisibility(View.VISIBLE);
                tv_1.setText("I. "+cardContentRes2.getAnswerI());
            }
        }

        if (cardContentRes2.getAnswerII()!=null)
        {
            if (cardContentRes2.getAnswerII().isEmpty())
            {
                tv_2.setVisibility(View.GONE);
            }else {
                tv_2.setVisibility(View.VISIBLE);
                tv_2.setText("II. "+cardContentRes2.getAnswerII());
            }
        }

          if (cardContentRes2.getAnswerIII()!=null)
          {
              if (cardContentRes2.getAnswerIII().isEmpty())
              {
                  tv_3.setVisibility(View.GONE);
              }else {
                  tv_3.setVisibility(View.VISIBLE);
                  tv_3.setText("III. "+cardContentRes2.getAnswerIII());
              }
          }

          if (cardContentRes2.getAnswerIV()!=null)
          {
              if (cardContentRes2.getAnswerIV().isEmpty())
              {
                  tv_4.setVisibility(View.GONE);
              }else {
                  tv_4.setVisibility(View.VISIBLE);
                  tv_4.setText("IV. "+cardContentRes2.getAnswerIV());
              }
          }

          if (cardContentRes2.getAnswerV()!=null)
          {
              if (cardContentRes2.getAnswerV().isEmpty())
              {
                  tv_5.setVisibility(View.GONE);
              }else {
                  tv_5.setVisibility(View.VISIBLE);
                  tv_5.setText("V. "+cardContentRes2.getAnswerV());
              }
          }

          if (cardContentRes2.getAnswerVI()!=null)
          {
              if (cardContentRes2.getAnswerVI().isEmpty())
              {
                  tv_6.setVisibility(View.GONE);
              }else {
                  tv_6.setVisibility(View.VISIBLE);
                  tv_6.setText("VI. "+cardContentRes2.getAnswerVI());
              }
          }

    }


    void  updateread2(String selectedAns,int pos)
    {
        int getCurrentItem = view_pager.getCurrentItem();
        String FlashCardID = response1.get(pos).getExamQuestionID();
        int checkValue = 1;
        response1.get(getCurrentItem).setKnownContent(true);
        Log.e("getCurrentItem", "getCurrentItem" + FlashCardID);
        String query = "UPDATE flashcardcontent3  SET isKnownContent ='" + checkValue + "',SelectedAnswer ='"+selectedAns+"' WHERE ExamQuestionID = '" + FlashCardID + "' ";
        databaseHandler.updateQuery(query);
        updateValueToMainDeck();

    }

    void checkresult(CardContentRes2 res2,ArrayList<CardContentRes2> response2)
    {
        ArrayList<CardContentRes2>  response1 = databaseHandler.getFlashCardListContent(FlashCardSetID);
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

    void checkCondition(ArrayList<CardContentRes2> response2, int position, RadioButton rb1,
                        RadioButton rb2, RadioButton rb3, RadioButton rb4, RelativeLayout rv_a, TypeFaceTextView tv_ep, RadioButton rb5, RadioButton rb6)
    {
        ArrayList<CardContentRes2> response1=  databaseHandler.getFlashCardListContent(FlashCardSetID);
        if (response1.get(position).getKnownContent())
        {
            rb1.setClickable(false);
            rb2.setClickable(false);
            rb3.setClickable(false);
            rb4.setClickable(false);
            rb5.setClickable(false);
            rb6.setClickable(false);
            rv_a.setVisibility(View.VISIBLE);
            String ans=response1.get(position).getSelectedAnswer().trim();
            String name= ApiClient.getCorrectAns(response1.get(position));
            String name1= ApiClient.getCorrectAns2(response1.get(position));
            Log.e("SelectedAns",response1.get(position).getSelectedAnswer());
            Log.e("SelectedAns",response1.get(position).getSelectedAnswer());

            if (rb1.getText().toString().equalsIgnoreCase(name1))
            {
                rb1.setChecked(true);
                if (rb1.getText().toString().equalsIgnoreCase(name))
                {
                    rb1.setTextColor(Color.parseColor("#008000"));
                }else {
                    rb1.setTextColor(Color.RED);
                }

            }else if (rb2.getText().toString().equalsIgnoreCase(name1))
            {
                rb2.setChecked(true);
                if (rb2.getText().toString().equalsIgnoreCase(name))
                {
                    rb2.setTextColor(Color.parseColor("#008000"));
                }else {
                    rb2.setTextColor(Color.RED);
                }
            }
            else if (rb3.getText().toString().equalsIgnoreCase(name1))
            {
                rb3.setChecked(true);
                if (rb3.getText().toString().equalsIgnoreCase(name))
                {
                    rb3.setTextColor(Color.parseColor("#008000"));
                }else {
                    rb3.setTextColor(Color.RED);
                }
            }
            else if (rb4.getText().toString().equalsIgnoreCase(name1))
            {
                rb4.setChecked(true);
                if (rb4.getText().toString().equalsIgnoreCase(name))
                {
                    rb4.setTextColor(Color.parseColor("#008000"));
                }else {
                    rb4.setTextColor(Color.RED);
                }
            }
            else if (rb5.getText().toString().equalsIgnoreCase(name1))
            {
                rb5.setChecked(true);
                if (rb5.getText().toString().equalsIgnoreCase(name))
                {
                    rb5.setTextColor(Color.parseColor("#008000"));
                }else {
                    rb5.setTextColor(Color.RED);
                }
            }
            else if (rb6.getText().toString().equalsIgnoreCase(name1))
            {
                rb6.setChecked(true);
                if (rb6.getText().toString().equalsIgnoreCase(name))
                {
                    rb6.setTextColor(Color.parseColor("#008000"));
                }else {
                    rb6.setTextColor(Color.RED);
                }
            }

            checkNewCondition(rb1,rb2,rb3,rb4,name,rb5,rb6);

            tv_ep.setVisibility(View.VISIBLE);
        }else {
            tv_ep.setVisibility(View.GONE);
            rv_a.setVisibility(View.GONE);
        }
    }

    private void checkNewCondition(RadioButton rb1, RadioButton rb2, RadioButton rb3, RadioButton rb4, String name, RadioButton rb5, RadioButton rb6)
    {
        if (name.equalsIgnoreCase(rb1.getText().toString()))
        {
            rb1.setTextColor(Color.parseColor("#008000"));
        }else if (name.equalsIgnoreCase(rb2.getText().toString()))
        {
            rb2.setTextColor(Color.parseColor("#008000"));
        }
        else if (name.equalsIgnoreCase(rb3.getText().toString()))
        {
            rb3.setTextColor(Color.parseColor("#008000"));
        }
        else if (name.equalsIgnoreCase(rb4.getText().toString()))
        {
            rb4.setTextColor(Color.parseColor("#008000"));
        }
        else if (name.equalsIgnoreCase(rb5.getText().toString()))
        {
            rb5.setTextColor(Color.parseColor("#008000"));
        }
        else if (name.equalsIgnoreCase(rb6.getText().toString()))
        {
            rb6.setTextColor(Color.parseColor("#008000"));
        }
    }

    void rglistionar(ArrayList<CardContentRes2> response1, int position, RadioButton rb1,
                     RadioButton rb2, RadioButton rb3, RadioButton rb4, RelativeLayout rv_a, TypeFaceTextView tv_ep, RadioGroup group, int checkedId, RadioButton rb5, RadioButton rb6)
    {
        RadioButton rb = (RadioButton) group.findViewById(checkedId);
        rb1.setClickable(false);
        rb2.setClickable(false);
        rb3.setClickable(false);
        rb4.setClickable(false);
        rb5.setClickable(false);
        rb6.setClickable(false);
        rv_a.setVisibility(View.VISIBLE);
        int index = group.indexOfChild(rb);
       // Toast.makeText(this, ""+index, Toast.LENGTH_SHORT).show();
        String name= ApiClient.getCorrectAns(response1.get(position));
        if (name.equalsIgnoreCase(rb.getText().toString()))
        {
            rb.setTextColor(Color.parseColor("#008000"));
            tv_ep.setVisibility(View.VISIBLE);
        }else {
            rb.setTextColor(Color.RED);
            tv_ep.setVisibility(View.VISIBLE);
        }

        if (name.equalsIgnoreCase(rb1.getText().toString()))
        {
            rb1.setTextColor(Color.parseColor("#008000"));
        }else if (name.equalsIgnoreCase(rb2.getText().toString()))
        {
            rb2.setTextColor(Color.parseColor("#008000"));
        }
        else if (name.equalsIgnoreCase(rb3.getText().toString()))
        {
            rb3.setTextColor(Color.parseColor("#008000"));
        }
        else if (name.equalsIgnoreCase(rb4.getText().toString()))
        {
            rb4.setTextColor(Color.parseColor("#008000"));
        }
        else if (name.equalsIgnoreCase(rb5.getText().toString()))
        {
            rb5.setTextColor(Color.parseColor("#008000"));
        }
        else if (name.equalsIgnoreCase(rb6.getText().toString()))
        {
            rb6.setTextColor(Color.parseColor("#008000"));
        }



        if (index==0)
        {
            updateread2("A",position);
        }else if (index==1)
        {
            updateread2("B",position);
        }else if (index==2)
        {
            updateread2("C",position);
        }else if (index==3)
        {
            updateread2("D",position);
        }
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
        Button btn_acpt=v.findViewById(R.id.btn_acpt);
        TypeFaceTextView tv_wrong=v.findViewById(R.id.tv_wrong);
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
      btn_acpt.setOnClickListener(v1 -> ad.dismiss());
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
