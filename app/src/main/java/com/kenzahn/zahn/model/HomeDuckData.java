package com.kenzahn.zahn.model;

import java.util.ArrayList;

public class HomeDuckData
{

    private ArrayList<FlashcardJsonList> result;

    private ArrayList<Preference> preference;

    public ArrayList<FlashcardJsonList> getResult() {
        return result;
    }

    public void setResult(ArrayList<FlashcardJsonList> result) {
        this.result = result;
    }

    public ArrayList<Preference> getPreference() {
        return preference;
    }

    public void setPreference(ArrayList<Preference> preference) {
        this.preference = preference;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [result = "+result+", preference = "+preference+"]";
    }

}
