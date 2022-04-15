package CRM_APP.Database.Bill;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Model.Bill;
import CRM_APP.Model.BillDetail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillDB {
    Database database;

    public ResultSet getInformation(Bill bill) throws SQLException, ClassNotFoundException {
        database = new Database();
        ResultSet rs = null;
        String query = " SELECT * FROM " + Const.BILL_TABLE +" INNER JOIN " + Const.BILL_DETAIL_TABLE+ " ON "
                        + Const.BILL_TABLE + "."+ Const.BILL_ID + " = " + Const.BILL_DETAIL_TABLE + "." + Const.BILL_ID
                        + " INNER JOIN " + Const.MODULE_TABLE
                        + " ON " + Const.BILL_DETAIL_TABLE +"." + Const.PROJECT_ID + " = "+ Const.MODULE_TABLE + "."+ Const.MODULE_PROJECT_ID
                        + " WHERE " + Const.BILL_TABLE + "."+ Const.BILL_ID + " =?";
        PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
        preparedStatement.setString(1, bill.getId());
        rs = preparedStatement.executeQuery();
        return  rs;
    }
    public void save(Bill bill, BillDetail billDetail){
        database = new Database();
        //insert to bill table
        String query = " INSERT INTO " + Const.BILL_TABLE + "( "
                        + Const.BILL_ID + ", "
                        + Const.BILL_CUS_ID + ", "
                        + Const.BILL_DATE + ", "
                        + Const.BILL_TOTAL_AMOUNT + ", "
                        + Const.BILL_STATUS + ", "
                        + Const.BILL_PERCENT + ") VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, bill.getId());
            preparedStatement.setString(2, bill.getCustomer());
            preparedStatement.setString(3, bill.getDate()+"");
            preparedStatement.setString(4, bill.getTotalAmount());
            preparedStatement.setString(5, bill.getStatus());
            preparedStatement.setString(6, bill.getPercent());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //insert  to bill detail table
        String query2 = "INSERT INTO " + Const.BILL_DETAIL_TABLE + "("
                        + Const.BILL_ID + ", "
                        + Const.PROJECT_ID + ", "
                        + Const.BILL_DETAIL_AMOUNT + ") VALUES (?, ? ,?)";
        try {
            PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query2);
            preparedStatement.setString(1, billDetail.getBillID());
            preparedStatement.setString(2, billDetail.getProjectID());
            preparedStatement.setString(3, bill.getTotalAmount());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void updateBill(Bill bill){
        database = new Database();

        String query = "UPDATE " + Const.BILL_TABLE + " SET "
                        + Const.BILL_STATUS + " = ?, "
                        + Const.BILL_DATE + " =?";
        try {
            PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, bill.getStatus());
            preparedStatement.setString(2, bill.getDate() + "");
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
