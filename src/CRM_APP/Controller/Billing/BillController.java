package CRM_APP.Controller.Billing;

import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ResourceBundle;
import CRM_APP.Model.Bill;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class BillController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane main_pane;

    @FXML
    private JFXListView<Bill> lv_detail;

    ObservableList<Bill> bills;

    @FXML
    void initialize() {
        Bill imp = new Bill();
        imp.setName("");
        bills = FXCollections.observableArrayList(imp);
        bills.addAll(imp);
        lv_detail.setItems(bills);
        lv_detail.setCellFactory(BillCellController -> new BillCellController());
    }

}

