package CRM_APP.Controller.Login;

import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Login.LoginDB;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Employee;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LoginController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_close;

    @FXML
    private Label lbl_invalid;

    @FXML
    private JFXTextField txt_username;

    @FXML
    private JFXPasswordField txt_password;

    @FXML
    private JFXButton btn_login;

    private SceneHandler sceneHandler;
    private LoginDB database = new LoginDB();
    private Database mydb = new Database();
    private Employee em = new Employee();
    private String userId;
    @FXML
    void initialize() {

    }
    //close app event
    @FXML
    void closeEvent(ActionEvent event) {
        System.exit(0);
    }
    @FXML
    void loginEvent(ActionEvent event) {
        sceneHandler = new SceneHandler();
        String loginText = txt_username.getText().trim();
        String loginPwd = txt_password.getText().trim();
        Employee employee = new Employee();
        employee.setId(loginText);
        employee.setPassword(loginPwd);

        try {
            ResultSet userRow = database.getUser(employee);
            int counter = 0;
            while(userRow.next()){
                counter++;
                String name  = userRow.getString("EmpName");
                userId=userRow.getString("EmpID");

            }
            if(counter == 1){
                System.out.println("Welcome "+userId);
                userLogin(userId);
                loginSuccess();
            }else{
                System.out.println("counter : " + counter);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //login event
    private void loginSuccess(){
        try {
            btn_login.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/CRM_APP/View/Home/home.fxml"));
            loader.load();

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setHeight(sceneHandler.getScreen()[0]*0.7);
            stage.setWidth(sceneHandler.getScreen()[1]*0.7);

            HomeController homeController = loader.getController();
            homeController.setUserId(userId);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //this func will fire when login success and be4 change scene
    private void userLogin(String uid) throws SQLException, ClassNotFoundException {
        String deviceName = getDevice();
        String aid = generateId();
        ResultSet rs = mydb.getSomeID(aid, Const.AUTHEN_TABLE, Const.AUTHEN_AUTHENID);
        while(rs.next()){
            aid = generateId();
            rs = mydb.getSomeID(aid, Const.AUTHEN_TABLE, Const.AUTHEN_AUTHENID);
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String logTime = dtf.format(now);
        database.authen("login", aid, uid, logTime, deviceName);
    }
    //get current device name
    private String getDevice(){
        String name = "Unknown";

        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            name = addr.getHostName();
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Hostname can not be resolved");
        }
        return name;
    }
    //auto generate random string - id when fire
    private String generateId(){
        int n=9;
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
