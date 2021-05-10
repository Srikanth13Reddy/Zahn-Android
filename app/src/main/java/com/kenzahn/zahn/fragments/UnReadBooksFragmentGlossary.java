package com.kenzahn.zahn.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kenzahn.zahn.AppApplication;
import com.kenzahn.zahn.BookListActivity;
import com.kenzahn.zahn.R;
import com.kenzahn.zahn.adapter.UnReadBookListAdapterGlossary;
import com.kenzahn.zahn.database.DatabaseHandler;
import com.kenzahn.zahn.interfaces.AdapterItemClickListener2;
import com.kenzahn.zahn.model.FlashcardJsonList;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.widget.TypeFaceTextView;

import java.util.ArrayList;

public class UnReadBooksFragmentGlossary extends Fragment implements AdapterItemClickListener2 {
    public DatabaseHandler databaseHandler;
    private AppPreference mAppPreference;
    private boolean readPresent;
    ArrayList<FlashcardJsonList> mResponse;
    public UnReadBookListAdapterGlossary mbookslist;
    private FrameLayout mainFrameLayout;
    private TypeFaceTextView emptyLayout;
    private RecyclerView recyclerView;
    private boolean firstVisit=true;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public UnReadBooksFragmentGlossary() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UnReadBooksFragmentGlossary newInstance(String param1, String param2) {
        UnReadBooksFragmentGlossary fragment = new UnReadBooksFragmentGlossary();
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
        View view = inflater.inflate(R.layout.fragment_unread, container, false);
        databaseHandler = new DatabaseHandler(getActivity());
        this.mAppPreference = AppApplication.getPreferenceInstance();
        FloatingActionButton floatshuffle = view.findViewById(R.id.floatshuffle);
        recyclerView = view.findViewById(R.id.recyclerView);
        // mSwipeRefreshLayout = (SwipeRefreshLayout)view. findViewById(R.id.swipeToRefresh);
        mainFrameLayout = view.findViewById(R.id.mainFrameLayout);
        emptyLayout = view.findViewById(R.id.emptyLayout);
        floatshuffle.setVisibility(View.GONE);

//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadData();
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        });
        return view;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onResume()
    {
        super.onResume();
        loadData();

    }

    @Override
    public void onItemClick(String pos, String type, Object var3, View var4) {
        Intent intent = new Intent(getActivity(), BookListActivity.class);
        mAppPreference.writeString("FlashCardSetID", pos);
        mAppPreference.writeString("cardcontent", type);
        mAppPreference.writeString("type","Glossary");
        startActivity(intent);
    }

    private void loadData()
    {
        mResponse = databaseHandler.getFlashCardList();


        ArrayList <FlashcardJsonList> arrayList=new ArrayList<>();
        arrayList.clear();
        for (int i=0;i<mResponse.size();i++)
        {

            if (mResponse.get(i).getFlashCardTypeID().equalsIgnoreCase("3"))
            {
                FlashcardJsonList flashcardJsonList=new FlashcardJsonList();
                flashcardJsonList.setActive(mResponse.get(i).getActive());
                flashcardJsonList.setCycleID(mResponse.get(i).getCycleID());
                flashcardJsonList.setAudioFile(mResponse.get(i).getAudioFile());
                flashcardJsonList.setExamSectionID(mResponse.get(i).getExamSectionID());
                flashcardJsonList.setCardContent(mResponse.get(i).getCardContent());
                flashcardJsonList.setExpiryDate(mResponse.get(i).getExpiryDate());
                flashcardJsonList.setFlashCardSetID(mResponse.get(i).getFlashCardSetID());
                flashcardJsonList.setFlashCardName(mResponse.get(i).getFlashCardName());
                flashcardJsonList.setTotalNoOfCards(mResponse.get(i).getTotalNoOfCards());
                flashcardJsonList.setAudio(mResponse.get(i).getAudio());
                flashcardJsonList.setCreateDate(mResponse.get(i).getCreateDate());
                flashcardJsonList.setFlashCardTypeID(mResponse.get(i).getFlashCardTypeID());
                flashcardJsonList.setCompletedCards(mResponse.get(i).getCompletedCards());
                flashcardJsonList.setStatus(mResponse.get(i).getStatus());
                flashcardJsonList.setDecksortOrder(mResponse.get(i).getDecksortOrder());
                arrayList.add(flashcardJsonList);

            }

        }


        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getCompletedCards() == 0) {
                readPresent = true;
            }
        }

        if (readPresent) {
            mainFrameLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        } else {
            mainFrameLayout.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }

        if (arrayList.size() > 0) {
            mbookslist = new UnReadBookListAdapterGlossary(arrayList, getActivity(), this);
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(mbookslist);
        }


        ArrayList<String> al=new ArrayList<>();
        for (int i=0;i<arrayList.size();i++)
        {
            if(arrayList.get(i).getCompletedCards()==0)
            {
                al.add("Nmae");
            }

        }
        Log.e("ArrayList",""+al);

        if (al.size()==0)
        {
            // Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
            emptyLayout.setVisibility(View.VISIBLE);
            mainFrameLayout.setVisibility(View.GONE);
        }
        else {
            emptyLayout.setVisibility(View.GONE);
            mainFrameLayout.setVisibility(View.VISIBLE);
        }
    }


}
