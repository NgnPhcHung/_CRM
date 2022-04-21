package CRM_APP.Database.Survey;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Model.Survey;
import CRM_APP.Model.SurveyDetail;
import CRM_APP.Model.SurveyType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SurveyDB {
    private Database db;

    public ResultSet getSurveyType() throws SQLException, ClassNotFoundException {
        db = new Database();
        ResultSet resultSet = null;

        String query = "SELECT * FROM " + Const.SURVEY_TABLE;
        PreparedStatement preparedStatement = db.getDbConnection().prepareStatement(query);
        resultSet = preparedStatement.executeQuery();

        return resultSet;
    }

    public void save(Survey survey) {
        try {
            db = new Database();
            String query = "INSERT INTO " + Const.SURVEY_TABLE + "( "
                    + Const.SURVEY_ID + ", "
                    + Const.CUSTOMER_ID + ", "
                    + Const.SURVEY_NAME + " ) VALUES  (?,?,?)";
            PreparedStatement preparedStatement = db.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, survey.getSurveyID());
            preparedStatement.setString(2, survey.getCusID());
            preparedStatement.setString(3, survey.getSurveyName());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void saveDetail(SurveyDetail surveyDetail){
        try {
            db = new Database();
            String query = "INSERT INTO " + Const.SURVEY_DETAIL_TABLE + "( "
                    + Const.SURVEY_ID + ", "
                    + Const.EMPLOYEE_ID + ", "
                    + Const.QUESTION_ID + " ) VALUES  (?,?,?)";
            PreparedStatement preparedStatement = db.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, surveyDetail.getSurveyID());
            preparedStatement.setString(2, surveyDetail.getEmpID());
            preparedStatement.setString(3, surveyDetail.getQuestionID());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void update(Survey survey) {
        db = new Database();
        String query = " UPDATE " + Const.SURVEY_TABLE + " SET "
                + Const.CUSTOMER_ID + " =?, "
                + Const.SURVEY_NAME + "=? WHERE " + Const.SURVEY_ID + "=?";
        try {
            PreparedStatement preparedStatement = db.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, survey.getCusID());
            preparedStatement.setString(2, survey.getSurveyName());
            preparedStatement.setString(3, survey.getSurveyID());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

}
