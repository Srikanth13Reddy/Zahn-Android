package com.kenzahn.zahn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kenzahn.zahn.adapter.SupplementAdapter;
import com.kenzahn.zahn.interfaces.SaveView;
import com.kenzahn.zahn.model.SupplementModel;
import com.kenzahn.zahn.rest.ApiClient;
import com.kenzahn.zahn.rest.SaveImpl;
import com.kenzahn.zahn.utils.SharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SupplementMaterialActivity extends AppCompatActivity implements SaveView
{

    private View progressLayout;
    private RecyclerView rv_supplementals;
    private TextView textCartItemCount;
    private int mCartItemCount=0;
    SharedPrefs sharedPrefs;
    String order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplement_material);
        getSupportActionBar().setTitle("Supplemental Materials");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPrefs=new SharedPrefs(this);
        order_id= sharedPrefs.getOrderID().get(SharedPrefs.KEY_ORDER_ID);
        progressLayout =findViewById(R.id.progressLayout);
        rv_supplementals =findViewById(R.id.rv_supplementals);
        rv_supplementals.setLayoutManager(new LinearLayoutManager(this));
        getCount(order_id);
        getSupplementalData();
    }


    private void getSupplementalData()
    {
        progressLayout.setVisibility(View.VISIBLE);
        new SaveImpl(this).handleSave(new JSONObject(),"api/Checkout/GetSupplementalMaterials","GET");
    }

    @Override
    public void onSaveSucess(String code, String response) {
        progressLayout.setVisibility(View.GONE);
        if (code.equalsIgnoreCase("200"))
        {
            Log.e("Response",response);
            assignData(response);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        else if (item.getItemId()==R.id.cart)
        {
            // Toast.makeText(this, "Cart", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(this,CartActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void assignData(String response)
    {
        ArrayList<SupplementModel> al=new ArrayList<>();
        try {
            JSONObject js=new JSONObject(response);
            int status= js.optInt("status");
            if (status==1)
            {
                JSONArray ja=js.getJSONArray("response");
                for (int i=0;i<ja.length();i++)
                {
                    JSONObject json= ja.getJSONObject(i);
                    String CycleId=  json.optString("CycleId");
                    String Title=  json.optString("Title");
                    String TestDate=  json.optString("TestDate");
                    String ApplicationDueDate=  json.optString("ApplicationDueDate");
                    String LastClassDate=  json.optString("LastClassDate");
                    String LastExamDate=  json.optString("LastExamDate");
                    String LastTutorDate=  json.optString("LastTutorDate");
                    SupplementModel supplementModel=new SupplementModel(CycleId,Title,TestDate,ApplicationDueDate,LastClassDate,LastExamDate,LastTutorDate);
                    al.add(supplementModel);
                }
                SupplementAdapter supplementAdapter=new SupplementAdapter(al,this);
                rv_supplementals.setAdapter(supplementAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveFailure(String error)
    {
        progressLayout.setVisibility(View.GONE);
        Log.e("Error",error);
    }

    public void getCount(String orderId)
    {
        progressLayout.setVisibility(View.VISIBLE);
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, ApiClient.API_BASE_URL + "api/Checkout/LoadCart?orderId="+orderId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                progressLayout.setVisibility(View.GONE);

                try {
                    JSONObject js=new JSONObject(response);
                    JSONArray ja=  js.getJSONArray("orderItemList");
                    Log.e("Response",response);
                    mCartItemCount=ja.length();
                    setupBadge();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //   textCartItemCount.setText("5");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressLayout.setVisibility(View.GONE);
                Log.e("Response",""+error);

            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.cart,menu);
        MenuItem menuItem = menu.findItem(R.id.cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
       // menuItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getCount(new SharedPrefs(this).getOrderID().get(SharedPrefs.KEY_ORDER_ID));
    }


}


