package com.kenzahn.zahn.model;

public class CardContentRes2
{
    private String ExamQuestionID;
    private String CreateDate;
    private String AnswerA;
    private String AnswerB;
    private String AnswerC;
    private String AnswerD;
    private String AnswerE;
    private String AnswerF;
    private String AnswerI;
    private String AnswerII;
    private String AnswerIII;
    private String AnswerIV;
    private String AnswerV;
    private String AnswerVI;
    private String ExamID;
    private String CorrectAnswer;
    private String Explanation;
    private String Question;
    private String ExamCaseID;
    private String SortOrder;
    private String ExamSectionID;
    private String PageBreak;
    private String PageBreak2;
    private String ExamSection;
    private int isKnownReadCountvar=1;
    private Boolean isKnownContent;
    private String OriginalCardOrder;
    private String SelectedAnswer;

    public String getSelectedAnswer() {
        return SelectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        SelectedAnswer = selectedAnswer;
    }

    public String getOriginalCardOrder() {
        return OriginalCardOrder;
    }

    public void setOriginalCardOrder(String originalCardOrder) {
        OriginalCardOrder = originalCardOrder;
    }

    public String getExamQuestionID() {
        return ExamQuestionID;
    }

    public void setExamQuestionID(String examQuestionID) {
        ExamQuestionID = examQuestionID;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getAnswerA() {
        return AnswerA;
    }

    public void setAnswerA(String answerA) {
        AnswerA = answerA;
    }

    public String getAnswerB() {
        return AnswerB;
    }

    public void setAnswerB(String answerB) {
        AnswerB = answerB;
    }

    public String getAnswerC() {
        return AnswerC;
    }

    public void setAnswerC(String answerC) {
        AnswerC = answerC;
    }

    public String getAnswerD() {
        return AnswerD;
    }

    public void setAnswerD(String answerD) {
        AnswerD = answerD;
    }

    public String getAnswerE() {
        return AnswerE;
    }

    public void setAnswerE(String answerE) {
        AnswerE = answerE;
    }

    public String getAnswerF() {
        return AnswerF;
    }

    public void setAnswerF(String answerF) {
        AnswerF = answerF;
    }

    public String getAnswerI() {
        return AnswerI;
    }

    public void setAnswerI(String answerI) {
        AnswerI = answerI;
    }

    public String getAnswerII() {
        return AnswerII;
    }

    public void setAnswerII(String answerII) {
        AnswerII = answerII;
    }

    public String getAnswerIII() {
        return AnswerIII;
    }

    public void setAnswerIII(String answerIII) {
        AnswerIII = answerIII;
    }

    public String getAnswerIV() {
        return AnswerIV;
    }

    public void setAnswerIV(String answerIV) {
        AnswerIV = answerIV;
    }

    public String getAnswerV() {
        return AnswerV;
    }

    public void setAnswerV(String answerV) {
        AnswerV = answerV;
    }

    public String getAnswerVI() {
        return AnswerVI;
    }

    public void setAnswerVI(String answerVI) {
        AnswerVI = answerVI;
    }

    public String getExamID() {
        return ExamID;
    }

    public void setExamID(String examID) {
        ExamID = examID;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        CorrectAnswer = correctAnswer;
    }

    public String getExplanation() {
        return Explanation;
    }

    public void setExplanation(String explanation) {
        Explanation = explanation;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getExamCaseID() {
        return ExamCaseID;
    }

    public void setExamCaseID(String examCaseID) {
        ExamCaseID = examCaseID;
    }

    public String getSortOrder() {
        return SortOrder;
    }

    public void setSortOrder(String sortOrder) {
        SortOrder = sortOrder;
    }

    public String getExamSectionID() {
        return ExamSectionID;
    }

    public void setExamSectionID(String examSectionID) {
        ExamSectionID = examSectionID;
    }

    public String getPageBreak() {
        return PageBreak;
    }

    public void setPageBreak(String pageBreak) {
        PageBreak = pageBreak;
    }

    public String getPageBreak2() {
        return PageBreak2;
    }

    public void setPageBreak2(String pageBreak2) {
        PageBreak2 = pageBreak2;
    }

    public String getExamSection() {
        return ExamSection;
    }

    public void setExamSection(String examSection) {
        ExamSection = examSection;
    }

    public int getIsKnownReadCountvar() {
        return isKnownReadCountvar;
    }

    public void setIsKnownReadCountvar(int isKnownReadCountvar) {
        this.isKnownReadCountvar = isKnownReadCountvar;
    }

    public Boolean getKnownContent()
    {
        return isKnownContent;
    }

    public void setKnownContent(Boolean knownContent) {
        isKnownContent = knownContent;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ExamSectionID = " + ExamSectionID + ", ExamSection = " + ExamSection + ", ExamID = " + ExamID + ", Question = " + Question + ", SortOrder = " + SortOrder + ", Explanation = " + Explanation + ", CorrectAnswer = " + CorrectAnswer + ", AnswerA = " + AnswerA + ", CreateDate = " + CreateDate + "]";
    }
}
