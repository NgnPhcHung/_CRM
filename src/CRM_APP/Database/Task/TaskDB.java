package CRM_APP.Database.Task;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.ThreadHandler;
import CRM_APP.Model.Project;
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

    public ResultSet getStatus(Task task) throws SQLException {
        ResultSet resultSet = null;
        String query = " SELECT * FROM " + Const.TASK_TABLE + " WHERE "
                        + Const.TASK_STATUS + " =? AND "
                        + Const.TASK_MOD_ID + " =?";
        PreparedStatement preparedStatement = Database.dbConnection.prepareStatement(query);
        preparedStatement.setString(1, task.getStatus());
        preparedStatement.setString(2, task.getModID());
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    public ResultSet getCountStatus(String modID) throws SQLException, ClassNotFoundException {
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
                "    FROM "+ Const.TASK_TABLE +" WHERE " + Const.TASK_MOD_ID +"=?";
        PreparedStatement preparedStatement = Database.dbConnection.prepareStatement(query);
        preparedStatement.setString(1, modID);
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    //employee Query
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

    public ResultSet countAll(String id) throws SQLException, ClassNotFoundException{
        ResultSet resultSet = null;
        String query = "SELECT COUNT(*) FROM " + Const.TASK_TABLE+ " WHERE " + Const.MODULE_ID +"=?";
        PreparedStatement preparedStatement = Database.dbConnection.prepareStatement(query);
        preparedStatement.setString(1, id);
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public ResultSet getDetailTeamMember(Project project){
        ThreadHandler threadHandler = new ThreadHandler();
        Thread thread = new Thread(threadHandler){
            @Override
            public void run() {
                super.run();
                ResultSet resultSet = null;
                db = new Database();

                String query = " SELECT * FROM " + Const.PROJECT_TEAM_DETAIL
                        + " INNER JOIN " + Const.TEAM_TABLE
                        + " ON " + Const.PROJECT_TEAM_DETAIL +"."+Const.TEAM_ID + " = " + Const.TEAM_TABLE +"." + Const.TEAM_ID
                        + " INNER JOIN " + Const.TEAM_DETAIL_TABLE
                        + " ON " + Const.TEAM_DETAIL_TABLE + "." + Const.TEAM_ID + " = " + Const.TEAM_TABLE +"." + Const.TEAM_ID
                        + " INNER JOIN " + Const.EMPLOYEE_TABLE
                        + " ON " + Const.EMPLOYEE_TABLE + "." + Const.EMPLOYEE_ID + " = " + Const.TEAM_DETAIL_TABLE + "." + Const.EMPLOYEE_ID
                        + " WHERE " + Const.PROJECT_ID + " =?";

                PreparedStatement preparedStatement = null;
                try {
                    preparedStatement = db.getDbConnection().prepareStatement(query);
                    preparedStatement.setString(1, project.getId());
                    resultSet = preparedStatement.executeQuery();
                    threadHandler.setRs(resultSet);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return threadHandler.getRs();
    }
    //region ADMIN QUERY
    public void save(Task task){
        db= new Database();
        String query  = "INSERT INTO "+ Const.TASK_TABLE +" ( "
                + Const.TASK_ID + ", "
                + Const.TASK_MOD_ID + ", "
                + Const.EMPLOYEE_ID + ", "
                + Const.TASK_NAME + ", "
                + Const.TASK_STATUS + ", "
                + Const.TASK_COLOR + ", "
                + Const.TASK_START + ", "
                + Const.TASK_END + ", "
                + Const.TASK_DES + " ) "
                +" VALUES(?,?,?,?,?,?,?,?,?) " ;

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = db.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, task.getTaskID());
            preparedStatement.setString(2, task.getModID());
            preparedStatement.setString(3, task.getEmpID());
            preparedStatement.setString(4, task.getTaskName());
            preparedStatement.setString(5, task.getStatus());
            preparedStatement.setString(6, task.getColor());
            preparedStatement.setString(7, task.getStartDate()+"");
            preparedStatement.setString(8, task.getEndDate()+"");
            preparedStatement.setString(9, task.getDes());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void update(Task task){

        String query = "UPDATE " + Const.TASK_TABLE + " SET "
                        + Const.TASK_NAME + " =?, "
                        + Const.TASK_MOD_ID + " =?, "
                        + Const.TASK_STATUS + " =?, "
                        + Const.TASK_EMP_ID + " =?, "
                        + Const.TASK_COLOR + " =?, "
                        + Const.TASK_START + " =?, "
                        + Const.TASK_END + " =?, "
                        + Const.TASK_DES + " =?"
                        +" WHERE " + Const.TASK_ID + "=?";

        try {
            PreparedStatement preparedStatement = Database.dbConnection.prepareStatement(query);
            preparedStatement.setString(1, task.getTaskName());
            preparedStatement.setString(2, task.getModID());
            preparedStatement.setString(3, task.getStatus());
            preparedStatement.setString(4, task.getEmpID());
            preparedStatement.setString(5, task.getColor());
            preparedStatement.setString(6, task.getStartDate()+"");
            preparedStatement.setString(7, task.getEndDate()+"");
            preparedStatement.setString(8, task.getDes());
            preparedStatement.setString(9, task.getTaskID());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //endregion

    //region TASKLIST
    public ResultSet projectFilter(String name) throws SQLException, ClassNotFoundException {
        db = new Database();
        ResultSet resultSet = null;

        String query = "SELECT * FROM " + Const.PROJECT_TABLE + " WHERE "
                + Const.PROJECT_NAME + " LIKE '%" + name + "%'";
        PreparedStatement preparedStatement = db.getDbConnection().prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    //endregion


}
