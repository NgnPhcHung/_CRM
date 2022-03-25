package CRM_APP.Controller.Billing;

import com.jfoenix.controls.JFXListCell;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BillCellController extends JFXListCell {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private static HBox main_pane;

    @FXML private Button btn_view;

    FXMLLoader fxmlLoader;

    @FXML
    void initialize() {

    }

    @FXML
    void editEvent(ActionEvent event) {

    }

    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else{
            if(fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("/CRM_APP/View/Bill/billCell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            setText(null);
            setGraphic(main_pane);
        }
    }

}
