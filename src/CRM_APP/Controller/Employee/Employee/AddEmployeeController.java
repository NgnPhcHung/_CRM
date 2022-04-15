package CRM_APP.Controller.Employee.Employee;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Employee.EmployeeDB;
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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
    public static String emID = "null";
    @FXML
    void initialize() {
        TextFieldHandler textfieldHandler = new TextFieldHandler();
        textfieldHandler.limitTextField(txt_Password, 6);
        cb_Role.getItems().addAll("Employee", "Admin");
        if(emID.equals("null")){
            btn_Delete.setVisible(false);
        }else {
            populateDetail();
            btn_Delete.setVisible(true);
        }
        textfieldHandler = new TextFieldHandler();
        textfieldHandler.numberOnly(txt_Phone);
        btn_Save.setOnAction(e ->{
            if(emID.equals("null")){
                save();
            }else{
                update();
            }
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_Save, EmployeeCellController.cellStack, "-X", "/CRM_APP/View/Employee/employee.fxml");
        });
        btn_Back.setOnAction(e ->{
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_Save, EmployeeCellController.cellStack, "-X", "/CRM_APP/View/Employee/employee.fxml");
        });
        btn_Delete.setOnAction(e -> {
            try {
                if(!OtherHandler.checkExist(Const.TASK_TABLE, Const.TASK_EMP_ID, emID)
                    && OtherHandler.checkExist(Const.TEAM_DETAIL_TABLE, Const.TEAM_EM_ID, emID)){
                    delete();
                    sceneHandler = new SceneHandler();
                    sceneHandler.slideScene(btn_Save, EmployeeCellController.cellStack, "-X", "/CRM_APP/View/Employee/employee.fxml");
                    lbl_Noti.setVisible(false);
                }else{
                    lbl_Noti.setVisible(true);
                    lbl_Noti.setText("This Staff have task, can not delete!");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        });
    }
    //region DATABASE PROCESS
    private void save(){
        //region INITIALIZE
        String number = OtherHandler.generateNumber();
        String name = txt_Name.getText().trim();
        String phone = txt_Phone.getText().trim();
        String address = txt_Address.getText().trim();
        String position = txt_Position.getText().trim();
        String password = txt_Password.getText().trim();
        if(number.equals("") || name.equals("")
            || phone.equals("") || address.equals("") || position.equals("") || password.equals("")){
            lbl_Noti.setVisible(true);
            lbl_Noti.setText("Invalid Input");
        }
        //endregion

        //region SAVE
        else{
            lbl_Noti.setVisible(false);
            String prefix = combine();
            String userID = prefix + number;
            try {
                database = new Database();
                //Regenerate number if user's number exist
                ResultSet row = database.getSomeID(userID, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_ID);
                while(row.next()){
                    number = OtherHandler.generateNumber();
                    userID = prefix + number;
                    row = database.getSomeID(userID, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_ID);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            employeeDB = new EmployeeDB();
            employee = new Employee();
            employee.setId(userID);
            employee.setName(name);
            employee.setAddress(address);
            employee.setPhone(phone);
            employee.setPosition(position);
            employee.setPassword(password);
            employeeDB.saveEmp(employee);
        }
        //endregion
    }
    private void delete(){
        database = new Database();
        database.detele(Const.AUTHEN_TABLE, Const.EMPLOYEE_ID, emID);
        database.detele(Const.TEAM_DETAIL_TABLE, Const.TEAM_EM_ID, emID);
        database.detele(Const.EMPLOYEE_TABLE, Const.EMPLOYEE_ID, emID);
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
                    cb_Role.setValue("Admin");
                }
                txt_Name.setText( row.getString(Const.EMPLOYEE_NAME));
                txt_Phone.setText(row.getString(Const.EMPLOYEE_PHONE));
                txt_Address.setText(row.getString(Const.EMPLOYEE_ADDRESS));
                txt_Position.setText(row.getString(Const.EMPLOYEE_POSITION));
                txt_Password.setText(row.getString(Const.EMPLOYEE_PASSWORD));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    private void update(){
        sceneHandler = new SceneHandler();
        employee = new Employee();
        employeeDB = new EmployeeDB();

        String name = txt_Name.getText().trim();
        String phone = txt_Phone.getText().trim();
        String address = txt_Address.getText().trim();
        String position = txt_Position.getText().trim();
        String password = txt_Password.getText().trim();
        if(name.equals("") || phone.equals("") || address.equals("") || position.equals("") || password.equals("")){
            lbl_Noti.setVisible(true);
            lbl_Noti.setText("Information can not be null");
        }else{
            lbl_Noti.setVisible(false);
            employee.setName(name);
            employee.setPhone(phone);
            employee.setAddress(address);
            employee.setPosition(position);
            employee.setPassword(password);
            employee.setId(emID);
            employeeDB.updateAdmin(employee);
        }
    }
    //endregion

    private String combine(){
        String _prefix = cb_Role.getValue();
        String pre = (_prefix.equals("Employee")) ? "EM" : "AD";
        return pre;
    }
}
