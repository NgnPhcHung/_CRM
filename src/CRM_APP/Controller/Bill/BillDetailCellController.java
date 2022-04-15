package CRM_APP.Controller.Bill;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Bill;
import CRM_APP.Model.Module;
import com.jfoenix.controls.JFXListCell;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BillDetailCellController extends JFXListCell<Module> {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox main_Pane;

    @FXML
    private Label lbl_Name;

    FXMLLoader fxmlLoader;
    public static StackPane cellStack;
    private Database database;
    private SceneHandler sceneHandler;


    @FXML
    void initialize() {

    }



    @Override
    protected void updateItem(Module item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else{
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/CRM_APP/View/Bill/billDetailCell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            lbl_Name.setText(item.getModName());

            setText(null);
            setGraphic(main_Pane);
        }
    }
}
