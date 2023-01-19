package it.italiandudes.ccu.client.javafx.controller;

import it.italiandudes.ccu.client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public final class ControllerSceneLoading {
    @FXML
    public Label lb_loadingText;

    public void show(String loadingText){
        lb_loadingText.setText(loadingText);
        Client.getLoadingStage().show();
    }
}
