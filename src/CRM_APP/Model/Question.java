package CRM_APP.Model;

public class Question {
    private String questionId;
    private String questionName;
    private String dateAdd;
    private String surID;

    public String getSurID() {
        return surID;
    }

    public void setSurID(String surID) {
        this.surID = surID;
    }

    public String getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(String dateAdd) {
        this.dateAdd = dateAdd;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }
}
