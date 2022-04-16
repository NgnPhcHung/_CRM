package CRM_APP.Database.Customer;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Model.Customer;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerDB {
    Database database;

    public void create(Customer customer){
        database = new Database();
        String query = " INSERT INTO " + Const.CUSTOMER_TABLE + "("
                        + Const.CUSTOMER_ID + ", "
                        + Const.CUSTOMER_NAME + ", "
                        + Const.CUSTOMER_ADDRESS + ", "
                        + Const.CUSTOMER_PHONE + ", "
                        + Const.CUSTOMER_TIN + ") VALUES(?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, customer.getId());
            preparedStatement.setString(2, customer.getCusName());
            preparedStatement.setString(3, customer.getAddress());
            preparedStatement.setString(4, customer.getPhone());
            preparedStatement.setString(5, customer.getTIN());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void update(Customer customer){
        database = new Database();
        String query = "UPDATE " + Const.CUSTOMER_TABLE + " SET "
                        + Const.CUSTOMER_NAME + " = ?, "
                        + Const.CUSTOMER_PHONE + " =?, "
                        + Const.CUSTOMER_ADDRESS + " =?, "
                        + Const.CUSTOMER_TIN + "=? WHERE " + Const.CUSTOMER_ID + " =?";
        try {
            PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, customer.getCusName());
            preparedStatement.setString(2, customer.getPhone());
            preparedStatement.setString(3, customer.getAddress());
            preparedStatement.setString(4, customer.getTIN());
            preparedStatement.setString(5, customer.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
