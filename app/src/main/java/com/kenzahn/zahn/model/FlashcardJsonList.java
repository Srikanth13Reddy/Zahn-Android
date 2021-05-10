package com.kenzahn.zahn.model;


import java.util.ArrayList;

public class FlashcardJsonList {
    private String CycleID;

    private String AudioFile;

    private String ExamSectionID;

    private String Active;

    private String ExpiryDate;

    private ArrayList<CardContentRes> CardContent;

    public ArrayList<CardContentRes> getCardContent() {
        return CardContent;
    }

    public void setCardContent(ArrayList<CardContentRes> cardContent) {
        CardContent = cardContent;
    }

    private String FlashCardSetID;

    private String FlashCardName;

    private String TotalNoOfCards;

    private String Audio;

    private String CreateDate;

    private String FlashCardTypeID;
    private int CompletedCards;
    private String Status;
    private String decksortOrder;
    private String OverAllSortOrder;

    public String getOverAllSortOrder() {
        return OverAllSortOrder;
    }

    public void setOverAllSortOrder(String overAllSortOrder) {
        OverAllSortOrder = overAllSortOrder;
    }

    public int getCompletedCards() {
        return CompletedCards;
    }

    public void setCompletedCards(int completedCards) {
        CompletedCards = completedCards;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDecksortOrder() {
        return decksortOrder;
    }

    public void setDecksortOrder(String decksortOrder) {
        this.decksortOrder = decksortOrder;
    }

    public String getOriginalDeckOrder() {
        return OriginalDeckOrder;
    }

    public void setOriginalDeckOrder(String originalDeckOrder) {
        OriginalDeckOrder = originalDeckOrder;
    }

    private String OriginalDeckOrder;

    public String getCycleID ()
    {
        return CycleID;
    }

    public void setCycleID (String CycleID)
    {
        this.CycleID = CycleID;
    }

    public String getAudioFile ()
    {
        return AudioFile;
    }

    public void setAudioFile (String AudioFile)
    {
        this.AudioFile = AudioFile;
    }

    public String getExamSectionID ()
    {
        return ExamSectionID;
    }

    public void setExamSectionID (String ExamSectionID)
    {
        this.ExamSectionID = ExamSectionID;
    }

    public String getActive ()
    {
        return Active;
    }

    public void setActive (String Active)
    {
        this.Active = Active;
    }

    public String getExpiryDate ()
    {
        return ExpiryDate;
    }

    public void setExpiryDate (String ExpiryDate)
    {
        this.ExpiryDate = ExpiryDate;
    }


    public String getFlashCardSetID ()
    {
        return FlashCardSetID;
    }

    public void setFlashCardSetID (String FlashCardSetID)
    {
        this.FlashCardSetID = FlashCardSetID;
    }

    public String getFlashCardName ()
    {
        return FlashCardName;
    }

    public void setFlashCardName (String FlashCardName)
    {
        this.FlashCardName = FlashCardName;
    }

    public String getTotalNoOfCards ()
    {
        return TotalNoOfCards;
    }

    public void setTotalNoOfCards (String TotalNoOfCards)
    {
        this.TotalNoOfCards = TotalNoOfCards;
    }

    public String getAudio ()
    {
        return Audio;
    }

    public void setAudio (String Audio)
    {
        this.Audio = Audio;
    }

    public String getCreateDate ()
    {
        return CreateDate;
    }

    public void setCreateDate (String CreateDate)
    {
        this.CreateDate = CreateDate;
    }

    public String getFlashCardTypeID ()
    {
        return FlashCardTypeID;
    }

    public void setFlashCardTypeID (String FlashCardTypeID)
    {
        this.FlashCardTypeID = FlashCardTypeID;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [CycleID = "+CycleID+", AudioFile = "+AudioFile+", ExamSectionID = "+ExamSectionID+", Active = "+Active+", ExpiryDate = "+ExpiryDate+", CardContent = "+CardContent+", FlashCardSetID = "+FlashCardSetID+", FlashCardName = "+FlashCardName+", TotalNoOfCards = "+TotalNoOfCards+", Audio = "+Audio+", CreateDate = "+CreateDate+", FlashCardTypeID = "+FlashCardTypeID+", OverAllSortOrder = "+OverAllSortOrder+"]";
    }
}
