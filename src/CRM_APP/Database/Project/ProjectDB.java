package CRM_APP.Database.Project;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Model.Project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectDB {
    private Database  database = new Database();

    //retrieve all data in project
    public ResultSet getProjectList() throws SQLException, ClassNotFoundException {
        database = new Database();
        ResultSet resultSet = null;

        String query = "SELECT * FROM " + Const.PROJECT_TABLE ;
        PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
        resultSet = preparedStatement.executeQuery();

        return resultSet;
    }

    public void insertProject(Project project){
        String query = "INSERT INTO " + Const.PROJECT_TABLE +" ("
                + Const.PROJECT_ID +", "
                + Const.CUSTOMER_ID +", "
                + Const.PROJECT_NAME +", "
                + Const.PROJECT_BEGIN_TIME+", "
                + Const.PROJECT_END_TIME+", "
                + Const.PROJECT_MANAGER+", "
                + Const.PROJECT_TOTAL_AMOUNT+") "
                + " VALUES(?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement =  Database.dbConnection.prepareStatement(query);
            preparedStatement.setString(1, project.getId());
            preparedStatement.setString(2, project.getCusId());
            preparedStatement.setString(3, project.getName());
            preparedStatement.setString(4, project.getBeginTime());
            preparedStatement.setString(5, project.getEndTime());
            preparedStatement.setString(6, project.getManager());
            preparedStatement.setString(7, project.getAmount()+"");
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateProject(Project project){
        String query = "UPDATE " + Const.PROJECT_TABLE +" SET "
                        + Const.PROJECT_NAME +" =?, "
                        + Const.PROJECT_BEGIN_TIME + " =?, "
                        + Const.PROJECT_END_TIME + " =?, "
                        + Const.PROJECT_TOTAL_AMOUNT + " =?, "
                        + Const.PROJECT_MANAGER + " =?, "
                        + Const.PROJECT_CUSTOMER + " =? "
                        + " WHERE " + Const.PROJECT_ID + " =?";
        try {
            PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, project.getName());
            preparedStatement.setString(2, project.getBeginTime());
            preparedStatement.setString(3, project.getEndTime());
            preparedStatement.setString(4, project.getAmount()+"");
            preparedStatement.setString(5, project.getManager());
            preparedStatement.setString(6, project.getCusId());
            preparedStatement.setString(7, project.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet projectFilter(String name) throws SQLException, ClassNotFoundException {
        database = new Database();
        ResultSet resultSet = null;

        String query = "SELECT * FROM " + Const.PROJECT_TABLE + " WHERE "
                        + Const.PROJECT_NAME + " LIKE '%" + name + "%'";
        PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
}
