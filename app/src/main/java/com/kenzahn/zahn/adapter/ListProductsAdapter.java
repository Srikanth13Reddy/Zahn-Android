package com.kenzahn.zahn.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kenzahn.zahn.R;
import com.kenzahn.zahn.model.ListProductsModel;
import com.kenzahn.zahn.rest.ApiClient;
import com.kenzahn.zahn.widget.TypeFaceTextView;
import com.kenzahn.zahn.widget.TypeFaceTextViewBold;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListProductsAdapter extends RecyclerView.Adapter<ListProductsAdapter.MyHolder>
{
    Context context;
    ProgressDialog pd;
    ArrayList<ListProductsModel> al;
    private AlertDialog ad;
    EventListener eventListener;
    String cycleID;

    public interface EventListener
    {
        void onEvent(String productId,String ProductTypeID);

    }

    public ListProductsAdapter(Context context, ArrayList<ListProductsModel> al, EventListener listener, String cycleId) {
        this.context = context;
        this.al = al;
        this.eventListener=listener;
        this.cycleID=cycleId;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=layoutInflater.inflate(R.layout.products_style,parent,false);
        return new MyHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position)
    {
        if (al.get(position)!=null)
        {
            holder.tv_name.setText(al.get(position).getProductName());
            holder.tv_cost.setText("$ "+al.get(position).getPrice());
            holder.btn_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showDailog(position);
                    getData(position);
                }
            });

            holder.add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onEvent(al.get(position).getProductId(),al.get(position).getProductTypeID());
                }
            });
        }
    }

    private void getData(int position)
    {
         pd=new ProgressDialog(context);
         pd.setMessage("Please wait.....");
         pd.show();
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, ApiClient.API_BASE_URL + "api/Checkout/GetProductTypeDescription?cycleID=" + cycleID + "&productTypeID=" + al.get(position).getProductTypeID(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                pd.dismiss();
                Log.e("Response",response);
                try {
                    JSONObject js=new JSONObject(response);
                   JSONArray ja= js.getJSONArray("response");
                    ArrayList<String> list = new ArrayList<String>();
                   // String[] str = new String[ja.length()];
                    for (int i=0;i<ja.length();i++)
                   {
                       list.add("<li>"+ja.get(i).toString()+"</li>");
                   }

                    StringBuilder builder = new StringBuilder();
                    for (String value : list) {
                        builder.append(value).append("<br>");
                    }
                    String text = builder.toString();

                    showDailog(position, text);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response",""+error);
                pd.dismiss();
                Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    @SuppressLint("SetTextI18n")
    private void showDailog(int position, String response)
    {
        AlertDialog.Builder alb=new AlertDialog.Builder(context);
       LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View v=layoutInflater.inflate(R.layout.quick_info_style,null,false);
        TypeFaceTextViewBold tv_title=v.findViewById(R.id.qtitle);
        TypeFaceTextView tv_qprice= v.findViewById(R.id.qprice);
        TypeFaceTextView tv_des= v.findViewById(R.id.tv_qdes);
        TypeFaceTextView tv_dess= v.findViewById(R.id.tv_qdess);
       ImageView iv_close= v.findViewById(R.id.iv_close);
       tv_title.setText(al.get(position).getProductName());
        tv_dess.setText(al.get(position).getDescription());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            tv_des.setText(Html.fromHtml(response, Html.FROM_HTML_MODE_COMPACT));
           // tv_des.setText(Html.fromHtml(response.replaceAll("^\"|\"$", ""), Html.FROM_HTML_MODE_COMPACT));
           // tv_des.setText(Html.fromHtml(json.optString("PolicyDescription"), Html.FROM_HTML_MODE_COMPACT));
            // tv_accept.setText(Html.fromHtml(json.optString("PolicyName"), Html.FROM_HTML_MODE_COMPACT));

            //textView.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>", Html.FROM_HTML_MODE_COMPACT));
        } else {

           // tv_title.setText(Html.fromHtml(json.optString("PolicyName")));
           // tv_des.setText(Html.fromHtml(response.replaceAll("^\"|\"$", "")));
            tv_des.setText(Html.fromHtml(response));
            // tv_accept.setText(Html.fromHtml(json.optString("PolicyName")));

            // textView.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"));
        }


       tv_qprice.setText("$ "+al.get(position).getPrice());
       iv_close.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {
               ad.cancel();
           }
       });
       alb.setView(v);
       ad=alb.create();
       ad.show();
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {
        TypeFaceTextView tv_name,tv_cost;
        Button add_cart,btn_info;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_pname);
            tv_cost = itemView.findViewById(R.id.tv_pcost);
            add_cart = itemView.findViewById(R.id.btn_addcart);
            btn_info = itemView.findViewById(R.id.btn_info);
        }
    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
}
