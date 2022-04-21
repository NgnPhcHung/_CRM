package CRM_APP.Model;

import com.jfoenix.controls.JFXCheckBox;

public class Question {
    private String questionId;
    private String questionName;
    private String surID;
    private String typeID;
    private JFXCheckBox remark;

    public Question() {
        this.remark = new JFXCheckBox();
    }

    public Question(String questionId, String questionName, String surID, String typeID, String value) {
        this.questionId = questionId;
        this.questionName = questionName;
        this.surID = surID;
        this.typeID = typeID;
        this.remark = new JFXCheckBox();
    }

    public JFXCheckBox getRemark() {
        return remark;
    }

    public void setRemark(JFXCheckBox remark) {
        this.remark = remark;
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
