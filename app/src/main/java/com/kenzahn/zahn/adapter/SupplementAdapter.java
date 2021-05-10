package com.kenzahn.zahn.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kenzahn.zahn.R;
import com.kenzahn.zahn.SupplemenMeterialsListActivity;
import com.kenzahn.zahn.model.SupplementModel;
import com.kenzahn.zahn.widget.TypeFaceTextView;
import com.kenzahn.zahn.widget.TypeFaceTextViewBold;

import java.util.ArrayList;

public class SupplementAdapter extends RecyclerView.Adapter<SupplementAdapter.MyHolder>
{
    ArrayList<SupplementModel> al;
    Context context;

    public SupplementAdapter(ArrayList<SupplementModel> al, Context context)
    {
        this.al = al;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
       LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View v=layoutInflater.inflate(R.layout.supplemantal_style,parent,false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        if (al.get(position)!=null)
        {
            holder.tv_title.setText(al.get(position).getTitle());
            holder.tv_applicationDate.setText(al.get(position).getApplicationDueDate());
            holder.tv_testDate.setText(al.get(position).getTestDate());
            holder.btn_sm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context, SupplemenMeterialsListActivity.class);
                    i.putExtra("cycleId",al.get(position).getCycleId());
                    context.startActivity(i);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
   {
       TypeFaceTextView tv_testDate,tv_applicationDate;
       TypeFaceTextViewBold tv_title;
       Button btn_sm;

       public MyHolder(@NonNull View itemView) {
           super(itemView);
           tv_title= itemView.findViewById(R.id.tv_title);
           tv_testDate= itemView.findViewById(R.id.tv_testDate);
           tv_applicationDate= itemView.findViewById(R.id.tv_applicationDate);
           btn_sm= itemView.findViewById(R.id.btn_sm);
       }
   }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
}
