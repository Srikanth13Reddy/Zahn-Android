package com.kenzahn.zahn.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kenzahn.zahn.AppApplication;
import com.kenzahn.zahn.BookListActivity;
import com.kenzahn.zahn.R;
import com.kenzahn.zahn.adapter.BookListAdapterGlossary;
import com.kenzahn.zahn.database.DatabaseHandler;
import com.kenzahn.zahn.interfaces.AdapterItemClickListener2;
import com.kenzahn.zahn.interfaces.AdapterResetClickListener;
import com.kenzahn.zahn.model.CardContentRes;
import com.kenzahn.zahn.model.FlashcardJsonList;
import com.kenzahn.zahn.utils.AnimationItem;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.widget.TypeFaceTextView;

import java.util.ArrayList;

public class AllBooksFragmentAcronyms extends Fragment implements AdapterItemClickListener2, AdapterResetClickListener {

    private ArrayList<CardContentRes> mResponseAll;
    public DatabaseHandler databaseHandler;
    private AppPreference mAppPreference;
    private boolean readPresent;
    private ArrayList<FlashcardJsonList> mResponse1;
    private String bookList = "";
    private AnimationItem[] mAnimationItems;
    private AnimationItem mSelectedItem;
    public BookListAdapterGlossary mbookslist;
    private RecyclerView recyclerView;
    private FrameLayout mainFrameLayout;
    private TypeFaceTextView emptyLayout;
    private static final String ARG_PARAM1 = "paramm1";
    private static final String ARG_PARAM2 = "paramm2";
    private String mParam1;
    private String mParam2;


    public AllBooksFragmentAcronyms() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AllBooksFragmentAcronyms newInstance(String param1, String param2)
    {
        AllBooksFragmentAcronyms fragment = new AllBooksFragmentAcronyms();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allbooks, container, false);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        databaseHandler = new DatabaseHandler(getActivity());
        this.mAppPreference = AppApplication.getPreferenceInstance();
        // FloatingActionButton floatshuffle = view.findViewById(R.id.floatshuffle);
        final View progressLayout = view.findViewById(R.id.progressLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        mainFrameLayout = view.findViewById(R.id.mainFrameLayout);
        emptyLayout = view.findViewById(R.id.emptyLayout);
        emptyLayout.setText("No cards available");
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
            tv_title.setText("Reset FlashcardSet");
            tv_mess.setText("FlashcardSet reset successfully");
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
        Intent intent = new Intent(getActivity(), BookListActivity.class);
        mAppPreference.writeString("FlashCardSetID",pos);
        mAppPreference.writeString("cardcontent",type);
        mAppPreference.writeString("type","Acronyms");
        startActivity(intent);
    }

    @Override
    public void onItemClickReset(int pos, String type, Object var3, View var4)
    {

        String query = "UPDATE flashcard  SET CompletedCards ='0' WHERE FlashCardSetID = '" + type + "' ";
        databaseHandler.updateQuery(query);
        mResponseAll = databaseHandler.getFlashCardListContent2(type);

        for (int i=0;i<mResponseAll.size();i++)
        {
            String query2 = " UPDATE flashcardcontent " +
                    " SET isKnownReadCount ='0', isKnownContent='0', SortOrder='"+mResponseAll.get(i).getOriginalCardOrder()+"' WHERE FlashCardID = '" + mResponseAll.get(i).getFlashCardID() + "' ";
            databaseHandler.updateQuery(query2);
        }

        setInprogressData();
        loadData("reset");
    }

    @Override
    public void onResume() {
        super.onResume();
        setInprogressData();
    }

    private void setInprogressData()
    {
        mResponse1 = databaseHandler.getFlashCardList();
        ArrayList <FlashcardJsonList> arrayList=new ArrayList<>();
        arrayList.clear();
        for (int i=0;i<mResponse1.size();i++)
        {

            if (mResponse1.get(i).getFlashCardTypeID().equalsIgnoreCase("4"))
            {
                FlashcardJsonList flashcardJsonList=new FlashcardJsonList();
                flashcardJsonList.setActive(mResponse1.get(i).getActive());
                flashcardJsonList.setCycleID(mResponse1.get(i).getCycleID());
                flashcardJsonList.setAudioFile(mResponse1.get(i).getAudioFile());
                flashcardJsonList.setExamSectionID(mResponse1.get(i).getExamSectionID());
                flashcardJsonList.setCardContent(mResponse1.get(i).getCardContent());
                flashcardJsonList.setExpiryDate(mResponse1.get(i).getExpiryDate());
                flashcardJsonList.setFlashCardSetID(mResponse1.get(i).getFlashCardSetID());
                flashcardJsonList.setFlashCardName(mResponse1.get(i).getFlashCardName());
                flashcardJsonList.setTotalNoOfCards(mResponse1.get(i).getTotalNoOfCards());
                flashcardJsonList.setAudio(mResponse1.get(i).getAudio());
                flashcardJsonList.setCreateDate(mResponse1.get(i).getCreateDate());
                flashcardJsonList.setFlashCardTypeID(mResponse1.get(i).getFlashCardTypeID());
                flashcardJsonList.setCompletedCards(mResponse1.get(i).getCompletedCards());
                flashcardJsonList.setStatus(mResponse1.get(i).getStatus());
                flashcardJsonList.setDecksortOrder(mResponse1.get(i).getDecksortOrder());
                arrayList.add(flashcardJsonList);

            }

        }

        if (arrayList.size() > 0) {
            mainFrameLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);



//                for (int i=0;i<mResponse1.size();i++)
//                {
//
//                    if (mResponse1.get(i).getFlashCardTypeID().equalsIgnoreCase("3"))
//                    {
//                        mResponse1.remove(i);
//                    }
//
//                }


            mbookslist = new BookListAdapterGlossary(arrayList, this.getActivity(), this, this);
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
          //  Toast.makeText(getContext(), ""+mParam1, Toast.LENGTH_SHORT).show();
        }
    }
}
