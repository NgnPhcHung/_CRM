package CRM_APP.Database.Task;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TaskDB {
    private Database db;
    public void createTask(String id, String emp, String task, String status,
                           String color, String start, String end) throws SQLException, ClassNotFoundException {
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
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, emp);
            preparedStatement.setString(3, task);
            preparedStatement.setString(4, status);
            preparedStatement.setString(5, color);
            preparedStatement.setString(6, start);
            preparedStatement.setString(7, end);

            preparedStatement.executeUpdate();
        }catch (SQLException e){

        }catch (ClassNotFoundException e){}
    }
}
