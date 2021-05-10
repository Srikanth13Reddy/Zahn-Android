package com.kenzahn.zahn.model;

/**
 * Created by suresh on 10/7/2018.
 */


public class MenuSubItem {

    private String mName;
    private Integer mIcon;

    public Integer getmIcon() {
        return mIcon;
    }

    public MenuSubItem(String name, Integer image) {
        mName = name;
        mIcon = image;
    }

    public String getName() {
        return mName;
    }
}