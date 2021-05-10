package com.kenzahn.zahn.model;

import java.util.ArrayList;

public class FlashcardJsonList2
{
    private String ExamID;

    private String ExamTypeID;

    private String Randomized;

    private String Timed;

    private String ExamHeader;

    private ArrayList<CardContentRes2> CardContent;
    private String QuestionCount;

    private String deckSortOrder;

    private String ExamName;

    private String Version;
    private String Active;
    private String ExamModuleID;
    private int CompletedCards;
    private String Status;

    private String OriginalDeckOrder;

    public String getOriginalDeckOrder() {
        return OriginalDeckOrder;
    }

    public void setOriginalDeckOrder(String originalDeckOrder) {
        OriginalDeckOrder = originalDeckOrder;
    }

    public String getDeckSortOrder() {
        return deckSortOrder;
    }

    public void setDeckSortOrder(String deckSortOrder) {
        this.deckSortOrder = deckSortOrder;
    }

    public String getExamID() {
        return ExamID;
    }

    public void setExamID(String examID) {
        ExamID = examID;
    }

    public String getExamTypeID() {
        return ExamTypeID;
    }

    public void setExamTypeID(String examTypeID) {
        ExamTypeID = examTypeID;
    }

    public String getRandomized() {
        return Randomized;
    }

    public void setRandomized(String randomized) {
        Randomized = randomized;
    }

    public String getTimed() {
        return Timed;
    }

    public void setTimed(String timed) {
        Timed = timed;
    }

    public String getExamHeader() {
        return ExamHeader;
    }

    public void setExamHeader(String examType) {
        ExamHeader = examType;
    }

    public ArrayList<CardContentRes2> getCardContent() {
        return CardContent;
    }

    public void setCardContent(ArrayList<CardContentRes2> cardContent) {
        CardContent = cardContent;
    }

    public String getQuestionCount() {
        return QuestionCount;
    }

    public void setQuestionCount(String questionCount) {
        QuestionCount = questionCount;
    }


    public String getExamName() {
        return ExamName;
    }

    public void setExamName(String examName) {
        ExamName = examName;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getExamModuleID() {
        return ExamModuleID;
    }

    public void setExamModuleID(String examModuleID) {
        ExamModuleID = examModuleID;
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

    @Override
    public String toString()
    {
        return "ClassPojo [ExamID = "+ExamID+", ExamTypeID = "+ExamTypeID+", Randomized = "+Randomized+", Timed = "+Timed+", ExamType = "+ExamHeader+", CardContent = "+CardContent+", QuestionCount = "+QuestionCount+", ExamName = "+ExamName+", ExamModuleID = "+ExamModuleID+", Active = "+Active+", Version = "+Version+"]";
    }
}
