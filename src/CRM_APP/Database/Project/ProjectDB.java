package CRM_APP.Database.Project;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectDB {
    private Database  database = new Database();
    public ResultSet getProjectList() throws SQLException, ClassNotFoundException {
        database = new Database();
        ResultSet resultSet = null;

        String query = "SELECT * FROM " + Const.PROJECT_TABLE ;
        PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
        resultSet = preparedStatement.executeQuery();

        return resultSet;
    }
}
