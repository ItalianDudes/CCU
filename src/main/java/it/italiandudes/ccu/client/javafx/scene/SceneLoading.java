package it.italiandudes.ccu.client.javafx.scene;

import it.italiandudes.ccu.CCU;
import it.italiandudes.ccu.client.javafx.JFXDefs;
import it.italiandudes.idl.common.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

@SuppressWarnings("unused")
public final class SceneLoading {

    //Scene Generator
    public static Scene getScene(){
        try {
            return new Scene(FXMLLoader.load(Objects.requireNonNull(CCU.class.getResource(JFXDefs.SceneDefs.SCENE_LOADING))));
        }catch (IOException e){
            Logger.log(e);
            return null;
        }
    }

}
