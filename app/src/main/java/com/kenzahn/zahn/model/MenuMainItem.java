package com.kenzahn.zahn.model;

/**
 * Created by suresh on 10/7/2018.
 */



import com.kenzahn.zahn.expandview.ParentListItem;

import java.util.List;

public class MenuMainItem implements ParentListItem {
    private String mName;
    private Integer mIcon;
    private List<MenuSubItem> menuSubItems;

    public MenuMainItem(Integer icon, String name, List<MenuSubItem> subItems) {
        mName = name;
        menuSubItems = subItems;
        mIcon = icon;
    }

    public String getName() {
        return mName;
    }

    public Integer getIcon() {
        return mIcon;
    }

    @Override
    public List<?> getChildItemList() {
        return menuSubItems;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}