package it.italiandudes.ccu.client.javafx.controller;

import it.italiandudes.ccu.client.models.controllers.LobbyModel;
import javafx.fxml.Initializable;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {

    private LobbyModel model;

    public void setModel(LobbyModel model){
        this.model=model;
    }

    public void changeReadyStatus(ActionEvent event){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
