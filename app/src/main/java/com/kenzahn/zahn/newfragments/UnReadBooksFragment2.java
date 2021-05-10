package com.kenzahn.zahn.newfragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kenzahn.zahn.AppApplication;
import com.kenzahn.zahn.BookListActivity;
import com.kenzahn.zahn.R;
import com.kenzahn.zahn.adapter.UnReadBookListAdapter;
import com.kenzahn.zahn.database.DatabaseHandler;
import com.kenzahn.zahn.database.DatabaseHandler2;
import com.kenzahn.zahn.interfaces.AdapterItemClickListener2;
import com.kenzahn.zahn.model.FlashcardJsonList;
import com.kenzahn.zahn.model.FlashcardJsonList2;
import com.kenzahn.zahn.newactivities.BookListActivity2;
import com.kenzahn.zahn.newadapters.UnReadBookListAdapter2;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.utils.SharedPrefs;
import com.kenzahn.zahn.widget.TypeFaceTextView;
import java.util.ArrayList;


public class UnReadBooksFragment2 extends Fragment implements AdapterItemClickListener2
{
    public DatabaseHandler2 databaseHandler;
    private AppPreference mAppPreference;
    private boolean readPresent;
    ArrayList<FlashcardJsonList2> mResponse;
    public UnReadBookListAdapter2 mbookslist;
    private FrameLayout mainFrameLayout;
    private TypeFaceTextView emptyLayout;
    private RecyclerView recyclerView;
    private boolean firstVisit=true;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SharedPrefs sharedPrefs;
    Context context;
    public UnReadBooksFragment2() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    // TODO: Rename and change types and number of parameters
    public static UnReadBooksFragment2 newInstance(String param1, String param2) {
        UnReadBooksFragment2 fragment = new UnReadBooksFragment2();
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
        databaseHandler = new DatabaseHandler2(getActivity());
        this.mAppPreference = AppApplication.getPreferenceInstance();
        FloatingActionButton floatshuffle = view.findViewById(R.id.floatshuffle);
        recyclerView = view.findViewById(R.id.recyclerView);
        // mSwipeRefreshLayout = (SwipeRefreshLayout)view. findViewById(R.id.swipeToRefresh);
        mainFrameLayout = view.findViewById(R.id.mainFrameLayout);
        emptyLayout = view.findViewById(R.id.emptyLayout);
        sharedPrefs=new SharedPrefs(context);
        String data=sharedPrefs.getError().get(SharedPrefs.KEY_Error);
        if (data.equalsIgnoreCase("No Data Found"))
        {
            emptyLayout.setTextColor(Color.parseColor("#000000"));
            emptyLayout.setText("No Exams are in Unread");
        }else {
            emptyLayout.setTextColor(Color.parseColor("#999999"));
            emptyLayout.setText(data);
        }
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
    public void onItemClick(String pos, String type, Object var3, View var4)
    {
        Log.e("Dataaaaaaaaaaaaaaaaaaa",""+pos);
        Intent intent = new Intent(getActivity(), BookListActivity2.class);
        mAppPreference.writeString("ExamID", pos);
        mAppPreference.writeString("cardcontent", type);
        startActivity(intent);
    }

    private void loadData()
    {
        mResponse = databaseHandler.getFlashCardList();
        for (int i = 0; i < mResponse.size(); i++) {
            if (mResponse.get(i).getCompletedCards() == 0) {
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

        if (mResponse.size() > 0) {
            mbookslist = new UnReadBookListAdapter2(mResponse, getActivity(), this);
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(mbookslist);
        }


        ArrayList<String> al=new ArrayList<>();
        for (int i=0;i<mResponse.size();i++)
        {
            if(mResponse.get(i).getCompletedCards()==0)
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
