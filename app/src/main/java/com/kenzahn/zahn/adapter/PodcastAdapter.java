package com.kenzahn.zahn.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kenzahn.zahn.PodcastListActivity;
import com.kenzahn.zahn.R;
import com.kenzahn.zahn.model.PodcastModel;
import com.kenzahn.zahn.widget.TypeFaceTextViewBold;

import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.MyViewHolder>
{
    Context context;
    ArrayList<PodcastModel> arrayList;

    public PodcastAdapter(Context context, ArrayList<PodcastModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=layoutInflater.inflate(R.layout.podcast_topic_style,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        PodcastModel obj=arrayList.get(position);
        holder.tv_topic_name.setText(obj.getPodcastTopic());
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isNetworkAvailable())
                {
                    Intent i=new Intent(context, PodcastListActivity.class);
                    i.putExtra("details",obj.getPodcastDetails());
                    i.putExtra("name",obj.getPodcastTopic());
                    context.startActivity(i);
                }else {
                    Toast.makeText(context, "Please check your network connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
 {
     TypeFaceTextViewBold tv_topic_name;

     public MyViewHolder(@NonNull View itemView) {
         super(itemView);
         tv_topic_name=itemView.findViewById(R.id.tv_topic_name);
     }
 }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
}
