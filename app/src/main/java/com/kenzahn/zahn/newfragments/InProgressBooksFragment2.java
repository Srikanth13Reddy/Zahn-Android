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
import com.kenzahn.zahn.adapter.InProgressBookListAdapter;
import com.kenzahn.zahn.database.DatabaseHandler;
import com.kenzahn.zahn.database.DatabaseHandler2;
import com.kenzahn.zahn.interfaces.AdapterItemClickListener2;
import com.kenzahn.zahn.interfaces.AdapterResetClickListener;
import com.kenzahn.zahn.model.CardContentRes;
import com.kenzahn.zahn.model.CardContentRes2;
import com.kenzahn.zahn.model.FlashcardJsonList;
import com.kenzahn.zahn.model.FlashcardJsonList2;
import com.kenzahn.zahn.newactivities.BookListActivity2;
import com.kenzahn.zahn.newadapters.InProgressBookListAdapter2;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.utils.SharedPrefs;
import com.kenzahn.zahn.widget.TypeFaceTextView;

import java.util.ArrayList;

public class InProgressBooksFragment2 extends Fragment implements AdapterItemClickListener2, AdapterResetClickListener {
    public DatabaseHandler2 databaseHandler;
    private AppPreference mAppPreference;
    private boolean readPresent;
    ArrayList<FlashcardJsonList2> mResponse;
    public InProgressBookListAdapter2 mbookslist;
    private FrameLayout mainFrameLayout;
    private TypeFaceTextView emptyLayout;
    private RecyclerView recyclerView;
    private ArrayList<CardContentRes2> mResponseAll;
    public AlertDialog shuffle_dialog;
    SharedPrefs sharedPrefs;
    Context context;

    public InProgressBooksFragment2() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    // TODO: Rename and change types and number of parameters
    public static InProgressBooksFragment2 newInstance(String param1, String param2) {
        InProgressBooksFragment2 fragment = new InProgressBooksFragment2();
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
        databaseHandler = new DatabaseHandler2(getActivity());
        this.mAppPreference = AppApplication.getPreferenceInstance();
        FloatingActionButton floatshuffle = view.findViewById(R.id.floatshuffle);
        recyclerView = view.findViewById(R.id.recyclerView);
        mainFrameLayout = view.findViewById(R.id.mainFrameLayout);
        emptyLayout = view.findViewById(R.id.emptyLayout);
        sharedPrefs=new SharedPrefs(context);
        String data=sharedPrefs.getError().get(SharedPrefs.KEY_Error);
        if (data.equalsIgnoreCase("No Data Found"))
        {
            emptyLayout.setTextColor(Color.parseColor("#000000"));
            emptyLayout.setText("No Exams are in-progress");
        }else {
            emptyLayout.setTextColor(Color.parseColor("#999999"));
            emptyLayout.setText(data);
        }

        floatshuffle.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.setInprogressData();
        // Toast.makeText(getActivity(), "Hiii", Toast.LENGTH_SHORT).show();
    }

    private void setInprogressData()
    {
        readPresent = false;
        mResponse = databaseHandler.getFlashCardList();
        ArrayList<String> al=new ArrayList<>();

        if (mResponse.size()==0)
        {
            //  Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < mResponse.size(); i++) {
            if (mResponse.get(i).getCompletedCards() == 0)
            {
                readPresent = mResponse.get(i).getCompletedCards() < Integer.parseInt(mResponse.get(i).getQuestionCount());
            }
        }

        if (readPresent)
        {
            mainFrameLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        } else
        {
            mainFrameLayout.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);

        }

        if (mResponse.size() > 0)
        {
            mbookslist = new InProgressBookListAdapter2(mResponse, getActivity(), this, this);
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(mbookslist);
        }


        for (int i=0;i<mResponse.size();i++)
        {
            if(mResponse.get(i).getCompletedCards()!=0&&mResponse.get(i).getCompletedCards()!=(Integer.parseInt(mResponse.get(i).getQuestionCount())))
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
    public void onItemClick(String pos, String type, Object var3, View var4) {
        Intent intent = new Intent(getActivity(), BookListActivity2.class);
        mAppPreference.writeString("ExamID", pos);
        mAppPreference.writeString("cardcontent", type);
        startActivity(intent);
    }

    @Override
    public void onItemClickReset(int pos, String type, Object var3, View var4) {

        String query = "UPDATE flashcard2  SET CompletedCards ='0' WHERE ExamID = '" + type + "' ";
        databaseHandler.updateQuery(query);
        mResponseAll = databaseHandler.getFlashCardListContent(type);

        for (int i = 0; i < mResponseAll.size(); i++)
        {
            String query2 = " UPDATE flashcardcontent3 " +
                    " SET isKnownReadCount ='0', isKnownContent='0',SelectedAnswer='SAns', SortOrder='"+mResponseAll.get(i).getOriginalCardOrder()+"' WHERE ExamQuestionID = '" + mResponseAll.get(i).getExamQuestionID() + "' ";

//            String query2 = " UPDATE flashcardcontent " +
//                    " SET isKnownReadCount ='0', isKnownContent='0', SortOrder='" + mResponseAll.get(i).getOriginalCardOrder() + "' WHERE FlashCardID = '" + mResponseAll.get(i).getExamQuestionID() + "' ";
            databaseHandler.updateQuery(query2);
        }
        setInprogressData();
        loadData();
    }

    private void loadData() {
        LayoutInflater inflater = this.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_dialog, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(getActivity()).create();
        deleteDialog.setView(alertLayout);
        TextView btn_yes = alertLayout.findViewById(R.id.btn_yes);
        TextView tv_mess = alertLayout.findViewById(R.id.tv_mess);
        TextView tv_title = alertLayout.findViewById(R.id.tv_title);
        tv_title.setText("Reset");
        tv_mess.setText("Reset successfully");

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show();
        deleteDialog.setCancelable(false);
    }


}
