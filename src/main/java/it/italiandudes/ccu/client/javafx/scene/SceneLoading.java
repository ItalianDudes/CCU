package it.italiandudes.ccu.client.javafx.scene;

import it.italiandudes.ccu.CCU;
import it.italiandudes.ccu.client.javafx.JFXDefs;
import it.italiandudes.ccu.client.javafx.controller.ControllerSceneLoading;
import it.italiandudes.idl.common.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

@SuppressWarnings("unused")
public final class SceneLoading {

    private static FXMLLoader loader;

    //Scene Generator
    public static Scene getScene(){
        try {
            loader = new FXMLLoader(Objects.requireNonNull(CCU.class.getResource(JFXDefs.SceneDefs.SCENE_LOADING)));
            return new Scene(loader.load());
        }catch (IOException e){
            Logger.log(e);
            return null;
        }
    }

    public static ControllerSceneLoading getController(){
        return loader.getController();
    }
}
