package CRM_APP.Model;

import javafx.scene.control.CheckBox;

public class SurveyDetail {
    private String surveyID, questionID, empID, result;

    public SurveyDetail(String surveyID, String questionID, String empID, String result, String value) {
        this.surveyID = surveyID;
        this.questionID = questionID;
        this.empID = empID;
        this.result = result;
    }

    public SurveyDetail() {
    }



    public String getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(String surveyID) {
        this.surveyID = surveyID;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
