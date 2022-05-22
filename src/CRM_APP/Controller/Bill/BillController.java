package CRM_APP.Controller.Bill;

import CRM_APP.Database.Bill.BillDB;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.DateTimePickerHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Handler.WriteBillHandler;
import CRM_APP.Model.Bill;
import CRM_APP.Model.BillDetail;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;

public class BillController extends Thread{

    @FXML
    private StackPane main_Stack;

    @FXML
    private Button btn_Add;

    @FXML
    private JFXTextField txt_Customer;

    @FXML
    private JFXButton btn_Waiting;

    @FXML
    private Label lbl_Waiting;

    @FXML
    private JFXButton btn_InProcess;

    @FXML
    private Label lbl_InProcess;

    @FXML
    private JFXButton btn_Done;

    @FXML
    private Label lbl_Done;

    @FXML
    private JFXButton btn_Cancel;

    @FXML
    private Label lbl_Cancel;

    @FXML
    private JFXButton btn_All;

    @FXML
    private Button btn_Export;

    @FXML
    private Label lbl_All;


    @FXML
    private JFXListView<Bill> lv_Bill;

    private Database database;
    private ObservableList<Bill> bills;
    private SceneHandler sceneHandler;
    ObservableList<Bill> data = FXCollections.observableArrayList();
    private BillDB billDB;
    private Bill bill;
    private static List<HashMap> billList = new ArrayList<HashMap>();

    @FXML
    void initialize() {
        database = new Database();

        Thread thread3 = new Thread(){
            @Override
            public void run() {
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
            }
        };

        thread3.start();

        filterText();
        fillCard();
        btn_Add.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            BillDetailController.backPane = main_Stack;
            BillDetailController.billID = "";
            sceneHandler.slideScene(btn_All, main_Stack, "X", "/CRM_APP/View/Bill/billDetail.fxml");
        });

        btn_Export.setOnAction(e ->{
            WriteBill();
        });
    }

    private void WriteBill(){

        HashMap<String, String> map = new HashMap<String, String>();

        billDB = new BillDB();
        ResultSet resultSet = billDB.getBillDetail();
        try {
            while(resultSet.next()){
                String status = statusToString(resultSet.getString(Const.BILL_STATUS));
                HashMap hm  = new HashMap();
                hm.put("Bill ID", resultSet.getString(Const.BILL_ID));
                hm.put("Customer", resultSet.getString(Const.CUSTOMER_NAME));
                hm.put("Project", resultSet.getString(Const.PROJECT_NAME));
                hm.put("Bill Date", resultSet.getString(Const.BILL_DATE));
                hm.put("Amount", resultSet.getString(Const.PROJECT_TOTAL_AMOUNT));
                hm.put("Percent", resultSet.getString(Const.BILL_PERCENT));
                hm.put("Total Amount", resultSet.getString(Const.BILL_DETAIL_AMOUNT));
                hm.put("Status", status);
                hm.put("Employee", resultSet.getString(Const.EMPLOYEE_NAME));
                billList.add(hm);
            }

            setBillList(billList);
            WriteBillHandler wb = new WriteBillHandler();
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(btn_Export.getScene().getWindow());
            wb.createBill(selectedDirectory);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void populateList(){
        sceneHandler= new SceneHandler();
        database = new Database();
        billDB = new BillDB();
        bills = FXCollections.observableArrayList();
        ResultSet row = null;
        try {
            row = billDB.getBillList();
            while(row.next()){
                Bill bill = new Bill();
                LocalDate billDate = DateTimePickerHandler.formatDate(row.getString(Const.BILL_DATE));
                bill.setId(row.getString(Const.BILL_ID));
                bill.setCustomer(row.getString(Const.BILL_CUS_ID));
                bill.setDate(billDate);
                bill.setPercent(row.getString(Const.BILL_PERCENT));
                bill.setStatus(row.getString(Const.BILL_STATUS));

                BillCellController.cellStack = main_Stack;
                bills.add(bill);
            }
            lv_Bill.setItems(bills);
            data = bills;
            lv_Bill.setCellFactory(BillCellController -> new BillCellController());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    void filterEvent(ActionEvent event) {
        FilteredList<Bill> filteredData = new FilteredList<Bill>(data, s -> true);
        Button button = (Button) event.getSource();
        String btnName = button.getText().trim();
        String status = formatStatus(btnName);

        if(status == null || status.length() == 0) {
            filteredData.setPredicate(s -> true);
        }
        else {
            filteredData.setPredicate(s -> s.getStatus().contains(status));
        }
        if(btnName.equals("Total")){
            populateList();
        }else{
            lv_Bill.setItems(filteredData);
        }
    }

    private void filterText(){
        txt_Customer.textProperty().addListener((observable, oldValue, newValue) -> {
            billDB = new BillDB();
            bills = FXCollections.observableArrayList();

            try {
                ResultSet row = billDB.getProjectName(newValue);
                while(row.next()){
                    Bill bill = new Bill();
                    LocalDate billDate = DateTimePickerHandler.formatDate(row.getString(Const.BILL_DATE));
                    bill.setId(row.getString(Const.BILL_ID));
                    bill.setCustomer(row.getString(Const.BILL_CUS_ID));
                    bill.setDate(billDate);
                    bill.setPercent(row.getString(Const.BILL_PERCENT));
                    bill.setStatus(row.getString(Const.BILL_STATUS));

                    BillCellController.cellStack = main_Stack;
                    bills.add(bill);
                }
                lv_Bill.setItems(bills);
                lv_Bill.setCellFactory(BillCellController -> new BillCellController());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    private String formatStatus(String in){
        switch (in){
            case"Waiting":
                in = "0";
                break;
            case "In Process":
                in = "1";
                break;
            case "Done":
                in = "2";
                break;
            case "Cancel":
                in = "3";
                break;
        }
        return in;
    }
    private String statusToString(String in){
        switch (in){
            case"0":
                in = "Waiting";
                break;
            case "1":
                in = "In Process";
                break;
            case "2":
                in = "Done";
                break;
            case "3":
                in = "Cancel";
                break;
        }
        return in;
    }

    private void fillCard(){
        billDB = new BillDB();
        String pending = "";
        String processing = "";
        String done = "";
        String cancel = "";
        int total = 0;
        try {
            ResultSet row = billDB.getCountStatus();
            if(row.next()){
                pending = row.getString("Pending");
                processing = row.getString("Process");
                done = row.getString("Done");
                cancel = row.getString("Cancel");
            }
            lbl_Waiting.setText(pending);
            lbl_InProcess.setText(processing);
            lbl_Done.setText(done);
            lbl_Cancel.setText(cancel);
            total = Integer.parseInt(pending) + Integer.parseInt(processing) + Integer.parseInt(done) + Integer.parseInt(cancel);
            lbl_All.setText(total+"");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static List<HashMap> getBillList() {
        return billList;
    }

    public static void setBillList(List<HashMap> billList) {
        BillController.billList = billList;
    }
}
