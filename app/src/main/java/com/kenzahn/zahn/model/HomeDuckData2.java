package com.kenzahn.zahn.model;

import java.util.ArrayList;

public class HomeDuckData2
{
    private ArrayList<FlashcardJsonList2> result;

    public ArrayList<FlashcardJsonList2> getResult() {
        return result;
    }

    public void setResult(ArrayList<FlashcardJsonList2> result) {
        this.result = result;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [result = "+result+"]";
    }
}
