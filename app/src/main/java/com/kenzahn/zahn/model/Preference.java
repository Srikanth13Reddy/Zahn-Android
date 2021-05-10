package com.kenzahn.zahn.model;

import java.util.ArrayList;

public class Preference {
    private ArrayList<PreferencesJsonRes> PreferencesJson;

    public ArrayList<PreferencesJsonRes> getPreferencesJson() {
        return PreferencesJson;
    }

    public void setPreferencesJson(ArrayList<PreferencesJsonRes> preferencesJson) {
        PreferencesJson = preferencesJson;
    }

}
