package CRM_APP.Database;

import CRM_APP.Handler.ThreadHandler;

import java.sql.*;

public class Database extends Configs{
    public static Connection dbConnection;

    //get connection to database
    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://"+ dbHost + ":"
                +dbPort +"/"
                +dbName;
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }

    //region ALL FUNC IN THIS REGION IS COMMON
    //this is use for get data on condition
    public ResultSet getSomeID(String id, String tableName, String colName) throws SQLException, ClassNotFoundException {
        ThreadHandler threadHandler = new ThreadHandler();
        Thread thread = new Thread(threadHandler){
            @Override
            public void run() {
                ResultSet resultSet = null;
                String query = "SELECT * FROM " + tableName +" WHERE " + colName + " =?";
                PreparedStatement preparedStatement = null;
                try {
                    preparedStatement = getDbConnection().prepareStatement(query);
                    preparedStatement.setString(1, id);
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

    //retrieve all data that has been given in tableName
    public ResultSet getAllTableValue(String tableName) throws SQLException, ClassNotFoundException {
        ThreadHandler threadHandler = new ThreadHandler();
        Thread thread = new Thread(threadHandler){
            @Override
            public void run() {
                ResultSet resultSet = null;
                String query = "SELECT * FROM " + tableName ;
                try {
                    PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
                    resultSet = preparedStatement.executeQuery();
                    threadHandler.setRs(resultSet);
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
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

    public void detele(String table, String colName, String id){
        ThreadHandler threadHandler = new ThreadHandler();
        Thread thread =  new Thread(threadHandler){
            @Override
            public void run() {
                try {
                    String query = "DELETE FROM " + table + " WHERE " + colName + " =?";
                    PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
                    preparedStatement.setString(1, id);
                    preparedStatement.execute();
                    preparedStatement.close();
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
    }
    public ResultSet filterDataInput(String table, String col, String name){
        ThreadHandler threadHandler = new ThreadHandler();
        Thread thread = new Thread(threadHandler){
            @Override
            public void run() {
                ResultSet resultSet = null;

                String query = "SELECT * FROM " + table + " WHERE "
                        + col + " LIKE '%" + name + "%'";
                PreparedStatement preparedStatement = null;
                try {
                    preparedStatement = getDbConnection().prepareStatement(query);
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
    //endregion
}
