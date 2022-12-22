package it.italiandudes.ccu.client.javafx.controller;

import it.italiandudes.ccu.CCU;
import it.italiandudes.ccu.client.javafx.JFXDefs;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public final class ControllerSceneLoading {

    //Attributes
    @FXML
    private ImageView loadingView;
    private static final Image loadingImage = new Image(Objects.requireNonNull(CCU.class.getResource(JFXDefs.ResourceDefs.GIF_LOADING)).toString());

    //Initialize
    @FXML
    private void initialize() {
        loadingView.setImage(loadingImage);
    }

}
