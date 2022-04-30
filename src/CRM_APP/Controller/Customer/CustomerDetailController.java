package CRM_APP.Controller.Customer;

import CRM_APP.Database.Const;
import CRM_APP.Database.Customer.CustomerDB;
import CRM_APP.Database.Database;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Handler.TextFieldHandler;
import CRM_APP.Model.Customer;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.util.StringUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CustomerDetailController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txt_Name;

    @FXML
    private JFXTextField txt_Phone;

    @FXML
    private JFXTextField txt_Address;

    @FXML
    private JFXTextField txt_TIN;

    @FXML
    private Label lbl_Noti;

    @FXML
    private JFXButton btn_Save;

    @FXML
    private Button btn_Back;

    @FXML
    private JFXButton btn_Detete;

    public static String cusID;
    private Database database;
    private SceneHandler sceneHandler;
    private CustomerDB customerDB;
    private Customer customer;
    private TextFieldHandler textFieldHandler;
    @FXML
    void initialize() {
        textFieldHandler = new TextFieldHandler();
        textFieldHandler.numberOnly(txt_Phone);
            System.out.println(cusID.isEmpty());
        if(StringUtils.isNullOrEmpty(cusID)){
            btn_Detete.setVisible(false);

        }else{
            populateDetail();
        }
        btn_Detete.setOnAction(e -> {
            deleteCustomer();
            btn_Back.fire();
        });
        btn_Save.setOnAction(e ->{
            if(StringUtils.isNullOrEmpty(cusID)){
                createCustomer();
            }else{
                updateCustomer();
            }
        });
        btn_Back.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_Back, CustomerCellController.cellStack, "X", "/CRM_APP/View/Customer/customer.fxml");
        });
    }

    private void populateDetail(){
        try {
            database = new Database();
            ResultSet row = database.getSomeID(cusID, Const.CUSTOMER_TABLE, Const.CUSTOMER_ID);
            while(row.next()){
                txt_Name.setText(row.getString(Const.CUSTOMER_NAME));
                txt_Phone.setText(row.getString(Const.CUSTOMER_PHONE));
                txt_Address.setText(row.getString(Const.CUSTOMER_ADDRESS));
                txt_TIN.setText(row.getString(Const.CUSTOMER_TIN));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void createCustomer(){
        customerDB = new CustomerDB();
        customer = new Customer();
        database = new Database();
        //check
        try {
            String id = OtherHandler.generateId();
            ResultSet check = database.getSomeID(id, Const.CUSTOMER_TABLE, Const.CUSTOMER_NAME);
            customer.setId(id);
            while(check.next()){
                id = OtherHandler.generateId();
                check = database.getSomeID(id, Const.CUSTOMER_TABLE, Const.CUSTOMER_NAME);
                customer.setId(id);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(!StringUtils.isNullOrEmpty(txt_Name.getText()) && !StringUtils.isNullOrEmpty(txt_Phone.getText())
        && !StringUtils.isNullOrEmpty(txt_Address.getText()) && !StringUtils.isNullOrEmpty(txt_TIN.getText())){
            String name = txt_Name.getText();
            ResultSet resultSet = null;
            lbl_Noti.setVisible(false);
            try {
                resultSet = database.getSomeID(name, Const.CUSTOMER_TABLE, Const.CUSTOMER_NAME);
                if(resultSet.next()){
                    lbl_Noti.setVisible(true);
                    lbl_Noti.setText("This customer already in list");
                }else{
                    textFieldHandler = new TextFieldHandler();

                    lbl_Noti.setVisible(false);
                    String phone = txt_Phone.getText();
                    String address = txt_Address.getText();
                    String TIN = txt_TIN.getText();
                    if(textFieldHandler.checkPhone(phone)){

                        customer.setCusName(name);
                        customer.setPhone(phone);
                        customer.setAddress(address);
                        customer.setTIN(TIN);
                        customerDB.create(customer);
                        lbl_Noti.setVisible(false);
                        btn_Back.fire();

                    }else{
                        lbl_Noti.setVisible(true);
                        lbl_Noti.setText("Invalid Phone number");
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            lbl_Noti.setVisible(true);
            lbl_Noti.setText("Invalid Input");
        }
    }
    private void updateCustomer(){
        customerDB = new CustomerDB();
        customer = new Customer();
        textFieldHandler = new TextFieldHandler();
        if(!StringUtils.isNullOrEmpty(txt_Name.getText()) && !StringUtils.isNullOrEmpty(txt_Phone.getText())
                && !StringUtils.isNullOrEmpty(txt_Address.getText()) && !StringUtils.isNullOrEmpty(txt_TIN.getText())){
            String name = txt_Name.getText();
            String phone = txt_Phone.getText();
            String address = txt_Address.getText();
            String TIN = txt_TIN.getText();
            if(textFieldHandler.checkPhone(phone)){
                customer.setCusName(name);
                customer.setPhone(phone);
                customer.setAddress(address);
                customer.setTIN(TIN);
                customer.setId(cusID);
                customerDB.update(customer);

                btn_Back.fire();
            }else{
                lbl_Noti.setVisible(true);
                lbl_Noti.setText("Invalid Phone number");
            }
        }
    }
    private void deleteCustomer(){
        database = new Database();
        try {
            ResultSet row = database.getSomeID(cusID, Const.PROJECT_TABLE, Const.PROJECT_CUSTOMER);
            if(row.next()){
                lbl_Noti.setVisible(true);
                lbl_Noti.setText("This customer have constraint can not delete");
            }else{
                lbl_Noti.setVisible(false);
                database = new Database();
                database.detele(Const.CUSTOMER_TABLE, Const.CUSTOMER_ID, cusID);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
