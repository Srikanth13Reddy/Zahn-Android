package com.kenzahn.zahn.adapter;

import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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

public class BookListDetailsToLearnAdapter extends RecyclerView.Adapter<BookListDetailsToLearnAdapter.MyViewHolder> {
    public DatabaseHandler databaseHandler;
    private int lastPosition;
    private ArrayList<CardContentRes> booklist;
    private final Context mContext;
    private AdapterItemClickListener adapterItemClickListener;
    public BookListDetailsToLearnAdapter(ArrayList<CardContentRes>  booklist, Context mContext, AdapterItemClickListener adapterItemClickListener) {
        this.booklist = booklist;
        this.mContext = mContext;
        this.adapterItemClickListener = adapterItemClickListener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_adapter, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try{
            this.databaseHandler = new DatabaseHandler(this.mContext);
            Spanned fromHtml = HtmlCompat.fromHtml(mContext, booklist.get(position).getFlashCardFront(), 0);
            holder.kolodaImage.setText(""+fromHtml);
            if(position==0){
                holder.isCheck.setVisibility(View.GONE);
            }else{
                holder.isCheck.setVisibility(View.VISIBLE);
            }
            if(booklist.get(position).getKnownContent()){
                holder.isCheck.setChecked(true);
            }else{
                holder.isCheck.setChecked(false);
            }
        }catch (Exception e){

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterItemClickListener.onItemClick(Integer.parseInt(booklist.get(position).getSortOrder()),booklist.get(position).getFlashCardSetID(),"",null);
            }
        });


    }

    @Override
    public int getItemCount() {
        return booklist.size();
    }

    public void shuffle(ArrayList<CardContentRes> mResponse1) {
        Collections.shuffle(mResponse1);
        for ( int i =0; i<mResponse1.size();i++){
            String query = "UPDATE flashcardcontent " +
                    " SET SortOrder ='" + i + "' WHERE FlashCardID = '" + mResponse1.get(i).getFlashCardID() + "' ";
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
