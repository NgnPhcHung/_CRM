package CRM_APP.Database.History;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.ThreadHandler;
import CRM_APP.Model.History;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoryDB {
    Database database = new Database();
    public ResultSet getLogAuthen(){
        ThreadHandler threadHandler = new ThreadHandler();
        Thread thread = new Thread(threadHandler){
            @Override
            public void run() {
                super.run();
                database = new Database();
                String query = " SELECT * FROM " + Const.AUTHEN_TABLE;
                try {
                    PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery();
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
        return  threadHandler.getRs();
    }

    public ResultSet fillerHistory(History history){
        database = new Database();
        ResultSet resultSet = null;
        String query = " SELECT * FROM " + Const.AUTHEN_TABLE
                        + " INNER JOIN " + Const.EMPLOYEE_TABLE + " ON "
                        + Const.AUTHEN_TABLE + "." + Const.EMPLOYEE_ID + " = "
                        + Const.EMPLOYEE_TABLE + "." + Const.EMPLOYEE_ID
                        + " WHERE " + Const.EMPLOYEE_NAME + " LIKE'%"+history.getName()+"%' AND "
                        + " DATE("+ Const.AUTHEN_LOGTIME + ") >= ? AND "
                        + " DATE("+ Const.AUTHEN_OUTTIME + ") <= ? ";
        try {
            PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, history.getStartDate());
            preparedStatement.setString(2, history.getEndDate());
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(resultSet);
        return resultSet;
    }
}
