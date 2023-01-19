package it.italiandudes.ccu.client.javafx.controller;

import it.italiandudes.ccu.client.Client;
import it.italiandudes.ccu.client.ClientSingleton;
import it.italiandudes.ccu.client.annotations.ControllerClass;
import it.italiandudes.ccu.client.exception.UsernameAlreadyBoundException;
import it.italiandudes.ccu.client.javafx.JFXDefs;
import it.italiandudes.ccu.client.javafx.controller.dialogs.NameDialog;
import it.italiandudes.ccu.client.javafx.controller.dialogs.PasswordDialog;
import it.italiandudes.ccu.client.javafx.scene.SceneLoading;
import it.italiandudes.ccu.client.models.controllers.LobbyModel;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.rmi.AlreadyBoundException;
import java.security.InvalidParameterException;
import java.util.Optional;
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
    private static boolean savePwd;
    private static boolean saveName;

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
        Service<Void> deleteService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Client.getLoadingController().show(Client.Defs.LoadingTexts.DELETING_SERVER_TEXT);
                        try {
                            if(selectedServerIndex>=0){
                                ClientSingleton.getInstance().deleteServer(ClientSingleton.getInstance().getServer(selectedServerIndex));

                                if(ClientSingleton.getInstance().isServerEmpty()){
                                    Platform.runLater(()->{
                                        try{
                                            Client.getStage().setScene(SceneLoading.getScene());
                                            //Since no server is saved, I automatically launch the scene to make you add a server.
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource(JFXDefs.SceneDefs.SCENE_STARTUP_1));
                                            Parent root = loader.load();

                                            Scene scene = new Scene(root);

                                            ServerSelectionController controller = loader.getController();
                                            controller.setModel(new ServerSelectionModel());

                                            Client.getLoadingStage().close();
                                            Client.getStage().setScene(scene);
                                        } catch (IOException e) {
                                            lb_errorMessage.setText(e.getMessage());
                                            if(!vb_errorMessage.isVisible()) {
                                                vb_errorMessage.setVisible(true);
                                            }
                                        }
                                    });
                                }else{
                                    //I reload the list view with the updated servers' list.
                                    Platform.runLater(()->lv_serversList.getItems().remove(selectedServerIndex));
                                }
                            }
                        } catch (IOException | ConfigFormatException e) {
                            Platform.runLater(()->{
                                lb_errorMessage.setText(e.getMessage());
                                if(!vb_errorMessage.isVisible()) {
                                    vb_errorMessage.setVisible(true);
                                }
                            });
                        }finally {
                            Client.getLoadingStage().close();
                        }
                        return null;
                    }
                };
            }
        };
        deleteService.start();
    }

    public void doConfirm(ActionEvent actionEvent) {
        Service<Void> confirmService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Platform.runLater(()->Client.getLoadingController().show(Client.Defs.LoadingTexts.REQUEST_PWD_TEXT));
                        if(selectedServerIndex>=0){
                            try {
                                boolean isNameOk=false;
                                boolean requirePwd;
                                requirePwd = model.confirm(ClientSingleton.getInstance().getServer(selectedServerIndex).getCname(),
                                        ClientSingleton.getInstance().getServer(selectedServerIndex).getAlias());
                                ClientSingleton.getInstance().setSelectedServer(ClientSingleton.getInstance().getServer(selectedServerIndex));

                                if (requirePwd) {
                                    System.out.println("Password richiesta");
                                    /*The password dialog is launched and, after the correct password has been typed, the name dialog is launched. At any time
                                    the process can be altered.*/
                                    PasswordDialog pwdDialog = new PasswordDialog();
                                    savePwd=false;
                                    Platform.runLater(()->{
                                        pwdDialog.setResizable(false);
                                        pwdDialog.getDialogPane().lookup(JFXDefs.IdDefs.REQUEST_CHKBOX).addEventFilter(
                                                ActionEvent.ACTION, event -> savePwd = ((CheckBox) event.getSource()).selectedProperty().get());
                                    });
                                    boolean isPwdOk = false;
                                    boolean cancel = false;
                                    Client.getLoadingStage().close();
                                    while(!isPwdOk && !cancel){
                                        try{
                                            Optional<ButtonType> result = pwdDialog.showAndWait();
                                            if(result.isPresent() && result.get() == ButtonType.FINISH) {
                                                Platform.runLater(()->Client.getLoadingController().show(Client.Defs.LoadingTexts.PWD_CONTROL_TEXT));
                                                String pwd = pwdDialog.getPwd();
                                                if(savePwd){
                                                    model.savePwd(pwd);
                                                }
                                                isPwdOk=model.pwdValidation(pwd);

                                                if(isPwdOk){
                                                    System.out.println("Correct password");
                                                }else{
                                                    System.out.println("Wrong password");
                                                }
                                            }else if(result.isPresent() && result.get() == ButtonType.CANCEL){
                                                cancel=true;
                                            }
                                        }catch(IOException e){
                                            Platform.runLater(()->pwdDialog.setErrorLabel(e.getMessage()));
                                        }finally {
                                            Client.getLoadingStage().close();
                                        }
                                    }

                                    if(!cancel){
                                        Platform.runLater(()->Client.getLoadingController().show(Client.Defs.LoadingTexts.NAME_REQUEST_LOADING_TEXT));
                                        NameDialog nameDialog = new NameDialog();
                                        saveName=false;
                                        Platform.runLater(()->{
                                            nameDialog.setResizable(false);
                                            nameDialog.getDialogPane().lookup(JFXDefs.IdDefs.REQUEST_CHKBOX).addEventFilter(
                                                    ActionEvent.ACTION, event -> {
                                                        saveName = ((CheckBox) event.getSource()).selectedProperty().get();
                                                    });
                                        });
                                        Client.getLoadingStage().close();
                                        while(!isNameOk && !cancel){
                                            try{
                                                Optional<ButtonType> result = nameDialog.showAndWait();
                                                if(result.isPresent() && result.get() == ButtonType.FINISH) {
                                                    Platform.runLater(()->Client.getLoadingController().show(Client.Defs.LoadingTexts.CHECKING_NAME_VALIDITY_TEXT));
                                                    String name = nameDialog.getName();
                                                    if(saveName){
                                                        model.saveName(name);
                                                    }
                                                    isNameOk=model.nameValidation(name);

                                                    if(isNameOk){
                                                        System.out.println("Name accepted");
                                                    }else{
                                                        System.out.println("Name not accepted");
                                                    }
                                                }else if(result.isPresent() && result.get() == ButtonType.CANCEL){
                                                    cancel=true;
                                                }
                                            }catch(IOException | UsernameAlreadyBoundException e){
                                                Platform.runLater(()->nameDialog.setErrorLabel(e.getMessage()));
                                            }finally {
                                                Client.getLoadingStage().close();
                                            }
                                        }
                                    }
                                } else {
                                    System.out.println("Password non richiesta");
                                    NameDialog nameDialog = new NameDialog();
                                    boolean cancel=false;
                                    saveName=false;
                                    Platform.runLater(()->{
                                        nameDialog.setResizable(false);
                                        nameDialog.getDialogPane().lookup(JFXDefs.IdDefs.REQUEST_CHKBOX).addEventFilter(
                                                ActionEvent.ACTION, event -> {
                                                    saveName = ((CheckBox) event.getSource()).selectedProperty().get();
                                                });
                                    });
                                    Client.getLoadingStage().close();
                                    while(!isNameOk && !cancel){
                                        try{
                                            Optional<ButtonType> result = nameDialog.showAndWait();
                                            if(result.isPresent() && result.get() == ButtonType.FINISH) {
                                                Platform.runLater(()->Client.getLoadingController().show(Client.Defs.LoadingTexts.CHECKING_NAME_VALIDITY_TEXT));
                                                String name = nameDialog.getName();
                                                if(saveName){
                                                    model.saveName(name);
                                                }
                                                isNameOk=model.nameValidation(name);

                                                if(isNameOk){
                                                    System.out.println("Name accepted");
                                                }else{
                                                    System.out.println("Name not accepted");
                                                }
                                            }else if(result.isPresent() && result.get() == ButtonType.CANCEL){
                                                cancel=true;
                                            }
                                        }catch(IOException | UsernameAlreadyBoundException e){
                                            Platform.runLater(()->nameDialog.setErrorLabel(e.getMessage()));
                                        }finally {
                                            Client.getLoadingStage().close();
                                        }
                                    }
                                }

                                if(isNameOk){
                                    Platform.runLater(()->Client.getLoadingController().show(Client.Defs.LoadingTexts.LOADIG_LOBBY_TEXT));
                                    //TODO: caricare i dati necessari alla lobby prima di lanciare lo stage
                                    Platform.runLater(()->{
                                        try{
                                            /*Init Lobby Stage launch*/
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource(JFXDefs.SceneDefs.LOBBY_SCENE));

                                            Parent root = loader.load();

                                            Scene scene = new Scene(root);

                                            LobbyController controller = loader.getController();
                                            controller.setModel(new LobbyModel());

                                            Client.getLoadingStage().close();
                                            Client.getStage().setScene(scene);
                                        } catch (IOException e) {
                                            Platform.runLater(()->{
                                                lb_errorMessage.setText(e.getMessage());
                                                if(!vb_errorMessage.isVisible()){
                                                    vb_errorMessage.setVisible(true);
                                                }
                                            });
                                        }finally {
                                            Client.getLoadingStage().close();
                                        }
                                    });
                                }
                            } catch (IOException | AlreadyBoundException | InvalidParameterException | NumberFormatException |
                                     ConfigFormatException e) {
                                Platform.runLater(() -> {
                                    lb_errorMessage.setText(e.getMessage());
                                    if (!vb_errorMessage.isVisible()) {
                                        vb_errorMessage.setVisible(true);
                                    }
                                });
                            }finally {
                                Client.getLoadingStage().close();
                            }
                        }
                        return null;
                    }
                };
            }
        };
        confirmService.start();
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
