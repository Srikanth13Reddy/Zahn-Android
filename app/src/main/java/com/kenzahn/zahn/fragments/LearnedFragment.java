package com.kenzahn.zahn.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kenzahn.zahn.AppApplication;
import com.kenzahn.zahn.BookDetailsActivity;
import com.kenzahn.zahn.R;
import com.kenzahn.zahn.adapter.BookListDetailsLearnAdapter;
import com.kenzahn.zahn.database.DatabaseHandler;
import com.kenzahn.zahn.interfaces.AdapterItemClickListener;
import com.kenzahn.zahn.model.CardContentRes;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.widget.TypeFaceTextView;

import java.util.ArrayList;

public class LearnedFragment extends Fragment implements AdapterItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String FlashCardSetID;
    public DatabaseHandler databaseHandler;
    private AppPreference mAppPreference;
    private ArrayList<CardContentRes> mResponseLearn;
    private RecyclerView recyclerView;
    private FrameLayout mainFrameLayout;
    private TypeFaceTextView emptyLayout;
    public BookListDetailsLearnAdapter bookListDetailsLearnAdapter;
    private Boolean _hasLoadedOnce = false;
    public LearnedFragment() {
        // Required empty public constructor
    }

    public static LearnedFragment newInstance(String param1) {
        LearnedFragment fragment = new LearnedFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        return view;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        databaseHandler = new DatabaseHandler(getActivity());
        mAppPreference = AppApplication.getPreferenceInstance();
        mainFrameLayout= view.findViewById(R.id.mainFrameLayout);
        emptyLayout= view.findViewById(R.id.emptyLayout1);
        recyclerView= view.findViewById(R.id.recyclerView);
        FloatingActionButton floatshuffleAll = view.findViewById(R.id.floatshuffleAll);
        floatshuffleAll.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecycler();
    }

    private void loadRecycler() {
        mResponseLearn = databaseHandler.getFlashCardListContentLearned(FlashCardSetID);
        if (mResponseLearn.size() > 0)
        {
            mainFrameLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            bookListDetailsLearnAdapter = new BookListDetailsLearnAdapter(mResponseLearn, this.getActivity(), this);
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(bookListDetailsLearnAdapter);
        } else {
            mainFrameLayout.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);

        }
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
    public void onItemClick(int pos, String type, Object var3, View var4) {
        Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
        mAppPreference.writeString("FlashCardSetID", type);
        mAppPreference.writeInteger("SortOrder",pos);
        startActivity(intent);
    }
}
