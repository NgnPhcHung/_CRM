package CRM_APP.Database.Survey;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Model.Employee;
import CRM_APP.Model.Survey;
import CRM_APP.Model.SurveyType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SurveyTypeDB {
    private Database db;

    public ResultSet getSurveyType() throws SQLException, ClassNotFoundException {
        db = new Database();
        ResultSet resultSet = null;

        String query = "SELECT * FROM " + Const.SURVEY_TYPE_TABLE;
        PreparedStatement preparedStatement = db.getDbConnection().prepareStatement(query);
        resultSet = preparedStatement.executeQuery();

        return resultSet;
    }

    public void saveSurveyType(SurveyType surveyType) {
        try {
            db = new Database();
            String query = "INSERT INTO " + Const.SURVEY_TYPE_TABLE + "( "
                    + Const.SURVEYTYPE_ID + ", "
                    + Const.SURVEYTYPE_NAME + ", "
                    + Const.SURVEYTYPE_DES + " ) VALUES  (?,?,?)";
            PreparedStatement preparedStatement = db.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, surveyType.getSurID());
            preparedStatement.setString(2, surveyType.getSurName());
            preparedStatement.setString(3, surveyType.getDes());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void update(SurveyType surveyType) {
        db = new Database();
        String query = " UPDATE " + Const.SURVEY_TYPE_TABLE + " SET "
                + Const.SURVEYTYPE_NAME + " =?, "
                + Const.SURVEYTYPE_DES + "=? WHERE " + Const.SURVEYTYPE_ID + "=?";
        try {
            PreparedStatement preparedStatement = db.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, surveyType.getSurName());
            preparedStatement.setString(2, surveyType.getDes());
            preparedStatement.setString(3, surveyType.getSurID());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public ResultSet countQuestion(SurveyType surveyType) throws SQLException {
        ResultSet resultSet = null;
        String  query= "SELECT COUNT(*) FROM " + Const.QUESTION_TABLE + " WHERE "+ Const.SURVEY_TYPE_ID + "=?";
        PreparedStatement preparedStatement = Database.dbConnection.prepareStatement(query);
        preparedStatement.setString(1, surveyType.getSurID());

        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
}
