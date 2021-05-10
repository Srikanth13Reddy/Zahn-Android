package com.kenzahn.zahn.model;

public class SupplementModel
{
    String CycleId;
    String Title;
    String TestDate;
    String ApplicationDueDate;
    String LastClassDate;
    String LastExamDate;
    String LastTutorDate;

    public SupplementModel(String cycleId, String title, String testDate, String applicationDueDate, String lastClassDate, String lastExamDate, String lastTutorDate) {
        CycleId = cycleId;
        Title = title;
        TestDate = testDate;
        ApplicationDueDate = applicationDueDate;
        LastClassDate = lastClassDate;
        LastExamDate = lastExamDate;
        LastTutorDate = lastTutorDate;
    }

    public String getCycleId() {
        return CycleId;
    }

    public void setCycleId(String cycleId) {
        CycleId = cycleId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTestDate() {
        return TestDate;
    }

    public void setTestDate(String testDate) {
        TestDate = testDate;
    }

    public String getApplicationDueDate() {
        return ApplicationDueDate;
    }

    public void setApplicationDueDate(String applicationDueDate) {
        ApplicationDueDate = applicationDueDate;
    }

    public String getLastClassDate() {
        return LastClassDate;
    }

    public void setLastClassDate(String lastClassDate) {
        LastClassDate = lastClassDate;
    }

    public String getLastExamDate() {
        return LastExamDate;
    }

    public void setLastExamDate(String lastExamDate) {
        LastExamDate = lastExamDate;
    }

    public String getLastTutorDate() {
        return LastTutorDate;
    }

    public void setLastTutorDate(String lastTutorDate) {
        LastTutorDate = lastTutorDate;
    }
}
