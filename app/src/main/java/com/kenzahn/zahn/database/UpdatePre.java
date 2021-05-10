package com.kenzahn.zahn.database;

import java.util.ArrayList;

public class UpdatePre
{
    private ArrayList<PreferencesJson> PreferencesJson = null;
    private int UserID = 0;

    public ArrayList<com.kenzahn.zahn.database.PreferencesJson> getPreferencesJson() {
        return PreferencesJson;
    }

    public void setPreferencesJson(ArrayList<com.kenzahn.zahn.database.PreferencesJson> preferencesJson) {
        PreferencesJson = preferencesJson;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }
}
