package CRM_APP.Controller.Employee.Employee;

import CRM_APP.Controller.Employee.DetailController;
import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Employee.EmployeeDB;
import CRM_APP.Handler.NotificationHandler;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Handler.TextFieldHandler;
import CRM_APP.Model.Employee;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.mysql.cj.util.StringUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class AddEmployeeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<String> cb_Role;

    @FXML
    private JFXTextField txt_Name;

    @FXML
    private JFXTextField txt_Phone;

    @FXML
    private JFXTextField txt_Address;

    @FXML
    private JFXTextField txt_Position;

    @FXML
    private JFXTextField txt_Password;

    @FXML
    private Label lbl_Noti;

    @FXML
    private JFXButton btn_Save;

    @FXML
    private Button btn_Back;

    @FXML
    private Button btn_Delete;

    private TextFieldHandler textfieldHandler;
    private Database database;
    private EmployeeDB employeeDB;
    private Employee employee;
    private SceneHandler sceneHandler;
    private NotificationHandler notificationHandler;
    private static HashMap<String, String> employeeHM = new HashMap<String, String>();
    public static String emID = "";
    public static StackPane backPane;

    @FXML
    void initialize() {
        TextFieldHandler textfieldHandler = new TextFieldHandler();
        textfieldHandler.limitTextField(txt_Password, 6);

        cb_Role.getItems().addAll("Employee", "Manager");
        if(StringUtils.isNullOrEmpty(emID)){
            btn_Delete.setVisible(false);
        }else {
            populateDetail();
            btn_Delete.setVisible(true);
        }
        textfieldHandler = new TextFieldHandler();
        textfieldHandler.numberOnly(txt_Phone);
        btn_Save.setOnAction(e ->{
            if(StringUtils.isNullOrEmpty(emID)){
                save();
            }else{
                update();
            }
        });
        btn_Back.setOnAction(e ->{
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_Save, backPane, "-X", "/CRM_APP/View/Employee/employee.fxml");
        });
        btn_Delete.setOnAction(e -> {
            delete();
        });
    }
    private void save(){
        notificationHandler = new NotificationHandler();
        TextFieldHandler textFieldHandler = new TextFieldHandler();

        if(StringUtils.isNullOrEmpty(txt_Name.getText()) || StringUtils.isNullOrEmpty(txt_Phone.getText())
                || StringUtils.isNullOrEmpty(txt_Address.getText()) || StringUtils.isNullOrEmpty(txt_Position.getText())
                || StringUtils.isNullOrEmpty(txt_Password.getText())){
            notificationHandler.popup(notificationHandler.error, "Name, Address,Phone,Password,Position " +
                    "\n Not Allow to Empty");
        }
        else{
            database = new Database();
            sceneHandler = new SceneHandler();
            String number = OtherHandler.generateNumber();
            String name = txt_Name.getText();
            String phone = txt_Phone.getText();
            String address = txt_Address.getText();
            String position = txt_Position.getText();
            String password = txt_Password.getText();

            try {
                //Regenerate number if user's number exist
                ResultSet isName = database.getSomeID(name, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_NAME);
                if(isName.next()){
                    notificationHandler.popup(notificationHandler.error, "This Staff Already in list");
                }else if(!textFieldHandler.checkPhone(phone)){
                    notificationHandler.popup(notificationHandler.error, "Invalid Phone number");
                }
                else{
                    String prefix = combine();
                    String userID = prefix + number;
                    ResultSet row = database.getSomeID(userID, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_ID);
                    while(row.next()){
                        number = OtherHandler.generateNumber();
                        userID = prefix + number;
                        row = database.getSomeID(userID, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_ID);
                    }
                    employeeHM.put("ID", userID);
                    employeeHM.put("name", name);
                    employeeHM.put("address", address);
                    employeeHM.put("phone", phone);
                    employeeHM.put("position", position);
                    employeeHM.put("password", password);
                    setEmployeeHM(employeeHM);
                    DetailController.condition = "newEm";
                    DetailController.backPane = backPane;
                    sceneHandler.slideScene(btn_Save, backPane, "-X", "/CRM_APP/View/Employee/detail.fxml");
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    private void delete(){
        notificationHandler = new NotificationHandler();
        try {
            if(emID.equals("SAD")){
                notificationHandler.popup(notificationHandler.error, "This is admin you can not delete him");
            }else if(emID.equals(HomeController.userId)){
                notificationHandler.popup(notificationHandler.error, "This is you, you can not your self");
            }else if(!OtherHandler.checkExist(Const.TASK_TABLE, Const.TASK_EMP_ID, emID)
                    && OtherHandler.checkExist(Const.TEAM_DETAIL_TABLE, Const.TEAM_EM_ID, emID)){
                notificationHandler.popup(notificationHandler.error, "This Staff have constrain, can not delete");
            }else{
                database = new Database();
                database.detele(Const.AUTHEN_TABLE, Const.EMPLOYEE_ID, emID);
                database.detele(Const.TEAM_DETAIL_TABLE, Const.TEAM_EM_ID, emID);
                database.detele(Const.EMPLOYEE_TABLE, Const.EMPLOYEE_ID, emID);
                notificationHandler.popup(notificationHandler.success, "Delete Successful");
                btn_Back.fire();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }
    private void populateDetail(){
        try {
            database = new Database();
            ResultSet row = database.getSomeID(emID, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_ID);
            while(row.next()){
                String str = row.getString(Const.EMPLOYEE_ID);
                if(str.contains("EM")){
                    cb_Role.setValue("Employee");
                }else{
                    cb_Role.setValue("Manager");
                }
                txt_Name.setText( row.getString(Const.EMPLOYEE_NAME));
                txt_Phone.setText(row.getString(Const.EMPLOYEE_PHONE));
                txt_Address.setText(row.getString(Const.EMPLOYEE_ADDRESS));
                txt_Position.setText(row.getString(Const.EMPLOYEE_POSITION));
                txt_Password.setText(row.getString(Const.EMPLOYEE_PASSWORD));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    private void update(){
        sceneHandler = new SceneHandler();
        employee = new Employee();
        employeeDB = new EmployeeDB();
        textfieldHandler = new TextFieldHandler();
        notificationHandler = new NotificationHandler();
        if(StringUtils.isNullOrEmpty(txt_Name.getText()) || StringUtils.isNullOrEmpty(txt_Phone.getText())
            || StringUtils.isNullOrEmpty(txt_Address.getText()) || StringUtils.isNullOrEmpty(txt_Position.getText())
            || StringUtils.isNullOrEmpty(txt_Password.getText())){
            notificationHandler.popup(notificationHandler.error, "Information can not be null");
        }else{
            String name = txt_Name.getText().trim();
            String phone = txt_Phone.getText().trim();
            String address = txt_Address.getText().trim();
            String position = txt_Position.getText().trim();
            String password = txt_Password.getText().trim();
//            ResultSet isName = database.getSomeID(name, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_NAME);
//            try {
//                if(isName.next()){
//                    notificationHandler.popup(notificationHandler.error, "This Staff Already in list");
//                }else{
//
//                }
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
            if(textfieldHandler.checkPhone(phone)){
                lbl_Noti.setVisible(false);
                employee.setName(name);
                employee.setPhone(phone);
                employee.setAddress(address);
                employee.setPosition(position);
                employee.setPassword(password);
                employee.setId(emID);
                employeeDB.updateAdmin(employee);
                notificationHandler.popup(notificationHandler.success, "Employee" +  name +" Update Success");
            }else{
                notificationHandler.popup(notificationHandler.error, "Invalid Phone number");
            }
        }
    }

    public static HashMap<String, String> getEmployeeHM() {
        return employeeHM;
    }

    public static void setEmployeeHM(HashMap<String, String> employeeHM) {
        AddEmployeeController.employeeHM = employeeHM;
    }

    private String combine(){
        String _prefix = cb_Role.getValue();
        String pre = (_prefix.equals("Employee")) ? "EM" : "AD";
        return pre;
    }
}
