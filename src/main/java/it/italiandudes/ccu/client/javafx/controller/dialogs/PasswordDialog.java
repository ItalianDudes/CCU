package it.italiandudes.ccu.client.javafx.controller.dialogs;

import it.italiandudes.ccu.client.ClientSingleton;
import it.italiandudes.ccu.client.annotations.ControllerClass;
import it.italiandudes.ccu.client.annotations.GraphicalOperation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;

//TODO: finire classe Dialog
//Fonti: https://stackoverflow.com/questions/64964471/java-fx-create-custom-dialog-with-fxml-file-how-to-set-or-get-result-from-it
//      https://stackoverflow.com/questions/36309385/how-to-change-the-text-of-yes-no-buttons-in-javafx-8-alert-dialogs
//      https://stackoverflow.com/questions/66515920/add-css-style-to-buttontype-in-javafx
@ControllerClass
public class PasswordDialog extends Dialog<ButtonType> implements Initializable {

    @FXML
    public PasswordField ptxt_password;
    @FXML
    public CheckBox chbx_save;
    @FXML
    public VBox vb_error;
    @FXML
    public Label lb_error;

    public PasswordDialog(){
        initModality(Modality.APPLICATION_MODAL);
    }

    @GraphicalOperation
    public String getPwd(){
        return ptxt_password.getText();
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
        if(ClientSingleton.getInstance().getSelectedServer().getPwd()==null || ClientSingleton.getInstance().getSelectedServer().getPwd().isEmpty()){
            chbx_save.setSelected(false);
        }else{
            ptxt_password.setText(ClientSingleton.getInstance().getSelectedServer().getPwd());
            chbx_save.setSelected(true);
        }
    }
}
