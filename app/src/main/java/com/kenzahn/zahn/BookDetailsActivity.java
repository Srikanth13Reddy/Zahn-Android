package com.kenzahn.zahn;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenzahn.zahn.database.DatabaseHandler;
import com.kenzahn.zahn.model.CardContentRes;
import com.kenzahn.zahn.rest.ApiController;
import com.kenzahn.zahn.utils.AnimationItem;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.widget.TypeFaceTextView;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.Collections;

public class BookDetailsActivity extends AppCompatActivity implements ApiController.ApiCallBack, ViewPager.OnPageChangeListener {
    public DatabaseHandler databaseHandler;
    private AppPreference mAppPreference;
    String FlashCardSetID,mainTitle;
    int SortOrder;
    CardPagerAdapter adapter;
    ViewPager view_pager;
    private ArrayList<CardContentRes> response1;
    private CardContentRes responseRemove;
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
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        view_pager = findViewById(R.id.view_pager);
        TypeFaceTextView  toolbarTitle= findViewById(R.id.toolbarTitle);
        txtCount = findViewById(R.id.txtCount);
        isCheckLearn = findViewById(R.id.isCheckLearn);
        ivPrevious = findViewById(R.id.ivPrevious);
        ivNext = findViewById(R.id.ivNext);
        ivShuffle = findViewById(R.id.ivShuffle);
        progressLayout = findViewById(R.id.progressLayout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MOVE_THRESHOLD_DP = 20 * getResources().getDisplayMetrics().density;
        databaseHandler = new DatabaseHandler(this);
        this.mAppPreference = AppApplication.getPreferenceInstance();
        FlashCardSetID = mAppPreference.readString("FlashCardSetID");
        SortOrder = mAppPreference.readInteger("SortOrder");
        mainTitle = mAppPreference.readString("type");
        toolbarTitle.setText(""+mainTitle);
        response1 = databaseHandler.getFlashCardListContent(FlashCardSetID);
        adapter = new CardPagerAdapter(this, response1);
        view_pager.setAdapter(adapter);
        view_pager.setCurrentItem(SortOrder);
        int tot = view_pager.getCurrentItem() + 1;
        txtCount.setText("" + (tot-1) + " of " + (this.response1.size()-1));
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
            public void onClick(View v) {
                if (isCheckLearn.isChecked())
                {
                   int getCurrentItem = view_pager.getCurrentItem();
                    String FlashCardID = response1.get(getCurrentItem).getFlashCardID();
                    int checkValue = 1;
                    response1.get(getCurrentItem).setKnownContent(true);
                    Log.e("getCurrentItem", "getCurrentItem" + FlashCardID);
                    String query = "UPDATE flashcardcontent  SET isKnownContent ='" + checkValue + "' WHERE FlashCardID = '" + FlashCardID + "' ";
                    databaseHandler.updateQuery(query);
                } else
                    {
                    int getCurrentItem = view_pager.getCurrentItem();
                    String FlashCardID = response1.get(getCurrentItem).getFlashCardID();
                    int checkValue = 0;
                    response1.get(getCurrentItem).setKnownContent(false);
                    String query = "UPDATE flashcardcontent  SET isKnownContent ='" + checkValue + "' WHERE FlashCardID = '" + FlashCardID + "' ";
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
            String query = "UPDATE flashcardcontent  SET SortOrder ='" + i + "' WHERE FlashCardID = '" + this.response1.get(i).getFlashCardID() + "' ";
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
        btn_yes.setOnClickListener(new View.OnClickListener() {
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
        ArrayList<CardContentRes>  mResponseLearn = databaseHandler.getFlashCardListContentLearned(FlashCardSetID);
        int readBooks = databaseHandler.getReadCountMainDeck(this.FlashCardSetID);
        Log.e("readBooks", "readBooks" + readBooks);
        String query2 = "UPDATE flashcard  SET CompletedCards ='" + mResponseLearn.size() + "' WHERE FlashCardSetID = '" + this.FlashCardSetID + "' ";
        databaseHandler.updateQuery(query2);
    }

    private void updateBookReadMark(int p0)
    {

        for (int i = 0; i < response1.size(); i++)
        {
            if (i <= p0)
            {
                String query = "UPDATE flashcardcontent " +
                        " SET isKnownReadCount ='1' WHERE FlashCardID = '" + response1.get(i).getFlashCardID() + "' ";
                databaseHandler.updateQuery(query);
            }
        }
        this.updateValueToMainDeck();
    }

    private void checkboxCheck(int p0)
    {

        if (p0==response1.size())
        {
            isCheckLearn.setVisibility(View.VISIBLE);
            txtCount.setVisibility(View.VISIBLE);
            isCheckLearn.setChecked(response1.get(p0-1).getKnownContent());
        }
        else {
            if (p0 == 0) {
                isCheckLearn.setVisibility(View.GONE);
                txtCount.setVisibility(View.GONE);
            } else {
                isCheckLearn.setVisibility(View.VISIBLE);
                txtCount.setVisibility(View.VISIBLE);
                isCheckLearn.setChecked(response1.get(p0).getKnownContent());
            }
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
        txtCount.setText("" + (tot-1) + " of " + (this.response1.size()-1));
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
        ArrayList<CardContentRes> response1;

        public CardPagerAdapter(Context context, ArrayList<CardContentRes> response1) {
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
        @SuppressLint({"ClickableViewAccessibility"})
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.item_viewpager, container, false);
            final EasyFlipView easyFlipView = itemView.findViewById(R.id.flipView);
            WebView webviewBack = itemView.findViewById(R.id.webviewback);
            WebView webviewFront = itemView.findViewById(R.id.webviewfront);
            CardView card_view_front = itemView.findViewById(R.id.card_view_front);
            CardView card_view_back = itemView.findViewById(R.id.card_view_back);
            easyFlipView.setFlipDuration(1000);
            easyFlipView.setFlipEnabled(true);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            double heightDouble = (double) height;
            double widthDouble = (double) width;

            String head1 = "<head><style>@font-face {font-family: 'arial';src: url('file:///android_asset/fonts/Poppins-Regular.ttf');}body {font-family: 'verdana';}</style></head>";
            String newString = this.response1.get(position).getFlashCardFront().replace("width:", "");
            String newStringtest = newString.replace("min-height:", "");
            String textFront = "<html>" + head1 + "<body style=\"font-family: arial\">" + newStringtest + "</body></html>";

            //String textBack = "<html>" + head1 + "<body style=\"font-family: arial\">" + this.response1.get(position).getFlashCardBack() + "</body></html>";
            String textBack = this.response1.get(position).getFlashCardBack().replace("width:", "");
            String textBacknew =textBack.replace("min-height:", "");
            String textFrontN = "<html>" + head1 + "<body style=\"font-family: arial\">" + textBacknew + "</body></html>";
            webviewFront.setVerticalFadingEdgeEnabled(false);
            webviewBack.setHorizontalFadingEdgeEnabled(false);
            webviewFront.loadDataWithBaseURL("", textFront, "text/html", "utf-8", "");
            webviewBack.loadDataWithBaseURL("", textFrontN, "text/html", "utf-8", "");
            webviewBack.setWebViewClient(new WebViewClient(){
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url != null && (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("www"))) {
                        startActivity(
                                new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            card_view_front.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    easyFlipView.flipTheView();
                }
            });

            card_view_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    easyFlipView.flipTheView();
                }
            });

            webviewBack.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.i("TAG", "touched down");
                            mMoveOccured = false;
                            mDownPosX = event.getX();
                            mDownPosY = event.getY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (Math.abs(event.getX() - mDownPosX) > MOVE_THRESHOLD_DP || Math.abs(event.getY() - mDownPosY) > MOVE_THRESHOLD_DP) {
                                mMoveOccured = true;
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (!mMoveOccured) {
                                Log.v("ACTION_UP", "ACTION_UP");
                                easyFlipView.flipTheView();
                            }
                            break;
                    }

                    return false;
                }
            });

            webviewFront.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.i("TAG", "touched down");
                            mMoveOccured = false;
                            mDownPosX = event.getX();
                            mDownPosY = event.getY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (Math.abs(event.getX() - mDownPosX) > MOVE_THRESHOLD_DP || Math.abs(event.getY() - mDownPosY) > MOVE_THRESHOLD_DP) {
                                mMoveOccured = true;
                                Log.v("ACTION_MOVE", "ACTION_MOVE");
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (!mMoveOccured) {
                                Log.v("ACTION_UP", "ACTION_UP");
                                easyFlipView.flipTheView();
                            }
                            break;
                    }


                    return false;
                }
            });

            container.addView(itemView);
            return itemView;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((EasyFlipView) object);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
