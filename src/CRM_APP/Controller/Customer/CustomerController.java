package CRM_APP.Controller.Customer;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Customer;
import CRM_APP.Model.Employee;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class CustomerController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private StackPane main_Stack;

    @FXML
    private JFXListView<Customer> lv_customer;

    @FXML
    private JFXButton btn_addCustomer;

    @FXML
    private JFXTextField txt_Find;

    ObservableList<Customer> customers;
    SceneHandler sceneHandler;
    Database database ;
    Customer customer;
    @FXML
    void initialize() {
        populateList();
        findCustomer();
        btn_addCustomer.setOnAction(e ->{
            sceneHandler = new SceneHandler();
            CustomerDetailController.cusID = "null";
            sceneHandler.slideScene(btn_addCustomer, main_Stack, "X", "/CRM_APP/View/Customer/customerDetail.fxml");
        });
    }
    private void populateList(){
        database = new Database();

        customers = FXCollections.observableArrayList();
        try {
            ResultSet row = database.getAllTableValue(Const.CUSTOMER_TABLE);
            while(row.next()){
                customer = new Customer();
                customer.setId(row.getString(Const.CUSTOMER_ID));
                customer.setCusName(row.getString(Const.CUSTOMER_NAME));
                customer.setAddress(row.getString(Const.CUSTOMER_ADDRESS));
                customer.setPhone(row.getString(Const.CUSTOMER_PHONE));
                customer.setTIN(row.getString(Const.CUSTOMER_TIN));
                CustomerCellController.cellStack =main_Stack;
                customers.add(customer);
            }
            lv_customer.setItems(customers);
            lv_customer.setCellFactory(CustomerCellController -> new CustomerCellController());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void findCustomer(){
        txt_Find.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                sceneHandler = new SceneHandler();
                database = new Database();
                ObservableList<Customer> customers = FXCollections.observableArrayList();
                ResultSet row = null;
                try {
                    row = database.filterDataInput(Const.CUSTOMER_TABLE, Const.CUSTOMER_NAME, newValue);
                    while(row.next()){
                        customer = new Customer();
                        customer.setId(row.getString(Const.CUSTOMER_ID));
                        customer.setCusName(row.getString(Const.CUSTOMER_NAME));
                        customer.setAddress(row.getString(Const.CUSTOMER_ADDRESS));
                        customer.setPhone(row.getString(Const.CUSTOMER_PHONE));
                        customer.setTIN(row.getString(Const.CUSTOMER_TIN));
                        CustomerCellController.cellStack =main_Stack;
                        customers.add(customer);
                    }
                    lv_customer.setItems(customers);
                    lv_customer.setCellFactory(CustomerCellController -> new CustomerCellController());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}