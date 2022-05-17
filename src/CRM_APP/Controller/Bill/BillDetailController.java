package CRM_APP.Controller.Bill;

import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Database.Bill.BillDB;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.*;
import CRM_APP.Model.Bill;
import CRM_APP.Model.BillDetail;
import CRM_APP.Model.Module;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.mysql.cj.util.StringUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

public class BillDetailController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXDatePicker datePick_TaskDate;

    @FXML
    private JFXComboBox<String> cb_Project;

    @FXML
    private JFXTextField txt_Amount;

    @FXML
    private ToggleButton tog_Wait;

    @FXML
    private ToggleButton tog_Process;

    @FXML
    private ToggleButton tog_Done;

    @FXML
    private ToggleButton tog_Cancel;

    @FXML
    private ToggleButton tog_30;

    @FXML
    private ToggleButton tog_70;

    @FXML
    private ToggleButton tog_100;

    @FXML
    private JFXButton btn_Save;

    @FXML
    private Button btn_Back;

    @FXML
    private Button btn_Delete;

    @FXML
    private JFXListView<Module> lv_Module;

    @FXML
    private Label lbl_BillID;

    @FXML
    private Label lbl_CustomerName;

    @FXML
    private Label lbl_Employee;

    private SceneHandler sceneHandler;
    private Database database;
    public static String billID ="";
    private ObservableList<Module> modules;
    private BillDB billDB;
    private Bill bill;
    private BillDetail billDetail;
    private String percent;
    private String status;
    public static StackPane backPane;
    private int projectBudget  = 0;
    private String id = "";
    private NotificationHandler notification;

    @FXML
    void initialize() {
        settingStart();
        toggleEvent();
        generateBilldID();

        if(StringUtils.isNullOrEmpty(billID) && HomeController.userId.contains("AD")){
            btn_Delete.setVisible(false);
            lbl_BillID.setText(id);
            lbl_Employee.setText(HomeController.emName);
        }
        else{
            lbl_BillID.setText(billID);
            cb_Project.setDisable(true);
            tog_30.setDisable(true);
            tog_70.setDisable(true);
            tog_100.setDisable(true);
            populateDetailList();
            manageTogglePopulate();
            btn_Delete.setVisible(true);
        }

        btn_Back.setOnAction(e ->{
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_Back, backPane, "-X", "/CRM_APP/View/Bill/bill.fxml");
        });

        btn_Save.setOnAction(e -> {
                if(StringUtils.isNullOrEmpty(billID)){
                    save();
                }else{
                    update();
                    btn_Back.fire();
                }
        });

        btn_Delete.setOnAction(e ->{
            delete ();
        });
    }

    private void settingStart(){
        OtherHandler.populateComboBox(cb_Project, Const.PROJECT_TABLE, Const.PROJECT_NAME);
        datePick_TaskDate.setValue(LocalDate.now());
        TextFieldHandler textfieldHandler = new TextFieldHandler();
        textfieldHandler.numberOnly(txt_Amount);

        cb_Project.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                database = new Database();
                ResultSet row = database.getSomeID(cb_Project.getValue(), Const.PROJECT_TABLE, Const.PROJECT_NAME);
                try {
                    if(row.next()){
                        String budget = row.getString(Const.PROJECT_TOTAL_AMOUNT);
                        String project = row.getString(Const.PROJECT_ID);
                        txt_Amount.setText(budget);
                        projectBudget = Integer.parseInt(budget);

                        ResultSet rsCustomer = database.getSomeID(row.getString(Const.CUSTOMER_ID), Const.CUSTOMER_TABLE, Const.CUSTOMER_ID);
                        ResultSet rowCell = database.getSomeID(project, Const.MODULE_TABLE, Const.PROJECT_ID);

                        if(rsCustomer.next()){
                            System.out.println(rsCustomer.getString(Const.CUSTOMER_NAME));
                            lbl_CustomerName.setText(rsCustomer.getString(Const.CUSTOMER_NAME));
                        }

                        modules = FXCollections.observableArrayList();

                        while(rowCell.next()){
                            Module module = new Module();
                            module.setModName(rowCell.getString(Const.MODULE_NAME));
                            modules.add(module);
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }
    private void populateDetailList(){
        sceneHandler= new SceneHandler();
        database = new Database();
        billDB = new BillDB();
        bill = new Bill();
        modules = FXCollections.observableArrayList();
        ResultSet row = null;

        try {
            bill.setId(billID);
            row = billDB.getInformation(bill);
            while(row.next()){
                ResultSet rsProject = database.getSomeID(row.getString(Const.PROJECT_ID), Const.PROJECT_TABLE, Const.PROJECT_ID);
                while(rsProject.next() ){
                    cb_Project.setValue(rsProject.getString(Const.PROJECT_NAME));
                    projectBudget = Integer.parseInt(rsProject.getString(Const.PROJECT_TOTAL_AMOUNT));
                }
                lbl_CustomerName.setText(row.getString(Const.CUSTOMER_NAME));
                txt_Amount.setText(row.getString(Const.BILL_DETAIL_AMOUNT));;
                datePick_TaskDate.setValue(DateTimePickerHandler.formatDate(row.getString(Const.BILL_DATE)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    private void manageTogglePopulate(){
        try {
            ResultSet row = database.getSomeID(billID, Const.BILL_TABLE, Const.BILL_ID);
            if(row.next()){
                //region STATUS
                String s = row.getString("Status");
                if(s.equals("0")){
                    tog_Wait.setSelected(true);
                }else if(s.equals("1")){
                    tog_Process.setSelected(true);
                }else if(s.equals("2")){
                    tog_Done.setSelected(true);
                }else if(s.equals("3")){
                    tog_Cancel.setSelected(true);
                }
                //endregion
                //region PERCENT
                String per = row.getString(Const.BILL_PERCENT);
                if(per.equals("30")){
                    tog_30.setSelected(true);
                }else if(per.equals("70")){
                    tog_70.setSelected(true);
                }else if(per.equals("100")){
                    tog_100.setSelected(true);
                }
                //endregion
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private void toggleEvent(){
        //region PERCENT
        ToggleGroup groupPercent = new ToggleGroup();
        tog_100.setUserData(100);
        tog_70.setUserData(70);
        tog_30.setUserData(30);
        tog_100.setToggleGroup(groupPercent);
        tog_70.setToggleGroup(groupPercent);
        tog_30.setToggleGroup(groupPercent);
        percent = groupPercent.getSelectedToggle().getUserData()+"";

        groupPercent.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(groupPercent.getSelectedToggle() != null){
                    percent = groupPercent.getSelectedToggle().getUserData()+"";
//                    System.out.println(( 1005000 * 30 )/ 100);
                    txt_Amount.setText((projectBudget * (Integer.parseInt(percent)))/ 100 + "");

                }
            }
        });
        //endregion
        //region STATUS
        ToggleGroup groupStatus = new ToggleGroup();
        tog_Wait.setUserData(0);
        tog_Process.setUserData(1);
        tog_Done.setUserData(2);
        tog_Cancel.setUserData(3);

        tog_Wait.setToggleGroup(groupStatus);
        tog_Process.setToggleGroup(groupStatus);
        tog_Done.setToggleGroup(groupStatus);
        tog_Cancel.setToggleGroup(groupStatus);
        status = groupStatus.getSelectedToggle().getUserData()+"";
        groupStatus.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(groupStatus.getSelectedToggle() != null){
                    status = groupStatus.getSelectedToggle().getUserData()+"";

                    if(status.equals("2") || status.equals("3")){
                        tog_30.setDisable(true);
                        tog_70.setDisable(true);
                        tog_100.setDisable(true);
                    }else{
                        tog_30.setDisable(false);
                        tog_70.setDisable(false);
                        tog_100.setDisable(false);
                    }
                }
            }
        });
        //endregion
    }
    private void generateBilldID(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String logTime = dtf.format(now);
        id = logTime;
        id = id.replaceAll("[^\\d.]", "");
    }
    private void save(){
        notification = new NotificationHandler();
        LocalDate date = datePick_TaskDate.getValue();
        String projectName = cb_Project.getValue();
        String customerName = lbl_CustomerName.getText();
        String amount = txt_Amount.getText();
        database  = new Database();
        billDB = new BillDB();
        try {
            ResultSet row = billDB.checkProjectExist(projectName);
            if(row.next()){
                notification.popup(notification.warning, "This project already in list");
            }else{
                if(!amount.equals("") ){
                    try {
                        bill = new Bill();
                        billDetail = new BillDetail();
                        ResultSet rowProject = database.getSomeID(projectName, Const.PROJECT_TABLE, Const.PROJECT_NAME);
                        ResultSet rowCustomer = database.getSomeID(customerName, Const.CUSTOMER_TABLE, Const.CUSTOMER_NAME);
                        while(rowProject.next() && rowCustomer.next()){
                            bill.setId(id);
                            bill.setDate(date);
                            bill.setTotalAmount(amount);
                            bill.setPercent(percent);
                            bill.setStatus(status);
                            bill.setEmID(HomeController.userId);
                            billDetail.setBillID(id);
                            billDetail.setProjectID(rowProject.getString(Const.PROJECT_ID));
                            billDetail.setCusID(rowCustomer.getString(Const.CUSTOMER_ID));
                            billDB.save(bill, billDetail);
                            notification.popup(notification.success, "Bill create success");
                            btn_Back.fire();
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private void update(){
        bill = new Bill();
        billDB = new BillDB();
        bill.setStatus(status);
        bill.setDate(datePick_TaskDate.getValue());
        bill.setId(billID);
        billDB.updateBill(bill);
    }
    private void delete (){
        database = new Database();
        database.detele(Const.BILL_DETAIL_TABLE, Const.BILL_ID, billID);
        database.detele(Const.BILL_TABLE, Const.BILL_ID, billID);
        btn_Back.fire();
    }
}
