package it.italiandudes.ccu.client.javafx.controller;

import it.italiandudes.ccu.client.annotations.ControllerClass;
import it.italiandudes.ccu.client.javafx.JFXDefs;
import it.italiandudes.ccu.client.models.controllers.ServerSelectionModel;
import it.italiandudes.idl.common.exceptions.IO.file.ConfigFormatException;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.rmi.AlreadyBoundException;
import java.security.InvalidParameterException;
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


    public void doConfirmServer(ActionEvent actionEvent) {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                String serverName = txt_serverName.getText();
                boolean isServerName, isAlias;
                isServerName= serverName != null && !serverName.isEmpty();

                String alias = txt_serverAlias.getText();
                isAlias= alias != null && !alias.isEmpty();

                if(isServerName && isAlias){
                    try {
                        boolean requirePwd;
                        requirePwd=model.confirm(serverName,alias);

                        if(requirePwd){
                            System.out.println("Password richiesta");
                            //TODO: passare direttamente alla schermata di richiesta password
                        }else{
                            System.out.println("Password non richiesta");
                            //TODO: passare direttamente alla schermata di richiesta nome
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
        service.start();
    }

    public void setModel(ServerSelectionModel model){
        this.model=model;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        originalTxtBorderColor = txt_serverName.getStyle();
    }
}
