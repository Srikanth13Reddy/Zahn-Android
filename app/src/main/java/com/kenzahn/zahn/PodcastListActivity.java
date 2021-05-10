package com.kenzahn.zahn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Parcel;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kenzahn.zahn.adapter.PodcastListAdapter;
import com.kenzahn.zahn.model.PodcastListModel;
import com.kenzahn.zahn.model.PodcastModel;
import com.kenzahn.zahn.widget.TypeFaceTextViewBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class PodcastListActivity extends AppCompatActivity
{
    RecyclerView rv_pd_list;
    RelativeLayout rl;
    TypeFaceTextViewBold tv_notfound;
    LinearLayout ll_play_all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv_pd_list=findViewById(R.id.rv_pd_list);
        rl=findViewById(R.id.rv__);
        ll_play_all=findViewById(R.id.ll_play_all);
        tv_notfound=findViewById(R.id.tv_not_found);
        rv_pd_list.setLayoutManager(new LinearLayoutManager(this));
        getData();
    }

    private void getData()
    {
        ArrayList<PodcastListModel> arrayList=new ArrayList<>();
//        for (int i=0;i<names.length;i++)
//        {
//            PodcastListModel podcastModel=new PodcastListModel();
//            podcastModel.setNames(names[i]);
//            podcastModel.setUrl(mp3files[i]);
//            arrayList.add(podcastModel);
//        }
//        PodcastListAdapter podcastListAdapter=new PodcastListAdapter(this,arrayList,mp3files);
//        rv_pd_list.setAdapter(podcastListAdapter);
        Bundle b=getIntent().getExtras();

        if (b != null) {
            String  response = b.getString("details");
            String  name = b.getString("name");
            Objects.requireNonNull(getSupportActionBar()).setTitle(name);
            if (response==null||response.equalsIgnoreCase("null"))
            {
                rl.setVisibility(View.GONE);
                tv_notfound.setVisibility(View.VISIBLE);
            }else {
                rl.setVisibility(View.VISIBLE);
                tv_notfound.setVisibility(View.GONE);
            }
            try {
                JSONArray ja=new JSONArray(response);

                for (int i=0;i<ja.length();i++)
                {
                    JSONObject json= ja.getJSONObject(i);
                    String LastClassDate= json.optString("LastClassDate");
                    int PodcastID= json.optInt("PodcastID");
                    String CreateDate= json.optString("CreateDate");
                    String PodcastURL= json.optString("PodcastURL");
                    String PodcastName= json.optString("PodcastName");
                    int PodcastTopicID= json.optInt("PodcastTopicID");
                    boolean Active= json.optBoolean("Active");
                    String Description= json.optString("PodCastDescription");
                    String PodcastTopic= json.optString("PodcastTopic");
                    PodcastListModel podcastListModel=new PodcastListModel(LastClassDate,PodcastID,CreateDate,PodcastURL,PodcastName,PodcastTopicID,Active,Description,PodcastTopic);
                    arrayList.add(podcastListModel);
                }
                PodcastListAdapter podcastListAdapter=new PodcastListAdapter(this,arrayList,response,ll_play_all);
                rv_pd_list.setAdapter(podcastListAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}