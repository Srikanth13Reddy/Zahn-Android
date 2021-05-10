package com.kenzahn.zahn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kenzahn.zahn.adapter.CartAdapter;
import com.kenzahn.zahn.interfaces.SaveView;
import com.kenzahn.zahn.model.CartModel;
import com.kenzahn.zahn.rest.ApiClient;
import com.kenzahn.zahn.rest.SaveImpl;
import com.kenzahn.zahn.utils.SharedPrefs;
import com.kenzahn.zahn.widget.TypeFaceTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements SaveView,CartAdapter.EventListener
{
    RecyclerView rv_cart_items;
    String orderID;
    SharedPrefs sharedPrefs;
    private View progressLayout;
    TypeFaceTextView tv_note,tv_empty_note;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cart");
        rv_cart_items= findViewById(R.id.rv_cart);
        tv_note= findViewById(R.id.tv_cart_note);
        tv_empty_note= findViewById(R.id.tv_empty_note);
        ll= findViewById(R.id.ll_btns);
        progressLayout =findViewById(R.id.progressLayout);
        sharedPrefs=new SharedPrefs(this);
        orderID= sharedPrefs.getOrderID().get(SharedPrefs.KEY_ORDER_ID);
        rv_cart_items.setLayoutManager(new LinearLayoutManager(this));
        getCartData(orderID);

    }

    private void getCartData(String orderID)
    {
        progressLayout.setVisibility(View.VISIBLE);
        new SaveImpl(this).handleSave(new JSONObject(),"api/Checkout/LoadCart?orderId="+orderID,"GET");
    }

    @Override
    public void onSaveSucess(String code, String response)
    {
        progressLayout.setVisibility(View.GONE);
        if (code.equalsIgnoreCase("200"))
        {
            assignData(response);
        }
        else {
            Toast.makeText(this, ""+response, Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onSaveFailure(String error)
    {
        progressLayout.setVisibility(View.GONE);
        Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
    }



    private void assignData(String response)
    {
        ArrayList<CartModel> al=new ArrayList<>();
        try {
            JSONObject js=new JSONObject(response);
           int code= js.optInt("status");
           if (code==1)
           {
             JSONArray ja= js.getJSONArray("orderItemList");

             if (ja.length()==0)
             {
                 tv_note.setVisibility(View.GONE);
                 ll.setVisibility(View.GONE);
                 tv_empty_note.setVisibility(View.VISIBLE);
             }
             else {
                 tv_note.setVisibility(View.VISIBLE);
                 ll.setVisibility(View.VISIBLE);
                 tv_empty_note.setVisibility(View.GONE);
             }
             for (int i=0;i<ja.length();i++)
             {
                JSONObject json= ja.getJSONObject(i);
                 String ProductID= json.optString("ProductID");
                 String ProductCode= json.optString("ProductCode");
                 String ProductName= json.optString("ProductName");
                 String Price= json.optString("Price");
                 String OrderItemID= json.optString("OrderItemID");
                 String OrderID= json.optString("OrderID");
                 String Quantity= json.optString("Quantity");
                 String IdentityKeyID= json.optString("IdentityKeyID");
                 String ProductTypeId= json.optString("ProductTypeId");
                 CartModel cm=new CartModel(ProductID,ProductCode,ProductName,Price,OrderItemID,OrderID,Quantity,IdentityKeyID,ProductTypeId);
                 al.add(cm);
             }
             CartAdapter ca=new CartAdapter(al,this,this);
             rv_cart_items.setAdapter(ca);
           }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateItem(String orderItemId,int qty)
    {
        updateCartItem(orderItemId,String.valueOf(qty+1));
    }


    @Override
    public void deleteItem(String orderItemId) {
        deleteItemCart(orderItemId);
    }

    @Override
    public void removeItem(String orderItemId, int qty)
    {
        if (qty>1)
        {
            updateCartItem(orderItemId,String.valueOf(qty-1));
        }
        else {
            deleteItemCart(orderItemId);
        }

    }

    private void deleteItemCart(String orderItemId)
    {
        progressLayout.setVisibility(View.VISIBLE);
        // RequestQueue queue = SingletonRequestQueue.getInstance(this).getRequestQueue();
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.DELETE, ApiClient.API_BASE_URL + "api/Checkout/DeleteOrderItem?orderItemId="+orderItemId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressLayout.setVisibility(View.GONE);
                Log.e("Res",response);

                getCartData(orderID);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressLayout.setVisibility(View.GONE);
                Log.e("Res",""+error);
                Toast.makeText(CartActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }


    private void updateCartItem(String orderItemId,String qty)
    {
        progressLayout.setVisibility(View.VISIBLE);
       // RequestQueue queue = SingletonRequestQueue.getInstance(this).getRequestQueue();
        RequestQueue requestQueue=Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.PUT, ApiClient.API_BASE_URL + "api/Checkout/UpdateOrderItemQuantity?orderItemId=" + orderItemId + "&quantity="+qty, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressLayout.setVisibility(View.GONE);
                Log.e("Res",response);

                getCartData(orderID);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressLayout.setVisibility(View.GONE);
                Log.e("Res",""+error);
                Toast.makeText(CartActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }


    public void continueShopping(View view)
    {
        finish();onBackPressed();
    }

    public void checkOut(View view)
    {
        Intent i=new Intent(this,PolicyActivity.class);
        startActivity(i);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getCartData(new SharedPrefs(this).getOrderID().get(SharedPrefs.KEY_ORDER_ID));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
