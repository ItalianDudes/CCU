package it.italiandudes.ccu.client.javafx.controller;

import it.italiandudes.ccu.client.ClientSingleton;
import it.italiandudes.ccu.client.annotations.ControllerClass;
import it.italiandudes.ccu.client.javafx.JFXDefs;
import it.italiandudes.ccu.client.models.controllers.ServerSelectionListModel;
import it.italiandudes.ccu.client.models.controllers.ServerSelectionModel;
import it.italiandudes.idl.common.exceptions.IO.file.ConfigFormatException;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(JFXDefs.SceneDefs.SCENE_STARTUP_1));
            Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
            Parent root = loader.load();

            Scene scene = new Scene(root);

            ServerSelectionController controller = loader.getController();
            controller.setModel(new ServerSelectionModel());

            stage.setScene(scene);
            stage.setTitle(JFXDefs.AppAssets.APP_TITLE);
            stage.getIcons().add(JFXDefs.AppAssets.APP_ICON);
            stage.show();
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
                    //Since no server is saved, I automatically launch the scene to make you add a server.
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(JFXDefs.SceneDefs.SCENE_STARTUP_1));
                    Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                    Parent root = loader.load();

                    Scene scene = new Scene(root);

                    ServerSelectionController controller = loader.getController();
                    controller.setModel(new ServerSelectionModel());

                    stage.setScene(scene);
                    stage.setTitle(JFXDefs.AppAssets.APP_TITLE);
                    stage.getIcons().add(JFXDefs.AppAssets.APP_ICON);
                    stage.show();
                }else{
                    //I reload the list view with the updated servers' list.
                    for(int i=0; i<ClientSingleton.getInstance().getServersNum(); i++){
                        System.out.println("Inside first cycle ("+i+")");
                        lv_serversList.getItems().add(ClientSingleton.getInstance().getServer(i).getAlias());
                    }
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
