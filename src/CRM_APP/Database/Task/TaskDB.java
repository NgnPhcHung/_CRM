package CRM_APP.Database.Task;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Model.Task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskDB {
    private Database db;
    public void createTask(Task task) throws SQLException, ClassNotFoundException {
        db= new Database();
        String query  = "INSERT INTO "+ Const.TASK_TABLE +" ( "
                        + Const.TASK_ID + ", "
                        + Const.EMPLOYEE_ID + ", "
                        + Const.TASK_NAME + ", "
                        + Const.TASK_STATUS + ", "
                        + Const.TASK_COLOR + ", "
                        + Const.TASK_START + ", "
                        + Const.TASK_END + " ) "
                        +" VALUES(?,?,?,?,?,?,?) " ;
        try {
            PreparedStatement preparedStatement = db.getDbConnection().prepareStatement(query);
//            preparedStatement.setString(1, id);
//            preparedStatement.setString(2, emp);
//            preparedStatement.setString(3, task);
//            preparedStatement.setString(4, status);
//            preparedStatement.setString(5, color);
//            preparedStatement.setString(6, start);
//            preparedStatement.setString(7, end);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e){

        }catch (ClassNotFoundException e){}
    }
    public ResultSet getSomeID(String id, String tableName, String colName) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + tableName +" WHERE " + colName + " =?";
//        String query = "SELECT * FROM task where ModID = \"2IhZxMp0c\" LIMIT 3";
        PreparedStatement preparedStatement = db.dbConnection.prepareStatement(query);
        preparedStatement.setString(1, id);
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public void empUpdateTask(Task task){
        String query = "UPDATE " + Const.TASK_TABLE + " SET "
                        + Const.TASK_COLOR + " =? ,"
                        + Const.TASK_DES + " =? ,"
                        + Const.TASK_STATUS + " =? "
                        + " WHERE " + Const.TASK_ID + " =?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = Database.dbConnection.prepareStatement(query);
            preparedStatement.setString(1, task.getColor());
            preparedStatement.setString(2, task.getDes());
            preparedStatement.setString(3, task.getStatus());
            preparedStatement.setString(4, task.getTaskID());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
