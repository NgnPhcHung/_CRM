package CRM_APP.Controller.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Customer;
import com.jfoenix.controls.JFXListCell;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class CustomerCellController extends JFXListCell<Customer> {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox main_pane;

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_phone;

    @FXML
    private Label lbl_Address;

    @FXML
    private Label lbl_project;

    @FXML
    private Button btn_view;

    @FXML
    private Button btn_edit;

    @FXML
    private Label lbl_dateAdded;


    FXMLLoader fxmlLoader ;
    SceneHandler sceneHandler;
    public static StackPane cellStack;

    @FXML
    void initialize() {
    }

    @Override
    protected void updateItem(Customer item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else{
            if(fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("/CRM_APP/View/Customer/customerCell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            lbl_name.setText(item.getCusName());
            lbl_Address.setText(item.getAddress());
            lbl_phone.setText(item.getPhone());
            btn_edit.setOnAction(e->{
                CustomerDetailController.cusID = item.getId();
                sceneHandler = new SceneHandler();
                sceneHandler.slideScene(btn_edit, cellStack, "X", "/CRM_APP/View/Customer/customerDetail.fxml");
            });
            setText(null);
            setGraphic(main_pane);
        }
    }

}
