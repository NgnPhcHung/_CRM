package CRM_APP.Controller.Bill;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Bill;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class BillCellController extends JFXListCell<Bill> {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox main_Pane;

    @FXML
    private Label lbl_Name;

    @FXML
    private Label lbl_Customer;

    @FXML
    private Label lbl_Percent;

    @FXML
    private Button btn_Edit;

    @FXML
    private Label lbl_Status;

    FXMLLoader fxmlLoader;
    public static StackPane cellStack;
    private Database database;
    private SceneHandler sceneHandler;

    @FXML
    void initialize() {
    }

    @Override
    protected void updateItem(Bill item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else{
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/CRM_APP/View/Bill/billCell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            lbl_Percent.setText(item.getPercent());
            handleStatus(item.getStatus());
            //region CUSTOMER NAME
            try {
                database = new Database();
                ResultSet rs = database.getSomeID(item.getCustomer(), Const.CUSTOMER_TABLE, Const.CUSTOMER_ID);
                while(rs.next()){
                    lbl_Customer.setText(rs.getString(Const.CUSTOMER_NAME));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //endregion
            //region PROJECT NAME
            try {
                database = new Database();
                ResultSet rs = database.getSomeID(item.getId(), Const.BILL_DETAIL_TABLE, Const.BILL_ID);
                while(rs.next()){
                    database = new Database();
                    ResultSet pj =  database.getSomeID(rs.getString(Const.PROJECT_ID), Const.PROJECT_TABLE, Const.PROJECT_ID);
                    while(pj.next()){
                        lbl_Name.setText(pj.getString(Const.PROJECT_NAME));
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            //endregion
            btn_Edit.setOnAction(e -> {
                sceneHandler = new SceneHandler();
                BillDetailController.billID = item.getId();
                BillDetailController.backPane = cellStack;

                sceneHandler.slideScene(btn_Edit, cellStack, "X", "/CRM_APP/View/Bill/billDetail.fxml");
            });
            setText(null);
            setGraphic(main_Pane);
        }
    }
    private void handleStatus(String stt){
        lbl_Status.getStylesheets().add("/CRM_APP/Style/StyleStatus.css");
        switch (stt) {
            case "0":
                lbl_Status.setText("Waiting");
                lbl_Status.getStyleClass().add("pending");
                break;
            case "1":
                lbl_Status.setText("In Process");
                lbl_Status.getStyleClass().add("working");
                break;
            case "2":
                lbl_Status.setText("Done");
                lbl_Status.getStyleClass().add("review");
                break;
            case "3":
                lbl_Status.setText("Cancel");
                lbl_Status.getStyleClass().add("done");
                break;
            default: break;
        }
    }
}
