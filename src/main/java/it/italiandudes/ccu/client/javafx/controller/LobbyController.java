package it.italiandudes.ccu.client.javafx.controller;

import it.italiandudes.ccu.client.models.controllers.LobbyModel;
import it.italiandudes.ccu.client.models.renderer.LobbyRendererModel;
import it.italiandudes.ccu.client.renderers.LobbyListRenderer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {

    @FXML
    public ListView<LobbyRendererModel> lv_players;
    @FXML
    public ToggleButton btn_ready;
    @FXML
    public Label lb_score;
    @FXML
    public Label lb_timer;

    private LobbyModel model;

    public void setModel(LobbyModel model){
        this.model=model;
    }

    public void changeReadyStatus(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lv_players.setCellFactory(new LobbyListRenderer());
    }
}
