package CRM_APP.Database;

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
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + tableName +" WHERE " + colName + " =?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, id);
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    //retrieve all data that has been given in tableName
    public ResultSet getAllTableValue(String tableName) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + tableName ;
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        resultSet = preparedStatement.executeQuery();

        return resultSet;
    }

    public void detele(String table, String colName, String id){
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
    public ResultSet filterDataInput(String table, String col, String name) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = null;

        String query = "SELECT * FROM " + table + " WHERE "
                + col + " LIKE '%" + name + "%'";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    //endregion
}
