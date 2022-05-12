package CRM_APP.Database.Project;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Model.Module;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModuleDB {
    Database database = new Database();

    public ResultSet getModule(){
        ResultSet rs = null;
        String query = " SELECT * FROM " + Const.MODULE_TABLE + " ORDER BY "
                        + Const.MODULE_STATUS + " ASC";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = database.getDbConnection().prepareStatement(query);
            rs = preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet getCountStatus(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = null;
        String query = "SELECT COUNT(CASE WHEN status = 0 THEN 1 END ) AS \"Pending\", \n" +
                        "COUNT(CASE WHEN status = 1 THEN 1 END ) AS \"Working\", \n" +
                        "COUNT(CASE WHEN status = 2 THEN 1 END ) AS \"Reviewing\", \n" +
                        "COUNT(CASE WHEN status = 3 THEN 1 END ) AS \"Done\"" +
                        " FROM " + Const.MODULE_TABLE +
                        " WHERE " + Const.PROJECT_ID +"=?";

        PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, id);
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    public ResultSet countAll(String id) throws SQLException, ClassNotFoundException{
        ResultSet resultSet = null;
        String query = "SELECT COUNT(*) FROM " + Const.MODULE_TABLE+ " WHERE " + Const.PROJECT_ID +"=?";
        PreparedStatement preparedStatement = Database.dbConnection.prepareStatement(query);
        preparedStatement.setString(1, id);
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
            preparedStatement.executeUpdate();
            preparedStatement.close();
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
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public ResultSet getStatus(Module module) throws SQLException {
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + Const.MODULE_TABLE + " WHERE "
                        + Const.MODULE_STATUS + " = ? AND "
                        + Const.MODULE_PROJECT_ID + " = ?";
        PreparedStatement preparedStatement = Database.dbConnection.prepareStatement(query);
        preparedStatement.setString(1, module.getStatus());
        preparedStatement.setString(2, module.getProjectID());
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
}
