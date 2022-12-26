package it.italiandudes.ccu.client.javafx.controller;

import it.italiandudes.ccu.client.ClientSingleton;
import it.italiandudes.ccu.client.annotations.ControllerClass;
import it.italiandudes.ccu.client.javafx.JFXDefs;
import it.italiandudes.ccu.client.models.controllers.ServerSelectionListModel;
import it.italiandudes.idl.common.exceptions.IO.file.ConfigFormatException;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@ControllerClass
public final class ServerSelectionListController implements Initializable {

    private ServerSelectionListModel model;

    @FXML
    public ListView<String> lv_serversList;
    @FXML
    public Button btn_addServer;
    @FXML
    public Button btn_confirm;
    @FXML
    public Button btn_deleteServer;
    @FXML
    public VBox vb_errorMessage;
    @FXML
    public Label lb_errorMessage;

    private int selectedServerIndex;

    private static String originalBtnStyle;

    public void doAddServer(ActionEvent actionEvent) {
    }

    public void doDeleteServer(ActionEvent actionEvent) {
    }

    public void doConfirm(ActionEvent actionEvent) {
    }

    public void setModel(ServerSelectionListModel model){
        this.model=model;
    }

    //TODO: finire metodo
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedServerIndex=-1;
        lv_serversList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedServerIndex = lv_serversList.getSelectionModel().getSelectedIndex();
            System.out.println("Selected Index: "+selectedServerIndex);
        });
        System.out.println("Listener aggiunto alla ListView");

        System.out.println("List View: "+lv_serversList);

        for(int i=0; i<ClientSingleton.getInstance().getServersNum(); i++){
            System.out.println("Inside first cycle ("+i+")");
            int finalI = i;
            Platform.runLater(()->{
                try {
                    lv_serversList.getItems().add(ClientSingleton.getInstance().getServer(finalI).getAlias());
                } catch (IOException | ConfigFormatException e) {
                    lb_errorMessage.setText(e.getMessage());
                    if(!vb_errorMessage.isVisible()){
                        vb_errorMessage.setVisible(true);
                    }
                }
            });
        }
        System.out.println("Fuori dal ciclo");

        originalBtnStyle=btn_confirm.getStyle();
        System.out.println("Stile prelevato");
        if(selectedServerIndex==-1){
            System.out.println("SelectedServerIndex= -1");
            Platform.runLater(()->{
                btn_confirm.setStyle(JFXDefs.StylesDefs.BTN_OFF);
            });
            System.out.println("Stile pulsante di conferma cambiato in BTN_OFF");
        }
    }
}
