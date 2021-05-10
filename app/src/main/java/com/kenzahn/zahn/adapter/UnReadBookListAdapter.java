package com.kenzahn.zahn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;
import com.kenzahn.zahn.R;
import com.kenzahn.zahn.database.DatabaseHandler;
import com.kenzahn.zahn.interfaces.AdapterItemClickListener2;
import com.kenzahn.zahn.model.FlashcardJsonList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.kenzahn.zahn.utils.Constants.SELECTED_DECK;
import static com.kenzahn.zahn.utils.Constants.SELECTED_DECK_COMPLETED;
import static com.kenzahn.zahn.utils.Constants.SELECTED_STATUS;

/**
 * Created by suresh on 4/2/2018.
 */

public class UnReadBookListAdapter extends RecyclerView.Adapter<UnReadBookListAdapter.MyViewHolder> {
    public DatabaseHandler databaseHandler;
    private int lastPosition;
    private ArrayList<FlashcardJsonList> booklist;
    private final Context mContext;
    private AdapterItemClickListener2 adapterItemClickListener;
    public UnReadBookListAdapter(ArrayList<FlashcardJsonList>  booklist, Context mContext, AdapterItemClickListener2 adapterItemClickListener) {
        this.booklist = booklist;
        this.mContext = mContext;
        this.adapterItemClickListener = adapterItemClickListener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_books, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        this.databaseHandler = new DatabaseHandler(this.mContext);
        if (booklist.get(position).getFlashCardTypeID().equalsIgnoreCase("3")||booklist.get(position).getFlashCardTypeID().equalsIgnoreCase("4"))
        {
            holder.txtExDate.setVisibility(View.GONE);
        }else {
            holder.txtExDate.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SELECTED_DECK = Integer.parseInt(booklist.get(position).getDecksortOrder());
                SELECTED_STATUS = booklist.get(position).getStatus();
                SELECTED_DECK_COMPLETED = booklist.get(position).getCompletedCards();
                adapterItemClickListener.onItemClick(booklist.get(position).getFlashCardSetID(), booklist.get(position).getFlashCardName(), "", null);

            }
        });
        holder.iv_reset.setVisibility(View.GONE);


        if(booklist.get(position).getCompletedCards()==0){
            holder.title.setText(booklist.get(position).getFlashCardName());
            holder.card_view.setVisibility(View.VISIBLE);
            holder.progress_bar.setProgressColors(ResourcesCompat.getColor(mContext.getResources(), R.color.bg, null), ResourcesCompat.getColor(mContext.getResources(), R.color.buttoncolor, null));
            holder.progress_bar.animateProgress(1000, 0, booklist.get(position).getCompletedCards());
            holder.txtCount.setText("" + booklist.get(position).getCompletedCards() + " / " + "" +(Integer.parseInt(booklist.get(position).getTotalNoOfCards())-1) );
            SimpleDateFormat inputFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            Date date = null;
            try {
                date = inputFormatter.parse(booklist.get(position).getExpiryDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat outputFormatter = new SimpleDateFormat("MM/dd/yyyy");
            String output = outputFormatter.format(date);
            holder.txtExDate.setText("Expiry Date: "+output);
            holder.progress_bar.setMax(Integer.parseInt(booklist.get(position).getTotalNoOfCards()));
            holder.progress_bar.animateProgress(1000, 0, booklist.get(position).getCompletedCards());
            holder.ivStatus.setImageResource(R.mipmap.icon_unread);
            holder.txtStatus.setText( "Unread");
        }else{
            holder.card_view.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return booklist.size();
    }

    public void shuffle(ArrayList<FlashcardJsonList> mResponse1)
    {
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,txtStatus,txtCount,txtExDate;
        ImageView ivStatus,iv_reset;
        RoundedHorizontalProgressBar progress_bar;
        CardView card_view;
        MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            txtCount = (TextView) itemView.findViewById(R.id.txtCount);
            txtExDate = (TextView) itemView.findViewById(R.id.txtExDate);
            ivStatus = (ImageView) itemView.findViewById(R.id.ivStatus);
            iv_reset = (ImageView) itemView.findViewById(R.id.iv_reset);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            progress_bar = (RoundedHorizontalProgressBar) itemView.findViewById(R.id.progress_bar);

        }
    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
}
