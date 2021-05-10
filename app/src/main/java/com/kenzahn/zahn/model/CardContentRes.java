package com.kenzahn.zahn.model;

public class CardContentRes {
    private String ExamSectionID;

    private String ExamSection;

    private String FlashCardSetID;

    private String FlashCardID;

    private String SortOrder;

    private String FlashCardFront;

    private String FlashCardName;

    private String FlashCardBack;

    private String CreateDate;
    private int isKnownReadCountvar=1;
    private Boolean isKnownContent;

    public Boolean getKnownContent() {
        return isKnownContent;
    }

    public void setKnownContent(Boolean knownContent) {
        isKnownContent = knownContent;
    }

    private String OriginalCardOrder;

    public int getIsKnownReadCountvar() {
        return isKnownReadCountvar;
    }

    public void setIsKnownReadCountvar(int isKnownReadCountvar) {
        this.isKnownReadCountvar = isKnownReadCountvar;
    }

    public String getOriginalCardOrder() {
        return OriginalCardOrder;
    }

    public void setOriginalCardOrder(String originalCardOrder) {
        OriginalCardOrder = originalCardOrder;
    }

    public String getExamSectionID() {
        return ExamSectionID;
    }

    public void setExamSectionID(String ExamSectionID) {
        this.ExamSectionID = ExamSectionID;
    }

    public String getExamSection() {
        return ExamSection;
    }

    public void setExamSection(String ExamSection) {
        this.ExamSection = ExamSection;
    }

    public String getFlashCardSetID() {
        return FlashCardSetID;
    }

    public void setFlashCardSetID(String FlashCardSetID) {
        this.FlashCardSetID = FlashCardSetID;
    }

    public String getFlashCardID() {
        return FlashCardID;
    }

    public void setFlashCardID(String FlashCardID) {
        this.FlashCardID = FlashCardID;
    }

    public String getSortOrder() {
        return SortOrder;
    }

    public void setSortOrder(String SortOrder) {
        this.SortOrder = SortOrder;
    }

    public String getFlashCardFront() {
        return FlashCardFront;
    }

    public void setFlashCardFront(String FlashCardFront) {
        this.FlashCardFront = FlashCardFront;
    }

    public String getFlashCardName() {
        return FlashCardName;
    }

    public void setFlashCardName(String FlashCardName) {
        this.FlashCardName = FlashCardName;
    }

    public String getFlashCardBack() {
        return FlashCardBack;
    }

    public void setFlashCardBack(String FlashCardBack) {
        this.FlashCardBack = FlashCardBack;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String CreateDate) {
        this.CreateDate = CreateDate;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ExamSectionID = " + ExamSectionID + ", ExamSection = " + ExamSection + ", FlashCardSetID = " + FlashCardSetID + ", FlashCardID = " + FlashCardID + ", SortOrder = " + SortOrder + ", FlashCardFront = " + FlashCardFront + ", FlashCardName = " + FlashCardName + ", FlashCardBack = " + FlashCardBack + ", CreateDate = " + CreateDate + "]";
    }
}
