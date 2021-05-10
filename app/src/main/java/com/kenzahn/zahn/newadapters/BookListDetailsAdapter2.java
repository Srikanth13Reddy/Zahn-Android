package com.kenzahn.zahn.newadapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.kenzahn.zahn.model.CardContentRes2;
import com.pixplicity.htmlcompat.HtmlCompat;
import com.kenzahn.zahn.R;
import com.kenzahn.zahn.database.DatabaseHandler;
import com.kenzahn.zahn.interfaces.AdapterItemClickListener;
import com.kenzahn.zahn.model.CardContentRes;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by suresh on 4/2/2018.
 */

public class BookListDetailsAdapter2 extends RecyclerView.Adapter<BookListDetailsAdapter2.MyViewHolder> {
    public DatabaseHandler databaseHandler;
    private int lastPosition;
    private ArrayList<CardContentRes2> booklist;
    private final Context mContext;
    private final AdapterItemClickListener adapterItemClickListener1;
    public BookListDetailsAdapter2(ArrayList<CardContentRes2>  booklist, Context mContext, AdapterItemClickListener adapterItemClickListener) {
        this.booklist = booklist;
        this.mContext = mContext;
        this.adapterItemClickListener1 = adapterItemClickListener;

    }

    @Override
    public BookListDetailsAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_adapter, parent, false);
        BookListDetailsAdapter2.MyViewHolder viewHolder = new BookListDetailsAdapter2.MyViewHolder(itemLayoutView);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final BookListDetailsAdapter2.MyViewHolder holder, final int position) {
        try{
            holder.isCheck.setVisibility(View.GONE);
            this.databaseHandler = new DatabaseHandler(this.mContext);
            holder.kolodaImage.setMaxLines(1);
            Spanned fromHtml = HtmlCompat.fromHtml(mContext, booklist.get(position).getQuestion(), 0);
            holder.kolodaImage.setText(fromHtml);
            if(position==0){
                holder.isCheck.setVisibility(View.GONE);
            }else{
                holder.isCheck.setVisibility(View.VISIBLE);
            }
            if(booklist.get(position).getKnownContent()){
                holder.isCheck.setVisibility(View.VISIBLE);
                holder.isCheck.setChecked(true);
            }else{
                holder.isCheck.setVisibility(View.GONE);
                holder.isCheck.setChecked(false);
            }
        }catch (Exception e){

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterItemClickListener1.onItemClick(Integer.parseInt(booklist.get(position).getSortOrder()),booklist.get(position).getExamID(),"",null);
            }
        });


    }

    @Override
    public int getItemCount() {
        return booklist.size();
    }

    public void shuffle(ArrayList<CardContentRes2> mResponse1)
    {
        Collections.shuffle(mResponse1);
        for ( int i =0; i<mResponse1.size();i++)
        {
            String query = "UPDATE flashcardcontent " +
                    " SET SortOrder ='" + i+1 + "' WHERE ExamQuestionID = '" + mResponse1.get(i).getExamQuestionID() + "' ";
            databaseHandler.updateQuery(query);
        }
        this.notifyDataSetChanged();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView kolodaImage;
        CheckBox isCheck;
        MyViewHolder(View itemView) {
            super(itemView);
            kolodaImage = (TextView) itemView.findViewById(R.id.kolodaImage);
            isCheck = (CheckBox) itemView.findViewById(R.id.isCheck);


        }
    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
}

