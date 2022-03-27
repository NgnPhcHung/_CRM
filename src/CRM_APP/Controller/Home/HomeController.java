package CRM_APP.Controller.Home;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

import CRM_APP.Controller.Task.FullCalendarView;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Login.LoginDB;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Employee;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.*;

public class HomeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TabPane tp_homeMain;

    @FXML
    private VBox tab_home;

    @FXML
    private Button btn_document;

    @FXML
    private Button btn_customer;

    @FXML
    private Button btn_task;

    @FXML
    private Button btn_project;

    @FXML
    private Button btn_bill;

    @FXML
    private Button btn_setting;

    @FXML
    private Button btn_exit;

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_position;



    private LoginDB database = new LoginDB();
    public static String userId;
    private Database mydb = new Database();

    @FXML
    void initialize() {
        getInfo();
    }

    //when user logout
    @FXML
    void closeApplication(ActionEvent event) {
        try {
            userLogout(getUserId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        //close
        Platform.exit();
        System.exit(0);
    }
    private void userLogout(String uid) throws SQLException, ClassNotFoundException {
        String deviceName = OtherHandler.getDevice();
        String aid = OtherHandler.generateId();
        database = new LoginDB();
        mydb = new Database();

//        try{
//            ResultSet rs = mydb.getSomeID(aid, Const.AUTHEN_TABLE, Const.AUTHEN_AUTHENID);
//            while(rs.next()){
//                aid = OtherHandler.generateId();
//                rs = mydb.getSomeID(aid, Const.AUTHEN_TABLE, Const.AUTHEN_AUTHENID);
//            }
//        }catch (Exception e){}
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String logTime = dtf.format(now);
        database.authen("logout", aid, uid, logTime, deviceName);
    }

    //check value is in array (now is tab)
    private boolean check(String arr[], String valueCheck){
        boolean test = false;
        for(int i  = 0; i < arr.length;i++){
            if(arr[i].equals(valueCheck)) {
                test = true;
                break;
            }
            else test = false;
        }
        return test;
    }

    //add tab to tabs
    private String[] addElement(String[] a, String e) {
        a  = Arrays.copyOf(a, a.length + 1);
        a[a.length - 1] = e;
        return a;
    }

    //show confirm when button survey fired (only survey)
    private void showConfirmation(Tab tab) throws IOException {
        Label label = new Label();
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Select type");
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        alert.setX(bounds.getMaxX() - 1240);//vertical
        alert.setY(bounds.getMaxY() - 900); //horizontal
        alert.getDialogPane().setStyle(
                "-fx-background-radius: 10px;"+
                "-fx-background-color: transparent;");
        alert.getDialogPane().getScene().setFill(Color.TRANSPARENT); // Used for better visual representation of the bug
        alert.setHeaderText("");
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initStyle(StageStyle.TRANSPARENT);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("/CRM_APP/Style/MyDialogs.css");
        dialogPane.getStyleClass().add("myDialog");

        ButtonType Result = new ButtonType("Result");
        ButtonType Question = new ButtonType("Question");
        ButtonType Close = new ButtonType("X");

        // remove default buttontype settings
        alert.getButtonTypes().clear();

        alert.getButtonTypes().addAll(Close, Result, Question);
        //style X button
        alert.getDialogPane().getChildren().forEach(node -> {
            if (node instanceof ButtonBar) {
                ButtonBar buttonBar = (ButtonBar) node;
                buttonBar.getButtons().forEach(possibleButtons -> {
                    if (possibleButtons instanceof Button) {
                        Button b = (Button) possibleButtons;
                        if (b.getText().equals("X")) {
                            b.setStyle("-fx-background-color: #ff5252;  -fx-text-fill: #ffffff;");
                        }
                    }
                });
            }
        });
        // option != null.
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == null) {
            label.setText("No selection!");
        } else if (option.get() == Result) {
            GridPane newPane = FXMLLoader.load(getClass().getResource(SceneHandler.getChooseFileFxml("result", "Survey")));
            if(tabManage("Result")){
                tab.setContent(newPane);
                tab.setText("Result");
                tp_homeMain.getTabs().add(tab);
            }
        } else if (option.get() == Question) {
            GridPane newPane = FXMLLoader.load(getClass().getResource(SceneHandler.getChooseFileFxml("question", "Survey")));
            if(tabManage("Question")){
                tab.setContent(newPane);
                tab.setText("Question");
                tp_homeMain.getTabs().add(tab);
            }
        }else if (option.get() == Close) {
           alert.close();
        } else {
            label.setText("-");
        }
    }

    //this will get all opened tab
    private boolean tabManage(String btn){
        boolean isOpen = false;

        String temp[] = new String[]{};
        String btnName = btn;

        //if tab not in tabs, add tab
        for(Tab tabs: tp_homeMain.getTabs()){
            if(check(temp,tabs.getText())==false){
                temp = addElement(temp,tabs.getText());
            }
        }

        if(check(temp,btnName)) return isOpen=false;
        else return isOpen=true;
    }

    //set new tab when press button
    @FXML
    void mouseClickEvent(MouseEvent event) throws IOException {
        Button button = (Button)event.getSource();
        String btnName = button.getText().trim();

        if(tabManage(btnName)){
            Tab tab = new Tab(btnName);

            tp_homeMain.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
            tp_homeMain.getStylesheets().add("/CRM_APP/Style/HomeStyle.css");
            tp_homeMain.setStyle("tab-close-button: #cfd8dc");

            if (btnName.equals("Survey")){
                showConfirmation(tab);
            }else {
                if(btnName.equals("Task")){
                    tab.setContent(new FullCalendarView(YearMonth.now()).getView());
                }else{
                    StackPane newPane = FXMLLoader.load(getClass().getResource(SceneHandler.getFileFXML(btnName)));
                    tab.setContent(newPane);
                }

                tab.setStyle("-fx-text-fill: #cfd8dc;\n" +
                        "    -fx-font-weight: bold;\n" +
                        "    -fx-font-family: Calibri;\n" +
                        "    -fx-font-size: 20;");

                tp_homeMain.getTabs().add(tab);
            }
        }
    }

    private void getInfo() {
        Database db = new Database();
        ResultSet row = null;
        try {
            row = db.getSomeID(userId, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_ID);
            if(row.next()){
                System.out.println("Hi,"+row.getString(Const.EMPLOYEE_NAME));
                lbl_name.setText("Welcome back, "+row.getString("EmpName"));
                lbl_position.setText(row.getString("Position"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    //get set
    public void setUserId(String userId){
        this.userId = userId;
    }
    public String getUserId(){
        return this.userId;
    }
}
