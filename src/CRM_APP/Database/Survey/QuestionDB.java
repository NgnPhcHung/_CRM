package CRM_APP.Database.Survey;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Model.Question;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionDB {
    private Database db = new Database();
    //create choice of question a.k.a question details
    public void createQuestionChoice(String id, String qId, String content){

    }
    //question
    public void createQuestion(String id, String surId, String name, String questionTypeId){
        String query = "INSERT INTO " + Const.QUESTION_TABLE +" ("
                + Const.QUESTION_ID + ", "
                + Const.SURVEYTYPE_ID + ", "
                + Const.QUESTION_NAME + ", "
                + Const.QUESTIONTYPE_ID
                +" ) VALUES (?,?,?,?)";
        try {
            PreparedStatement preparedStatement = db.dbConnection.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, surId);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, questionTypeId);
            preparedStatement.executeUpdate();
            db.getDbConnection().close();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
    public void createQuestionDetail(String id, String qId,String ans){
        String query = "INSERT INTO " + Const.QUESTION_DETAIL_TABLE +" ("
                + Const.QUESTIONDETAIL_ID + ", "
                + Const.QUESTION_ID + ", "
                + Const.QUESTIONDETAIL_ANSWER
                +" ) VALUES (?,?,?)";
        try {
            PreparedStatement preparedStatement = db.dbConnection.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, qId);
            preparedStatement.setString(3, ans);
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public ResultSet getValidAnswer(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + Const.QUESTION_DETAIL_TABLE +" WHERE " + Const.QUESTION_ID + " =?";
        PreparedStatement preparedStatement = db.getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, id);
        resultSet = preparedStatement.executeQuery();

        return resultSet;
    }
    public void delete(Question question){

    }

}
