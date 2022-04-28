package CRM_APP.Database.Home;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.ThreadHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeDB {
    private Database database;
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
}
