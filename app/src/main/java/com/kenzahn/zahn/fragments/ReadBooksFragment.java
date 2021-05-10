package com.kenzahn.zahn.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kenzahn.zahn.AppApplication;
import com.kenzahn.zahn.BookListActivity;
import com.kenzahn.zahn.R;
import com.kenzahn.zahn.adapter.ReadBookListAdapter;
import com.kenzahn.zahn.database.DatabaseHandler;
import com.kenzahn.zahn.interfaces.AdapterItemClickListener2;
import com.kenzahn.zahn.interfaces.AdapterResetClickListener;
import com.kenzahn.zahn.model.CardContentRes;
import com.kenzahn.zahn.model.FlashcardJsonList;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.widget.TypeFaceTextView;

import java.util.ArrayList;

public class ReadBooksFragment extends Fragment implements AdapterItemClickListener2, AdapterResetClickListener {
    public DatabaseHandler databaseHandler;
    private AppPreference mAppPreference;
    private boolean readPresent;
    ArrayList<FlashcardJsonList> mResponse;
    ArrayList<CardContentRes> mResponseAll;
    public ReadBookListAdapter mbookslist;
    private FrameLayout mainFrameLayout;
    private TypeFaceTextView emptyLayout;
    private RecyclerView recyclerView;
    public AlertDialog shuffle_dialog;
    public ReadBooksFragment() {
        // Required empty public constructor
    }

    public static ReadBooksFragment newInstance(String param1, String param2) {
        ReadBooksFragment fragment = new ReadBooksFragment();
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
        View view = inflater.inflate(R.layout.fragment_allbooks, container, false);
        return view;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        databaseHandler = new DatabaseHandler(getActivity());
        this.mAppPreference = AppApplication.getPreferenceInstance();
        FloatingActionButton floatshuffle = view.findViewById(R.id.floatshuffle);
        recyclerView = view.findViewById(R.id.recyclerView);
        mainFrameLayout = view.findViewById(R.id.mainFrameLayout);
        emptyLayout = view.findViewById(R.id.emptyLayout);
        floatshuffle.setVisibility(View.GONE);
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
    private void setInprogressData() {
        readPresent = false;
        mResponse = databaseHandler.getFlashCardList();

        ArrayList <FlashcardJsonList> arrayList=new ArrayList<>();
        arrayList.clear();
        for (int i=0;i<mResponse.size();i++)
        {

            if (mResponse.get(i).getFlashCardTypeID().equalsIgnoreCase("2"))
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



        for (int i=0;i<arrayList.size();i++){
            if(arrayList.get(i).getCompletedCards() ==0){
                readPresent = arrayList.get(i).getCompletedCards() < Integer.parseInt(arrayList.get(i).getTotalNoOfCards());
            }
        }

        if(readPresent){
            mainFrameLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }else{
            mainFrameLayout.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }

        if (arrayList.size() > 0)
        {
            mbookslist = new ReadBookListAdapter(arrayList, getActivity(), this,this);
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(mbookslist);
        }

         ArrayList<String> al=new ArrayList<>();
        for (int i=0;i<arrayList.size();i++)
        {
            if(arrayList.get(i).getCompletedCards()==Integer.parseInt(arrayList.get(i).getTotalNoOfCards())-1)
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

    @Override
    public void onResume() {
        super.onResume();
        setInprogressData();
    }

    @Override
    public void onItemClick(String pos, String type, Object var3, View var4) {
        Intent intent = new Intent(getActivity(), BookListActivity.class);
        mAppPreference.writeString("FlashCardSetID", pos);
        mAppPreference.writeString("cardcontent", type);
        mAppPreference.writeString("type","Flashcards Online");
        startActivity(intent);
    }

    @Override
    public void onItemClickReset(int pos, String type, Object var3, View var4)
    {
//        String query = "UPDATE flashcard  SET CompletedCards ='0' WHERE FlashCardSetID = '" + type + "' ";
//        databaseHandler.updateQuery(query);
//        mResponseAll = databaseHandler.getFlashCardListContent2(type);
//
//        for (int i=0;i<mResponseAll.size();i++) {
//
//            String query2 = " UPDATE flashcardcontent " +
//                    " SET isKnownReadCount ='0', isKnownContent='0', SortOrder='"+mResponseAll.get(pos).getOriginalCardOrder()+"' WHERE FlashCardID = '" + mResponseAll.get(pos).getFlashCardID() + "' ";
//            databaseHandler.updateQuery(query2);
//        }
//        setInprogressData();
//        loadData("reset");


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
}
