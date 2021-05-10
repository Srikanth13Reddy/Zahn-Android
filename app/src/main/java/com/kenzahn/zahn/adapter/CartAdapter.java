package com.kenzahn.zahn.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kenzahn.zahn.R;
import com.kenzahn.zahn.model.CartModel;
import com.kenzahn.zahn.widget.TypeFaceTextView;
import com.kenzahn.zahn.widget.TypeFaceTextViewBold;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyHolder>
{
    ArrayList<CartModel> al;
    Context context;
    EventListener eventListener;
    public interface EventListener
    {
        void updateItem(String orderItemId,int qty);
        void deleteItem(String orderItemId);
        void removeItem(String orderItemId,int qty);
    }

    public CartAdapter(ArrayList<CartModel> al, Context context,EventListener eventListener) {
        this.al = al;
        this.context = context;
        this.eventListener=eventListener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
       LayoutInflater li= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      View v= li.inflate(R.layout.cart_style,parent,false);
        return new MyHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position)
    {
        if (al.get(position)!=null)
        {
          CartModel obj=  al.get(position);
          holder.tv_name.setText(obj.getProductName());
          holder.tv_code.setText("ProductId : "+obj.getProductCode());
          holder.tv_qty.setText(obj.getQuantity()+"*"+obj.getPrice());
          holder.tv_price.setText("$ "+(Integer.parseInt(obj.getQuantity())*(Double.parseDouble(obj.getPrice()))));
          holder.iv_add.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  eventListener.updateItem(obj.getOrderItemID(),Integer.parseInt(obj.getQuantity()));
              }
          });

          holder.iv_minus.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  eventListener.removeItem(obj.getOrderItemID(),Integer.parseInt(obj.getQuantity()));

              }
          });

                  holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          eventListener.deleteItem(obj.getOrderItemID());
                      }
                  });
        }

    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder
   {
       TypeFaceTextView tv_name,tv_code,tv_qty;
       ImageView iv_add,iv_minus,iv_delete;
       TypeFaceTextViewBold tv_price;

       public MyHolder(@NonNull View itemView)
       {
           super(itemView);
           tv_name= itemView.findViewById(R.id.tv_p_name);
           tv_code= itemView.findViewById(R.id.tv_p_code);
           tv_qty= itemView.findViewById(R.id.tv_qty);
           tv_price= itemView.findViewById(R.id.tv_price);
           iv_add= itemView.findViewById(R.id.item_add);
           iv_minus= itemView.findViewById(R.id.item_remove);
           iv_delete= itemView.findViewById(R.id.item_delete);
       }
   }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }



}
