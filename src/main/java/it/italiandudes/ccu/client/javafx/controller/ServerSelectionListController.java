package it.italiandudes.ccu.client.javafx.controller;

import it.italiandudes.ccu.client.Client;
import it.italiandudes.ccu.client.ClientSingleton;
import it.italiandudes.ccu.client.annotations.ControllerClass;
import it.italiandudes.ccu.client.javafx.JFXDefs;
import it.italiandudes.ccu.client.javafx.scene.SceneLoading;
import it.italiandudes.ccu.client.models.controllers.ServerSelectionListModel;
import it.italiandudes.ccu.client.models.controllers.ServerSelectionModel;
import it.italiandudes.idl.common.exceptions.IO.file.ConfigFormatException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.rmi.AlreadyBoundException;
import java.security.InvalidParameterException;
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
        Client.getStage().setScene(SceneLoading.getScene());
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(JFXDefs.SceneDefs.SCENE_STARTUP_1));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            ServerSelectionController controller = loader.getController();
            controller.setModel(new ServerSelectionModel());

            Client.getStage().setScene(scene);
        } catch (IOException e) {
            lb_errorMessage.setText(e.getMessage());
            if(!vb_errorMessage.isVisible()){
                vb_errorMessage.setVisible(true);
            }
        }
    }

    public void doDeleteServer(ActionEvent actionEvent) {
        try {
            if(selectedServerIndex>=0){
                ClientSingleton.getInstance().deleteServer(ClientSingleton.getInstance().getServer(selectedServerIndex));

                if(ClientSingleton.getInstance().isServerEmpty()){
                    Client.getStage().setScene(SceneLoading.getScene());
                    //Since no server is saved, I automatically launch the scene to make you add a server.
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(JFXDefs.SceneDefs.SCENE_STARTUP_1));
                    Parent root = loader.load();

                    Scene scene = new Scene(root);

                    ServerSelectionController controller = loader.getController();
                    controller.setModel(new ServerSelectionModel());

                    Client.getStage().setScene(scene);
                }else{
                    //I reload the list view with the updated servers' list.
                    lv_serversList.getItems().remove(selectedServerIndex);
                }
            }
        } catch (IOException | ConfigFormatException e) {
            lb_errorMessage.setText(e.getMessage());
            if(!vb_errorMessage.isVisible()){
                vb_errorMessage.setVisible(true);
            }
        }
    }

    public void doConfirm(ActionEvent actionEvent) {
        if(selectedServerIndex>=0){
            try {
                boolean requirePwd;
                requirePwd = model.confirm(ClientSingleton.getInstance().getServer(selectedServerIndex).getCname(),
                        ClientSingleton.getInstance().getServer(selectedServerIndex).getAlias());

                if (requirePwd) {
                    System.out.println("Password richiesta");
                    //TODO: passare direttamente alla schermata di richiesta password
                } else {
                    System.out.println("Password non richiesta");
                    //TODO: passare direttamente alla schermata di richiesta nome
                }
            } catch (IOException | AlreadyBoundException | InvalidParameterException | NumberFormatException |
                     ConfigFormatException e) {
                Platform.runLater(() -> {
                    lb_errorMessage.setText(e.getMessage());
                    if (!vb_errorMessage.isVisible()) {
                        vb_errorMessage.setVisible(true);
                    }
                });
            }
        }
    }

    public void setModel(ServerSelectionListModel model){
        this.model=model;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedServerIndex=-1;

        System.out.println("List View: "+lv_serversList);

        for(int i=0; i<ClientSingleton.getInstance().getServersNum(); i++){
            System.out.println("Inside first cycle ("+i+")");
            try {
                lv_serversList.getItems().add(ClientSingleton.getInstance().getServer(i).getAlias());
            } catch (IOException | ConfigFormatException e) {
                lb_errorMessage.setText(e.getMessage());
                if(!vb_errorMessage.isVisible()){
                    vb_errorMessage.setVisible(true);
                }
            }
        }
        System.out.println("Fuori dal ciclo");

        lv_serversList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedServerIndex = lv_serversList.getSelectionModel().getSelectedIndex();
            System.out.println("Selected Index: "+selectedServerIndex);
            System.out.println("Selected Item: "+lv_serversList.getSelectionModel().getSelectedItem());
            btn_confirm.setStyle(originalBtnStyle);
            btn_deleteServer.setStyle(originalBtnStyle);
        });
        System.out.println("Listener aggiunto alla ListView");

        originalBtnStyle=btn_confirm.getStyle();
        System.out.println("Stile prelevato");
        if(selectedServerIndex==-1){
            System.out.println("SelectedServerIndex= -1");
            Platform.runLater(()->{
                btn_confirm.setStyle(JFXDefs.StylesDefs.BTN_OFF);
                btn_deleteServer.setStyle(JFXDefs.StylesDefs.BTN_OFF);
            });
            System.out.println("Stile pulsante di conferma cambiato in BTN_OFF");
        }
    }
}
