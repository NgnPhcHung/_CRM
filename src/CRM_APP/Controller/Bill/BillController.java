package CRM_APP.Controller.Bill;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.DateTimePickerHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Bill;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class BillController {

    @FXML
    private StackPane main_Stack;

    @FXML
    private Button btn_Add;

    @FXML
    private JFXTextField txt_Customer;

    @FXML
    private JFXButton btn_30;

    @FXML
    private Label lbl_30;

    @FXML
    private JFXButton btn_70;

    @FXML
    private Label lbl_70;

    @FXML
    private JFXButton btn_100;

    @FXML
    private Label lbl_100;

    @FXML
    private JFXButton btn_All;

    @FXML
    private Label lbl_All;

    @FXML
    private JFXListView<Bill> lv_Bill;

    private Database database;
    private ObservableList<Bill> bills;
    private SceneHandler sceneHandler;

    @FXML
    void filterEvent(ActionEvent event) {

    }

    @FXML
    void initialize() {
        database = new Database();
        try {
            ResultSet rs = database.getAllTableValue(Const.BILL_TABLE);
            if (rs.next()){
                populateList();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        btn_Add.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_All, main_Stack, "X", "/CRM_APP/View/Bill/billDetail.fxml");
        });

    }
    private void populateList(){
        sceneHandler= new SceneHandler();
        database = new Database();
        bills = FXCollections.observableArrayList();
        ResultSet row = null;
        try {
            row = database.getAllTableValue(Const.BILL_TABLE);
            while(row.next()){
                Bill bill = new Bill();
                LocalDate billDate = DateTimePickerHandler.formatDate(row.getString(Const.BILL_DATE));
                bill.setId(row.getString(Const.BILL_ID));
                bill.setCustomer(row.getString(Const.BILL_CUS_ID));
                bill.setDate(billDate);
                bill.setPercent(row.getString(Const.BILL_PERCENT));
                bill.setStatus(row.getString(Const.BILL_STATUS));
                bill.setTotalAmount(row.getString(Const.BILL_TOTAL_AMOUNT));

                BillCellController.cellStack = main_Stack;
                bills.add(bill);
            }
            lv_Bill.setItems(bills);
            lv_Bill.setCellFactory(BillCellController -> new BillCellController());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
