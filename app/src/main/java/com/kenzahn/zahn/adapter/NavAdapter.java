package com.kenzahn.zahn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenzahn.zahn.R;


public class NavAdapter extends BaseAdapter
{
    String names[]={"All","Unread","In-Progress","Completed"};
    int [] images={R.mipmap.all_deck,R.mipmap.unread_deck,R.mipmap.inprogress,R.mipmap.completed_deck};
    Context context;

    public NavAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
       LayoutInflater li= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v= null;
        if (li != null) {
            v = li.inflate(R.layout.navstyle,parent,false);
            ImageView iv= v.findViewById(R.id.nav_iv);
            TextView tvv= v.findViewById(R.id.nav_text);
            iv.setImageResource(images[position]);
            tvv.setText(names[position]);
        }
        return v;
    }
}
