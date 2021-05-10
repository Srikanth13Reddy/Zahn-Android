package com.kenzahn.zahn.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kenzahn.zahn.MediaPlayerActivity;
import com.kenzahn.zahn.R;
import com.kenzahn.zahn.model.PodcastListModel;
import com.kenzahn.zahn.widget.TypeFaceTextView;
import com.kenzahn.zahn.widget.TypeFaceTextViewBold;

import java.io.Serializable;
import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class PodcastListAdapter extends RecyclerView.Adapter<PodcastListAdapter.MyViewHoder>
{
    Context context;
    ArrayList<PodcastListModel> arrayList;
    String response;
    LinearLayout ll_play_all;

    public PodcastListAdapter(Context context, ArrayList<PodcastListModel> arrayList, String response, LinearLayout ll_play_all) {
        this.context = context;
        this.arrayList = arrayList;
        this. response=response;
        this.ll_play_all=ll_play_all;
    }

    @NonNull
    @Override
    public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
       LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View v=layoutInflater.inflate(R.layout.podcast_list_style,parent,false);
        return new MyViewHoder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoder holder, int position)
    {
       PodcastListModel obj= arrayList.get(position);
       holder.tv_pd_name.setText(obj.getPodcastName());
       holder.tv_time.setText(obj.getLastClassDate());
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               if (isNetworkAvailable())
               {
                   Intent i=new Intent(context, MediaPlayerActivity.class);
//               i.putExtra("array",arrayList.toString());
//               i.putExtra("pos",position);
                   // Bundle bundle = new Bundle();
                   //  bundle.putParcelableArrayList("array", arrayList);
                   i.putExtra("pos",position);
                   //bundle.putStringArray("song",mp3Files);
                   i.putExtra("res",response);
                   i.putExtra("name",response);
                   //i.putExtras(bundle);
                   context.startActivity(i);
               }else
               {
                   Toast.makeText(context, "Please check your network connection", Toast.LENGTH_SHORT).show();

               }

           }
       });

       ll_play_all.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               if (isNetworkAvailable())
               {
                   Intent i=new Intent(context, MediaPlayerActivity.class);
//               i.putExtra("array",arrayList.toString());
//               i.putExtra("pos",position);
                   // Bundle bundle = new Bundle();
                   //  bundle.putParcelableArrayList("array", arrayList);
                   i.putExtra("pos",0);
                   //bundle.putStringArray("song",mp3Files);
                   i.putExtra("res",response);
                   //i.putExtras(bundle);
                   context.startActivity(i);
               }else
               {
                   Toast.makeText(context, "Please check your network connection", Toast.LENGTH_SHORT).show();

               }

           }
       });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHoder extends RecyclerView.ViewHolder
   {
       TypeFaceTextViewBold tv_pd_name;
       TypeFaceTextView tv_time;

       public MyViewHoder(@NonNull View itemView) {
           super(itemView);
           tv_pd_name= itemView.findViewById(R.id.tv_pd_name);
           tv_time= itemView.findViewById(R.id.tv_time);
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
