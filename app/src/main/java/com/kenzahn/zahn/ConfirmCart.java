package com.kenzahn.zahn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
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

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class ConfirmCart extends AppCompatActivity implements SaveView,CartAdapter.EventListener
{
    private static final int PAYPAL_REQUEST_CODE = 1234;
    RecyclerView rv_cart_items;
    String orderID;
    SharedPrefs sharedPrefs;
    private View progressLayout;
    TypeFaceTextView tv_subtotal,tv_total,tv_shipping;
    double total;

    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId(ApiClient.PAYPAL_CLIENT_ID);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cart");
        rv_cart_items= findViewById(R.id.rv_cart);
        tv_subtotal= findViewById(R.id.tv_subTotal);
        tv_shipping= findViewById(R.id.tv_shipping);
        tv_total= findViewById(R.id.tv_total);
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



    @SuppressLint("SetTextI18n")
    private void assignData(String response)
    {
        total=0;
        ArrayList<CartModel> al=new ArrayList<>();
        try {
            JSONObject js=new JSONObject(response);
            int code= js.optInt("status");
            if (code==1)
            {
                JSONArray ja= js.getJSONArray("orderItemList");

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
                    total+=(Integer.parseInt(al.get(i).getQuantity()))*(Double.parseDouble(al.get(i).getPrice()));
                }
                total=Double.parseDouble(js.optString("total").replace("$",""));
                tv_subtotal.setText(""+js.optString("subTotal"));
                tv_total.setText(""+js.optString("total"));
                tv_shipping.setText(""+js.optString("shipping"));
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
        RequestQueue requestQueue= Volley.newRequestQueue(this);
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
                Toast.makeText(ConfirmCart.this, ""+error, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ConfirmCart.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }


    @Override
    public void onSaveFailure(String error)
    {
        progressLayout.setVisibility(View.GONE);
        Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
    }

    public void nextPayment(View view)
    {
        getPayment();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    //Getting the payment details
                    String paymentDetails = confirm.toJSONObject().toString();
                    Log.i("paymentExample", paymentDetails);
                    try {
                        JSONObject js=new JSONObject(paymentDetails);
                       String pid= js.getJSONObject("response").optString("id");
                       completeOrder(pid);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Starting a new activity for the payment details and also putting the payment details with intent
                    startActivity(new Intent(this, ConfirmationActivity.class)
                            .putExtra("PaymentDetails", paymentDetails)
                            .putExtra("sid", orderID)
                            .putExtra("PaymentAmount",String.valueOf(total)));

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }


    private void getPayment()
    {
        //Getting the amount from editText

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(total)), "USD", "Zahn",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, com.paypal.android.sdk.payments.PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);

        //Puting paypal payment to the intent
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    void completeOrder(String pId)
    {

        final ProgressDialog pd= new ProgressDialog(this);
        pd.setMessage("Please wait....");
        pd.setCancelable(false);
        pd.show();
        JSONObject js=new JSONObject();
        try {
            js.put("OrderId",orderID);
            js.put("Pnref",pId);
            js.put("Authcode","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody=js.toString();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ApiClient.API_BASE_URL + "api/Checkout/CompleteOrder", new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                pd.dismiss();
                Log.e("responsePayment",response);
                try {
                    JSONObject js=new JSONObject(response);
                   int code= js.optInt("status");
                   if (code==1)
                   {
                       sharedPrefs.setOrderId("0");
                       Toast.makeText(ConfirmCart.this, ""+js.optString("response"), Toast.LENGTH_SHORT).show();
                       finish();
                       startActivity(new Intent(ConfirmCart.this, ThankYouActivity.class));
                   }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Log.e("responsePayment",""+error);
                Toast.makeText(ConfirmCart.this, ""+error, Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }


}
