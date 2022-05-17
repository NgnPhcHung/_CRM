package CRM_APP.Database.Bill;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.ThreadHandler;
import CRM_APP.Model.Bill;
import CRM_APP.Model.BillDetail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillDB{
    Database database;

    public ResultSet getInformation(Bill bill) {
        ThreadHandler threadHandler = new ThreadHandler();
        Thread thread = new Thread(threadHandler) {
            ResultSet rs = null;
            @Override
            public void run() {
                super.run();
                database = new Database();
                String query = " SELECT * FROM " + Const.BILL_TABLE
                        + " INNER JOIN " + Const.BILL_DETAIL_TABLE
                        + " ON " + Const.BILL_DETAIL_TABLE + "." + Const.BILL_ID + " = "
                        + Const.BILL_TABLE + "." + Const.BILL_ID
                        + " INNER JOIN " + Const.PROJECT_TABLE
                        + " ON " + Const.BILL_DETAIL_TABLE + "." + Const.PROJECT_ID
                        + " = " + Const.PROJECT_TABLE + "." + Const.PROJECT_ID
                        + " INNER JOIN " + Const.CUSTOMER_TABLE
                        + " ON " + Const.CUSTOMER_TABLE + "." + Const.CUSTOMER_ID + " = "
                        + Const.BILL_DETAIL_TABLE + "." + Const.BILL_CUS_ID
                        + " WHERE " + Const.BILL_TABLE + "." + Const.BILL_ID + " = ?";
                try {
                PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
                preparedStatement.setString(1, bill.getId());

                rs = preparedStatement.executeQuery();

                threadHandler.setRs(rs);
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            };
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return threadHandler.getRs();
    }

    public void save(Bill bill, BillDetail billDetail){
        ThreadHandler threadHandler = new ThreadHandler();
        Thread thread = new Thread(threadHandler){
            @Override
            public void run() {
                database = new Database();
                //insert to bill table
                String query = " INSERT INTO " + Const.BILL_TABLE + "( "
                        + Const.BILL_ID + ", "
                        + Const.EMPLOYEE_ID + ", "
                        + Const.BILL_DATE + ", "
                        + Const.BILL_STATUS + ", "
                        + Const.BILL_PERCENT +
                        ") VALUES (?, ?, ?, ?, ?)";
                try {
                    PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
                    preparedStatement.setString(1, bill.getId());
                    preparedStatement.setString(2, bill.getEmID());
                    preparedStatement.setString(3, bill.getDate()+"");
                    preparedStatement.setString(4, bill.getStatus());
                    preparedStatement.setString(5, bill.getPercent());
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
                        + Const.CUSTOMER_ID + ", "
                        + Const.BILL_DETAIL_AMOUNT + ") VALUES (?, ? ,?, ?)";
                try {
                    PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query2);
                    preparedStatement.setString(1, billDetail.getBillID());
                    preparedStatement.setString(2, billDetail.getProjectID());
                    preparedStatement.setString(3, billDetail.getCusID());
                    preparedStatement.setString(4, bill.getTotalAmount());
                    preparedStatement.executeUpdate();
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
    public void updateBill(Bill bill){
        ThreadHandler threadHandler = new ThreadHandler();
        Thread thread = new Thread(threadHandler){
            @Override
            public void run() {
                database = new Database();

                String query = "UPDATE " + Const.BILL_TABLE + " SET "
                        + Const.BILL_STATUS + " = ?, "
                        + Const.BILL_DATE + " =? WHERE " + Const.BILL_ID + "=?";
                try {
                    PreparedStatement preparedStatement = database.getDbConnection().prepareStatement(query);
                    preparedStatement.setString(1, bill.getStatus());
                    preparedStatement.setString(2, bill.getDate() + "");
                    preparedStatement.setString(3, bill.getId());
                    preparedStatement.executeUpdate();
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

    public ResultSet getBillList(){
        database = new Database();
        ResultSet rs = null;
        try {
            String query = " SELECT * FROM " + Const.BILL_TABLE + " INNER JOIN " + Const.BILL_DETAIL_TABLE
                        + " ON " + Const.BILL_TABLE + "." + Const.BILL_ID + " = "
                        + Const.BILL_DETAIL_TABLE + "." + Const.BILL_ID;
            PreparedStatement preparedStatement = null;
            preparedStatement = database.getDbConnection().prepareStatement(query);
            rs = preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rs;
    }
    public ResultSet getBillDetail(Bill bill){
        database = new Database();
        ResultSet resultSet = null;
        String query = " SELECT * FROM " + Const.BILL_TABLE
                + " INNER JOIN " + Const.PROJECT_TABLE
                + " ON " + Const.BILL_TABLE + "." + Const.PROJECT_ID
                + " = " + Const.PROJECT_TABLE + "." + Const.PROJECT_ID
                + " INNER JOIN " + Const.BILL_DETAIL_TABLE
                + " ON " + Const.BILL_DETAIL_TABLE + "." + Const.BILL_ID + " = "
                + Const.BILL_TABLE + "." + Const.BILL_ID
                + " INNER JOIN " + Const.CUSTOMER_TABLE
                + " ON " + Const.CUSTOMER_TABLE + "." + Const.CUSTOMER_ID + " = "
                + Const.BILL_DETAIL_TABLE + "." + Const.BILL_CUS_ID
                + " WHERE " + Const.BILL_TABLE + "." + Const.BILL_ID + " = ?";
        try {
            PreparedStatement preparedStatement  = database.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, bill.getId());
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    public ResultSet getProjectName(String input){
        ThreadHandler threadHandler= new ThreadHandler();
        Thread thread = new Thread(threadHandler){
            @Override
            public void run() {
                database = new Database();
                ResultSet rs = null;
                String query = " SELECT * FROM " + Const.BILL_DETAIL_TABLE
                        + " INNER JOIN " + Const.BILL_TABLE + " ON "
                        + Const.BILL_TABLE +"." + Const.BILL_ID + " = "
                        + Const.BILL_DETAIL_TABLE+"." + Const.BILL_ID
                        + " INNER JOIN " + Const.PROJECT_TABLE + " ON "
                        + Const.BILL_DETAIL_TABLE + "." + Const.PROJECT_ID +" = "
                        + Const.PROJECT_TABLE + "." + Const.PROJECT_ID
                        + " WHERE " + Const.PROJECT_TABLE + "." + Const.PROJECT_NAME + " LIKE'%" + input + "%'";
                PreparedStatement preparedStatement = null;
                try {
                    preparedStatement = database.getDbConnection().prepareStatement(query);
                    rs = preparedStatement.executeQuery();
                    threadHandler.setRs(rs);
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
    public ResultSet checkProjectExist(String projectName){
        ThreadHandler threadHandler = new ThreadHandler();
        Thread thread = new Thread(threadHandler){
            @Override
            public void run() {
                database = new Database();
                String query = " SELECT * FROM " + Const.BILL_DETAIL_TABLE
                        + " INNER JOIN " + Const.PROJECT_TABLE
                        + " ON " + Const.BILL_DETAIL_TABLE + "." + Const.PROJECT_ID
                        + " = " + Const.PROJECT_TABLE + "." + Const.PROJECT_ID
                        + " WHERE " + Const.PROJECT_TABLE + "." + Const.PROJECT_NAME + " = '" + projectName+"'";
                PreparedStatement preparedStatement  = null;
                try {
                    preparedStatement = database.getDbConnection().prepareStatement(query);
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
        return threadHandler.getRs();
    }
    public ResultSet getCountStatus(){
        ThreadHandler threadHandler = new ThreadHandler();
        Thread thread = new Thread(threadHandler){
            @Override
            public void run() {
                ResultSet resultSet = null;
                String query = "SELECT COUNT(CASE WHEN Status = \"0\" THEN 1\n" +
                        " ELSE NULL " +
                        " END) AS Pending " +
                        ",COUNT(CASE WHEN Status = \"1\" THEN 1 " +
                        " ELSE NULL\n" +
                        " END) AS Process " +
                        ",COUNT(CASE WHEN Status = \"2\" THEN 1 " +
                        "                   ELSE NULL\n" +
                        " END) AS Done " +
                        ",COUNT(CASE WHEN Status = \"3\" THEN 1 " +
                        "                   ELSE NULL " +
                        " END) AS Cancel " + " FROM " + Const.BILL_TABLE;
                try {
                    PreparedStatement preparedStatement = Database.dbConnection.prepareStatement(query);
                    resultSet = preparedStatement.executeQuery();
                    threadHandler.setRs(resultSet);
                } catch (SQLException throwables) {
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
}
