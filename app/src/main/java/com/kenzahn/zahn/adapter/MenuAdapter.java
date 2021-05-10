package com.kenzahn.zahn.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenzahn.zahn.R;
import com.kenzahn.zahn.expandview.ExpandableRecyclerAdapter;
import com.kenzahn.zahn.expandview.ParentListItem;
import com.kenzahn.zahn.interfaces.AdapterItemClickListener;
import com.kenzahn.zahn.model.MenuMainItem;
import com.kenzahn.zahn.model.MenuSubItem;

import java.util.List;

/**
 * Created by suresh on 4/2/2018.
 */

public class MenuAdapter extends ExpandableRecyclerAdapter<MenuMainViewHolder, MenuSubViewHolder> {

    private AdapterItemClickListener mItemClickListener;
    private List<ParentListItem> parentListItems;
    private LayoutInflater mInflator;

    public MenuAdapter(Context context, List<ParentListItem> parentItemList, AdapterItemClickListener mItemClickListener) {
        super(parentItemList);
        this.mInflator = LayoutInflater.from(context);
        this.mItemClickListener = mItemClickListener;
        this.parentListItems = parentItemList;
    }

    @Override
    public MenuMainViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View movieCategoryView = this.mInflator.inflate(R.layout.menu_list_item, parentViewGroup, false);
        return new MenuMainViewHolder(movieCategoryView);
    }

    @Override
    public MenuSubViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View moviesView = this.mInflator.inflate(R.layout.menu_sublist_item, childViewGroup, false);
        return new MenuSubViewHolder(moviesView);
    }


    @Override
    public void onBindParentViewHolder(MenuMainViewHolder mainViewHolder, final int position, ParentListItem parentListItem) {
        final MenuMainItem mainitem = (MenuMainItem)parentListItem;
        mainViewHolder.bind(mainitem);

        mainViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it) {
                if (mainitem.getChildItemList() == null) {
                    mItemClickListener.onItemClick(position, mainitem.getName(), "", null);
                }else{
                  //  expandParent(position+1);

                }

            }
        });

    }

    @Override
    public void onBindChildViewHolder(MenuSubViewHolder subViewHolder, final int position, Object childListItem) {
        final MenuSubItem subsitem = (MenuSubItem)childListItem;
        subViewHolder.bind(subsitem);
        subViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View it)
            {
                mItemClickListener.onItemClick(position-2, subsitem.getName(), "", null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return parentListItems.size();
    }

}
