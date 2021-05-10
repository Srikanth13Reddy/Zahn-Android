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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kenzahn.zahn.adapter.ListProductsAdapter;
import com.kenzahn.zahn.interfaces.SaveView;
import com.kenzahn.zahn.model.ListProductsModel;
import com.kenzahn.zahn.model.LoginModel;
import com.kenzahn.zahn.rest.ApiClient;
import com.kenzahn.zahn.rest.SaveImpl;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.utils.Constants;
import com.kenzahn.zahn.utils.SharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SupplemenMeterialsListActivity extends AppCompatActivity implements SaveView,ListProductsAdapter.EventListener
{

    String cycleId;
    RecyclerView rv_supplproducts;
    private View progressLayout;
    private TextView textCartItemCount;
    int mCartItemCount = 0;
    private LoginModel loginRes;
    private AppPreference mAppPreference;
    private int user_id;
    String orderId;
    SharedPrefs sharedPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplemen_meterials_list);
        getSupportActionBar().setTitle("Supplemental Materials");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv_supplproducts=findViewById(R.id.rv_supplproducts);
        progressLayout =findViewById(R.id.progressLayout);
        sharedPrefs=new SharedPrefs(this);
        orderId= sharedPrefs.getOrderID().get(SharedPrefs.KEY_ORDER_ID);
        rv_supplproducts.setLayoutManager(new LinearLayoutManager(this));
        Bundle b=getIntent().getExtras();
        if (b != null) {
            cycleId= b.getString("cycleId");
            getListProducts(cycleId);
        }
        getUSerID();
        getCount(orderId);
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

    private void getListProducts(String cycleId)
    {
        progressLayout.setVisibility(View.VISIBLE);
        new SaveImpl(this).handleSave(new JSONObject(),"api/Checkout/GetProductsByCycleId?cycleID="+cycleId,"GET");
    }

    @Override
    public void onSaveSucess(String code, String response)
    {
        progressLayout.setVisibility(View.GONE);
        Log.e("Response",response);
        if (code.equalsIgnoreCase("200"))
        {
            assignData(response);
        }
    }

    private void assignData(String response) {

        ArrayList<ListProductsModel> arrayList=new ArrayList<>();

        try {
            JSONObject jsonObject=new JSONObject(response);
           int status= jsonObject.optInt("status");
           if (status==1)
           {
             JSONArray ja= jsonObject.getJSONArray("response");
             for (int i=0;i<ja.length();i++)
             {
               JSONObject json=  ja.getJSONObject(i);
                 String ProductId= json.optString("ProductId");
                 String ProductName= json.optString("ProductName");
                 String Price= json.optString("Price");
                 String Description= json.optString("Description");
                 String ProductTypeID= json.optString("ProductTypeID");
                 String ProductType= json.optString("ProductType");
                 ListProductsModel productsModel=new ListProductsModel(ProductId,ProductName,Price,Description,ProductTypeID,ProductType);
                 arrayList.add(productsModel);

             }
               ListProductsAdapter adapter=new ListProductsAdapter(this,arrayList,this,cycleId);
             rv_supplproducts.setAdapter(adapter);
           }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveFailure(String error) {
        progressLayout.setVisibility(View.GONE);
        Log.e("error",error);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
       MenuInflater menuInflater= getMenuInflater();
       menuInflater.inflate(R.menu.cart,menu);
        final MenuItem menuItem = menu.findItem(R.id.cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
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
    public void onEvent(String productId,String ProductTypeID) {
        if (productId!=null&&ProductTypeID!=null)
        {
            saveData(productId,ProductTypeID);
        }
    }

    private void saveData(String productId, String productTypeID)
    {
       String orderId= sharedPrefs.getOrderID().get(SharedPrefs.KEY_ORDER_ID);
        progressLayout.setVisibility(View.VISIBLE);
        JSONObject js=new JSONObject();
        try {
            js.put("OrderId",orderId);
            js.put("ContactId",String.valueOf(user_id));
            JSONArray jsonArray=new JSONArray();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("ProductId",productId);
            jsonObject.put("ProductTypeId",productTypeID);
            jsonObject.put("Quantity","1");
            jsonArray.put(jsonObject);
            js.put("ProductList",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody=js.toString();



        RequestQueue requestQueue= Volley.newRequestQueue(this);
      //  RequestQueue requestQueue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiClient.API_BASE_URL + "api/Checkout/AddOrderItem", new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                progressLayout.setVisibility(View.GONE);
                if (response!=null) {
                    try {
                        JSONObject js = new JSONObject(response);
                       String orderId= js.optString("orderId");
                        sharedPrefs.setOrderId(orderId);
                        getCount(orderId);
                        Toast.makeText(SupplemenMeterialsListActivity.this, ""+js.optString("response"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.e("Response_add",response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response_add",""+error);
                progressLayout.setVisibility(View.GONE);
            }
        })
        {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getCount(String orderId)
    {
        progressLayout.setVisibility(View.VISIBLE);
        RequestQueue requestQueue=Volley.newRequestQueue(this);
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
    protected void onRestart() {
        super.onRestart();
        getCount(new SharedPrefs(this).getOrderID().get(SharedPrefs.KEY_ORDER_ID));
    }


}

