package CRM_APP.Database.Project;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Model.Module;

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
    public void insertModule(Module module){
        String query = "INSERT INTO " + Const.MODULE_TABLE +" ("
                + Const.MODULE_ID +", "
                + Const.MODULE_NAME +", "
                + Const.MODULE_STATUS +", "
                + Const.PROJECT_ID+" )"
                + " VALUES(?,?,?,?)";
        try {
            PreparedStatement preparedStatement =  Database.dbConnection.prepareStatement(query);
            preparedStatement.setString(1, module.getModuleID());
            preparedStatement.setString(2, module.getModName());
            preparedStatement.setString(3, module.getStatus());
            preparedStatement.setString(4, module.getProjectID());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    public void updateModule(Module module){
        String query = "UPDATE " + Const.MODULE_TABLE + " SET "
                + Const.MODULE_NAME + " =?, " + Const.MODULE_STATUS + " =?"
                + " WHERE " + Const.MODULE_ID + " =?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = Database.dbConnection.prepareStatement(query);
            preparedStatement.setString(1, module.getModName());
            preparedStatement.setString(2, module.getStatus());
            preparedStatement.setString(3, module.getModuleID());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
