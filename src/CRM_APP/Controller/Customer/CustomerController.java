package CRM_APP.Controller.Customer;

import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Customer;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
public class CustomerController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXListView<Customer> lv_customer;

    @FXML
    private JFXButton btn_addCustomer;

    ObservableList<Customer> customers;
    SceneHandler sceneHandler;

    @FXML
    void initialize() {
        sceneHandler= new SceneHandler();
        Customer customer = new Customer();
        customer.setTestName("MyName");
        customer.setTestEmail("myemail@gmail.com");
        customers = FXCollections.observableArrayList();
        customers.addAll(customer);
        lv_customer.setItems(customers);
        lv_customer.setCellFactory(CustomerCellController -> new CustomerCellController());

        btn_addCustomer.setOnAction(event -> sceneHandler.newScene("/CRM_APP/View/Customer/customerDetail.fxml"));
    }
}
