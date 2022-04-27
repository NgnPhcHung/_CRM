package CRM_APP.Database.Login;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.ThreadHandler;
import CRM_APP.Model.Employee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDB {
    private Database db;
    //when user login
    public ResultSet getUser(Employee user){
        ThreadHandler threadHandler = new ThreadHandler();
        Thread thread = new Thread(threadHandler){
            @Override
            public void run() {
                super.run();
                db = new Database();
                ResultSet resultSet = null;
                if(!user.getId().equals("") || !user.getPassword().equals("") ){
                    String query = "SELECT * FROM " + Const.EMPLOYEE_TABLE + " WHERE "
                            + Const.EMPLOYEE_ID + " =?" + " AND " + Const.EMPLOYEE_PASSWORD
                            + " =? ";
                    try {
                        PreparedStatement preparedStatement = db.getDbConnection().prepareStatement(query);
                        preparedStatement.setString(1 ,user.getId());
                        preparedStatement.setString(2 ,user.getPassword());

                        resultSet = preparedStatement.executeQuery();
                    } catch (SQLException | ClassNotFoundException throwables) {
                        throwables.printStackTrace();
                    }
                }else{
                    System.out.println("error from login getUser");
                }
                threadHandler.setRs(resultSet);
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

    //add login time
    public void authen(String action, String aid,String id, String time, String device)  {
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                if(action.equals("login")){
                    String login ="INSERT INTO "+ Const.AUTHEN_TABLE + " ( "
                            + Const.AUTHEN_AUTHENID +", "
                            + Const.EMPLOYEE_ID +", "
                            + Const.AUTHEN_LOGTIME + ", "
                            + Const.AUTHEN_DEVICE+ " )"
                            + "VALUES(?,?,?,?)";
                    PreparedStatement preparedStatement = null;
                    try {
                        preparedStatement = Database.dbConnection.prepareStatement(login);
                        preparedStatement.setString(1, aid);
                        preparedStatement.setString(2, id);
                        preparedStatement.setString(3, time);
                        preparedStatement.setString(4, device);
                        preparedStatement.executeUpdate();

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }else if(action.equals("logout")){
                    String login = "UPDATE " + Const.AUTHEN_TABLE + " SET "
                                    + Const.AUTHEN_OUTTIME + " = ? ORDER BY "
                                    + Const.AUTHEN_LOGTIME+ " DESC LIMIT 1";
                    try {
                        PreparedStatement preparedStatement = Database.dbConnection.prepareStatement(login);
                        preparedStatement.setString(1, time);
                        preparedStatement.executeUpdate();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
