package CRM_APP.Model;

public class Question {
    private String questionId;
    private String questionName;
    private String surID;
    private String typeID;

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

    public String getSurID() {
        return surID;
    }

    public void setSurID(String surID) {
        this.surID = surID;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }
}
