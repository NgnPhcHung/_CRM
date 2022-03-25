package CRM_APP.Database.Project;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModuleDB {
    Database database = new Database();

    public ResultSet getCountStatus(String projectID) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = null;
        String query = "SELECT COUNT(CASE WHEN Status = \"0\" THEN 1\n" +
                "                  ELSE NULL\n" +
                "             END) AS Pending\n" +
                "\t\t,COUNT(CASE WHEN Status = \"1\" THEN 1\n" +
                "                   ELSE NULL\n" +
                "              END) AS Working\n" +
                "\t\t,COUNT(CASE WHEN Status = \"2\" THEN 1\n" +
                "                   ELSE NULL\n" +
                "              END) AS Reviewing\n" +
                "\t\t,COUNT(CASE WHEN Status = \"3\" THEN 1\n" +
                "                   ELSE NULL\n" +
                "              END) AS Done\n" +
                "    FROM crm.module WHERE " + Const.PROJECT_ID +"=?";
        PreparedStatement preparedStatement = Database.dbConnection.prepareStatement(query);
        preparedStatement.setString(1, projectID);
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
}
