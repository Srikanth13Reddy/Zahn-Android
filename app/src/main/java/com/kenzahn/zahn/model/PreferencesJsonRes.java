package com.kenzahn.zahn.model;

import java.util.ArrayList;

public class PreferencesJsonRes {
    private String Status;

    private String CompletedCards;

    public ArrayList<CardContentResPreference> getCardContent() {
        return CardContent;
    }

    public void setCardContent(ArrayList<CardContentResPreference> cardContent) {
        CardContent = cardContent;
    }

    private ArrayList<CardContentResPreference> CardContent;

    private String FlashCardSetID;

    private String DecksortOrder;

    private String TotalCardCount;

    public String getStatus ()
    {
        return Status;
    }

    public void setStatus (String Status)
    {
        this.Status = Status;
    }

    public String getCompletedCards ()
    {
        return CompletedCards;
    }

    public void setCompletedCards (String CompletedCards)
    {
        this.CompletedCards = CompletedCards;
    }



    public String getFlashCardSetID ()
    {
        return FlashCardSetID;
    }

    public void setFlashCardSetID (String FlashCardSetID)
    {
        this.FlashCardSetID = FlashCardSetID;
    }

    public String getDecksortOrder ()
    {
        return DecksortOrder;
    }

    public void setDecksortOrder (String DecksortOrder)
    {
        this.DecksortOrder = DecksortOrder;
    }

    public String getTotalCardCount ()
    {
        return TotalCardCount;
    }

    public void setTotalCardCount (String TotalCardCount)
    {
        this.TotalCardCount = TotalCardCount;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Status = "+Status+", CompletedCards = "+CompletedCards+", CardContent = "+CardContent+", FlashCardSetID = "+FlashCardSetID+", DecksortOrder = "+DecksortOrder+", TotalCardCount = "+TotalCardCount+"]";
    }
}
