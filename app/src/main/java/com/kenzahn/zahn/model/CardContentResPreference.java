package com.kenzahn.zahn.model;

public class CardContentResPreference {
    private Boolean IsKnownContent;

    private String FlashCardSetID;

    private String FlashCardID;

    private String SortOrder;

    public Boolean getIsKnownContent ()
    {
        return IsKnownContent;
    }

    public void setIsKnownContent (Boolean IsKnownContent)
    {
        this.IsKnownContent = IsKnownContent;
    }

    public String getFlashCardSetID ()
    {
        return FlashCardSetID;
    }

    public void setFlashCardSetID (String FlashCardSetID)
    {
        this.FlashCardSetID = FlashCardSetID;
    }

    public String getFlashCardID ()
    {
        return FlashCardID;
    }

    public void setFlashCardID (String FlashCardID)
    {
        this.FlashCardID = FlashCardID;
    }

    public String getSortOrder ()
    {
        return SortOrder;
    }

    public void setSortOrder (String SortOrder)
    {
        this.SortOrder = SortOrder;
    }

}
