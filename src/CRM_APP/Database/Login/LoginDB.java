package CRM_APP.Database.Login;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Model.Employee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDB {
    private Database db;
    //when user login
    public ResultSet getUser(Employee user) throws SQLException, ClassNotFoundException {
        db = new Database();
        ResultSet resultSet = null;
        if(!user.getId().equals("") || !user.getPassword().equals("") ){
            String query = "SELECT * FROM " + Const.EMPLOYEE_TABLE + " WHERE "
                    + Const.EMPLOYEE_ID + " =?" + " AND " + Const.EMPLOYEE_PASSWORD
                    + " =? ";
            PreparedStatement preparedStatement = db.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1 ,user.getId());
            preparedStatement.setString(2 ,user.getPassword());

            resultSet = preparedStatement.executeQuery();
        }else{
            System.out.println("error from login getUser");
        }
        return resultSet;
    }

    //add login time
    public void authen(String action, String aid,String id, String time, String device) throws SQLException, ClassNotFoundException {

        if(action.equals("login")){
            String login ="INSERT INTO "+ Const.AUTHEN_TABLE + " ( "
                    + Const.AUTHEN_AUTHENID +", "
                    + Const.EMPLOYEE_ID +", "
                    + Const.AUTHEN_LOGTIME + ", "
                    + Const.AUTHEN_DEVICE+ " )"
                    + "VALUES(?,?,?,?)";
            System.out.println("login!");
            PreparedStatement preparedStatement = Database.dbConnection.prepareStatement(login);
            preparedStatement.setString(1, aid);
            preparedStatement.setString(2, id);
            preparedStatement.setString(3, time);
            preparedStatement.setString(4, device);

            preparedStatement.executeUpdate();
        }else if(action.equals("logout")){
            String login ="INSERT INTO "+ Const.AUTHEN_TABLE + " ( "
                    + Const.AUTHEN_AUTHENID +", "
                    + Const.EMPLOYEE_ID +", "
                    + Const.AUTHEN_OUTTIME + ", "
                    + Const.AUTHEN_DEVICE+ " )"
                    + "VALUES(?,?,?,?)";
            System.out.println("logout!");
            PreparedStatement preparedStatement = Database.dbConnection.prepareStatement(login);
            preparedStatement.setString(1, aid);
            preparedStatement.setString(2, id);
            preparedStatement.setString(3, time);
            preparedStatement.setString(4, device);

            preparedStatement.executeUpdate();
        }
    }

}
