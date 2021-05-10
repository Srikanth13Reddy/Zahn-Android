package com.kenzahn.zahn.newfragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kenzahn.zahn.AppApplication;
import com.kenzahn.zahn.BookDetailsActivity;
import com.kenzahn.zahn.R;
import com.kenzahn.zahn.adapter.BookListDetailsAdapter;
import com.kenzahn.zahn.database.DatabaseHandler;
import com.kenzahn.zahn.database.DatabaseHandler2;
import com.kenzahn.zahn.interfaces.AdapterItemClickListener;
import com.kenzahn.zahn.model.CardContentRes;
import com.kenzahn.zahn.model.CardContentRes2;
import com.kenzahn.zahn.model.FlashcardJsonList;
import com.kenzahn.zahn.model.FlashcardJsonList2;
import com.kenzahn.zahn.newactivities.BookDetailsActivity2;
import com.kenzahn.zahn.newadapters.BookListDetailsAdapter2;
import com.kenzahn.zahn.utils.AnimationItem;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.utils.SharedPrefs;
import com.kenzahn.zahn.widget.TypeFaceTextView;

import java.util.ArrayList;

public class AllFragment2 extends Fragment implements AdapterItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    public DatabaseHandler2 databaseHandler;
    private ArrayList<CardContentRes2> mResponseAll;
    private CardContentRes2 mResponseAllLocal;
    private AppPreference mAppPreference;
    private boolean readPresent;
    private ArrayList<FlashcardJsonList2> mResponse1;
    private String bookList = "";
    public AlertDialog shuffle_dialog;
    AnimationItem[] mAnimationItems;
    private AnimationItem mSelectedItem;
    public BookListDetailsAdapter2 bookListDetailsAdapter;
    private RecyclerView recyclerView;
    private FrameLayout mainFrameLayout;
    private TypeFaceTextView emptyLayout;
    // TODO: Rename and change types of parameters
    private String FlashCardSetID;
    private Boolean _hasLoadedOnce = false;
    View progressLayout;
    SharedPrefs sharedPrefs;
    Context context;
    public AllFragment2() {
        // Required empty public constructor
    }

    public static AllFragment2 newInstance(String param1)
    {
        AllFragment2 fragment = new AllFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            FlashCardSetID = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        databaseHandler = new DatabaseHandler2(getActivity());
        mainFrameLayout= view.findViewById(R.id.mainFrameLayout);
        recyclerView= view.findViewById(R.id.recyclerView);
        emptyLayout= view.findViewById(R.id.emptyLayout1);
        sharedPrefs=new SharedPrefs(getActivity());
        mAppPreference = AppApplication.getPreferenceInstance();
        FloatingActionButton floatshuffleAll = view.findViewById(R.id.floatshuffleAll);
        progressLayout = view.findViewById(R.id.progressLayout);
        floatshuffleAll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                shuffle();
            }

        });

        //  shuffle();

    }

    private void shuffle()
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            public void run() {
                progressLayout.setVisibility(View.GONE);
                loadData();
            }
        }, 3000);
        mAnimationItems = getAnimationItems();
        mSelectedItem = mAnimationItems[0];
        runLayoutAnimation(recyclerView, mSelectedItem);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible_ && !_hasLoadedOnce) {
                _hasLoadedOnce = true;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecycler();
    }

    private void loadRecycler()
    {
        databaseHandler.getData();

        mResponseAll = databaseHandler.getFlashCardListContent(FlashCardSetID);
        if (mResponseAll.size() > 0)
        {
            mainFrameLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            bookListDetailsAdapter = new BookListDetailsAdapter2(mResponseAll, this.getActivity(), this);
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(bookListDetailsAdapter);


        } else {
            mainFrameLayout.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);

        }


    }

    private void loadData()
    {
        mResponseAllLocal = mResponseAll.get(0);
        mResponseAll.remove(mResponseAll.get(0));
        bookListDetailsAdapter.shuffle(mResponseAll);
        mResponseAll.add(0, mResponseAllLocal);
        for (int i =0;i<mResponseAll.size();i++)
        {
            String query = "UPDATE flashcardcontent3 " +
                    " SET SortOrder ='" + i + "' WHERE ExamQuestionID = '" + mResponseAll.get(i).getExamQuestionID() + "' ";
            databaseHandler.updateQuery(query);
        }
        loadRecycler();

        LayoutInflater inflater = this.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_dialog, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(getActivity()).create();
        deleteDialog.setView(alertLayout);
        TextView btn_yes = alertLayout.findViewById(R.id.btn_yes);
        TextView tv_mess = alertLayout.findViewById(R.id.tv_mess);
        TextView tv_title = alertLayout.findViewById(R.id.tv_title);
        tv_mess.setText("Questions shuffled successfully");
        tv_title.setText("Questions Shuffle");
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show();
        deleteDialog.setCancelable(false);

    }

    private final void runLayoutAnimation(RecyclerView recyclerView, AnimationItem item) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, item.getResourceId());
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
        // shuffle();
    }
    protected final AnimationItem[] getAnimationItems()
    {
        return new AnimationItem[]{new AnimationItem("Slide from right", R.anim.layout_animation_from_right)};
    }
    @Override
    public void onItemClick(int pos, String type, Object var3, View var4)
    {
        Intent intent = new Intent(getActivity(), BookDetailsActivity2.class);
        mAppPreference.writeString("ExamID", type);
        mAppPreference.writeInteger("SortOrder",pos);
        startActivity(intent);
    }


}
