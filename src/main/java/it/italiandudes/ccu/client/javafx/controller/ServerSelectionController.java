package it.italiandudes.ccu.client.javafx.controller;

import it.italiandudes.ccu.client.Client;
import it.italiandudes.ccu.client.annotations.ControllerClass;
import it.italiandudes.ccu.client.exception.UsernameAlreadyBoundException;
import it.italiandudes.ccu.client.javafx.JFXDefs;
import it.italiandudes.ccu.client.javafx.controller.dialogs.NameDialog;
import it.italiandudes.ccu.client.javafx.controller.dialogs.PasswordDialog;
import it.italiandudes.ccu.client.models.controllers.LobbyModel;
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

//TODO: finire di configurare la classe e di inserire la licenza
@ControllerClass
public final class ServerSelectionController implements Initializable {

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


    private static String originalTxtBorderColor;
    private static boolean savePwd;
    private static boolean saveName;

    public void doConfirmServer(ActionEvent actionEvent) {
        Service<Void> confirmServerService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        String serverName = txt_serverName.getText();
                        boolean isServerName, isAlias;
                        isServerName= serverName != null && !serverName.isEmpty();

                        String alias = txt_serverAlias.getText();
                        isAlias= alias != null && !alias.isEmpty();

                        if(isServerName && isAlias){
                            try {
                                boolean isNameOk = false;
                                boolean requirePwd;
                                requirePwd=model.confirm(serverName,alias);

                                if(requirePwd){
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
                                    while(!isPwdOk && !cancel){
                                        try{
                                            Optional<ButtonType> result = pwdDialog.showAndWait();
                                            if(result.isPresent() && result.get() == ButtonType.FINISH) {
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
                                            Platform.runLater(()-> pwdDialog.setErrorLabel(e.getMessage()));
                                        }
                                    }

                                    if(!cancel){
                                        NameDialog nameDialog = new NameDialog();
                                        saveName=false;
                                        Platform.runLater(()->{
                                            nameDialog.setResizable(false);
                                            nameDialog.getDialogPane().lookup(JFXDefs.IdDefs.REQUEST_CHKBOX).addEventFilter(
                                                    ActionEvent.ACTION, event -> saveName = ((CheckBox) event.getSource()).selectedProperty().get());
                                        });
                                        while(!isNameOk && !cancel){
                                            try{
                                                Optional<ButtonType> result = nameDialog.showAndWait();
                                                if(result.isPresent() && result.get() == ButtonType.FINISH) {
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
                                            }
                                        }
                                    }
                                }else{
                                    System.out.println("Password non richiesta");
                                    /*The name dialog is launched, since no password is required to enter the server.*/
                                    NameDialog nameDialog = new NameDialog();
                                    saveName=false;
                                    boolean cancel=false;
                                    Platform.runLater(()->{
                                        nameDialog.setResizable(false);
                                        nameDialog.getDialogPane().lookup(JFXDefs.IdDefs.REQUEST_CHKBOX).addEventFilter(
                                                ActionEvent.ACTION, event -> {
                                                    saveName = ((CheckBox) event.getSource()).selectedProperty().get();
                                                });
                                    });
                                    while(!isNameOk && !cancel){
                                        try{
                                            Optional<ButtonType> result = nameDialog.showAndWait();
                                            if(result.isPresent() && result.get() == ButtonType.FINISH) {
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
                                        }
                                    }
                                }

                                if(isNameOk){
                                    Platform.runLater(()->{
                                        try{
                                            /*Init Lobby Stage launch*/
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource(JFXDefs.SceneDefs.LOBBY_SCENE));

                                            Parent root = loader.load();

                                            Scene scene = new Scene(root);

                                            LobbyController controller = loader.getController();
                                            controller.setModel(new LobbyModel());

                                            Client.getStage().setScene(scene);
                                            Client.getStage().setTitle(JFXDefs.AppAssets.APP_TITLE);
                                            Client.getStage().getIcons().add(JFXDefs.AppAssets.APP_ICON);
                                            Client.getStage().show();
                                        } catch (IOException e) {
                                            Platform.runLater(()->{
                                                txt_serverName.setStyle(originalTxtBorderColor);
                                                txt_serverAlias.setStyle(originalTxtBorderColor);
                                                lab_errorSupplement.setText(e.getMessage());
                                                if(!vb_errorSupplement.isVisible()){
                                                    vb_errorSupplement.setVisible(true);
                                                }
                                            });
                                        }
                                    });
                                }
                            } catch (IOException | AlreadyBoundException | InvalidParameterException | NumberFormatException | ConfigFormatException e) {
                                Platform.runLater(()->{
                                    txt_serverName.setStyle(originalTxtBorderColor);
                                    txt_serverAlias.setStyle(originalTxtBorderColor);
                                    lab_errorSupplement.setText(e.getMessage());
                                    if(!vb_errorSupplement.isVisible()){
                                        vb_errorSupplement.setVisible(true);
                                    }
                                });
                            }
                        }else{
                            if(isServerName){
                                Platform.runLater(()->{
                                    txt_serverName.setStyle(originalTxtBorderColor);
                                    lab_errorSupplement.setText("Alias is empty");
                                    if(!vb_errorSupplement.isVisible()){
                                        vb_errorSupplement.setVisible(true);
                                    }
                                    txt_serverAlias.setStyle(JFXDefs.StylesDefs.ERROR_TXT_BORDER_COLOR);
                                });
                            }else if(isAlias){
                                Platform.runLater(()->{
                                    txt_serverAlias.setStyle(originalTxtBorderColor);
                                    lab_errorSupplement.setText("Server is empty");
                                    if(!vb_errorSupplement.isVisible()){
                                        vb_errorSupplement.setVisible(true);
                                    }
                                    txt_serverName.setStyle(JFXDefs.StylesDefs.ERROR_TXT_BORDER_COLOR);
                                });
                            }else{
                                Platform.runLater(()->{
                                    lab_errorSupplement.setText("Both Alias and Server are empty");
                                    if(!vb_errorSupplement.isVisible()){
                                        vb_errorSupplement.setVisible(true);
                                    }
                                    txt_serverName.setStyle(JFXDefs.StylesDefs.ERROR_TXT_BORDER_COLOR);
                                    txt_serverAlias.setStyle(JFXDefs.StylesDefs.ERROR_TXT_BORDER_COLOR);
                                });
                            }
                        }
                        return null;
                    }
                };
            }
        };
        confirmServerService.start();
    }

    public void setModel(ServerSelectionModel model){
        this.model=model;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        originalTxtBorderColor = txt_serverName.getStyle();
    }
}
