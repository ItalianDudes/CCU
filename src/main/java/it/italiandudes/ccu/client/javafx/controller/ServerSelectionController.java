package it.italiandudes.ccu.client.javafx.controller;

import it.italiandudes.ccu.client.models.ServerSelectionModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

//TODO: finire di configurare la classe e di inserire la licenza
public class ServerSelectionController {

    private ServerSelectionModel model;

    @FXML
    public TextField txt_serverName;
    @FXML
    public TextField txt_serverAlias;
    @FXML
    public Button btn_confirm;
    @FXML
    public VBox vb_errorSupplement;
    @FXML
    public Label lab_errorSupplement;

    public void doConfirmServer(ActionEvent actionEvent) {
        System.out.println("Ciao");
    }

    public void setModel(ServerSelectionModel model){
        this.model=model;
    }
}
