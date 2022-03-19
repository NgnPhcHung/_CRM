package CRM_APP.Controller.Survey;

import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Survey;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class SurveyController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btn_add;

    @FXML
    private JFXTextField txt_search;

    @FXML
    private JFXListView<Survey> lv_product;

    ObservableList<Survey> documents;
    SceneHandler sceneHandler;
    @FXML
    void initialize() {
        sceneHandler = new SceneHandler();
        Survey document = new Survey();
//        document.setName("ASDAS");
//        document.setStaff("Staff A");
//        document.setVersion("0.3");
        documents = FXCollections.observableArrayList(document);
        documents.addAll();
        lv_product.setItems(documents);
        lv_product.setCellFactory(DocumentCellController -> new SurveyCellController());
        btn_add.setOnAction(event -> {
            sceneHandler.newScene("/CRM_APP/View/Survey/surveyDetail.fxml");
        });
    }
}
