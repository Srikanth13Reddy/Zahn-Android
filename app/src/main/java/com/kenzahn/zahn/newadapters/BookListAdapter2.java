package com.kenzahn.zahn.newadapters;


import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kenzahn.zahn.model.FlashcardJsonList2;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;
import com.kenzahn.zahn.R;
import com.kenzahn.zahn.database.DatabaseHandler;
import com.kenzahn.zahn.interfaces.AdapterItemClickListener2;
import com.kenzahn.zahn.interfaces.AdapterResetClickListener;
import com.kenzahn.zahn.model.FlashcardJsonList;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import static com.kenzahn.zahn.utils.Constants.SELECTED_DECK;
import static com.kenzahn.zahn.utils.Constants.SELECTED_DECK_COMPLETED;
import static com.kenzahn.zahn.utils.Constants.SELECTED_STATUS;

/**
 * Created by suresh on 4/2/2018.
 */

public class BookListAdapter2 extends RecyclerView.Adapter<BookListAdapter2.MyViewHolder> {
    public DatabaseHandler databaseHandler;
    private int lastPosition;
    private ArrayList<FlashcardJsonList2> booklist;
    private final Context mContext;
    private AdapterItemClickListener2 adapterItemClickListener;
    private AdapterResetClickListener adapterResetClickListener;
    public BookListAdapter2(ArrayList<FlashcardJsonList2>  booklist, Context mContext, AdapterItemClickListener2 adapterItemClickListener, AdapterResetClickListener adapterResetClickListener) {
        this.booklist = booklist;
        this.mContext = mContext;
        this.adapterItemClickListener = adapterItemClickListener;
        this.adapterResetClickListener = adapterResetClickListener;

    }

    @Override
    public BookListAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_books, parent, false);
        BookListAdapter2.MyViewHolder viewHolder = new BookListAdapter2.MyViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BookListAdapter2.MyViewHolder holder, final int position) {
        this.databaseHandler = new DatabaseHandler(this.mContext);
        if(booklist.get(position).getCompletedCards()==Integer.parseInt(booklist.get(position).getQuestionCount()))
        {
            holder.ivStatus.setImageResource(R.mipmap.icon_read);
            holder.txtStatus.setText("Completed");
            holder.iv_reset.setVisibility(View.VISIBLE);
        }
         if(booklist.get(position).getCompletedCards()==0){
            holder.ivStatus.setImageResource(R.mipmap.icon_unread);
            holder.txtStatus.setText("Unread");
            holder.iv_reset.setVisibility(View.GONE);
        }
         if(booklist.get(position).getCompletedCards()!=0&&booklist.get(position).getCompletedCards()!=(Integer.parseInt(booklist.get(position).getQuestionCount()))){
            if(booklist.get(position).getCompletedCards()<(booklist.get(position).getCardContent().size())){
                holder.ivStatus.setImageResource(R.mipmap.icon_progress);
                holder.txtStatus.setText("In-Progress");
                holder.iv_reset.setVisibility(View.VISIBLE);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SELECTED_DECK =  Integer.parseInt(booklist.get(position).getDeckSortOrder());
                SELECTED_STATUS = booklist.get(position).getStatus();
                SELECTED_DECK_COMPLETED = booklist.get(position).getCompletedCards();
                adapterItemClickListener.onItemClick(booklist.get(position).getExamID(), booklist.get(position).getExamName(), "", null);
            }
        });
        holder.txtCount.setText("" + booklist.get(position).getCompletedCards() + " / " + "" +(Integer.parseInt(booklist.get(position).getQuestionCount())) );
        holder.title.setText(booklist.get(position).getExamName());
        try {
            SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          //  Date date = inputFormatter.parse(booklist.get(position).getExpiryDate());
            SimpleDateFormat outputFormatter = new SimpleDateFormat("MM-dd-yyyy");
           // String output = outputFormatter.format(date);
            holder.txtExDate.setText("Expiry Date: "+booklist.get(position).getTimed());
            holder.progress_bar.setProgressColors(ResourcesCompat.getColor(mContext.getResources(), R.color.bg, null), ResourcesCompat.getColor(mContext.getResources(), R.color.buttoncolor, null));
            holder.progress_bar.setMax(Integer.parseInt(booklist.get(position).getQuestionCount()));
            holder.progress_bar.animateProgress(1000, 0, booklist.get(position).getCompletedCards());
        }catch (Exception e){

        }
        final Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.rotate);
        holder.iv_reset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                holder.iv_reset.startAnimation(animation);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run()
                    {
                        adapterResetClickListener.onItemClickReset(position,booklist.get(position).getExamID(),"",null);
                    }
                }, 3000);

            }
        });

    }

    @Override
    public int getItemCount() {
        return booklist.size();
    }

    public void shuffle(ArrayList<FlashcardJsonList> mResponse1) {
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,txtStatus,txtCount,txtExDate;
        ImageView ivStatus,iv_reset;
        RoundedHorizontalProgressBar progress_bar;
        MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            txtCount = (TextView) itemView.findViewById(R.id.txtCount);
            txtExDate = (TextView) itemView.findViewById(R.id.txtExDate);
            ivStatus = (ImageView) itemView.findViewById(R.id.ivStatus);
            iv_reset = (ImageView) itemView.findViewById(R.id.iv_reset);
            progress_bar = (RoundedHorizontalProgressBar) itemView.findViewById(R.id.progress_bar);

        }
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
}

