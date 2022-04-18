package CRM_APP.Database.Employee;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Model.Employee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDB {
    private Database database;

    public void saveEmp(Employee employee){
        try {
            database = new Database();
            String query = "INSERT INTO " + Const.EMPLOYEE_TABLE + "( "
                            + Const.EMPLOYEE_ID + ", "
                            + Const.EMPLOYEE_NAME + ", "
                            + Const.EMPLOYEE_ADDRESS + ", "
                            + Const.EMPLOYEE_PHONE + ", "
                            + Const.EMPLOYEE_POSITION + ", "
                            + Const.EMPLOYEE_PASSWORD +" ) VALUES  (?,?,?,?,?,?)";
                PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
                preparedStatement.setString(1, employee.getId());
                preparedStatement.setString(2, employee.getName());
                preparedStatement.setString(3, employee.getAddress());
                preparedStatement.setString(4, employee.getPhone());
                preparedStatement.setString(5, employee.getPosition());
                preparedStatement.setString(6, employee.getPassword());
            System.out.println(preparedStatement);
                preparedStatement.executeUpdate();
                preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateAdmin(Employee employee){
        database = new Database();
        String query  = " UPDATE " + Const.EMPLOYEE_TABLE + " SET "
                        + Const.EMPLOYEE_NAME  +  " =?, "
                        + Const.EMPLOYEE_ADDRESS + " =?, "
                        + Const.EMPLOYEE_PHONE + "=?, "
                        + Const.EMPLOYEE_POSITION + "=?, "
                        + Const.EMPLOYEE_PASSWORD + "=? WHERE " +  Const.EMPLOYEE_ID + "=?";
        try {
            PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getAddress());
            preparedStatement.setString(3, employee.getPhone());
            preparedStatement.setString(4, employee.getPosition());
            preparedStatement.setString(5, employee.getPassword());
            preparedStatement.setString(6, employee.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateEmp(Employee employee){
        database = new Database();

        String query = "UPDATE " + Const.EMPLOYEE_TABLE + " SET "
                        + Const.EMPLOYEE_PASSWORD + "=? WHERE "
                        + Const.EMPLOYEE_ID + "=?";
        try {
            PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, employee.getPassword());
            preparedStatement.setString(2, employee.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet countTask1(Employee employee) throws SQLException, ClassNotFoundException {
        database = new Database();
        String query = "SELECT COUNT(*) as TotalTask FROM " + Const.TASK_TABLE + " WHERE " + Const.TEAM_EM_ID +" =?";
        PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, employee.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    public ResultSet countTaskExpired(Employee employee, String checkDate) throws SQLException, ClassNotFoundException{
        database = new Database();

        String query = "SELECT COUNT(*) FROM " + Const.TASK_TABLE + " WHERE "
                + Const.TEAM_EM_ID +" =? AND "
                + Const.TASK_END + " > " + checkDate
                + " AND "+ Const.TASK_STATUS + " <> 3";
        PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, employee.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    public ResultSet getFirstTeam(Employee employee) throws SQLException, ClassNotFoundException {
        database = new Database();

        String query = "SELECT * FROM " + Const.TEAM_DETAIL_TABLE + " WHERE " + Const.TEAM_EM_ID +" =? LIMIT 1";
        PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, employee.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
}
