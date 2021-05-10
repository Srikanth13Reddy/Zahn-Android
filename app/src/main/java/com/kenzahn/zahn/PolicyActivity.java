package com.kenzahn.zahn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kenzahn.zahn.interfaces.SaveView;
import com.kenzahn.zahn.rest.ApiClient;
import com.kenzahn.zahn.rest.SaveImpl;
import com.kenzahn.zahn.utils.SharedPrefs;
import com.kenzahn.zahn.widget.TypeFaceTextView;
import com.kenzahn.zahn.widget.TypeFaceTextViewBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PolicyActivity extends AppCompatActivity implements SaveView
{

    TypeFaceTextViewBold tv_title,tv_accept;
    TypeFaceTextView tv_des;
  //  private View progressLayout;
    SharedPrefs sharedPrefs;
    String order_id;
    private String StudentPolicyID;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        getSupportActionBar().setTitle("Policy");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pd=new ProgressDialog(this);
        pd.setMessage("Please wait----");
        sharedPrefs=new SharedPrefs(this);
        order_id=sharedPrefs.getOrderID().get(SharedPrefs.KEY_ORDER_ID);
      //  progressLayout =findViewById(R.id.progressLayout);
        tv_title=findViewById(R.id.p_name);
        tv_accept=findViewById(R.id.tv_p_accept);
        tv_des=findViewById(R.id.tv_des);
        getData();
    }

    private void getData()
    {
           pd.show();
       //  pd.show();
        // RequestQueue queue = SingletonRequestQueue.getInstance(this).getRequestQueue();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, ApiClient.API_BASE_URL + "api/Checkout/DisplayPolicy", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();


                if (response!=null)
                {
                    try {
                        JSONObject js=new JSONObject(response);
                       int code= js.optInt("status");
                       if (code==1)
                       {

                         JSONArray ja= js.getJSONArray("response");
                         if (ja.length()>0)
                         {


                             JSONObject json=  ja.getJSONObject(0);
                             StudentPolicyID= json.optString("StudentPolicyID");

                             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                                 tv_title.setText(Html.fromHtml(json.optString("PolicyName"), Html.FROM_HTML_MODE_COMPACT));
                                 tv_des.setText(Html.fromHtml(json.optString("PolicyDescription"), Html.FROM_HTML_MODE_COMPACT));
                                // tv_accept.setText(Html.fromHtml(json.optString("PolicyName"), Html.FROM_HTML_MODE_COMPACT));

                                 //textView.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>", Html.FROM_HTML_MODE_COMPACT));
                             } else {

                                 tv_title.setText(Html.fromHtml(json.optString("PolicyName")));
                                 tv_des.setText(Html.fromHtml(json.optString("PolicyDescription")));
                                // tv_accept.setText(Html.fromHtml(json.optString("PolicyName")));

                                // textView.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"));
                             }



                         }
                       }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.e("Res",response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pd.dismiss();
                Log.e("Res",""+error);
                Toast.makeText(PolicyActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void acceptPolicy(View view)
    {
         pd.show();
        JSONObject js=new JSONObject();
        try {
            js.put("OrderId",order_id);
            JSONArray ja=new JSONArray();
            ja.put(StudentPolicyID);
            js.put("StudentPolicyID",ja);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SaveImpl(this).handleSave(js,"api/Checkout/AcceptPolicy","POST");
    }

    @Override
    public void onSaveSucess(String code, String response)
    {
        pd.dismiss();
        if (code.equalsIgnoreCase("200"))
        {
            try {
                JSONObject js=new JSONObject(response);
                Toast.makeText(this, ""+js.optString("response"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent i=new Intent(this,CheckOutActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onSaveFailure(String error) {
        pd.dismiss();
        Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
