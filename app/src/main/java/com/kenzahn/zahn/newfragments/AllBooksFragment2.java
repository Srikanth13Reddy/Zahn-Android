package com.kenzahn.zahn.newfragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kenzahn.zahn.AppApplication;
import com.kenzahn.zahn.BookListActivity;
import com.kenzahn.zahn.R;
import com.kenzahn.zahn.adapter.BookListAdapter;
import com.kenzahn.zahn.database.DatabaseHandler;
import com.kenzahn.zahn.database.DatabaseHandler2;
import com.kenzahn.zahn.interfaces.AdapterItemClickListener2;
import com.kenzahn.zahn.interfaces.AdapterResetClickListener;
import com.kenzahn.zahn.model.CardContentRes;
import com.kenzahn.zahn.model.CardContentRes2;
import com.kenzahn.zahn.model.FlashcardJsonList;
import com.kenzahn.zahn.model.FlashcardJsonList2;
import com.kenzahn.zahn.newactivities.BookListActivity2;
import com.kenzahn.zahn.newadapters.BookListAdapter2;
import com.kenzahn.zahn.utils.AnimationItem;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.utils.SharedPrefs;
import com.kenzahn.zahn.widget.TypeFaceTextView;

import java.util.ArrayList;

public class AllBooksFragment2 extends Fragment implements AdapterItemClickListener2, AdapterResetClickListener {

    private ArrayList<CardContentRes2> mResponseAll;
    public DatabaseHandler2 databaseHandler;
    private AppPreference mAppPreference;
    private boolean readPresent;
    private ArrayList<FlashcardJsonList2> mResponse1;
    private String bookList = "";
    private AnimationItem[] mAnimationItems;
    private AnimationItem mSelectedItem;
    public BookListAdapter2 mbookslist;
    private RecyclerView recyclerView;
    private FrameLayout mainFrameLayout;
    private TypeFaceTextView emptyLayout;
    SharedPrefs sharedPrefs;
    Context context;


    public AllBooksFragment2() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    // TODO: Rename and change types and number of parameters
    public static AllBooksFragment2 newInstance(String param1, String param2)
    {
        AllBooksFragment2 fragment = new AllBooksFragment2();
        Bundle args = new Bundle();
        args.putString("", param1);
        args.putString("", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_books_fragment2, container, false);
        sharedPrefs=new SharedPrefs(context);
        sharedPrefs.setFirst();
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        databaseHandler = new DatabaseHandler2(getActivity());
        this.mAppPreference = AppApplication.getPreferenceInstance();
        // FloatingActionButton floatshuffle = view.findViewById(R.id.floatshuffle);
        final View progressLayout = view.findViewById(R.id.progressLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        mainFrameLayout = view.findViewById(R.id.mainFrameLayout);
        emptyLayout = view.findViewById(R.id.emptyLayout);
        emptyLayout.setText(sharedPrefs.getError().get(SharedPrefs.KEY_Error));
        emptyLayout.setTextColor(Color.parseColor("#999999"));
        Log.e("Alert",sharedPrefs.getError().get(SharedPrefs.KEY_Error));

//        floatshuffle.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    public void run() {
//                        progressLayout.setVisibility(View.GONE);
//                        loadData("suffle");
//                    }
//                }, 3000);
//                mAnimationItems = getAnimationItems();
//                mSelectedItem = mAnimationItems[0];
//                mbookslist.shuffle(mResponse1);
//                runLayoutAnimation(recyclerView, mSelectedItem);
//
//            }
//        });
    }

    private void loadData(String s)
    {
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(R.layout.custom_dialog, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(getActivity()).create();
        deleteDialog.setView(deleteDialogView);
        TextView btn_yes = deleteDialogView.findViewById(R.id.btn_yes);
        TextView tv_mess = deleteDialogView.findViewById(R.id.tv_mess);
        TextView tv_title = deleteDialogView.findViewById(R.id.tv_title);
        if(s.equals("reset")) {
            tv_title.setText("Reset");
            tv_mess.setText("Reset successfully");
        }else{
            tv_title.setText("Shuffle FlashcardSet");
            tv_mess.setText("FlashcardSet Shuffled successfully");
        }

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show();
        deleteDialog.setCancelable(false);
    }

    @Override
    public void onItemClick(String pos, String type, Object var3, View var4)
    {
        Intent intent = new Intent(getActivity(), BookListActivity2.class);
        mAppPreference.writeString("ExamID",pos);
        mAppPreference.writeString("cardcontent",type);
        startActivity(intent);
    }

    @Override
    public void onItemClickReset(int pos, String type, Object var3, View var4)
    {

        String query = "UPDATE flashcard2  SET CompletedCards ='0' WHERE ExamID = '" + type + "' ";
        databaseHandler.updateQuery(query);
        mResponseAll = databaseHandler.getFlashCardListContent(type);

        for (int i=0;i<mResponseAll.size();i++)
        {
            String query2 = " UPDATE flashcardcontent3 " +
                    " SET isKnownReadCount ='0', isKnownContent='0',SelectedAnswer='SAns', SortOrder='"+mResponseAll.get(i).getOriginalCardOrder()+"' WHERE ExamQuestionID = '" + mResponseAll.get(i).getExamQuestionID() + "' ";
            databaseHandler.updateQuery(query2);
        }

        setInprogressData();
        loadData("reset");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setInprogressData();
    }

    private void setInprogressData()
    {

        mResponse1 = databaseHandler.getFlashCardList();
        if (mResponse1.size() > 0) {
            mainFrameLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            mbookslist = new BookListAdapter2(mResponse1, this.getActivity(), this, this);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(mbookslist);
        } else {
            mainFrameLayout.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);

        }
    }

    protected final AnimationItem[] getAnimationItems() {
        return new AnimationItem[]{new AnimationItem("Slide from right", R.anim.layout_animation_from_right)};
    }

    private final void runLayoutAnimation(RecyclerView recyclerView, AnimationItem item) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, item.getResourceId());
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
