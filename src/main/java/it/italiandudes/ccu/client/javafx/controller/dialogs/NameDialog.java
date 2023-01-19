package it.italiandudes.ccu.client.javafx.controller.dialogs;

import it.italiandudes.ccu.client.Client;
import it.italiandudes.ccu.client.ClientSingleton;
import it.italiandudes.ccu.client.annotations.ControllerClass;
import it.italiandudes.ccu.client.annotations.GraphicalOperation;
import it.italiandudes.idl.common.exceptions.IO.file.ConfigFormatException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@ControllerClass
public class NameDialog extends Dialog<ButtonType> implements Initializable {
    @FXML
    public TextField txt_name;
    @FXML
    public CheckBox chbx_save;
    @FXML
    public VBox vb_error;
    @FXML
    public Label lb_error;

    public NameDialog(){
        initModality(Modality.APPLICATION_MODAL);
    }

    @GraphicalOperation
    public String getName(){
        return txt_name.getText();
    }

    @GraphicalOperation
    public void setErrorLabel(String error){
        lb_error.setText(error);
        if(!vb_error.isVisible()){
            vb_error.setVisible(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            String name = ClientSingleton.getInstance().getProperty(Client.Defs.Config.USER_KEYWORD).getValue();
            if(name == null){
                chbx_save.setSelected(false);
            }else{
                txt_name.setText(name);
                chbx_save.setSelected(true);
            }
        } catch (IOException | ConfigFormatException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }
}
