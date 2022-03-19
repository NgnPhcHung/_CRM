package CRM_APP.Database.Survey;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Model.Employee;
import CRM_APP.Model.Survey;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SurveyDB {
    private Database db;
    //when user login
    public ResultSet getSurveyType() throws SQLException, ClassNotFoundException {
        db = new Database();
        ResultSet resultSet = null;

        String query = "SELECT * FROM " + Const.SURVEY_TYPE_TABLE ;
        PreparedStatement preparedStatement = db.getDbConnection().prepareStatement(query);
        resultSet = preparedStatement.executeQuery();

        return resultSet;
    }
}
