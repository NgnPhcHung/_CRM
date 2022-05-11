package CRM_APP.Controller.Bill;

import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Database.Bill.BillDB;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.DateTimePickerHandler;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Handler.TextFieldHandler;
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
    private JFXComboBox<String> cb_Customer;

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
    private Label lbl_Noti;

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

    @FXML
    void initialize() {
        setDetail();
        toggleEvent();
        if(StringUtils.isNullOrEmpty(billID) || !HomeController.userId.contains("AD")){
            btn_Delete.setVisible(false);
        }
        else{
            cb_Customer.setDisable(true);
            cb_Project.setDisable(true);
            tog_30.setDisable(true);
            tog_70.setDisable(true);
            tog_100.setDisable(true);
            txt_Amount.setDisable(true);
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
                    if(StringUtils.isNullOrEmpty(txt_Amount.getText())){
                        lbl_Noti.setVisible(true);
                        lbl_Noti.setText("Invalid Input!");

                    }else {
                        lbl_Noti.setVisible(false);
                        save();
                    }
                }else{
                    update();
                    btn_Back.fire();
                }
        });

        btn_Delete.setOnAction(e ->{
            delete ();
        });
    }

    private void setDetail(){
        OtherHandler.populateComboBox(cb_Customer, Const.CUSTOMER_TABLE, Const.CUSTOMER_NAME);
        OtherHandler.populateComboBox(cb_Project, Const.PROJECT_TABLE, Const.PROJECT_NAME);
        datePick_TaskDate.setValue(LocalDate.now());
        TextFieldHandler textfieldHandler = new TextFieldHandler();
        textfieldHandler.numberOnly(txt_Amount);

    }
    private void populateDetailList(){
        sceneHandler= new SceneHandler();
        database = new Database();
        modules = FXCollections.observableArrayList();
        billDB = new BillDB();
        ResultSet row = null;
        bill = new Bill();

        bill.setId(billID);
        try {
            row = billDB.getInformation(bill);
            while(row.next()){
                //region ADD ITEM TO LIST
                Module module = new Module();
                module.setModName(row.getString(Const.MODULE_NAME));
                modules.add(module);
                //endregion
                //region LEFT SIDE DETAIL
                ResultSet rsProject = database.getSomeID(row.getString(Const.PROJECT_ID), Const.PROJECT_TABLE, Const.PROJECT_ID);
                ResultSet rsCustomer = database.getSomeID(row.getString(Const.CUSTOMER_ID), Const.CUSTOMER_TABLE, Const.CUSTOMER_ID);
                while(rsProject.next() && rsCustomer.next()){
                    cb_Project.setValue(rsProject.getString(Const.PROJECT_NAME));
                    cb_Customer.setValue(rsCustomer.getString(Const.CUSTOMER_NAME));
                }
                txt_Amount.setText(row.getString(Const.BILL_TOTAL_AMOUNT));
                datePick_TaskDate.setValue(DateTimePickerHandler.formatDate(row.getString(Const.BILL_DATE)));
                //endregion
            }
            lv_Module.setItems(modules);
            lv_Module.setCellFactory(BillDetailCellController -> new BillDetailCellController());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
                }
            }
        });
        //endregion
    }
    private void save(){
        String id = OtherHandler.generateId();
        LocalDate date = datePick_TaskDate.getValue();
        String projectName = cb_Project.getValue();
        String customerName = cb_Customer.getValue();
        String amount = txt_Amount.getText();
        database  = new Database();
        billDB = new BillDB();
        try {
            ResultSet row = billDB.checkProjectExist(projectName);
            if(row.next()){
                lbl_Noti.setText("This project already in list");
                lbl_Noti.setVisible(true);
            }else{
                lbl_Noti.setVisible(false);
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
                            bill.setCustomer(rowCustomer.getString(Const.CUSTOMER_ID));

                            billDetail.setBillID(id);
                            billDetail.setProjectID(rowProject.getString(Const.PROJECT_ID));
                            billDB.save(bill, billDetail);
                            btn_Back.fire();
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
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
