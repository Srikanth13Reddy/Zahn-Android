package com.kenzahn.zahn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kenzahn.zahn.adapter.PodcastAdapter;
import com.kenzahn.zahn.interfaces.SaveView;
import com.kenzahn.zahn.model.LoginModel;
import com.kenzahn.zahn.model.PodcastModel;
import com.kenzahn.zahn.rest.SaveImpl;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.utils.Constants;
import com.kenzahn.zahn.widget.TypeFaceTextViewBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PodcastActivity extends AppCompatActivity implements SaveView
{
    RecyclerView rv_pod;
    String [] topics={"Design 01","Design 02","Design 03","Design 04","Design 05"};
    private View progressLayout;
    private AppPreference mAppPreference;
    private LoginModel loginRes;
    private int user_id;
    TypeFaceTextViewBold tv_not_found;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast);
        getSupportActionBar().setTitle("Podcast");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv_pod=findViewById(R.id.rv_pod);
        tv_not_found=findViewById(R.id.tv_not_found);
        progressLayout =findViewById(R.id.progressLayout);
        rv_pod.setLayoutManager(new LinearLayoutManager(this));
        getUSerID();
        getData();
    }

    private void getUSerID()
    {
        this.mAppPreference = AppApplication.getPreferenceInstance();
        String userData = mAppPreference.readString(Constants.USER_DATA);
        TextView tvUserName = findViewById(R.id.tvUserName);
        TextView tvUserEmail = findViewById(R.id.tvUserEmail);

        if (userData != null)
        {
            Gson gson = new Gson();
            String userDataNew = mAppPreference.readString(Constants.USER_DATA);
            loginRes = gson.fromJson(userDataNew, LoginModel.class);
            user_id= loginRes.getResponse().getUserid();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    void getData()
    {
//        ArrayList<PodcastModel> arrayList=new ArrayList<>();
//        for (int i=0;i<topics.length;i++)
//        {
//            PodcastModel podcastModel=new PodcastModel();
//            podcastModel.setTopicName(topics[i]);
//            arrayList.add(podcastModel);
//        }
//        PodcastAdapter podcastAdapter=new PodcastAdapter(this,arrayList);
//        rv_pod.setAdapter(podcastAdapter);


        progressLayout.setVisibility(View.VISIBLE);
        new SaveImpl(this).handleSave(new JSONObject(),"api/AppUsers/GetPodcasts?ContactID="+user_id,"GET");


    }

    @Override
    public void onSaveSucess(String code, String response)
    {
        progressLayout.setVisibility(View.GONE);
          assignData(response);
    }

    @Override
    public void onSaveFailure(String error)
    {
        progressLayout.setVisibility(View.GONE);
        Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
    }

    void assignData(String response)
    {
        ArrayList<PodcastModel> arrayList=new ArrayList<>();
        try {
            JSONObject js=new JSONObject(response);
           String status= js.optString("status");
           if (status.equalsIgnoreCase("Success"))
           {
               tv_not_found.setVisibility(View.GONE);
              JSONArray ja=js.getJSONArray("data");
//              if (ja.length()==0)
//              {
//                  tv_not_found.setVisibility(View.VISIBLE);
//              }else {
//                  tv_not_found.setVisibility(View.GONE);
//              }
              for (int i=0;i<ja.length();i++)
              {
                 JSONObject json= ja.getJSONObject(i);
                  String PodcastTopic=json.optString("PodcastTopic");
                  String Status=json.optString("Status");
                  int StatusTypeID=json.optInt("StatusTypeID");
                  int ContactID=json.optInt("ContactID");
                  int SortOrder=json.optInt("SortOrder");
                  String podcastDetails=json.optString("podcastDetails");
                  PodcastModel podcastModel=new PodcastModel(PodcastTopic,Status,StatusTypeID,ContactID,SortOrder,podcastDetails);
                  arrayList.add(podcastModel);

              }
              PodcastAdapter podcastAdapter=new PodcastAdapter(this,arrayList);
              rv_pod.setAdapter(podcastAdapter);
           }else {
               tv_not_found.setVisibility(View.VISIBLE);
               tv_not_found.setText(""+js.optString("errMsg"));
              // Toast.makeText(this, ""+js.optString("errMsg"), Toast.LENGTH_SHORT).show();
           }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}