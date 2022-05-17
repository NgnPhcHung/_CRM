package CRM_APP.Controller.History;

import CRM_APP.Controller.Project.Module.ModuleCellController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.History.HistoryDB;
import CRM_APP.Database.Home.HomeDB;
import CRM_APP.Handler.DateTimePickerHandler;
import CRM_APP.Handler.NotificationHandler;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Model.History;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;

public class HistoryController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private StackPane main_stack;

    @FXML
    private JFXButton btn_Export;

    @FXML
    private Button btn_Find;

    @FXML
    private JFXTextField txt_find;

    @FXML
    private JFXDatePicker dp_Start;

    @FXML
    private JFXDatePicker dp_End;

    @FXML
    private JFXListView<History> lv_History;

    private ObservableList<History> histories;
    private HistoryDB historyDB;
    private Database database;
    private History history;
    private NotificationHandler notificationHandler;

    @FXML
    void initialize() {
        populateList();
        btn_Export.setOnAction(e -> {
            createLogFile();
        });
        btn_Find.setOnAction(e -> {
            findHistory();
        });
        dateTimeFormat();
    }

    private void populateList(){
        database = new Database();
        histories = FXCollections.observableArrayList();

        try {
            ResultSet row = database.getAllTableValue(Const.AUTHEN_TABLE);
            while(row.next()){
                history = new History();
                ResultSet rowName = database.getSomeID(row.getString(Const.EMPLOYEE_ID), Const.EMPLOYEE_TABLE, Const.EMPLOYEE_ID);
                while(rowName.next()){
                    history.setName(rowName.getString(Const.EMPLOYEE_NAME));
                }

                String startDate = row.getString(Const.AUTHEN_LOGTIME);
                String endDate = row.getString(Const.AUTHEN_OUTTIME);

                if(StringUtils.isNullOrEmpty(endDate)){
                    history.setEndDate(startDate);
                }else{
                    history.setEndDate(endDate);
                }

                history.setId(row.getString(Const.EMPLOYEE_ID));
                history.setStartDate(row.getString(Const.AUTHEN_LOGTIME));

                history.setDevice(row.getString(Const.AUTHEN_DEVICE));
                histories.add(history);
            }
            lv_History.setItems(histories);
            lv_History.setCellFactory(HistoryCellController -> new HistoryCellController());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createLogFile(){
        String fileName = "Log_" + OtherHandler.generateId();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(btn_Export.getScene().getWindow());
        try {
            PrintWriter pw = new PrintWriter(selectedDirectory+"\\"+fileName+".csv");
            StringBuilder sb = new StringBuilder();
            sb.append("Employee Name");
            sb.append(", ");
            sb.append("Employee ID");
            sb.append(", ");
            sb.append("Login time");
            sb.append(", ");
            sb.append("Logout time");
            sb.append(", ");
            sb.append("Devices");
            sb.append("\r\n");
            historyDB = new HistoryDB();
            ResultSet row =  historyDB.getLogAuthen();
            try {
                while (row.next()){
                    sb.append(row.getString(Const.EMPLOYEE_ID));
                    sb.append(", ");
                    ResultSet rowName = database.getSomeID(row.getString(Const.EMPLOYEE_ID), Const.EMPLOYEE_TABLE, Const.EMPLOYEE_ID);
                    while(rowName.next()){
                        sb.append(rowName.getString(Const.EMPLOYEE_NAME));
                        sb.append(", ");
                    }
                    sb.append(row.getString(Const.AUTHEN_LOGTIME));
                    sb.append(", ");
                    sb.append(row.getString(Const.AUTHEN_OUTTIME));
                    sb.append(", ");
                    sb.append(row.getString(Const.AUTHEN_DEVICE));
                    sb.append("\r\n");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            pw.write(sb.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void dateTimeFormat(){
        LocalDate dateStart = dp_Start.getValue();
        DateTimePickerHandler.disableDate(dp_End, dateStart);
        dp_Start.setValue(LocalDate.now());
        dp_End.setValue(LocalDate.now());

        dp_Start.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                DateTimePickerHandler.disableDate(dp_End, dp_Start.getValue());
                LocalDate dateStart = dp_Start.getValue();
                dp_End.setValue(dateStart);
            }
        });
    }
    private void findHistory(){
        historyDB = new HistoryDB();
        history = new History();
        database = new Database();
        notificationHandler = new NotificationHandler();
        if(StringUtils.isNullOrEmpty(txt_find.getText())){
            notificationHandler.popup(notificationHandler.error, "Employee to find can not blank");
        }else{
            String name = txt_find.getText();
            ResultSet row = database.getSomeID(name, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_NAME);
            histories = FXCollections.observableArrayList();
            try {
                if(row.next()){
                    String id = row.getString(Const.EMPLOYEE_ID);
                    String start = dp_Start.getValue().toString();
                    String end = dp_End.getValue().toString();
                    history.setId(id);
                    history.setName(name);
                    history.setStartDate(start);
                    history.setEndDate(end);
                    ResultSet rowFiller = historyDB.fillerHistory(history);
                    while(rowFiller.next()){
                        history = new History();
                        ResultSet rowName = database.getSomeID(rowFiller.getString(Const.EMPLOYEE_ID), Const.EMPLOYEE_TABLE, Const.EMPLOYEE_ID);
                        while(rowName.next()){
                            history.setName(rowName.getString(Const.EMPLOYEE_NAME));
                        }

                        String startDate = rowFiller.getString(Const.AUTHEN_LOGTIME);
                        String endDate = rowFiller.getString(Const.AUTHEN_OUTTIME);

                        if(StringUtils.isNullOrEmpty(endDate)){
                            history.setEndDate(startDate);
                        }else{
                            history.setEndDate(endDate);
                        }

                        history.setId(rowFiller.getString(Const.EMPLOYEE_ID));
                        history.setStartDate(rowFiller.getString(Const.AUTHEN_LOGTIME));

                        history.setDevice(rowFiller.getString(Const.AUTHEN_DEVICE));
                        histories.add(history);
                    }
                    lv_History.setItems(histories);
                    lv_History.setCellFactory(HistoryCellController -> new HistoryCellController());
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
